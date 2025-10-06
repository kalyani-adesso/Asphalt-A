//
//  CreateAccount.swift
//  iosApp
//
//  Created by Lavanya Selvan on 29/09/25.
//

import SwiftUI
import Firebase

struct CreateAccount: View {

    @State private var firstName: String = ""
    @State private var lastName: String = ""
    @State private var password: String = ""
    @State private var confirmPassword: String = ""
    @State private var isPasswordEntering: Bool = false
    @State private var isConfirmPasswordEntering: Bool = false
    @State private var showPassword: Bool = false
    @State private var showConfirmPassword: Bool = false
    @StateObject private var signUpViewModel =  SignUpViewModal()
    @State private var isSignupSuccess: Bool = false
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
                    // TODO: Change the order - User name, email , password and confirm password.
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
                    ButtonView( title: AppStrings.SignUpLabel.continueButton.rawValue, onTap: {
                        Task {
                            let emailExist = await signUpViewModel.checkEmailDomainExists( firstName)
                            if emailExist {
                                signUpViewModel.didTapSignUp(email: firstName, username: lastName, password: confirmPassword, onSuccess: {
                                    isSignupSuccess = true
                                })
                            }
                        }
                    }
                    )
                    .navigationDestination(isPresented: $isSignupSuccess, destination: {
                        SignInView()
                    })
                }
            }
            
            // TODO: do validation for other case as well - check sign in flow.
            ToastView(message:AppStrings.ValidationMessage.validateEmail.rawValue , isShowing: $signUpViewModel.showToast)
        }
        .padding()
        .padding(.horizontal, 24)
    }
}

#Preview {
    CreateAccount()
}
