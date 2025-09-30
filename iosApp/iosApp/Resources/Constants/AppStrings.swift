//
//  File.swift
//  DesignSystem
//
//  Created by Lavanya Selvan on 25/09/25.
//

import Foundation

public struct AppStrings{
    
    public enum WelcomeStrings {
        public static let welcomeTitle = "Welcome to the Joy\nof Riding"
        public static let welcomeSubtitle = "Connect with fellow motorcycle enthusiasts and discover amazing rides in your area."
        
        public static let communityTitle = "Community\nFeatures"
        public static let communitySubtitle = "Share knowledge, ask questions, and connect with mechanics and experienced riders."
        
        public static let rideTitle = "Ride Together\nSafely"
        public static let rideSubtitle = "Create group rides, track locations in real-time, and ensure everyone stays connected."
    }
    
    enum SignUpLabel: String {
 
        case continueButton = "CONTINUE"
        case welcome = "Create Your Account"
        case welcomeSubtitle = "Create your account to get started"
        case clubName = "adesso Rider’s Club"
        case emailOrPhone = "Email or Phone Number"
        case password = "Password"
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }
    enum VerificationLabel: String {
        case verifyButton = "VERIFY ACCOUNT"
        case confirmation = "Confirm Your Email"
        case confirmationText = "We’ve sent 5 digits verification code to %@"
        case verifyTitle = "Enter Verification Code"
        case verificationCode = "Code"
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }
    
    
    enum CreateAccountLabel: String {
        case createTitle = "CREATE ACCOUNT"
        case firstName = "First Name"
        case lastName = "Last Name"
        case password = "Password"
        case confirmPassword = "Confirm Password"
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }
    
    enum SignUpPlaceholder: String {
 
        case email = "Enter your email"
        case otp = "Enter your OTP"
        case firstName = "First Name"
        case lastName = "Last Name"
        case password = "Enter your password"
        case confirmPassword = "Confirm Password"
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }
    
    
 
    
    enum SignInLabel: String {
        case signInTitle = "SIGN IN"
        case welcome = "Welcome"
        case welcomeSubtitle = "Let’s login for explore continues"
        case clubName = "adesso Rider’s Club"
        case emailOrPhone = "Email or Phone Number"
        case password = "Password"

        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }

    enum SignInPlaceholder: String {
        case email = "Enter your email"
        case password = "Enter your password"

        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }

    enum SignInToggle: String {
        case keepMeSignedIn = "Keep me signed in"
        case rememberMe = "Remember me"

        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }

    enum SignInAction: String {
        case forgotPassword = "Forgot password"
        case signInButton = "SIGN IN"
        case signUpPrompt = "Don’t have an account?"
        case signUpAction = "Sign Up here"
        case connectWith = "You can connect with"

        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }
    
   
    enum SignInSucessView: String {
        case loginSuccessTitle = "Yey! Login Successfull"
        case loginSuccessSubtitle = "You will be moved to home screen right now. Enjoy the features!"
       case exploreButton = "Lets Explore"
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }


}

