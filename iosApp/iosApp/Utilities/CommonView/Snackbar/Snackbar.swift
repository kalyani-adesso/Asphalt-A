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
    let icon: Image
    var background: Color? = nil
    var foregroundColor: Color = AppColor.white
    var showShadow: Bool = true
    var borderColor: Color? = nil
    var borderWidth: CGFloat = 1
    var onTap: (() -> Void)? = nil
    var height: CGFloat? = 50
    var body: some View {
        
        VStack {
            HStack(alignment: .center, spacing: 10) {
                icon
                
                VStack(alignment: .leading, spacing: 5) {
                    Text(message)
                        .font(KlavikaFont.bold.font(size: 14))
                        .foregroundColor(foregroundColor)
                    Text(subMessage)
                        .font(KlavikaFont.regular.font(size: 12))
                        .foregroundColor(foregroundColor)
                }
                Spacer()
            }
            .padding(.all, 15)
            .background(
                background
            )
            .cornerRadius(15)
            .overlay(
                RoundedRectangle(cornerRadius: 15)
                    .stroke(borderColor ?? .clear, lineWidth: borderColor == nil ? 0 : borderWidth)
            )
            .shadow(color: showShadow ? Color.black.opacity(0.20) : .clear,
                    radius: showShadow ? 4 : 0,
                    x: 0, y: showShadow ? 2 : 0)
            .cornerRadius(12)
            .padding(.horizontal, 16)
            
        }
    }
}

#Preview {
    Snackbar(message: "Ride succesful", subMessage: "completed", icon:  AppIcon.ConnectedRide.checkmark)
}
