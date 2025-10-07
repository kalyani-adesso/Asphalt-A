//
//  HomeView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI


struct HomeView: View {
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
            }
            .ignoresSafeArea(edges: .top)
            .navigationBarHidden(true) // Hide system bar
        }
    }
}



#Preview {
    HomeView()
}
