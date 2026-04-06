// The Swift Programming Language
// https://docs.swift.org/swift-book

import SwiftUI

struct SignUpView: View {
    @State private var emailOrPhone: String = ""
    @State private var password: String = ""
    @State private var isValidEmail: Bool = false
    @FocusState private var isEmailFieldFocused: Bool
    @StateObject private var signUpViewModel =  SignUpViewModal()
    @State private var isVerified: Bool = false
    var body: some View {
        ScrollView {
            VStack {
                HeaderView(
                    title: AppStrings.SignUpLabel.welcome.rawValue,
                    subtitle: AppStrings.SignUpLabel.welcomeSubtitle.rawValue
                )

                Image("createAccountBg")
                    .frame(width: 240, height: 240)
                    .padding(.bottom, 30)

                VStack(spacing: 21) {
                    FormFieldView(
                        label: AppStrings.SignInLabel.emailOrPhone.rawValue,
                        icon: AppIcon.Login.email,
                        placeholder: AppStrings.SignInPlaceholder.email.rawValue,
                        value: $emailOrPhone,
                        isValidEmail: .constant(true)
                    )

                    ButtonView(title: AppStrings.SignUpLabel.continueButton.rawValue) {
                        isVerified = true
                    }
                    .navigationDestination(isPresented: $isVerified) {
                        if emailOrPhone.isValidEmail {
                            SignInView()
                        } else {
                            Verification(email: emailOrPhone)
                        }
                    }
                }
            }
            // Same horizontal inset as `SignInView` / `CreateAccount`: one inset for header + form.
            .padding(.horizontal, 24)
            .padding(.top, 40)
            .padding(.bottom, 16)
        }
    }
}

struct SignUpView_Previews: PreviewProvider {
    static var previews: some View {
        SignUpView()
    }
}
