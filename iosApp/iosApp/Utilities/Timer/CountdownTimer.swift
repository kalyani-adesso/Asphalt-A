//
//  CountdownTimer.swift
//  iosApp
//
//  Created by Lavanya Selvan on 06/10/25.
//

import Foundation
import SwiftUI

class CountdownTimer : ObservableObject  {
    @Published var timeRemaining = 180
   var timer: Timer? = nil
    @Published var canResend = false

    func startTimer() {
        timer?.invalidate()  // stop previous timer if any
        timeRemaining = 180
        canResend = false
        timer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { _ in
            if self.timeRemaining > 0 {
                self.timeRemaining -= 1
            } else {
                self.timer?.invalidate()
                self.canResend = true
            }
        }
    }
     func resendOTP() {
        // Call API to resend the OTP here
        startTimer()  // restart the timer after resend
    }

    var formattedTime: String {
        let minutes = timeRemaining / 60
        let seconds = timeRemaining % 60
        return String(format: "%02d:%02d", minutes, seconds)
    }

}
