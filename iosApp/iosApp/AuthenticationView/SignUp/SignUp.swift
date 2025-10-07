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
        VStack {
            // Header
            HeaderView(
                title: AppStrings.SignUpLabel.welcome.rawValue,
                subtitle: AppStrings.SignUpLabel.welcomeSubtitle.rawValue
            )
            
            Image("createAccountBg")
                .frame(width: 240, height: 240)
                .padding(.bottom, 30)
            
            // Form fields
            VStack(spacing: 21) {
                FormFieldView(
                    label: AppStrings.SignInLabel.emailOrPhone.rawValue,
                    icon: AppIcon.Login.email,
                    placeholder: AppStrings.SignInPlaceholder.email.rawValue,
                    emailOrPhone: $emailOrPhone,
                    isValidEmail: $isValidEmail
                )
                
                // Continue button (navigates to Verification)
                ButtonView( title: AppStrings.SignUpLabel.continueButton.rawValue, onTap: {
                        isVerified = true
                    }
                )
                .navigationDestination(isPresented: $isVerified, destination: {
                    if emailOrPhone.isValidEmail {
                        SignInView()
                    } else {
                        Verification(email: emailOrPhone)
                    }
                })
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
