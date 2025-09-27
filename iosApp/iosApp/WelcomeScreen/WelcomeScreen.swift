import SwiftUI

public struct WelcomeScreen: View {
    public init() {}
    
    @State private var currentPage = 0
    private let totalPages = 3
    
    private let backgrounds: [Image] = [
            Image("WelcomeBg"),
            Image("WelcomeBg2"),
            Image("WelcomeBg3")
        ]
    
    public var body: some View {
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
                    
                 
                    
                    VStack(spacing: 16) {
                    
                        
                        // Gradient button
                        Button(action: {
                           NavigationLink("", destination: LoginView())
                        }) {
                            HStack {
                                Text("GET STARTED")
                                    .font(.custom("Klavika-Bold", size: 18))
                                    .foregroundColor(.white)
                                Spacer()
                                Image(systemName: "chevron.right")
                                    .foregroundColor(.white)
                            }
                            .padding(.horizontal, 20)
                            .frame(width: 296, height: 60)
                            .background(
                                LinearGradient(
                                    gradient: Gradient(colors: [
                                       
                                        Color("RoyalBlue"),
                                        Color("PursianBlue"),
                                    ]),
                                    startPoint: .leading,
                                    endPoint: .trailing
                                )
                            )
                            .cornerRadius(15)
                            .shadow(color: Color.black.opacity(0.20), radius: 4, x: 0, y: 2)
                        }
                        .padding(.bottom, 30)

                    }
                    .padding(.top, 305)
                }
                .frame(height: 525)
            }
            VStack {
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


