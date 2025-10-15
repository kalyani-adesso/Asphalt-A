//  FormFieldView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 30/09/25.
//

import SwiftUI

struct FormFieldView: View {
    let label: String
    let icon : Image
    let placeholder: String
    var iconColor: Color? = nil
   
    @Binding var value: String
    @Binding var isValidEmail: Bool
    
    var backgroundColor: Color = AppColor.backgroundLight
    
    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text(label)
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundStyle(AppColor.black)
            HStack {
                icon
                    .renderingMode(iconColor == nil ? .original : .template)
                    .foregroundColor(iconColor ?? .primary)
                    .frame(width: 20, height: 20)
                TextField(placeholder, text: $value)
                    .onChange(of: value) { value in
                        isValidEmail = value.isValidEmail
                    }
                    .font(KlavikaFont.regular.font(size: 16))
                    .autocapitalization(.none)
                    .foregroundStyle(AppColor.richBlack)
                if isValidEmail {
                    AppIcon.Login.validEmail
                        .frame(width: 20, height: 20)
                }
            }
            .padding()
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(backgroundColor)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(isValidEmail ? AppColor.celticBlue : .clear, lineWidth: 1.5) // border only when valid
            )
            .cornerRadius(10)
        }
    }
}

#Preview {
    FormFieldView(
        label: AppStrings.SignInLabel.emailOrPhone.rawValue,
        icon: AppIcon.Login.email,
        placeholder: AppStrings.SignInPlaceholder.email.rawValue,
        iconColor: AppColor.stoneGray,
        value: .constant(""),
        isValidEmail: .constant(false)
    )
}
