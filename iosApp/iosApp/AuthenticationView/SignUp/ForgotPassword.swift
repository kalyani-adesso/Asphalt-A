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
                    PasswordFormField(
                        label: AppStrings.SignInLabel.password.localized,
                        icon: AppIcon.Login.password,
                        placeholder: AppStrings.SignInPlaceholder.password.localized,
                        password: $password
                    )
                    PasswordFormField(
                        label: AppStrings.SignInLabel.confirmPassword.localized,
                        icon: AppIcon.Login.password,
                        placeholder: AppStrings.SignInPlaceholder.confirmPassword.localized,
                        password: $confirmPassword
                    )
                }
                .padding(.bottom, 18)
                ButtonView(title: AppStrings.SignInLabel.updatePassword.localized.uppercased(), onTap: {
                    viewModel.getEmailorPhoneNumber(emailorPhoneNumber: emailOrPhone, password: password, confirmPassword: confirmPassword, onSucess: {
                        hasPasswordReset = true
                    })
                })
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
            ToastView(message: viewModel.errorMessage ?? "", isShowing: $viewModel.showToast)
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
