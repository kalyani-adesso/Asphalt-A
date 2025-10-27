//
//  CustomNavBar.swift
//  iosApp
//
//  Created by Lavanya Selvan on 22/10/25.
//

import SwiftUI

struct SimpleCustomNavBar: View {
    var title: String
    var onBackToHome: (() -> Void)?
    var body: some View {
        ZStack {
            HStack {
                Button(action: { onBackToHome?() }) {
                    AppIcon.CreateRide.backButton
                        .resizable()
                        .frame(width: 24, height: 24)
                        .padding(.leading, 4)
                }
                Spacer()
                Text(title)
                    .font(KlavikaFont.bold.font(size: 19))
                    .foregroundColor(AppColor.black)
                Spacer()
                
            }
        }
        .padding(.horizontal)
        .frame(height: 56)
        .background(AppColor.white)
        .shadow(color: Color.black.opacity(0.08), radius: 4, x: 0, y: 2)
    }
}



#Preview {
    SimpleCustomNavBar(title: "Home")
}
