//
//  Snackbar.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct Snackbar: View {
    
    let message: String
    let subMessage: String
    var background: LinearGradient? = nil
    var icon: Image? = nil
    
    var body: some View {
        
        VStack {
            HStack(alignment: .center, spacing: 10) {
                icon ?? AppIcon.SignUp.snackbar
                
                VStack(alignment: .leading, spacing: 5) {
                    Text(message)
                        .font(KlavikaFont.bold.font(size: 14))
                        .foregroundColor(.white)
                    Text(subMessage)
                        .font(KlavikaFont.regular.font(size: 12))
                        .foregroundColor(.white)
                }
                Spacer()
            }
            .padding(.all, 15)
            .background(
                background ??
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
            .padding(.horizontal, 16)
            .shadow(radius: 4)
        }
    }
}

#Preview {
    Snackbar(message: "", subMessage: "")
}
