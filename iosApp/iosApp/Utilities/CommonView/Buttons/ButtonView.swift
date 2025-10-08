//
//  ButtonView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 30/09/25.
//

import SwiftUI

struct ButtonView: View {
    let title: String
    var onTap: (() -> Void)? = nil
    var body: some View {
        Button(action: {
            onTap?()
        }) {
            Text(title)
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
                .padding(.bottom, 21)
        }
    }
}
