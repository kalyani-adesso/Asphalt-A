import SwiftUI
import FirebaseCore

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
    FirebaseApp.configure()

    return true
  }
}

@main
struct iOSApp: App {
    @AppStorage(AppStrings.userdefaultKeys.hasSeenOnboarding.rawValue) var hasSeenOnboarding: Bool = false
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    var body: some Scene {
        WindowGroup {
            if MBUserDefaults.hasSeenOnboardingStatic {
                NavigationStack {
              // SignInView()
                    BottomNavBar()
                }
            } else {
                WelcomeScreen()
            }
        }
        
    }
}
