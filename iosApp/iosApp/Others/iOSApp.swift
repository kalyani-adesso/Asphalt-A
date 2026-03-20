import SwiftUI
import FirebaseCore
import shared

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
    init() {
        KoinHelperKt.doInitKoin()
    }
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
            .preferredColorScheme(.light)
            .onOpenURL { url in
                let rideId: String? = {
                    if !url.lastPathComponent.isEmpty { return url.lastPathComponent }
                    if let host = url.host, !host.isEmpty { return host }
                    return nil
                }()
                if let id = rideId, !id.isEmpty {
                    MBUserDefaults.deepLinkRideIdStatic = id
                }
            }
        }
    }
}
