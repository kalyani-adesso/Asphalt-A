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

    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    @AppStorage("com.adesso.rider.club.rememberMeData") private var isLoggedIn: Bool = false
    @AppStorage("com.adesso.rider.club.hasSeenOnboarding") private var hasSeenOnboarding: Bool = false

    var body: some Scene {
        WindowGroup {
            NavigationStack {
                if isLoggedIn {
                    BottomNavBar()
                } else if hasSeenOnboarding {
                    SignInView()

                } else {
                    WelcomeScreen()
                }
            }
        }
    }
}
