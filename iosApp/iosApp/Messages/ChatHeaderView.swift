//
//  ChatHeaderView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 15/12/25.
//

import SwiftUI

struct ChatHeaderView: View {
    let chatName: String
    let subtitle: String
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


            VStack(alignment: .leading, spacing: 2) {
                Text(chatName)
                    .font(KlavikaFont.bold.font(size: 16))
                    .foregroundColor(AppColor.white)

                Text(subtitle)
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(AppColor.white)
            }

            Spacer()
        }
        .padding()
        .background(AppColor.celticBlue)
        .frame(width: 400, height: 72)
    }
}


#Preview {
    ChatHeaderView(chatName: "Sooraj",  subtitle: "Weekend Coastal Ride")
}
