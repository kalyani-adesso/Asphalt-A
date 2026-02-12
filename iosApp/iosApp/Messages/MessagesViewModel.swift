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
    @Published var messages: [Message] = []
    @Published var messageText: String = ""

    private let chatRepository = ChatRepository()

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

}
