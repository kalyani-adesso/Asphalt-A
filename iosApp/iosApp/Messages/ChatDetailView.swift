//
//  ChatDetailView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 15/12/25.
//

import SwiftUI

struct ChatDetailView: View {

    let chatName: String
    let subtitle: String
    let isGroup: Bool

    @State private var messageText = ""
    @State private var showNotification = false
    @State private var showSlideBar = false
    @State var showHome: Bool = false
    @State var showBack: Bool = false
    @Environment(\.dismiss) private var dismiss

    let messages: [Message] = [
        Message(text: "Hey! Looking forward to the ride!", isMe: false, time: "10:30 AM", senderName: "Sooraj"),
        Message(text: "Same here! It's going to be amazing!", isMe: true, time: "10:32 AM", senderName: nil),
        Message(text: "Will catch you there.", isMe: false, time: "10:38 AM", senderName: "Vyshnav")
    ]

    var body: some View {
        VStack {
            ChatHeaderView(chatName: chatName, subtitle: subtitle)
            ScrollView {
                VStack(spacing: 12) {
                    ForEach(messages) { message in
                        MessageBubbleView(message: message,isGroup: isGroup)
                    }
                }
                .padding()
            }

            Divider()

            HStack {
                TextField("Type a message...", text: $messageText)
                    .padding(15)
                    .background(AppColor.white)
                    .font(KlavikaFont.regular.font(size: 14))
                    .foregroundColor(
                            messageText.isEmpty
                            ? AppColor.grey
                            : AppColor.black
                        )
                    .cornerRadius(15)
                    .tint(AppColor.black) 
                    .overlay(
                        RoundedRectangle(cornerRadius: 15)
                            .stroke(AppColor.celticBlue, lineWidth: 1)
                    )
                    .frame(width: 292, height: 45)

                Button {
                    messageText = ""
                } label: {
                    AppIcon.Chat.send
                        .padding(12)
                        .background(AppColor.celticBlue)
                        .clipShape(RoundedRectangle(cornerRadius: 15))
                }
            }
            .padding()
            .opacity(messageText.isEmpty ? 0.4 : 1)
        }
        .toolbar {
                ToolbarItemGroup(placement: .navigationBarLeading) {
                    Button {
                            dismiss()
                        
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
        .navigationBarBackButtonHidden(true)
    }
}




