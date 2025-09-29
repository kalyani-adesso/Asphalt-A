//
//  File.swift
//  DesignSystem
//
//  Created by Lavanya Selvan on 25/09/25.
//

import Foundation

public struct AppStrings{
    
   
    public static let appName = "adesso Rider’s\n Club"
    
   
    
    public enum loginStrings{
        public static let loginTitle = "Welcome"
        public static let loginSubtitle = "Let’s login for explore continues"
        public static let loginSuccessfulTitle = "Yey! Login Successfull"
        public static let loginSuccessfulSubtitle = "You will be moved to home screen right now. Enjoy the features!"
    }
    
    public enum signUpStrings{
        public static let signUpTitle = "Create your account"
        public static let signUpSubtitle = "Create your account to get started"
    }
    


    public enum WelcomeStrings {
        public static let welcomeTitle = "Welcome to the Joy\nof Riding"
        public static let welcomeSubtitle = "Connect with fellow motorcycle enthusiasts and discover amazing rides in your area."
        
        public static let communityTitle = "Community\nFeatures"
        public static let communitySubtitle = "Share knowledge, ask questions, and connect with mechanics and experienced riders."
        
        public static let rideTitle = "Ride Together\nSafely"
        public static let rideSubtitle = "Create group rides, track locations in real-time, and ensure everyone stays connected."
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

}

