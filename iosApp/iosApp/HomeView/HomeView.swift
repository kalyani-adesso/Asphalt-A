//
//  HomeView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct HomeView: View {
    
    var body: some View {
        ZStack(alignment: .top) {
            ScrollView {
                VStack{
                    Color.clear.frame(height: 50)
                    ActionButtonView()
                    DashboardView()
                    UpcomingRidesView()
                    JourneyCardView()
                    PlacesVisitedView()
                }
                .padding()
            }
            TopNavBar()
                .ignoresSafeArea(edges: .top)
        }
    }
}



#Preview {
    HomeView()
}
