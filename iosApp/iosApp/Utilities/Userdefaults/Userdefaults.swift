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
    
    static var userIdStatic: String? {
        get {
            return UserDefaults.standard.string(forKey: AppStrings.userdefaultKeys.userId.rawValue)
        }
        set {
            UserDefaults.standard.set(newValue, forKey: AppStrings.userdefaultKeys.userId.rawValue) 
        }
    }
    static var userNameStatic: String? {
        get {
            return UserDefaults.standard.string(forKey: AppStrings.userdefaultKeys.userName.rawValue)
        }
        set {
            UserDefaults.standard.set(newValue, forKey: AppStrings.userdefaultKeys.userName.rawValue)
        }
    }
    
    static var rideIdStatic: String? {
        get {
            return UserDefaults.standard.string(forKey: AppStrings.userdefaultKeys.rideId.rawValue)
        }
        set {
            UserDefaults.standard.set(newValue, forKey: AppStrings.userdefaultKeys.rideId.rawValue)
        }
    }
    
    static var isRideJoinedID:String? {
        get {
            return UserDefaults.standard.string(forKey: AppStrings.userdefaultKeys.rideJoinedId.rawValue)
        }
        set {
            UserDefaults.standard.set(newValue, forKey: AppStrings.userdefaultKeys.rideJoinedId.rawValue)
        }
    }
    
    static var removeAllUserDefaults: Void {
        UserDefaults.standard.removePersistentDomain(forName: Bundle.main.bundleIdentifier!)
    }
}
