//
//  HeaderView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 30/09/25.
//

import SwiftUI

struct HeaderView: View {
    
    let title : String
    let subtitle : String
    
    var body: some View {
        Text(title)
            .font(KlavikaFont.bold.font(size: 22))
            .padding(.bottom,11)
            .foregroundStyle(AppColor.black)
        Text(subtitle)
            .font(KlavikaFont.medium.font(size: 16))
            .foregroundStyle(AppColor.stoneGray)
            .padding(.bottom,18)
        AppImage.Logos.rider
            .resizable()
            .frame(width: 50, height: 60)
            .padding(16)
        Text(AppStrings.SignInLabel.clubName.rawValue)
            .font(KlavikaFont.medium.font(size: 22))
            .padding(.bottom,42)
    }
}

#Preview {
    HeaderView(title: "", subtitle: "")
}
