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
    let name: String
    let lastMessage: String
    let time: String
    var unreadCount: Int
    let isGroup: Bool
}

struct LocalMessage: Identifiable {
    let id = UUID()
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
                    
                    
                    ScrollView {
                        VStack(spacing: 12) {
                            ForEach(viewModel.recentChats) { chat in
                                NavigationLink {
                                    let detailVM = MessagesViewModel(
                                        currentUserId:  MBUserDefaults.userIdStatic ?? "", recipientId: chat.id,
                                        chatType: chat.isGroup ? .group : .private,
                                    )

                                    ChatDetailView(
                                        viewModel: detailVM,
                                        chatName: chat.isGroup
                                        ? chat.name
                                        : (viewModel.usersById[chat.id]?.name ?? chat.name),
                                        isGroup: chat.isGroup,
                                        isOverlay: false
                                    )
                                    .onAppear {
                                        detailVM.receiveMessageFromKMP(chatRoomId: chat.id)
                                        detailVM.markChatAsRead(chatRoomId: chat.id)
                                    }
                                }label: {
                                    ChatRowView(chat: chat)
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
                .task {
                        viewModel.fetchRecentChats()
                }
            }
            .navigationBarBackButtonHidden(true)
        }
    }
}
