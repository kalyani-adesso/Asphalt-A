//
//  MessageListView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 15/12/25.
//

import SwiftUI
import Foundation

struct Chat: Identifiable {
    let id: String
    let recipientId: String?
    let name: String
    let lastMessage: String
    let time: String
    var unreadCount: Int
    let isGroup: Bool
    let memberList: [String]?
        let rideTitle: String?
        let rideId: String?
    var isFavourite: Bool = false
}

struct LocalMessage: Identifiable {
    let id : String
    let text: String
    let isMe: Bool
    let time: String
    let senderName: String?
}


struct MessagesListView: View {
    
    @ObservedObject var viewModel: MessagesViewModel
    @State private var showNotification = false
    @State private var showSlideBar = false
    @State var showHome: Bool = false
    @State var showBack: Bool = false
    
    var emptyStateTitle: String {
        switch viewModel.selectedCategory {
        case MessagesViewModel.MessageCategory.Unread.rawValue:
            return "No unread messages"
        case MessagesViewModel.MessageCategory.Groups.rawValue:
            return "No group chats"
        case MessagesViewModel.MessageCategory.Favourites.rawValue:
            return "No favourite chats"
        default:
            return "No messages yet"
        }
    }
    
    var emptyStateSubtitle: String {
        switch viewModel.selectedCategory {
        case MessagesViewModel.MessageCategory.Unread.rawValue:
            return "You're all caught up "
        case MessagesViewModel.MessageCategory.Groups.rawValue:
            return "You haven't joined any groups"
        case MessagesViewModel.MessageCategory.Favourites.rawValue:
            return "Mark chats as favourite to see them here"
        default:
            return "Start a conversation to see chats here"
        }
    }
    
    var body: some View {
        AppToolBar(showBack: true){
            NavigationStack {
                
                VStack(spacing: 0) {
                    ReusableHeader {
                        Text("Messages")
                            .font(KlavikaFont.bold.font(size: 22))
                            .foregroundColor(AppColor.black)
                    } trailing: {
                        EmptyView()
                    }
                    VStack(spacing: 20) {
                        FormFieldView(
                            label: " ",
                            icon: AppIcon.CreateRide.searchLens,
                            placeholder: AppStrings.Chat.searchLabel,
                            iconColor: AppColor.celticBlue,
                            value: $viewModel.searchText,
                            isValidEmail: .constant(false),
                            backgroundColor: AppColor.listGray
                        )
                        
                        ScrollView(.horizontal, showsIndicators: false) {
                            HStack(spacing: 10) {
                                ForEach(viewModel.messageStatus, id: \.self) { status in
                                    let isSelected = viewModel.selectedCategory == status.rawValue
                                    QSegmentButtonView(
                                        rideStatus: status.rawValue,
                                        isSelected: isSelected
                                    ) {
                                        if viewModel.selectedCategory == status.rawValue {
                                            viewModel.selectedCategory = nil
                                        } else {
                                            viewModel.selectedCategory = status.rawValue
                                        }
                                    }
                                }
                            }
                            .padding(.horizontal, 10)
                            .padding(.vertical, 15)
                        }
                        .background(
                            RoundedRectangle(cornerRadius: 10)
                                .fill(AppColor.listGray)
                        )
                    }
                    .padding(.horizontal, 20)
                    .zIndex(1)
                    .padding(.bottom, 20)
                    if viewModel.isLoading {
                        
                        Spacer()
                        
                        ProgressView("Loading chats...")
                            .scaleEffect(1.2)
                        
                        Spacer()
                        
                    } else if viewModel.filteredChats.isEmpty {
                        
                        Spacer()
                        
                        VStack(spacing: 12) {
                            Image(systemName: "bubble.left.and.bubble.right")
                                .font(.system(size: 40))
                                .foregroundColor(.gray.opacity(0.6))
                            
                            Text(emptyStateTitle)
                                .font(KlavikaFont.bold.font(size: 16))
                                .foregroundColor(AppColor.black)
                            
                            Text(emptyStateSubtitle)
                                .font(KlavikaFont.regular.font(size: 13))
                                .foregroundColor(AppColor.stoneGray)
                        }
                        .multilineTextAlignment(.center)
                        .padding(.horizontal, 40)
                        
                        Spacer()
                        
                    } else {
                        
                        
                        ScrollView {
                            VStack(spacing: 12) {
                                ForEach(viewModel.filteredChats) { chat in
                                    NavigationLink {
                                        ChatDetailContainer(chat: chat)
                                    }label: {
                                        ChatRowView(
                                            chat: chat,
                                            onFavouriteTapped: {
                                            viewModel.toggleFavourite(chatId: chat.id)
                                        })
                                    }
                                    .buttonStyle(.plain)
                                }
                            }
                            .padding(.horizontal, 16)
                            .padding(.top, 20)
                        }
                        
                        .background(
                            RoundedRectangle(cornerRadius: 10)
                                .fill(AppColor.listGray)
                        )
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                    }
                }
            }
            .onAppear {
                Task {
                    try? await viewModel.fetchAllUsers()
                    viewModel.fetchRecentChats()
                }
            }
            .navigationBarBackButtonHidden(true)
        }
    }
}
struct ChatDetailContainer: View {

    let chat: Chat
    @StateObject private var viewModel: MessagesViewModel

    init(chat: Chat) {
        let currentUserId = MBUserDefaults.userIdStatic ?? ""
        let recipient = chat.isGroup
            ? "" : MessagesViewModel.otherUserId(from: chat.id, currentUserId: currentUserId)
        _viewModel = StateObject(
            wrappedValue: MessagesViewModel(
                currentUserId: currentUserId,
                recipientId: recipient,
                chatType: chat.isGroup ? .group : .private,
                memberList: chat.memberList,
                rideTitle: chat.rideTitle,
                rideId: chat.rideId
            )
        )

        self.chat = chat
    }

    var body: some View {
        ChatDetailView(
            viewModel: viewModel,
            chatName: chat.name,
            isGroup: chat.isGroup,
            isOverlay: false,
            chatId: chat.id
        )
        .onAppear {
            viewModel.receiveMessageFromKMP(chatRoomId: chat.id)
            viewModel.markChatAsRead(chatRoomId: chat.id)
        }
    }
}
