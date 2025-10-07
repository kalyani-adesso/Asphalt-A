// The Swift Programming Language
// https://docs.swift.org/swift-book
import SwiftUI


struct HomeScreen : View {
        var body: some View {
            NavigationLink(destination: NavigationSlideBar()) {
                Text("Hello, World!")
            }
        
    }
}

#Preview {
    HomeScreen()
}
