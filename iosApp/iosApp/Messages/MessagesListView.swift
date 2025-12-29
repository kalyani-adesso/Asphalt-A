//
//  MessageListView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 15/12/25.
//

import SwiftUI
import Foundation

struct Chat: Identifiable {
    let id = UUID()
    let name: String
    let lastMessage: String
    let time: String
    let unreadCount: Int
    let isGroup: Bool
}

struct Message: Identifiable {
    let id = UUID()
    let text: String
    let isMe: Bool
    let time: String
    let senderName: String?
}


struct MessagesListView: View {
    
    @StateObject private var viewModel = MessagesViewModel()
    @State private var showNotification = false
    @State private var showSlideBar = false
    @State var showHome: Bool = false
    @State var showBack: Bool = false
    @Environment(\.dismiss) private var dismiss
    
    let chats: [Chat] = [
        Chat(name: "Sooraj",
             lastMessage: "See you at the meeting point!",
             time: "10:45 AM",
             unreadCount: 2,
             isGroup: false),
        
        Chat(name: "Abhishek",
             lastMessage: "See you at the meeting point!",
             time: "10:45 AM",
             unreadCount: 2,
             isGroup: false),
        
        Chat(name: "Vyshnav",
             lastMessage: "See you at the meeting point!",
             time: "10:45 AM",
             unreadCount: 2,
             isGroup: false),
        
        Chat(name: "Group Chat",
             lastMessage: "See you at the meeting point!",
             time: "10:45 AM",
             unreadCount: 2,
             isGroup: true)
    ]
    
    var body: some View {
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
                            ForEach(chats) { chat in
                                NavigationLink {
                                    ChatDetailView(chatName: chat.name, isGroup: chat.isGroup,   isOverlay: false)
                                } label: {
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
                    .frame(width: 355, height: 400)
                }
        }
        .navigationBarBackButtonHidden(true)
        .toolbar {
            ToolbarItemGroup(placement: .navigationBarLeading) {
                Button {
                    showHome = true
                    
                } label: {
                    AppIcon.CreateRide.backButton
                }
                
            }
            ToolbarItemGroup(placement: .navigationBarTrailing) {
                Button {
                    showNotification = true
                } label: {
                    ZStack(alignment: .topTrailing) {
                        Image(systemName: "bell")
                            .font(.system(size: 15))
                            .foregroundColor(AppColor.celticBlue)
                        Circle()
                            .fill(Color.red)
                            .frame(width: 8, height: 8)
                            .offset(x: -2, y: 1)
                    }
                }
                
                Button {
                    showSlideBar = true
                } label: {
                    AppIcon.Home.navigation
                }
            }
            
        }
        .navigationDestination(isPresented: $showSlideBar, destination: {
            NavigationSlideBar()
        })
        .navigationDestination(isPresented: $showNotification, destination: {
            NotificationView()
        })
        .navigationDestination(isPresented: $showHome, destination: {
            BottomNavBar()
        })
    }
}

#Preview {
    MessagesListView()
}
