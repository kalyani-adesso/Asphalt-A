//
//  FormFieldView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 30/09/25.
//

import SwiftUI

struct EmailFormFieldView: View {
    let label: String
    let icon : Image
    let placeholder: String
    @Binding var emailOrPhone: String
     @Binding var isValidEmail: Bool
    
    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text(label)
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundStyle(AppColor.black)
            HStack {
                icon
                    .frame(width: 20, height: 20)
                TextField(placeholder, text: $emailOrPhone)
                    .onChange(of: emailOrPhone) { value in
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
            .background(AppColor.backgroundLight)
            .cornerRadius(10)
        }
    }
}

#Preview {
    EmailFormFieldView(
        label: AppStrings.SignInLabel.emailOrPhone.rawValue,
        icon: AppIcon.Login.email,
        placeholder: AppStrings.SignInPlaceholder.email.rawValue,
        emailOrPhone: .constant(""),
        isValidEmail: .constant(false)
    )
}
