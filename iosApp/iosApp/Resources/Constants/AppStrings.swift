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
        case userName = "User Name"
        case email = "Email Id"
        case password = "Password"
        case confirmPassword = "Confirm Password"
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }
    
    enum SignUpPlaceholder: String {
 
        case email = "Enter your email"
        case userName = "Enter User Name"
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

    enum AppStorageKey: String {
        case hasSeenOnboarding
    }


    enum ValidationMessage: String {
        case validatePassword = "Please enter a password"
        case validateEmail = "Please enter a valid email"
    }

    enum userdefaultKeys: String {
        case rememberMeData = "com.adesso.rider.club.rememberMeData"
        case hasSeenOnboarding = "com.adesso.rider.club.hasSeenOnboarding"
    }
    
    enum ForgotPassword: String {
        case title = "Forgot Password?"
        case subtitle = "We’ll send you reset instructions"
        case forgotAction = "SEND RESET LINK"
     
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }
    enum ResetSnackbarLabel: String {
        case title = "Reset link sent!"
        case subtitle = "Check your mail for password reset link"
     
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }
    enum CreateAccountSnackbarLabel: String {
        case title = "Account Created Successfully!"
       
    }

}

