//
//  ButtonView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 30/09/25.
//

import SwiftUI

struct ButtonView: View {
    let title: String
    var icon: Image? = nil
    var background: LinearGradient? = nil
    var foregroundColor: Color = AppColor.white
    var showShadow: Bool = true
    var borderColor: Color? = nil
    var borderWidth: CGFloat = 1
    var onTap: (() -> Void)? = nil
    var height: CGFloat? = 60
    var body: some View {
        Button(action: {
            onTap?()
        }) {
            HStack{
                icon
                Text(title)
            }
            .frame(maxWidth: .infinity)
            .frame(height: height)
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
            .overlay(
                RoundedRectangle(cornerRadius: 15)
                    .stroke(borderColor ?? .clear, lineWidth: borderColor == nil ? 0 : borderWidth)
            )
            .shadow(color: showShadow ? Color.black.opacity(0.20) : .clear,
                    radius: showShadow ? 4 : 0,
                    x: 0, y: showShadow ? 2 : 0)
            .foregroundStyle(foregroundColor)
            .font(KlavikaFont.bold.font(size: 18))
            .padding(.bottom,20)
            
        }
        .buttonStyle(.plain)
        .containerShape(Rectangle())
    }
}


#Preview {
    ButtonView(title: "CREATE A RIDE", icon: AppIcon.Home.createRide)
}


