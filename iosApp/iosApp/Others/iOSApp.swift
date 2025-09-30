import SwiftUI

@main
struct iOSApp: App {
    @AppStorage("hasSeenOnboarding") var hasSeenOnboarding: Bool = false
    var body: some Scene {
        WindowGroup {
            if hasSeenOnboarding {
                NavigationStack {
                    SignInView()
                }
            } else {
                WelcomeScreen()
            }
        }
        
    }
}
