// The Swift Programming Language
// https://docs.swift.org/swift-book

import SwiftUI

struct SignUpView: View {
    @State private var emailOrPhone: String = ""
    @State private var password: String = ""
    @State private var isValidEmail: Bool = false
    @FocusState private var isEmailFieldFocused: Bool

    var body: some View {
        VStack {
            // Header
            HeaderView(
                title: AppStrings.SignUpLabel.welcome.rawValue,
                subtitle: AppStrings.SignUpLabel.welcomeSubtitle.rawValue
            )

            AppImage.SignUp.createAccountBg
                .frame(width: 240, height: 240)
                .padding(.bottom, 30)

            // Form fields
            VStack(spacing: 21) {
                EmailFormFieldView(
                    label: AppStrings.SignInLabel.emailOrPhone.rawValue,
                    icon: AppIcon.Login.email,
                    placeholder: AppStrings.SignInPlaceholder.email.rawValue,
                    emailOrPhone: $emailOrPhone,
                    isValidEmail: $isValidEmail
                )

                // Continue button (navigates to Verification)
                ButtonView(
                    title: AppStrings.SignUpLabel.continueButton.rawValue
                ) {
                    Verification(email: emailOrPhone)
                }
                .disabled(!isValidEmail)
                .opacity(isValidEmail ? 1 : 0.5)
            }
        }
        .padding(.horizontal, 24)
    }
}

struct SignUpView_Previews: PreviewProvider {
    static var previews: some View {
        SignUpView()
    }
}
