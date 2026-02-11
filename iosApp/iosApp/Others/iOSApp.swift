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

    @AppStorage("rememberMeDataStatic") private var isLoggedIn: Bool = false
    @AppStorage("hasSeenOnboardingStatic") private var hasSeenOnboarding: Bool = false

    @StateObject private var homeViewModel = HomeViewModel()
    @StateObject private var upcomingVM = UpcomingRideViewModel()

    var body: some Scene {
        WindowGroup {
            NavigationStack {
                if isLoggedIn {
                    BottomNavBar()
                        .environmentObject(homeViewModel)
                        .environmentObject(upcomingVM)

                } else if hasSeenOnboarding {
                    SignInView()

                } else {
                    WelcomeScreen()
                }
            }
        }
    }
}
