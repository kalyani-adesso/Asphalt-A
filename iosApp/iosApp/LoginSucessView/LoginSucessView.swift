//
//  LoginSucessView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 29/09/25.
//

import Foundation
import SwiftUI

struct LoginSucessView: View {
    var body: some View {
        VStack {
            AppImage.Login.loginSuccessBg
                .padding(.bottom,82)
                .frame(height: 278)
            Text(AppStrings.SignInSucessView.loginSuccessTitle.rawValue)
                .font(KlavikaFont.bold.font(size: 24))
                .padding(.bottom,16)
                .foregroundStyle(AppColor.black)
            Text(AppStrings.SignInSucessView.loginSuccessSubtitle.rawValue)
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundStyle(AppColor.stoneGray)
                .padding(.bottom,82)
                .frame(alignment: .center)
                .multilineTextAlignment(.center)
                .lineSpacing(5)
            NavigationLink(destination: HomeScreen()) {
                Text(AppStrings.SignInSucessView.exploreButton.rawValue)
                    .frame(maxWidth: .infinity)
                    .frame(height: 60)
                    .background(
                        LinearGradient(
                            gradient: Gradient(colors: [
                                AppColor.royalBlue,
                                AppColor.pursianBlue,
                            ]),
                            startPoint: .leading,
                            endPoint: .trailing
                        )
                    )
                    .cornerRadius(15)
                    .shadow(color: Color.black.opacity(0.20), radius: 4, x: 0, y: 2)
                    .foregroundStyle(AppColor.white)
                    .font(KlavikaFont.bold.font(size: 18))
            }
            .padding(.bottom, 21)
        }
        .padding(.horizontal, 24)
        .navigationBarBackButtonHidden(true)
        .padding(.top, 100)
    }
}

struct LoginSucessView_Previews: PreviewProvider {
    static var previews: some View {
        LoginSucessView()
    }
}
