// The Swift Programming Language
// https://docs.swift.org/swift-book

import SwiftUI

struct SignInView: View {
    @State private var emailOrPhone: String = ""
    @State private var password: String = ""
    @State private var showPassword: Bool = false
    @State private var rememberMe: Bool = false
    @State private var isValidEmail: Bool = false
    @State private var isPasswordEntering: Bool = false
    @StateObject private var viewModel: LoginViewModel = .init()
    @State var hasLoggedIn: Bool = false
    var body: some View {
        ZStack {
            VStack {
                HeaderView(
                    title: AppStrings.SignInLabel.welcome.localized,
                    subtitle: AppStrings.SignInLabel.welcomeSubtitle.localized
                )
                VStack(spacing: 21) {
                    EmailFormFieldView(
                        label: AppStrings.SignInLabel.emailOrPhone.localized,
                        icon: AppIcon.Login.email,
                        placeholder: AppStrings.SignInPlaceholder.email.localized,
                        emailOrPhone: $emailOrPhone,
                        isValidEmail: $isValidEmail
                    )
                    PasswordFormField(
                        label: AppStrings.SignInLabel.password.localized,
                        icon: AppIcon.Login.password,
                        placeholder: AppStrings.SignInPlaceholder.password.localized,
                        password: $password
                    )
                }
                .padding(.bottom, 18)
                rememberMeToggle()
                ButtonView(title: AppStrings.SignUpLabel.continueButton.localized, onTap: {
                    viewModel.didTapLogin(email: emailOrPhone, password: password, completion: {
                        hasLoggedIn = true
                    })
                })
                socialSignInOptions
                signUpLink()
            }
            .padding(.horizontal, 24)
            .navigationBarBackButtonHidden(true)
            .padding(.top, 40)
            .navigationDestination(isPresented: $hasLoggedIn, destination: {
                LoginSucessView()
            })
            ToastView(message: viewModel.errorMessage ?? "", isShowing: $viewModel.showToast)
        }
        .onAppear() {
            MBUserDefaults.rememberMeDataStatic = true
        }
    }
}

extension SignInView {
    private var seperator: some View {
        Text("")
            .frame(maxWidth: .infinity)
            .frame(height: 1.5)
            .background(AppColor.seperatorGray)
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
    
    @ViewBuilder func rememberMeToggle() -> some View {
        HStack {
            Button(action: {
                rememberMe.toggle()
                MBUserDefaults.rememberMeDataStatic = rememberMe
            }) {
                HStack {
                    (rememberMe
                     ? AppIcon.Login.loginRemembered
                     : AppIcon.Login.rememberLogin)
                    .frame(width: 20, height: 20)
                    Text(AppStrings.SignInToggle.keepMeSignedIn.localized)
                        .font(KlavikaFont.regular.font(size: 14))
                        .foregroundColor(.stoneGray)
                }
            }
            Spacer()
            NavigationLink(destination: SignUpView()) {
                Text(AppStrings.SignInAction.forgotPassword.localized)
                    .font(KlavikaFont.medium.font(size: 14))
                    .foregroundColor(.celticBlue)
            }
        }
        .padding(.bottom, 27)
    }
}
