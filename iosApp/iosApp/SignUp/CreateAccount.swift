//
//  CreateAccount.swift
//  iosApp
//
//  Created by Lavanya Selvan on 29/09/25.
//

import SwiftUI

struct CreateAccount: View {
    
    @State private var firstName: String = ""
    @State private var lastName: String = ""
    @State private var password: String = ""
    @State private var confirmPassword: String = ""
    @State private var isPasswordEntering: Bool = false
    @State private var isConfirmPasswordEntering: Bool = false
    @State private var showPassword: Bool = false
    @State private var showConfirmPassword: Bool = false
    @State private var passwordsMatch: Bool = true

    
    var body: some View {
        VStack{
            // Header
            Text(AppStrings.SignUpLabel.welcome.rawValue)
                .font(KlavikaFont.bold.font(size: 22))
                .padding(.bottom,11)
                .foregroundStyle(AppColor.black)
            Text(AppStrings.SignUpLabel.welcomeSubtitle.rawValue)
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundStyle(AppColor.stoneGray)
                .padding(.bottom,18)
            AppImage.Logos.rider
                .resizable()
                .frame(width: 50, height: 60)
                .padding(16)
            Text(AppStrings.SignInLabel.clubName.rawValue)
                .font(KlavikaFont.medium.font(size: 22))
                .padding(.bottom,42)
            
            // Form fields
            VStack(spacing: 25) {
                VStack(alignment: .leading, spacing: 10) {
                    Text(AppStrings.CreateAccountLabel.firstName.rawValue)
                        .font(KlavikaFont.medium.font(size: 16))
                        .foregroundStyle(AppColor.black)
                    HStack {
                        AppIcon.SignUp.userDetail
                            .frame(width: 20, height: 20)
                        TextField(AppStrings.SignUpPlaceholder.firstName.rawValue, text: $firstName)
                            .font(KlavikaFont.regular.font(size: 16))
                            .autocapitalization(.none)
                            .foregroundStyle(AppColor.richBlack)
                    }
                    .padding()
                    .background(AppColor.backgroundLight)
                    .cornerRadius(10)
                }
                
                VStack(alignment: .leading, spacing: 10) {
                    Text(AppStrings.CreateAccountLabel.lastName.rawValue)
                        .font(KlavikaFont.medium.font(size: 16))
                        .foregroundStyle(AppColor.black)
                    HStack {
                        AppIcon.SignUp.userDetail
                            .frame(width: 20, height: 20)
                        TextField(AppStrings.SignUpPlaceholder.lastName.rawValue, text: $lastName)
                            .font(KlavikaFont.regular.font(size: 16))
                            .autocapitalization(.none)
                            .foregroundStyle(AppColor.richBlack)
                    }
                    .padding()
                    .background(AppColor.backgroundLight)
                    .cornerRadius(10)
                }
                
                VStack(alignment: .leading, spacing: 10) {
                    Text(AppStrings.CreateAccountLabel.password.rawValue)
                        .font(KlavikaFont.medium.font(size: 16))
                        .foregroundStyle(AppColor.black)
                    HStack {
                        if showPassword {
                            AppIcon.Login.password
                                .frame(width: 20, height: 20)
                            TextField(AppStrings.SignUpPlaceholder.password.rawValue, text: $password)
                                .font(KlavikaFont.regular.font(size: 16))
                                .autocapitalization(.none)
                                .foregroundStyle(AppColor.richBlack)
                        } else {
                            (isPasswordEntering ? AppIcon.Login.passwordEncrypt : AppIcon.Login.password)
                                .frame(width: 20, height: 20)
                            SecureField(AppStrings.SignUpPlaceholder.password.rawValue, text: $password)
                                .onChange(of: password) { value in
                                    isPasswordEntering = !value.isEmpty
                                    validatePasswords()
                                                           
                                }
                                .font(KlavikaFont.regular.font(size: 16))
                                .autocapitalization(.none)
                        }
                        
                        if isPasswordEntering  {
                            Button(action: { showPassword.toggle() }) {
                                Image(systemName: showPassword ? "eye" : "eye.slash")
                                    .foregroundColor(.stoneGray)
                                    .frame(width: 20, height: 20)
                            }
                        }
                    }
                    .padding()
                    .background(AppColor.backgroundLight)
                    .cornerRadius(10)
                }
                
                VStack(alignment: .leading, spacing: 10) {
                    Text(AppStrings.CreateAccountLabel.confirmPassword.rawValue)
                        .font(KlavikaFont.medium.font(size: 16))
                        .foregroundStyle(AppColor.black)
                    HStack {
                        if showConfirmPassword {
                            AppIcon.Login.password
                                .frame(width: 20, height: 20)
                            TextField(AppStrings.SignUpPlaceholder.confirmPassword.rawValue, text: $confirmPassword)
                                .font(KlavikaFont.regular.font(size: 16))
                                .autocapitalization(.none)
                                .foregroundStyle(AppColor.richBlack)
                        } else {
                            (isConfirmPasswordEntering ? AppIcon.Login.passwordEncrypt : AppIcon.Login.password)
                                .frame(width: 20, height: 20)
                            SecureField(AppStrings.SignUpPlaceholder.confirmPassword.rawValue, text: $confirmPassword)
                                .onChange(of: confirmPassword) { value in
                                    isConfirmPasswordEntering = !value.isEmpty
                                    validatePasswords()
                                }
                                .font(KlavikaFont.regular.font(size: 16))
                                .autocapitalization(.none)
                        }
                        
                        if isConfirmPasswordEntering {
                            Button(action: { showConfirmPassword.toggle() }) {
                                Image(systemName: showConfirmPassword ? "eye" : "eye.slash")
                                    .foregroundColor(.stoneGray)
                                    .frame(width: 20, height: 20)
                            }
                        }
                    }
                    .padding()
                    .background(AppColor.backgroundLight)
                    .cornerRadius(10)
                }
                
                // Continue button (navigates to Verification)
                NavigationLink(destination: SignInView()) {
                    Text(AppStrings.SignUpLabel.continueButton.rawValue)
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
                }
                .padding(.bottom, 21)
                
            }
            
            
            
            
            
            
            
            
        }
        .padding()
        .padding(.horizontal, 24)
        
    }
    private func validatePasswords() {
           if confirmPassword.isEmpty {
               passwordsMatch = true
           } else {
               passwordsMatch = password == confirmPassword
           }
       }
}

#Preview {
    CreateAccount()
}
