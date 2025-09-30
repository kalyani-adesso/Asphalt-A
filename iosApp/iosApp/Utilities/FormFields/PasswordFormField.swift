//
//  PasswordFormField.swift
//  iosApp
//
//  Created by Lavanya Selvan on 30/09/25.
//

import SwiftUI

struct PasswordFormField: View {
    
    let label: String
    let icon : Image
    let placeholder: String
    @Binding var password: String
    @State private var showPassword = false
    @State private var isPasswordEntering = false
    
    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text(label)
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundStyle(AppColor.black)
            HStack {
                if showPassword {
                   icon
                        .frame(width: 20, height: 20)
                    TextField(placeholder, text: $password)
                        .font(KlavikaFont.regular.font(size: 16))
                        .autocapitalization(.none)
                        .foregroundStyle(AppColor.richBlack)
                } else {
                    (isPasswordEntering ? AppIcon.Login.passwordEncrypt : AppIcon.Login.password)
                        .frame(width: 20, height: 20)
                    SecureField(AppStrings.SignInPlaceholder.password.rawValue, text: $password)
                        .onChange(of: password) { value in
                            isPasswordEntering = !value.isEmpty
                        }
                        .font(KlavikaFont.regular.font(size: 16))
                        .autocapitalization(.none)
                }
                
                if isPasswordEntering {
                    Button(action: { showPassword.toggle() }) {
                        Image(systemName: showPassword ? "eye" : "eye.slash")
                            .foregroundColor(.stoneGray)
                            .frame(width: 20, height: 20)
                    }
                }
            }
            .padding()
            .background(AppColor.backgroundLight)
            .cornerRadius(10)
        }
    }
}

#Preview {
    PasswordFormField(
        label: AppStrings.SignInLabel.password.rawValue,
        icon: AppIcon.Login.password,
        placeholder: AppStrings.SignInPlaceholder.password.rawValue,
        password: .constant("")
    )
}
