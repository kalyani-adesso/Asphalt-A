// The Swift Programming Language
// https://docs.swift.org/swift-book


import SwiftUI
import Shared

struct WelocmePage: View {
    public init() {}
       
       public var body: some View {
           TabView {
               WelcomePageView(
                   image: "WelcomeBg",
                   title: AppStrings.welcomeTitle,
                   subtitle: WelcomeStrings.welcomeSubtitle
               )
               WelcomePageView(
                   image: "WelcomeBg2",
                   title: WelcomeStrings.communityTitle,
                   subtitle: WelcomeStrings.communitySubtitle
               )
               WelcomePageView(
                   image: "WelcomeBg3",
                   title: WelcomeStrings.rideTitle,
                   subtitle: WelcomeStrings.rideSubtitle
               )
           }
           .tabViewStyle(PageTabViewStyle())
       }
}

@available(iOS 13.0, *)
#Preview {
    WelocmePage()
}

struct WelcomePageView: View {
    let image: String
    let title: String
    let subtitle: String
    
    var body: some View {
        VStack {
            Image(image)
                .resizable()
                .scaledToFit()
                .frame(height: 300)
                .clipShape(RoundedRectangle(cornerRadius: 16))
                .padding()
            
            Text(title)
                .font(.title)
                .fontWeight(.bold)
                .padding(.top, 16)
            
            Text(subtitle)
                .font(.body)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 24)
                .padding(.top, 8)
            
            Spacer()
            
            Button(action: {
                // Navigate to Home or Login
            }) {
                Text("Get Started")
                    .bold()
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.blue)
                    .foregroundColor(.white)
                    .cornerRadius(12)
            }
            .padding(.horizontal, 24)
            .padding(.bottom, 40)
        }
    }
}
