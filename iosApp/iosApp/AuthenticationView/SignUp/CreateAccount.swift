//
//  CreateAccount.swift
//  iosApp
//
//  Created by Lavanya Selvan on 29/09/25.
//

import SwiftUI

struct CreateAccount: View {
    
    @State private var userName: String = ""
    @State private var email: String = ""
    @State private var isValidEmail: Bool = false
    @State private var password: String = ""
    @State private var confirmPassword: String = ""
    @State private var isPasswordEntering: Bool = false
    @State private var isConfirmPasswordEntering: Bool = false
    @State private var passwordsMatch: Bool = true
    @State private var showPassword: Bool = false
    @State private var showConfirmPassword: Bool = false
    @StateObject private var signUpViewModel =  SignUpViewModal()
    @State private var isSignupSuccess: Bool = false
    @Environment(\.dismiss) var dismiss
    var body: some View {
        ZStack {
            VStack {
                // Header
                HeaderView(
                    title: AppStrings.SignUpLabel.welcome.rawValue,
                    subtitle: AppStrings.SignUpLabel.welcomeSubtitle.rawValue
                )
                
                // Form fields
                VStack(spacing: 25) {
                    FormFieldView(
                        label: AppStrings.CreateAccountLabel.userName.rawValue,
                        icon: AppIcon.SignUp.userDetail,
                        placeholder: AppStrings.SignUpPlaceholder.userName.rawValue,
                        value: $userName,
                        isValidEmail: .constant(false)
                    )
                    FormFieldView(
                        label: AppStrings.CreateAccountLabel.email.rawValue,
                        icon: AppIcon.Login.email,
                        placeholder: AppStrings.SignUpPlaceholder.email.rawValue,
                        value: $email,
                        isValidEmail: $isValidEmail
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
                    .onChange(of: confirmPassword) { _ in validatePasswords() }
                    
                    if !passwordsMatch {
                        Text("Passwords do not match")
                            .font(.system(size: 14))
                            .foregroundColor(.red)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(.horizontal, 4)
                            .transition(.opacity.combined(with: .slide))
                            .animation(.easeInOut, value: passwordsMatch)
                    }
                    
                    // Continue button (navigates to Verification)
                    ButtonView( title: AppStrings.CreateAccountLabel.createTitle.rawValue, onTap: {
                        Task {
                            let emailExist = await signUpViewModel.checkEmailDomainExists( email)
                            if emailExist {
                                signUpViewModel.didTapSignUp(email: email, username: userName, password: confirmPassword, confirmPassword: confirmPassword, onSuccess: {
                                    isSignupSuccess = true
                                })
                            }
                        }
                    }
                    )
                    .disabled(!isFormValid)
                    .opacity(isFormValid ? 1 : 0.5)
                    .navigationDestination(isPresented: $isSignupSuccess, destination: {
                        SignInView()
                    })
                    .navigationBarBackButtonHidden(true)
                    .toolbar {
                        ToolbarItem(placement: .navigationBarLeading) {
                            Button {
                                dismiss()
                            } label: {
                                AppIcon.CreateRide.backButton
                            }
                        }
                    }
                }
            }
            
            // TODO: do validation for other case as well - check sign in flow.
            ToastView(message:AppStrings.ValidationMessage.validateEmail.rawValue , isShowing: $signUpViewModel.showToast)
        }
        .padding()
        .padding(.horizontal, 24)
    }
    private func validatePasswords() {
        
        if password.isEmpty || confirmPassword.isEmpty {
            passwordsMatch = true
            return
        }
        passwordsMatch = (password == confirmPassword)
    }
    var isFormValid: Bool {
        !userName.trimmingCharacters(in: .whitespaces).isEmpty &&
        isValidEmail &&
        !password.isEmpty &&
        !confirmPassword.isEmpty &&
        passwordsMatch
    }
}

#Preview {
    CreateAccount()
}
