//
//  AppImages.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 29/09/25.
//

import Foundation
import SwiftUI

struct AppImage {
    struct Login {
        static let signInWithApple = Image("btnSignInwithApple")
        static let signInWithFb = Image("btnSigninwithFb")
        static let signInWithGoogle = Image("btnSigninwithGoogle")
        static let loginSuccessBg = Image("loginSucessBg")
    }
    
    struct Logos {
        static let adesso = Image("adessoLogo")
        static let appName = Image("appNameLogo")
        static let rider = Image("riderLogo", bundle: Bundle.main)
        static let riderCircle = Image("riderLogoCircle")
    }
    
    struct SignUp {
        static let confirmOtp = Image("confirmOtp")
        static let createAccountBg = Image("createAccountBg")
        static let forgotPassword = Image("forgotPassword")
        
    }
    
    struct Welcome {
        static let bg = Image("WelcomeBg")
        static let bg2 = Image("WelcomeBg2")
        static let bg3 = Image("WelcomeBg3")
    }
    
    struct Profile {
        static let profile = Image("profile")
    }
    
    
}
