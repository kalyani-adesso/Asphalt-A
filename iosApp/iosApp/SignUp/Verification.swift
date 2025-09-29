//
//  Verification.swift
//  iosApp
//
//  Created by Lavanya Selvan on 29/09/25.
//

import SwiftUI

struct Verification: View {
    var email : String
    @State private var otp: String = ""
    
    @State private var timeRemaining = 180
    @State private var timer: Timer? = nil
    @State private var canResend = false

    func startTimer() {
        timer?.invalidate() // stop previous timer if any
        timeRemaining = 180
        canResend = false
        timer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { _ in
            if timeRemaining > 0 {
                timeRemaining -= 1
            } else {
                timer?.invalidate()
                canResend = true
            }
        }
    }
    private func resendOTP() {
           print("🔁 Resend OTP tapped")
           // Call your API to resend the OTP here
           startTimer()  // restart the timer after resend
       }
    
    var formattedTime: String {
        let minutes = timeRemaining / 60
        let seconds = timeRemaining % 60
        return String(format: "%02d:%02d", minutes, seconds)
    }

    
    var body: some View {
        VStack {
            AppImage.SignUp.confirmOtp
                .resizable()
                .frame(width: 300, height: 300)
                .padding(.bottom,20)
            Text(AppStrings.VerificationLabel.confirmation.rawValue)
                .font(KlavikaFont.bold.font(size: 24))
                .padding(.bottom,11)
                .foregroundStyle(AppColor.black)
        
            formattedConfirmationText
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
                        TextField(AppStrings.SignUpPlaceholder.otp.rawValue, text: $otp)
                            .keyboardType(.numberPad)
                                .onChange(of: otp) { newValue in
                                    otp = newValue.numbersOnly
                                }
                            .font(KlavikaFont.regular.font(size: 16))
                            .autocapitalization(.none)
                            .foregroundStyle(AppColor.richBlack)
                        if canResend {
                            Text("Resend")
                                .font(KlavikaFont.regular.font(size: 16))
                                .foregroundStyle(AppColor.celticBlue)
                        }
                        else{
                            Text("Resend in \(formattedTime)")
                                .font(KlavikaFont.regular.font(size: 16))
                                .foregroundStyle(AppColor.stoneGray)
                        }
                       
                    }
                    .padding()
                    .background(AppColor.backgroundLight)
                    .cornerRadius(10)
                }
                
                
                
                
                // Continue button (navigates to Verification)
                NavigationLink(destination: CreateAccount()) {
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
        .padding(.horizontal, 24)
        .onAppear {
            startTimer()
        }
        
    }
    private var formattedConfirmationText: Text {
        let parts = AppStrings.VerificationLabel.confirmationText.rawValue.components(separatedBy: "%@")
        let prefix = parts.first ?? ""
        let suffix = parts.count > 1 ? parts.last! : ""
        
        return Text(prefix)
            .foregroundColor(AppColor.stoneGray)
        + Text(email)
            .foregroundColor(AppColor.celticBlue)
            .fontWeight(.semibold)
        + Text(suffix)
            .foregroundColor(AppColor.stoneGray)
    }

}




#Preview {
    Verification(email: "")
}
