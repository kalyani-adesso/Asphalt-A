//
//  CustomNavBar.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct TopNavBar: View {
    @EnvironmentObject var home: HomeViewModel
    @State var showNotification: Bool = false
    var body: some View {
        HStack(alignment: .center , spacing: 20) {
            AppImage.Profile.profile.resizable()
                .frame(width: 63, height: 63)
                .clipShape(Circle())
                .overlay(
                    RoundedRectangle(cornerRadius: 32.5)
                        .stroke(AppColor.lightGray, lineWidth: 2.5)
                )
            
            VStack(alignment: .leading, spacing: 4) {
                Text("Hello")
                    .font(KlavikaFont.light.font(size: 16))
                    .foregroundColor(AppColor.black)
                
                Text(MBUserDefaults.userNameStatic ?? "Balagopalakrishnan")
                    .font(KlavikaFont.medium.font(size: 28))
                    .foregroundColor(AppColor.black)
                
                HStack(spacing: 5) {
                    AppIcon.Home.badge
                        .font(.system(size: 17))
                    
                    Text("Level 4 - Rider")
                        .font(KlavikaFont.regular.font(size: 14))
                        .foregroundColor(AppColor.black)
                }
            }
            Spacer()
        }
        .task{
            home.loadUserName()
        }
    }
}

#Preview {
    TopNavBar()
}
