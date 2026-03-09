//
//  MessagesViewModel.swift
//  iosApp
//
//  Created by Lavanya Selvan on 15/12/25.
//

import Foundation
import shared
import FirebaseDatabase

@MainActor
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
    private let chatRepository = ChatRepository()
    private var messageJob: Kotlinx_coroutines_coreJob?
    private let userRepo: UserRepository
    @Published var isLoading: Bool = false
    
    var recipientId: String
    let chatType: ChatType
    let memberList: [String]?
    var rideTitle: String?
    var rideId: String?
    @Published var recentChats: [Chat] = []
    private var recentChatsTask: Task<Void, Never>?
    @Published var usersById: [String: UserDomain] = [:]
    private let currentUserId: String
    var filteredChats: [Chat] {
        var chats = recentChats
        
        //search filter
        if !searchText.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
            let query = searchText.lowercased()
            chats = chats.filter {
                $0.name.lowercased().contains(query) ||
                $0.lastMessage.lowercased().contains(query)
            }
        }
        
        // category filter
        guard let selected = selectedCategory else { return chats }
        
        switch selected {
            
        case MessageCategory.All.rawValue:
            return chats
            
        case MessageCategory.Unread.rawValue:
            return chats.filter { $0.unreadCount > 0 }
            
        case MessageCategory.Groups.rawValue:
            return chats.filter { $0.isGroup }
            
        case MessageCategory.Favourites.rawValue:
            return chats.filter { $0.isFavourite }
            
        default:
            return chats
        }
    }
    
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
    func toggleFavourite(chatId: String) {

        guard let index = recentChats.firstIndex(where: { $0.id == chatId }) else { return }

        let currentValue = recentChats[index].isFavourite
        let newValue = !currentValue


        chatRepository.toggleFavorite (
            userId: currentUserId,
            chatRoomId: chatId,
            isFavorite: newValue
        )
        fetchRecentChats()
        recentChats[index].isFavourite = newValue
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

        let chatRoomId = chatRepository.getCanonicalChatId(
            uid1: currentUserId,
            uid2: recipientId
        )

        let textToSend = messageText
        messageText = ""

        Task {
            do {
                try await chatRepository.createOrGet1v1Chat(
                    userAId: currentUserId,
                    userBId: recipientId
                )

                chatRepository.sendMessage(
                    chatRoomId: chatRoomId,
                    senderId: currentUserId,
                    recipientId: recipientId,
                    text: textToSend
                )

                print("Message sent successfully")

                // Refresh recent chats
                await MainActor.run {
                    self.fetchRecentChats()
                }

            } catch {
                print("Chat creation failed:", error)
            }
        }
    }
    private func send1v1MessageViaKMP(chatRoomId: String) {
        let textToSend = messageText
        messageText = ""
        
        chatRepository.sendMessage(
            chatRoomId: chatRoomId,
            senderId: currentUserId,
            recipientId: recipientId,
            text: textToSend
        )
        
        print("Message sent successfully: \(currentUserId)-\(recipientId)")
    }
    
    func sendGroupMessage() {
        guard !messageText.isEmpty else { return }
        guard let memberList = memberList,
              let rideTitle = rideTitle,
              let rideId = rideId else {
            print("Group metadata missing")
            return
        }

        let textToSend = messageText
        messageText = ""

        Task {
            do {
                // Create or get group chat
                try await chatRepository.createOrGetGroupChat(
                    memberList: memberList,
                    rideID: rideId,
                    rideTitle: rideTitle
                )

                // Send group message
                chatRepository.sendGroupMessage(
                    rideId: rideId,
                    senderId: currentUserId,
                    text: textToSend,
                    allMemberIds: memberList
                )

                print("Group message sent successfully")

                // REFRESH recent chats so MessagesListView can see it
                await MainActor.run {
                    self.fetchRecentChats()
                }

            } catch {
                print("Group chat creation failed:", error)
            }
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
            self.usersById = Dictionary(uniqueKeysWithValues: users.map { ($0.uid, $0) })
        } else {
            print("Unexpected user data type")
        }
    }
    static func otherUserId(from chatId: String, currentUserId: String) -> String {
        let ids = chatId.split(separator: "_").map(String.init)
        return ids.first(where: { $0 != currentUserId }) ?? ""
    }
    static func otherUserName(
        from chatId: String,
        currentUserId: String,
        usersById: [String: UserDomain]
    ) -> String {
        let otherId = otherUserId(from: chatId, currentUserId: currentUserId)
        return usersById[otherId]?.name ?? "User"
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
                                
                                let mappedMessages: [LocalMessage] = messages.map { message in
                                    
                                    let senderName =
                                    message.senderId == self.currentUserId
                                    ? "You"
                                    : (self.usersById[message.senderId]?.name ?? "Unknown")
                                    
                                    return LocalMessage(
                                        id : message.id,
                                        text: message.text,
                                        isMe: message.senderId == self.currentUserId,
                                        time: self.formatTime(from: message.timestamp),
                                        senderName: senderName
                                    )
                                }
                                
                                // Update UI on MainActor
                                Task { @MainActor in
                                    Task { @MainActor in
                                        for msg in mappedMessages {
                                            if !self.messages.contains(where: { $0.id == msg.id }) {
                                                self.messages.append(msg)
                                            }
                                        }
                                        
                                        self.messages.sort { $0.id < $1.id }
                                    }
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
        Task { @MainActor in
            
            if usersById.isEmpty {
                try? await fetchAllUsers()
            }
            
            isLoading = true
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
                            let mappedChats = chatRooms
                                .sorted { $0.lastTimestamp > $1.lastTimestamp } .map { room in
                                    
                                    let isGroup = room.type == "group"
                                    
                                    let name: String
                                    let recipient: String?
                                    
                                    if isGroup {
                                        name = room.name ?? "Group"
                                        recipient = nil
                                    } else {
                                        let otherId = MessagesViewModel.otherUserId(from: room.id, currentUserId: self.currentUserId)
                                        name = self.usersById[otherId]?.name ?? "User"
                                        recipient = otherId
                                    }
                                    let unread: Int = {
                                        let rawMap = room.unreadCounts as NSDictionary
                                        let rawValue = rawMap[self.currentUserId]
                                        
                                        if let n = rawValue as? NSNumber {
                                            return n.intValue
                                        }
                                        
                                        if let k = rawValue as? KotlinLong {
                                            return Int(k.int64Value)
                                        }
                                        
                                        return 0
                                    }()
                                    let memberList: [String] = {
                                        let rawMap = room.members as NSDictionary
                                        return rawMap.allKeys.compactMap { $0 as? String }
                                    }()
                                    return Chat(
                                        id: room.id,
                                        recipientId: recipient,
                                        name: name,
                                        lastMessage: room.lastMessage,
                                        time: room.lastTimestamp > 0 ? self.formatTime(from: room.lastTimestamp) : "",
                                        unreadCount: unread,
                                        isGroup: isGroup,
                                        memberList: memberList,
                                        rideTitle: room.name,
                                        rideId: room.id,
                                        isFavourite: room.isFavorite
                                    )
                                }
                            
                            // Update UI directly on MainActor
                            Task { @MainActor in
                                print("Setting recentChats with \(mappedChats.count) items")
                                self.recentChats = mappedChats
                                self.isLoading = false
                            }
                        }
                    )
                } catch {
                    print(" Error fetching recent chats:", error)
                    await MainActor.run {
                        self.isLoading = false
                    }
                }
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
