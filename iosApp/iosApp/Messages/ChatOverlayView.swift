//
//  ChatOverlayView.swift
//  iosApp
//
//  Created by Selvan, Lavanya on 09/03/26.
//

import SwiftUI

struct ChatOverlayView: View {

    let chat: ActiveChat
    @ObservedObject var viewModel: MessagesViewModel
    var onClose: () -> Void
    @State private var keyboardHeight: CGFloat = 0
    @State private var safeAreaTop: CGFloat = 0

    var body: some View {
        ZStack {

            Color.black.opacity(0.45)
                .ignoresSafeArea()
                .onTapGesture {
                    withAnimation(.easeInOut) {
                        onClose()
                    }
                }

            VStack(spacing: 0) {

                header

                ChatDetailView(
                    viewModel: viewModel,
                    chatName: chat.name,
                    isGroup: chat.chatType == .group,
                    isOverlay: true,
                    chatId: chat.id
                )
                .onAppear {
                    viewModel.receiveMessageFromKMP(chatRoomId: chat.id)
                }
            }
            .frame(
                width: UIScreen.main.bounds.width - 32,
                height: popupHeight
            )
            .background(AppColor.backgroundLight)
            .cornerRadius(22)
            .shadow(radius: 20)
            .offset(y: -keyboardHeight / 2)
            .animation(.easeOut(duration: 0.25), value: keyboardHeight)
            .background(
                GeometryReader { geo in
                    Color.clear
                        .onAppear {
                            safeAreaTop = geo.safeAreaInsets.top
                        }
                }
            )
            .onAppear {
                NotificationCenter.default.addObserver(
                    forName: UIResponder.keyboardWillShowNotification,
                    object: nil,
                    queue: .main
                ) { notification in
                    if let frame = notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? CGRect {
                        keyboardHeight = frame.height
                    }
                }

                NotificationCenter.default.addObserver(
                    forName: UIResponder.keyboardWillHideNotification,
                    object: nil,
                    queue: .main
                ) { _ in
                    keyboardHeight = 0
                }
            }
            .onDisappear {
                NotificationCenter.default.removeObserver(self)
            }
        }
    }
    var popupHeight: CGFloat {
        let baseHeight = UIScreen.main.bounds.height * 0.6
        
        if keyboardHeight > 0 {
            return max(baseHeight - keyboardHeight * 0.3, 300)
        } else {
            return baseHeight
        }
    }
    private var header: some View {
        HStack(spacing: 12) {

            ZStack(alignment: .bottomTrailing) {

                AppImage.Profile.profile
                    .resizable()
                    .frame(width: 37, height: 37)
                    .clipShape(Circle())
                    .overlay(
                        RoundedRectangle(cornerRadius: 32.5)
                            .stroke(AppColor.green, lineWidth: 2.5)
                    )

                Circle()
                    .fill(Color.green)
                    .frame(width: 13, height: 13)
                    .offset(x: 2, y: 2)
                    .overlay(
                        Circle()
                            .offset(x: 2, y: 2)
                            .stroke(Color.white, lineWidth: 1.5)
                    )
            }

            VStack(alignment: .leading) {
                Text(chat.name)
                    .font(KlavikaFont.bold.font(size: 16))
                    .foregroundColor(.white)
            }

            Spacer()

            Button {
                withAnimation {
                    onClose()
                }
            } label: {
                Image(systemName: "xmark")
                    .foregroundColor(.white)
                    .padding(8)
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .background(AppColor.celticBlue)
    }
}
