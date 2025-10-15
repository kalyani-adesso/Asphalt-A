//
//  NotificationView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 08/10/25.
//

import SwiftUI

struct NotificationView: View {
    @StateObject var viewModel = NotificationViewModel()
    var body: some View {
        VStack {
            List(viewModel.notifications, id: \.id) { notification in
                HStack(spacing: 17) {
                    notification.image!
                        .resizable()
                        .frame(width: 30, height: 30)
                    VStack(alignment: .leading, spacing: 5) {
                        HStack {
                            Text(notification.title)
                                .font(KlavikaFont.bold.font(size: 16))
                                .foregroundColor(AppColor.black)
                            Spacer()
                            Text(notification.time)
                                .font(KlavikaFont.regular.font(size: 12))
                                .foregroundColor(AppColor.stoneGray)
                        }
                        Text(notification.message)
                            .font(KlavikaFont.regular.font(size: 12))
                            .foregroundColor(AppColor.stoneGray)
                    }
                }.padding([.leading, .trailing],16)
                    .frame(height: 74)
                    .cornerRadius(10)
                    .listRowSeparator(.hidden)
                    .background(
                        RoundedRectangle(cornerRadius: 8)
                            .fill(notification.title == AppStrings.Notification.rideReminder.localized ? AppColor.iceBlue : AppColor.backgroundLight)
                    )
                
            }.listStyle(.plain)
        }
        .onAppear {
            viewModel.fetchNotifications()
        }
        .navigationTitle(AppStrings.Notification.notifications.localized)
        .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
    NotificationView()
}
