//
//  CreateAccount.swift
//  iosApp
//
//  Created by Lavanya Selvan on 29/09/25.
//

import SwiftUI

struct CreateAccount: View {

    @State private var firstName: String = ""
    @State private var lastName: String = ""
    @State private var password: String = ""
    @State private var confirmPassword: String = ""
    @State private var isPasswordEntering: Bool = false
    @State private var isConfirmPasswordEntering: Bool = false
    @State private var showPassword: Bool = false
    @State private var showConfirmPassword: Bool = false

    var body: some View {
        VStack {
            // Header
            HeaderView(
                title: AppStrings.SignUpLabel.welcome.rawValue,
                subtitle: AppStrings.SignUpLabel.welcomeSubtitle.rawValue
            )

            // Form fields
            VStack(spacing: 25) {
                EmailFormFieldView(
                    label: AppStrings.CreateAccountLabel.firstName.rawValue,
                    icon: AppIcon.SignUp.userDetail,
                    placeholder: AppStrings.SignInPlaceholder.email.rawValue,
                    emailOrPhone: $firstName,
                    isValidEmail: .constant(false)
                )
                EmailFormFieldView(
                    label: AppStrings.CreateAccountLabel.lastName.rawValue,
                    icon: AppIcon.SignUp.userDetail,
                    placeholder: AppStrings.SignUpPlaceholder.lastName.rawValue,
                    emailOrPhone: $lastName,
                    isValidEmail: .constant(false)
                )

                PasswordFormField(
                    label: AppStrings.CreateAccountLabel.password.rawValue,
                    icon: AppIcon.Login.password,
                    placeholder: AppStrings.SignUpPlaceholder.password.rawValue,
                    password: $password
                )
                PasswordFormField(
                    label: AppStrings.CreateAccountLabel.confirmPassword
                        .rawValue,
                    icon: AppIcon.Login.password,
                    placeholder: AppStrings.SignUpPlaceholder.confirmPassword
                        .rawValue,
                    password: $confirmPassword
                )

                // Continue button (navigates to Verification)
                ButtonView(
                    title: AppStrings.SignUpLabel.continueButton.rawValue
                ) {
                    SignInView()
                }
            }
        }
        .padding()
        .padding(.horizontal, 24)
    }
}

#Preview {
    CreateAccount()
}
