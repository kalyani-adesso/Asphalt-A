//
//  Userdefaults.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 03/10/25.
//

import Foundation

struct MBUserDefaults {
 
    static var hasSeenOnboardingStatic: Bool {
        get {
            return UserDefaults.standard.bool(forKey: AppStrings.userdefaultKeys.hasSeenOnboarding.rawValue)
        }
        set {
            UserDefaults.standard.set(newValue, forKey: AppStrings.userdefaultKeys.hasSeenOnboarding.rawValue)
        }
    }
    static var hasShownLoginSuccessStatic: Bool {
        get {
            return UserDefaults.standard.bool(forKey: AppStrings.userdefaultKeys.hasShownLoginSuccess.rawValue)
        }
        set {
            UserDefaults.standard.set(newValue, forKey: AppStrings.userdefaultKeys.hasShownLoginSuccess.rawValue)
        }
    }
    
    static var rememberMeDataStatic: Bool {
        get {
            return UserDefaults.standard.bool(forKey: AppStrings.userdefaultKeys.rememberMeData.rawValue)
        }
        set {
            UserDefaults.standard.set(newValue, forKey: AppStrings.userdefaultKeys.rememberMeData.rawValue)
        }
    }
    
    static var removeAllUserDefaults: Void {
        UserDefaults.standard.removePersistentDomain(forName: Bundle.main.bundleIdentifier!)
    }
}
