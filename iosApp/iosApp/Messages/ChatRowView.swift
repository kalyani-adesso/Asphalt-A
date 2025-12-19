//
//  ChatRowView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 15/12/25.
//

import SwiftUI

struct ChatRowView: View {
    let chat: Chat

    var body: some View {
        HStack(spacing: 12) {

            ZStack(alignment: .bottomTrailing) {
                AppImage.Profile.profile.resizable()
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

            VStack(alignment: .leading, spacing: 4) {
                Text(chat.name)
                    .font(KlavikaFont.bold.font(size: 14))
                    .foregroundColor(AppColor.black)

                Text(chat.subtitle)
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(AppColor.celticBlue)

                Text(chat.lastMessage)
                    .font(KlavikaFont.regular.font(size: 13))
                    .foregroundColor(AppColor.stoneGray)
                    .lineLimit(1)
            }

            Spacer()

            VStack(alignment: .trailing, spacing: 6) {
                Text(chat.time)
                    .font(KlavikaFont.regular.font(size: 11))
                    .foregroundColor(AppColor.stoneGray)
                
                if chat.unreadCount > 0 {
                    Text("\(chat.unreadCount)")
                        .font(KlavikaFont.bold.font(size: 10))
                        .foregroundColor(AppColor.white)
                        .frame(width: 10, height: 10)
                        .padding(6)
                        .background(Color.red)
                        .clipShape(Circle())
                }
            }
        }
        .padding()
        .background(Color.white)
        .cornerRadius(10)
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.darkGray, lineWidth: 1)
        )
    }
}

#Preview {
    ChatRowView(chat: Chat(name: "Sooraj",
                           subtitle: "Weekend Coastal Ride",
                           lastMessage: "See you at the meeting point!",
                           time: "10:45 AM",
                           unreadCount: 2,
                           isGroup: false))
}


