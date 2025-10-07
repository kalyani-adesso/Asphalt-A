//
//  HomeView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI


struct HomeView: View {
    
    @State var showSnackbar: Bool = false
    var body: some View {
        NavigationStack {
            ZStack(alignment: .top) {
                
                ScrollView {
                    VStack{
                        Color.clear.frame(height: 100)
                        ActionButton()
                        DashboardView()
                        UpcomingRides()
 
                    }
                    .padding()
                }
                
                CustomNavBar()
                if showSnackbar {
                    AppColor.overlay.opacity(0.75)
                        .ignoresSafeArea()
                        .transition(.opacity)
                        .zIndex(0.5)
                }
                
                if showSnackbar {
                    VStack(spacing: 8) {
                        Spacer().frame(height: 130) // Space from top
                        Snackbar(
                            message: AppStrings.HomeSnackbarLabel.title.rawValue,
                            subMessage: AppStrings.HomeSnackbarLabel.subtitle.rawValue,
                            background: LinearGradient(
                                gradient: Gradient(colors: [AppColor.snackbargradientLight, AppColor.snackbargradientDark]),
                                startPoint: .leading,
                                endPoint: .trailing),
                            icon: AppIcon.Home.snackbar
                        )
                    }
                    .transition(.move(edge: .top).combined(with: .opacity))
                    .zIndex(1)
                }
            }
            .animation(.spring(), value: showSnackbar)
            .ignoresSafeArea(edges: .top)
            .navigationBarHidden(true)
            .onAppear {
                showSnackbar = true
                DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                    withAnimation {
                        showSnackbar = false
                    }
                }
            }

        }
        
    }
}



#Preview {
    HomeView(showSnackbar: false)
}
