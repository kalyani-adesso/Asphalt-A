//
//  Verification.swift
//  iosApp
//
//  Created by Lavanya Selvan on 29/09/25.
//

import SwiftUI

struct Verification: View {
    var email: String
    @State private var otp: String = ""
    @StateObject var timer = CountdownTimer()
    @StateObject var formattedText = ConfirmationText()
    
    var body: some View {
        VStack {
            AppImage.SignUp.confirmOtp
                .resizable()
                .frame(width: 300, height: 300)
                .padding(.bottom, 20)
            Text(AppStrings.VerificationLabel.confirmation.rawValue)
                .font(KlavikaFont.bold.font(size: 24))
                .padding(.bottom, 11)
                .foregroundStyle(AppColor.black)
            
            formattedText.formattedConfirmationText
                .font(KlavikaFont.medium.font(size: 16))
                .multilineTextAlignment(.center)
                .lineSpacing(5)
            
            Spacer().frame(height: 100)
            
            VStack(spacing: 21) {
                VStack(alignment: .leading, spacing: 10) {
                    Text(AppStrings.VerificationLabel.verifyTitle.rawValue)
                        .font(KlavikaFont.medium.font(size: 16))
                        .foregroundStyle(AppColor.black)
                    HStack {
                        AppIcon.SignUp.smsEdit
                            .frame(width: 20, height: 20)
                        TextField(
                            AppStrings.SignUpPlaceholder.otp.rawValue,
                            text: $otp
                        )
                        .keyboardType(.numberPad)
                        .onChange(of: otp) { newValue in
                            otp = newValue.numbersOnly
                        }
                        .font(KlavikaFont.regular.font(size: 16))
                        .autocapitalization(.none)
                        .foregroundStyle(AppColor.richBlack)
                        if timer.canResend {
                            Button {
                                timer.resendOTP()
                            } label: {
                                Text("Resend")
                                    .font(KlavikaFont.regular.font(size: 16))
                                    .foregroundStyle(AppColor.celticBlue)
                            }
                        }
                        else {
                            Text("Resend in \(timer.formattedTime)")
                                .font(KlavikaFont.regular.font(size: 16))
                                .foregroundStyle(AppColor.stoneGray)
                        }
                        
                    }
                    .padding()
                    .background(AppColor.backgroundLight)
                    .cornerRadius(10)
                }
                
                // Continue button (navigates to Verification)
                ButtonView(
                    title: AppStrings.SignUpLabel.continueButton.rawValue
                ) {
                    CreateAccount()
                }
                
            }
        }
        .padding(.horizontal, 24)
        .onAppear {
            formattedText.email = email
            timer.startTimer()
        }
        
    }
    
}

#Preview {
    Verification(email: "")
}
