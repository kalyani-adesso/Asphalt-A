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

    var body: some View {
        VStack {
            // Header
            HeaderView(
                title: AppStrings.SignInLabel.welcome.rawValue,
                subtitle: AppStrings.SignInLabel.welcomeSubtitle.rawValue
            )

            // Form fields
            VStack(spacing: 21) {
                FormFieldView(
                    label: AppStrings.SignInLabel.emailOrPhone.rawValue,
                    icon: AppIcon.Login.email,
                    placeholder: AppStrings.SignInPlaceholder.email.rawValue,
                    emailOrPhone: $emailOrPhone,
                    isValidEmail: $isValidEmail
                )
                PasswordFormField(
                    label: AppStrings.SignInLabel.password.rawValue,
                    icon: AppIcon.Login.password,
                    placeholder: AppStrings.SignInPlaceholder.password.rawValue,
                    password: $password
                )
            }
            .padding(.bottom, 18)

            HStack {
                Button(action: { rememberMe.toggle() }) {
                    HStack {
                        (rememberMe
                            ? AppIcon.Login.loginRemembered
                            : AppIcon.Login.rememberLogin)
                            .frame(width: 20, height: 20)
                        Text(AppStrings.SignInToggle.keepMeSignedIn.rawValue)
                            .font(KlavikaFont.regular.font(size: 14))
                            .foregroundColor(.stoneGray)
                    }
                }
                Spacer()
                Button(AppStrings.SignInAction.forgotPassword.rawValue) {
                    // Handler for forgot password
                }
                .font(KlavikaFont.medium.font(size: 14))
                .foregroundColor(.celticBlue)
            }
            .padding(.bottom, 27)

            // Sign In button
            ButtonView(title: AppStrings.SignInLabel.signInTitle.rawValue) {
                LoginSucessView()
            }

            // Social sign-in options
            VStack(spacing: 16) {
                HStack(spacing: 21) {
                    Text("")
                        .frame(maxWidth: .infinity)
                        .frame(height: 1.5)
                        .background(AppColor.seperatorGray)
                    Text(AppStrings.SignInAction.connectWith.rawValue)
                        .font(KlavikaFont.medium.font(size: 12))
                        .foregroundColor(.stoneGray)
                        .frame(width: 120)
                    Text("")
                        .frame(maxWidth: .infinity)
                        .frame(height: 1.5)
                        .background(AppColor.seperatorGray)
                }
                .frame(maxWidth: .infinity)
                HStack(spacing: 14) {
                    AppImage.Login.signInWithFb
                    AppImage.Login.signInWithGoogle
                    AppImage.Login.signInWithApple
                }
                .frame(height: 44)
            }

            // Sign Up Link
            HStack {
                Text(AppStrings.SignInAction.signUpPrompt.rawValue)
                    .font(KlavikaFont.medium.font(size: 14))
                    .foregroundStyle(AppColor.black)
                NavigationLink(destination: SignUpView()) {
                    Text(AppStrings.SignInAction.signUpAction.rawValue)
                        .font(KlavikaFont.medium.font(size: 14))
                        .foregroundStyle(AppColor.celticBlue)
                }

                .font(KlavikaFont.medium.font(size: 14))
                .foregroundStyle(AppColor.celticBlue)
            }
            .padding(.top, 30)
            Spacer()
        }
        .padding(.horizontal, 24)
        .navigationBarBackButtonHidden(true)
        .padding(.top, 40)
    }
}

struct SignInView_Previews: PreviewProvider {
    static var previews: some View {
        SignInView()
    }
}
