// The Swift Programming Language
// https://docs.swift.org/swift-book


import SwiftUI

struct SignUpView: View {
    @State private var emailOrPhone: String = ""
    @State private var password: String = ""
    @State private var isValidEmail: Bool = false
    @FocusState private var isEmailFieldFocused: Bool
    
    
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
                .padding(.bottom,20)
            Image("createAccountBg")
                .frame(width: 240, height: 240)
                .padding(.bottom, 30)
            // Form fields
            VStack(spacing: 21) {
                VStack(alignment: .leading, spacing: 10) {
                    Text(AppStrings.SignInLabel.emailOrPhone.rawValue)
                        .font(KlavikaFont.medium.font(size: 16))
                        .foregroundStyle(AppColor.black)
                    HStack {
                        AppIcon.Login.email
                            .frame(width: 20, height: 20)
                        TextField(AppStrings.SignUpPlaceholder.email.rawValue, text: $emailOrPhone)
                            .focused($isEmailFieldFocused)
                               .onChange(of: emailOrPhone) { value in
                                   isValidEmail = value.isValidEmail
                               }
                            .font(KlavikaFont.regular.font(size: 16))
                            .autocapitalization(.none)
                            .foregroundStyle(AppColor.richBlack)
                        
                        if !emailOrPhone.isEmpty {
                                        if isValidEmail {
                                            AppIcon.Login.validEmail
                                                .frame(width: 20, height: 20)
                                        }
                                    }
                    }
                    .padding()
                    .background(AppColor.backgroundLight)
                    .cornerRadius(10)
                }
//                if !isEmailFieldFocused && !emailOrPhone.isEmpty && !isValidEmail {
//                    Text("Please enter a valid email address")
//                        .font(KlavikaFont.regular.font(size: 13))
//                        .foregroundStyle(.red)
//                }
                
                
                
                
                // Continue button (navigates to Verification)
                NavigationLink(destination: Verification(email: emailOrPhone)) {
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
                .disabled(!isValidEmail)
                .opacity(isValidEmail ? 1 : 0.5) 
                
                
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
