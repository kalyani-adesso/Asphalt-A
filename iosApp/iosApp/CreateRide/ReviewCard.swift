//
//  ReviewCard.swift
//  iosApp
//
//  Created by Lavanya Selvan on 14/10/25.
//

import SwiftUI

struct ReviewCard: View {
    
    let icon: Image
    let iconColor: Color
    let title: String
    let subtitle: String
    var tag: String? = nil
    
    var body: some View {
        HStack(alignment: .top, spacing: 12) {
            icon
                .resizable()
                .renderingMode(.template)
                .foregroundColor(AppColor.white)
                .frame(width: 20, height: 20)
                .padding(8)
                .background(iconColor)
                .cornerRadius(5)
            
            VStack(alignment: .leading, spacing: 4) {
                HStack {
                    Text(title)
                        .font(KlavikaFont.bold.font(size: 16))
                        .foregroundColor(AppColor.black)
                    if let tag = tag {
                        Spacer()
                        Text(tag)
                            .foregroundColor(AppColor.darkGreen)
                            .font(KlavikaFont.regular.font(size: 13))
                            .padding(.vertical, 4)
                            .padding(.horizontal, 8)
                            .background(AppColor.lightGreen)
                            .cornerRadius(6)
                    }
                }
                Text(subtitle)
                    .font(KlavikaFont.regular.font(size: 14))
                    .foregroundColor(AppColor.richBlack.opacity(0.8))
            }
            Spacer()
        }
        .padding()
        .background(Color.white)
        .cornerRadius(10)
    }
}

#Preview {
    ReviewCard(icon: AppIcon.CreateRide.route, iconColor: AppColor.purple, title: "Route", subtitle: "Kochi - Munnar")
}
