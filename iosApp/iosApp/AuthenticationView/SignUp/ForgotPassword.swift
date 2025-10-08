//
//  ForgotPassword.swift
//  iosApp
//
//  Created by Lavanya Selvan on 06/10/25.
//

import SwiftUI

struct ForgotPassword: View {
    
    @State var email : String
    @State private var password: String = ""
    @State private var isValidEmail: Bool = false
    @State private var showingSignIn: Bool = false
    @State private var showSnackbar: Bool = false
    
    var body: some View {
        
        ZStack(alignment: .top) {
            VStack {
                
                HeaderView(
                    title: AppStrings.ForgotPassword.title.rawValue,
                    subtitle: AppStrings.ForgotPassword.subtitle.rawValue
                )
                
                AppImage.SignUp.forgotPassword
                
                    .frame(width: 241, height: 240)
                    .padding(.bottom, 30)
                ZStack {
                    
                    VStack(spacing: 21) {
                        FormFieldView(
                            label: AppStrings.SignInLabel.emailOrPhone.rawValue,
                            icon: AppIcon.Login.email,
                            placeholder: AppStrings.SignInPlaceholder.email.rawValue,
                            emailOrPhone: $email,
                            isValidEmail: $isValidEmail
                        )
                        
                        
                        ButtonView(
                            title: AppStrings.ForgotPassword.forgotAction.rawValue
                        ) {
                            
                            showSnackbar = true
                            
                            // Auto hide after 2 seconds
                            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                                withAnimation {
                                    showSnackbar = false
                                    showingSignIn = true
                                }
                            }
                        }
                        .disabled(!isValidEmail)               
                           .opacity(isValidEmail ? 1 : 0.5)
                    }
                    .padding(.horizontal, 24)
                    
                    .navigationDestination(isPresented: $showingSignIn, destination: {
                        SignInView()
                    })
                }
            }
            if showSnackbar {
                AppColor.overlay.opacity(0.75)
                    .ignoresSafeArea()
                    .transition(.opacity)
                    .zIndex(0.5)
            }
            
            if showSnackbar {
                    Snackbar(
                        message: AppStrings.ResetSnackbarLabel.title.rawValue,
                        subMessage: AppStrings.ResetSnackbarLabel.subtitle.rawValue
                    )
                
                .transition(.move(edge: .top).combined(with: .opacity))
                .zIndex(1)
            }
        }
        .animation(.spring(), value: showSnackbar)
    }
}

#Preview {
    ForgotPassword(email: "")
}
