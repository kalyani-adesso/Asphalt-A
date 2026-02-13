//
//  MessagesViewModel.swift
//  iosApp
//
//  Created by Lavanya Selvan on 15/12/25.
//

import Foundation
import shared
import FirebaseDatabase


class MessagesViewModel: ObservableObject {
    enum MessageCategory: String, CaseIterable {
        case All, Unread, Groups, Favourites
    }
    var messageStatus: [MessageCategory] = MessageCategory.allCases
    @Published  var searchText = ""
    @Published var selectedCategory: String? = MessageCategory.All.rawValue
    @Published var messages: [LocalMessage] = []
    @Published var messageText: String = ""

    private let chatRepository = ChatRepository()
    private var messageJob: Kotlinx_coroutines_coreJob?

    let currentUserId: String
    var recipientId: String

    init(recipientId: String) {
        self.currentUserId = MBUserDefaults.userIdStatic ?? ""
        self.recipientId = recipientId
    }
    
    func sendMessage() {
        guard !messageText.isEmpty else { return }

        let chatRoomId = chatRepository
            .getCanonicalChatId(uid1: currentUserId, uid2: recipientId)

        let chatRef = Database.database()
            .reference()
            .child("chats")
            .child(chatRoomId)

        chatRef.observeSingleEvent(of: .value) { snapshot in
            
            if snapshot.exists() {
                self.sendViaKMP(chatRoomId: chatRoomId)
            } else {
                self.chatRepository.createOrGet1v1Chat(
                    userAId: self.currentUserId,
                    userBId: self.recipientId
                )
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) {
                    self.sendViaKMP(chatRoomId: chatRoomId)
                }
            }
            
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) {
            self.receiveMessageFromKMP(chatRoomId: chatRoomId)
        }
    }

    private func sendViaKMP(chatRoomId: String) {
        chatRepository.sendMessage(
            chatRoomId: chatRoomId,
            senderId: currentUserId,
            recipientId: recipientId,
            text: messageText
        )

        print("Message sent successfully: \(currentUserId)-\(recipientId)")
        messageText = ""
    }

    func receiveMessageFromKMP(chatRoomId:String) {
        var localChatRoomId:String = ""
        print("Current userId:\(currentUserId), \(recipientId)")
        if chatRoomId.isEmpty{
            localChatRoomId = chatRepository
                .getCanonicalChatId(uid1: currentUserId, uid2: recipientId)
            
        } else {
            localChatRoomId = chatRoomId
        }
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) {
            Task { [weak self] in
                guard let self = self else { return }
                
                do {
                    let flow = chatRepository.getMessages(chatRoomId: localChatRoomId)
                    
                    try await flow.collect(
                        collector: ChatMessageCollector(
                            onValue: { [weak self] messages in
                                guard let self = self else { return }
                                
                                // Convert KMP → Local model
                                let mappedMessages: [LocalMessage] = messages.map { message in
                                    LocalMessage(
                                        text: message.text,
                                        isMe: message.senderId == self.currentUserId,
                                        time: self.formatTime(from: message.timestamp),
                                        senderName: message.senderId
                                    )
                                }
                                
                                // Update UI on MainActor
                                Task { @MainActor in
                                    self.messages = mappedMessages.reversed()
                                }
                            },
                            onError: { error in
                                print("Chat Flow Error:", error.localizedDescription)
                            }
                        )
                    )
                    
                } catch {
                    print("Outer Flow Error:", error.localizedDescription)
                }
            }
        }
    }
    
    func stopReceivingMessages() {
        messageJob?.cancel(cause_: nil)
        messageJob = nil
    }
    
    func formatTime(from timestamp: Int64) -> String {
        let date = Date(timeIntervalSince1970: TimeInterval(timestamp) / 1000)
        let diff = Int(Date().timeIntervalSince(date))
        
        if diff < 60 {
            return "\(diff)s ago"
        } else if diff < 3600 {
            return "\(diff / 60)m ago"
        } else {
            return "\(diff / 3600)h ago"
        }
    }
}

class ChatMessageCollector: Kotlinx_coroutines_coreFlowCollector {

    let onValue: ([Message]) -> Void
    let onError: (Error) -> Void

    init(onValue: @escaping ([Message]) -> Void,
         onError: @escaping (Error) -> Void) {
        self.onValue = onValue
        self.onError = onError
    }
    
    func emit(value: Any?, completionHandler: @escaping ((any Error)?) -> Void) {
        guard let unwrapped = value else {
            completionHandler(nil)
            return
        }
        
        if let array = unwrapped as? [Any] {
            let messages = array.compactMap { $0 as? Message }
            onValue(messages)
            completionHandler(nil)
            return
        }

        print("Still invalid type:", type(of: unwrapped))
        completionHandler(nil)
    }

}

