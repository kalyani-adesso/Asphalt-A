//
//  ForgotPassword.swift
//  iosApp
//
//  Created by Lavanya Selvan on 06/10/25.
//

import SwiftUI

struct ForgotPassword: View {
    
    @State private var emailOrPhone: String = ""
    @State private var password: String = ""
    @State private var confirmPassword: String = ""
    @State private var showPassword: Bool = false
    @State private var rememberMe: Bool = false
    @State private var isValidEmail: Bool = false
    @State private var isPasswordEntering: Bool = false
    @StateObject private var viewModel: SignUpViewModal = .init()
    @State var hasPasswordReset: Bool = false
    @State private var showSnackbar: Bool = false  // Added for snackbar control
    @State private var navigationTimer: Timer? = nil  // Added for timer
    
    var body: some View {
        ZStack {
            VStack {
                HeaderView(
                    title: AppStrings.SignInLabel.createNewPassword.localized,
                    subtitle: AppStrings.SignInLabel.enterYourNewPassword.localized
                )
                VStack(spacing: 21) {
                    FormFieldView(
                        label: AppStrings.SignInLabel.emailOrPhone.localized,
                        icon: AppIcon.Login.email,
                        placeholder: AppStrings.SignInPlaceholder.email.localized,
                        value: $emailOrPhone,
                        isValidEmail: $isValidEmail
                    )
                }
                .padding(.bottom, 18)
                //TODO: Show forgot password mail send toast.
                ButtonView(title: AppStrings.SignInLabel.updatePassword.localized.uppercased(), onTap: {
                    viewModel.getEmailorPhoneNumber(emailorPhoneNumber: emailOrPhone, password: password, confirmPassword: confirmPassword, onSucess: {
                        // Show snackbar and start timer for navigation
                        showSnackbar = true
                        navigationTimer = Timer.scheduledTimer(withTimeInterval: 3.0, repeats: false) { _ in
                            hasPasswordReset = true
                            showSnackbar = false
                        }
                    })
                }).disabled(!isValidEmail)
                .padding(.bottom,20)
                NavigationLink(destination: {
                    SignInView()
                }, label: {
                    Text(AppStrings.SignInLabel.backToSignIn.localized.uppercased())
                        .frame(maxWidth: .infinity)
                        .frame(height: 50)
                        .background(AppColor.white)
                        .foregroundStyle(AppColor.black)
                        .font(KlavikaFont.bold.font(size: 14))
                        .overlay(
                            RoundedRectangle(cornerRadius: 10)
                                .stroke(AppColor.black, lineWidth: 1)
                        )
                        .padding(.bottom,20)
                        .buttonStyle(.plain)
                })
            }
            .padding(.horizontal, 24)
            .navigationBarBackButtonHidden(true)
            .padding(.top, 40)
            .navigationDestination(isPresented: $hasPasswordReset, destination: {
                SignInView()
            })
            .onDisappear {
                // Clean up timer to avoid memory leaks
                navigationTimer?.invalidate()
                navigationTimer = nil
            }
            ToastView(message: viewModel.errorMessage ?? "", isShowing: $viewModel.showToast)
            if showSnackbar {  // Changed from hasPasswordReset to showSnackbar
                VStack {
                    Snackbar(
                        message: "Reset link sent!",
                        subMessage: "Check your mail for password reset link", icon:  AppIcon.Login.resetSent
                    )
                    Spacer()
                }
                .background(AppColor.darkGray)
                .frame(maxWidth: .infinity)
                .transition(.scale)
                .zIndex(2)
            }
         
        }
    }
}

extension ForgotPassword {
    private var seperator: some View {
        Text("")
            .frame(maxWidth: .infinity)
            .frame(height: 1.5)
            .background(AppColor.whiteGray)
    }
    
    @ViewBuilder var socialSignInOptions: some View {
        VStack(spacing: 16) {
            HStack(spacing: 21) {
                seperator
                Text(AppStrings.SignInAction.connectWith.localized)
                    .font(KlavikaFont.medium.font(size: 12))
                    .foregroundColor(.stoneGray)
                    .frame(width: 120)
                seperator
            }
            .frame(maxWidth: .infinity)
            HStack(spacing: 14) {
                AppImage.Login.signInWithFb
                AppImage.Login.signInWithGoogle
                AppImage.Login.signInWithApple
            }
            .frame(height: 44)
        }
    }
    
    @ViewBuilder func signUpLink() -> some View {
        HStack {
            Text(AppStrings.SignInAction.signUpPrompt.localized)
                .font(KlavikaFont.medium.font(size: 14))
                .foregroundStyle(AppColor.black)
            NavigationLink(destination: CreateAccount()) {
                Text(AppStrings.SignInAction.signUpAction.localized)
                    .font(KlavikaFont.medium.font(size: 14))
                    .foregroundStyle(AppColor.celticBlue)
            }
            .font(KlavikaFont.medium.font(size: 14))
            .foregroundStyle(AppColor.celticBlue)
        }
        .padding(.top, 30)
        Spacer()
    }
}

#Preview {
    ForgotPassword(hasPasswordReset: false)
}
