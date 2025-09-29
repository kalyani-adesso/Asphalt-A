// The Swift Programming Language
// https://docs.swift.org/swift-book


import SwiftUI

struct SignInView: View {
    @State private var emailOrPhone: String = ""
    @State private var password: String = ""
    @State private var showPassword: Bool = false
    @State private var rememberMe: Bool = false
    
    var body: some View {
        VStack {
            // Header
            Text(AppStrings.SignInLabel.welcome.rawValue)
                .font(KlavikaFont.bold.font(size: 22))
                .padding(.bottom,11)
                .foregroundStyle(AppColor.black)
            Text(AppStrings.SignInLabel.welcomeSubtitle.rawValue)
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundStyle(AppColor.stoneGray)
                .padding(.bottom,18)
            AppImage.Logos.rider
                .resizable()
                .frame(width: 50, height: 60)
                .padding(16)
            Text(AppStrings.SignInLabel.clubName.rawValue)
                .font(KlavikaFont.medium.font(size: 22))
                .foregroundColor(.celticBlue)
                .padding(.bottom,42)
            
            // Form fields
            VStack(spacing: 21) {
                VStack(alignment: .leading, spacing: 10) {
                    Text(AppStrings.SignInLabel.emailOrPhone.rawValue)
                        .font(KlavikaFont.medium.font(size: 16))
                        .foregroundStyle(AppColor.black)
                    HStack {
                        AppIcon.Login.email
                            .frame(width: 20, height: 20)
                        TextField(AppStrings.SignInPlaceholder.email.rawValue, text: $emailOrPhone)
                            .font(KlavikaFont.regular.font(size: 16))
                            .autocapitalization(.none)
                            .foregroundStyle(AppColor.richBlack)
                        AppIcon.Login.validEmail
                            .frame(width: 20, height: 20)
                    }
                    .padding()
                    .background(AppColor.backgroundLight)
                    .cornerRadius(10)
                }
                
                VStack(alignment: .leading, spacing: 10) {
                    Text(AppStrings.SignInLabel.password.rawValue)
                        .font(KlavikaFont.medium.font(size: 16))
                        .foregroundStyle(AppColor.black)
                    HStack {
                        if showPassword {
                            AppIcon.Login.passwordEncrypt
                                .frame(width: 20, height: 20)
                            TextField(AppStrings.SignInPlaceholder.password.rawValue, text: $password)
                                .font(KlavikaFont.regular.font(size: 16))
                                .autocapitalization(.none)
                                .foregroundStyle(AppColor.richBlack)
                        } else {
                            AppIcon.Login.password
                                .frame(width: 20, height: 20)
                            SecureField(AppStrings.SignInPlaceholder.password.rawValue, text: $password)
                                .font(KlavikaFont.regular.font(size: 16))
                                .autocapitalization(.none)
                        }
                        Button(action: { showPassword.toggle() }) {
                            Image(systemName: showPassword ? "eye" : "eye.slash")
                                .foregroundColor(.stoneGray)
                                .frame(width: 20, height: 20)
                        }
                    }
                    .padding()
                    .background(AppColor.backgroundLight)
                    .cornerRadius(10)
                }
            }
            .padding(.bottom, 18)
            
            HStack {
                Button(action: { rememberMe.toggle() }) {
                    HStack {
                        (rememberMe ? AppIcon.Login.loginRemembered : AppIcon.Login.rememberLogin)
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
            .padding(.bottom,27)
            
            // Sign In button
            Button(AppStrings.SignInLabel.signInTitle.rawValue) {
                // Handler for sign-in
            }
            .frame(maxWidth: .infinity)
            .frame(height: 60)
            .background(
                LinearGradient(
                    gradient: Gradient(colors: [
                        AppColor.royalBlue,
                        AppColor.pursianBlue,
                    ]),
                    startPoint: .leading,
                    endPoint: .trailing
                )
            )
            .cornerRadius(15)
            .shadow(color: Color.black.opacity(0.20), radius: 4, x: 0, y: 2)
            .foregroundStyle(AppColor.white)
            .font(KlavikaFont.bold.font(size: 18))
            .padding(.bottom, 21)
            
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
                Button(AppStrings.SignInAction.signUpAction.rawValue) {
                    // Handler for sign up
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


