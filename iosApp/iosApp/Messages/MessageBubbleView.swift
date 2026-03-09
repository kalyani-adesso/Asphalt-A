//
//  MessageBubbleView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 15/12/25.
//
//

import SwiftUI

struct MessageBubbleView: View {
    let message: LocalMessage
    let isGroup: Bool
    
    var body: some View {
        HStack(alignment: .bottom, spacing: 8) {
            
            if !message.isMe {
                AppImage.Profile.profile.resizable()
                    .frame(width: 37, height: 37)
                    .clipShape(Circle())
            }
            VStack(alignment: .leading, spacing: 6) {
                if isGroup, !message.isMe, let name = message.senderName {
                    Text(name)
                        .font(KlavikaFont.regular.font(size: 11))
                        .foregroundColor(AppColor.celticBlue)
                }
                
                Text(message.text)
                    .font(KlavikaFont.regular.font(size: 13))
                    .foregroundColor(message.isMe ? .white : .black)
                
                Text(message.time)
                    .font(KlavikaFont.regular.font(size: 10))
                    .foregroundColor(
                        message.isMe
                        ? Color.white.opacity(0.8)
                        : Color.gray
                    )
            }
            .padding(12)
            .background(
                RoundedRectangle(cornerRadius: 14)
                    .fill(message.isMe ? AppColor.celticBlue : AppColor.white)
                    .shadow(
                        color: Color.black.opacity(
                            message.isMe ? 0.18 : 0.12
                        ),
                        radius: 4,
                        x: 0,
                        y: 2
                    )
            )
            .frame(maxWidth: 500,alignment: message.isMe ? .trailing : .leading)
          
            if message.isMe {
                Spacer()
            }
        }
        .frame(
                   maxWidth: .infinity,
                   alignment: message.isMe ? .trailing : .leading
               )
        .padding(.vertical, 4)
    }
}
