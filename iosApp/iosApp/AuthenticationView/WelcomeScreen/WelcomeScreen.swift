import SwiftUI

public struct WelcomeScreen: View {
    @State private var currentPage = 0
    private let totalPages = 3
    @State private var showSignin: Bool = false
    private let backgrounds: [Image] = [
        AppImage.Welcome.bg,
        AppImage.Welcome.bg2,
        AppImage.Welcome.bg3
    ]
    
    public var body: some View {
        NavigationStack {
            ZStack {
                // Top background = image area
                backgrounds[currentPage]
                    .resizable()
                    .scaledToFill()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .ignoresSafeArea()
                
                // Blue curved bottom section
                VStack {
                    Spacer()
                    ZStack {
                        Color("CelticBlue")
                            .clipShape(RoundedRectangle(cornerRadius: 40, style: .continuous))
                            .edgesIgnoringSafeArea(.bottom)
                    }
                    .frame(height: 525)
                }
                VStack {
                    Spacer() .frame(height: 150)
                    
                    TabView(selection: $currentPage) {
                        ForEach(0..<totalPages, id: \.self) { index in
                            VStack(spacing: 16) {
                                // Each page: image, dots, then text
                                WelcomeTabView(
                                    image: backgrounds[index],
                                    title: title(for: index),
                                    subtitle: subtitle(for: index),
                                    currentPage: $currentPage,
                                    totalPages: totalPages
                                )
                            }
                            .tag(index)
                        }
                    }
                    .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
                    Button(action: {
                        MBUserDefaults.hasSeenOnboardingStatic = true
                        showSignin = true
                    }) {
                        HStack {
                            Text("GET STARTED")
                            Spacer()
                            AppIcon.Welcome.arrow
                            
                        }
                        .padding(.horizontal, 30)
                        .frame(width: 296, height: 60)
                        .background(
                            LinearGradient(
                                gradient: Gradient(colors: [
                                    AppColor.royalBlue,
                                    AppColor.pursianBlue,
                                ]),
                                startPoint: .leading,
                                endPoint: .trailing
                            )
                        )
                        .cornerRadius(15)
                        .shadow(color: Color.black.opacity(0.20), radius: 4, x: 0, y: 2)
                        .foregroundStyle(AppColor.white)
                        .font(KlavikaFont.bold.font(size: 18))
                        
                    }
                    .padding(.bottom, 100)
                    .navigationDestination(isPresented: $showSignin, destination: {
                        SignInView()
                    })
                }
            }
        }
        
    }
    private func title(for index: Int) -> String {
        switch index {
        case 0: return AppStrings.WelcomeStrings.welcomeTitle
        case 1: return AppStrings.WelcomeStrings.communityTitle
        case 2: return AppStrings.WelcomeStrings.rideTitle
        default: return ""
        }
    }
    
    private func subtitle(for index: Int) -> String {
        switch index {
        case 0: return AppStrings.WelcomeStrings.welcomeSubtitle
        case 1: return AppStrings.WelcomeStrings.communitySubtitle
        case 2: return AppStrings.WelcomeStrings.rideSubtitle
        default: return ""
        }
    }
}

#Preview {
    WelcomeScreen()
}


