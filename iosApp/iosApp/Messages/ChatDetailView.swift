//
//  ChatDetailView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 15/12/25.
//
import SwiftUI

struct ChatDetailView: View {

    @ObservedObject var viewModel: MessagesViewModel
       let chatName: String
       let isGroup: Bool
       let isOverlay: Bool
    let chatId: String
    
    @State private var showNotification = false
    @State private var showSlideBar = false
    @State var showHome: Bool = false
    @State var showBack: Bool = false
    @Environment(\.dismiss) private var dismiss

    var body: some View {
        VStack {
            if !isOverlay {
                    ChatHeaderView(chatName: chatName)
                }
            ScrollViewReader { proxy in
                ScrollView {
                    VStack(spacing: 12) {
                        ForEach(viewModel.messages) { message in
                            MessageBubbleView(message: message, isGroup: isGroup)
                                .id(message.id)
                        }
                    }
                    .padding()
                }
                .onAppear {
                    if let last = viewModel.messages.last {
                        proxy.scrollTo(last.id, anchor: .bottom)
                    }
                }
                .onChange(of: viewModel.messages.count) { _ in
                    if let last = viewModel.messages.last {
                        withAnimation {
                            proxy.scrollTo(last.id, anchor: .bottom)
                        }
                    }
                }
            }
            Divider()

            HStack {
                TextField("Type a message...", text:  $viewModel.messageText)
                    .padding(15)
                    .background(AppColor.white)
                    .font(KlavikaFont.regular.font(size: 14))
                    .foregroundColor(
                        viewModel.messageText.isEmpty
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
                    viewModel.sendMessage()
                } label: {
                    AppIcon.Chat.send
                        .padding(12)
                        .background(AppColor.celticBlue)
                        .clipShape(RoundedRectangle(cornerRadius: 15))
                }
            }
            .padding()
            .opacity(viewModel.messageText.isEmpty ? 0.4 : 1)
        }
        .if (!isOverlay) { view in
            view
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
}
extension View {
    @ViewBuilder
    func `if`<Content: View>(
        _ condition: Bool,
        transform: (Self) -> Content
    ) -> some View {
        if condition {
            transform(self)
        } else {
            self
        }
    }
}
