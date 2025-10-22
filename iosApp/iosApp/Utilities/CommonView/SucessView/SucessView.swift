//
//  SucessView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 19/10/25.
//

import SwiftUI

struct SucessView: View {
    let title:String
    let subtitle:String
    var body: some View {
        VStack(spacing: 15) {
            ZStack {
                Circle()
                    .fill(AppColor.darkCyanLimeGreen)
                    .frame(width: 100, height: 100)
                Image(systemName: "checkmark")
                    .font(.system(size: 40, weight: .medium))
                    .foregroundColor(.white)
            }
            
            Text(title)
                .font(KlavikaFont.medium.font(size: 20))
                .foregroundColor(AppColor.black)
            
            Text(subtitle)
                .font(KlavikaFont.regular.font(size: 14))
                .foregroundColor(AppColor.stoneGray)
        }
        .frame(height: 200)
        .frame(maxWidth: .infinity)
        .background(AppColor.white)
        .cornerRadius(10)
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.darkGray, lineWidth: 1)
        )
    }
}

#Preview {
    SucessView(title: "Ride Created!", subtitle: "Share your ride with friends")
}
