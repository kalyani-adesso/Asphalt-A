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
    enum ChatType {
        case `private`
        case group
    }
    var messageStatus: [MessageCategory] = MessageCategory.allCases
    @Published  var searchText = ""
    @Published var selectedCategory: String? = MessageCategory.All.rawValue
    @Published var messages: [LocalMessage] = []
    @Published var messageText: String = ""
//    private var currentUserId: String {
//        MBUserDefaults.userIdStatic ?? ""
//    }
//    
    private let chatRepository = ChatRepository()
    private var messageJob: Kotlinx_coroutines_coreJob?
    private let userRepo: UserRepository
    
    var recipientId: String
    let chatType: ChatType
    let memberList: [String]?
    var rideTitle: String?
    var rideId: String?
    @Published var recentChats: [Chat] = []
    private var recentChatsTask: Task<Void, Never>?
    @Published var usersById: [String: UserDomain] = [:]
    private let currentUserId: String
    
    init(
        currentUserId: String,
        recipientId: String,
        chatType: ChatType,
        memberList: [String]? = nil,
        rideTitle: String? = nil,
        rideId: String? = nil,
    ) {
        let userApiService = UserAPIServiceImpl(client: KtorClient())
        self.userRepo = UserRepository(apiService: userApiService)
        self.recipientId = recipientId
        self.chatType = chatType
        self.memberList = memberList
        self.rideTitle = rideTitle
        self.rideId = rideId
        self.currentUserId = currentUserId
    }
    func sendMessage() {
        guard !messageText.isEmpty else { return }
        
        switch chatType {
        case .private:
            send1v1Message()
        case .group:
            sendGroupMessage()
        }
    }
    func send1v1Message() {
        guard !messageText.isEmpty else { return }
        
        let chatRoomId = chatRepository.getCanonicalChatId(uid1: currentUserId, uid2: recipientId)
        
        print("chatRoomId: \(chatRoomId)")
        
        self.chatRepository.createOrGet1v1Chat(userAId: self.currentUserId, userBId: self.recipientId)
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            self.send1v1MessageViaKMP(chatRoomId: chatRoomId)
        }
    }
    private func send1v1MessageViaKMP(chatRoomId: String) {
        chatRepository.sendMessage(
            chatRoomId: chatRoomId,
            senderId: currentUserId,
            recipientId: recipientId,
            text: messageText
        )
        
        print("Message sent successfully: \(currentUserId)-\(recipientId)")
        messageText = ""
    }
    
    func sendGroupMessage() {
        guard !messageText.isEmpty else { return }
        
        guard let memberList = memberList,
              let rideTitle = rideTitle ,
              let rideId = rideId  else {
            print("Group metadata missing")
            return
        }
        
        let textToSend = messageText
        
        chatRepository.createOrGetGroupChat(
            memberList: memberList,
            rideID: rideId,
            rideTitle: rideTitle
        )
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            self.sendGroupMessageViaKMP(
                rideId: rideId,
                senderId: self.currentUserId,
                text: textToSend,
                allMemberIds: memberList
            )
        }
    }
    
    
    private func sendGroupMessageViaKMP(rideId: String, senderId: String, text: String, allMemberIds: [String]) {
        chatRepository.sendGroupMessage(rideId: rideId, senderId: senderId, text: text, allMemberIds: allMemberIds)
        
        print("Group Message sent successfully: \(rideId)-\(allMemberIds)")
        messageText = ""
    }
    
    // MARK: - Fetch Users
    @MainActor
    func fetchAllUsers() async throws {
        let result = try await userRepo.getAllUsers()
        
        if let success = result as? APIResultSuccess<AnyObject>,
           let users = success.data as? [UserDomain] {
            //            for user in users {
            //                print("User: \(user.uid) - \(user.name)")
            //            }
            // dictionary for easy findings
            self.usersById = Dictionary(uniqueKeysWithValues: users.map { ($0.uid, $0) })
            
        } else {
            print("Unexpected user data type")
        }
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
                    if self.usersById.isEmpty {
                        try await self.fetchAllUsers()
                    }
                    
                    let flow = chatRepository.getMessages(chatRoomId: localChatRoomId)
                    
                    try await flow.collect(
                        collector: ChatMessageCollector(
                            onValue: { [weak self] messages in
                                guard let self = self else { return }
                                
                                // Convert KMP → Local model
                                let mappedMessages: [LocalMessage] = messages.map { message in
                                    let senderName =
                                    message.senderId == self.currentUserId
                                    ? "You"
                                    : (self.usersById[message.senderId]?.name ?? "Unknown")
                                    
                                    return LocalMessage(
                                        text: message.text,
                                        isMe: message.senderId == self.currentUserId,
                                        time: self.formatTime(from: message.timestamp),
                                        senderName: senderName
                                    )
                                }
                                
                                // Update UI on MainActor
                                Task { @MainActor in
                                    self.messages = mappedMessages
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
    
    // MARK: - Recent Chats
    
    func fetchRecentChats() {
        guard !currentUserId.isEmpty else {
            print("fetchRecentChats called with empty userId\(MBUserDefaults.userIdStatic ?? "")")
            return
        }
        print("fetchRecentChats called for user: \(MBUserDefaults.userIdStatic ?? "")")
        
        let chatFlow = chatRepository.getRecentChats(myUserId: currentUserId)
        recentChatsTask?.cancel()
        
        recentChatsTask = Task {
            do {
                try await chatFlow.collect(
                    collector: ChatRoomCollector { [weak self] chatRooms in
                        guard let self = self else { return }
                        
                        print("Collector received chatRooms: \(chatRooms.count)")
                        chatRooms.enumerated().forEach { index, room in
                        }
                        
                        // Map to Local Swift Chat model
                        let mappedChats = chatRooms.map { room in
                            
                            Chat(
                                id: room.id ?? "",
                                name:  room.name ?? "",
                                lastMessage: room.lastMessage ?? "",
                                time: room.lastTimestamp > 0 ? self.formatTime(from: room.lastTimestamp) : "",
                                unreadCount: 0,
                                isGroup: room.type == "group"
                            )
                        }
                        
                        // Update UI directly on MainActor
                        Task { @MainActor in
                            print("Setting recentChats with \(mappedChats.count) items")
                            self.recentChats = mappedChats
                        }
                    }
                )
            } catch {
                print(" Error fetching recent chats:", error)
            }
        }
    }
    
    func markChatAsRead(chatRoomId: String) {
        chatRepository.markAsRead(chatRoomId: chatRoomId, myUserId: currentUserId)
        
        if let index = recentChats.firstIndex(where: { $0.id == chatRoomId }) {
            recentChats[index].unreadCount = 0
        }
    }
}

// MARK: - Collectors

class ChatRoomCollector: Kotlinx_coroutines_coreFlowCollector {
    
    let onValue: ([ChatRoom]) -> Void
    
    init(onValue: @escaping ([ChatRoom]) -> Void) {
        self.onValue = onValue
    }
    
    func emit(value: Any?, completionHandler: @escaping (Error?) -> Void) {
        guard let unwrapped = value else {
            completionHandler(nil)
            return
        }
        
        // Try to convert any KMP list to array of ChatRoom
        if let array = unwrapped as? [Any] {
            let chatRooms = array.compactMap { $0 as? ChatRoom }
            print("ChatRoomCollector: emitting \(chatRooms.count) chat rooms")
            onValue(chatRooms)
            completionHandler(nil)
            return
        }
        
        // Fallback for unexpected type
        print("ChatRoomCollector: invalid type:", type(of: unwrapped))
        completionHandler(nil)
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

