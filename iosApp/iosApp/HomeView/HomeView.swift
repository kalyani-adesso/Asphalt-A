//
//  HomeView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct HomeView: View {
    @EnvironmentObject var home: HomeViewModel
    @EnvironmentObject var viewModel : UpcomingRideViewModel
    var body: some View {
        ZStack(alignment: .top) {
            ScrollView {
                VStack{
                    Color.clear.frame(height: 50)
                    ActionButtonView()
                    DashboardView()
                    UpcomingRidesView()
                        .environmentObject(home)
                        .environmentObject(viewModel)
                    JourneyCardView()
                    PlacesVisitedView()
                }
                .padding()
            }
            TopNavBar()
                .ignoresSafeArea(edges: .top)
            if viewModel.isRideLoading {
                ProgressViewReusable(title: "Loading ...")
            }
        }
        .task {
            viewModel.isRideLoading = true
            
            async let rides = viewModel.fetchAllUsers()
            async let allRides = viewModel.fetchAllRides()
            
            _ = await (rides, allRides)
            
            viewModel.isRideLoading = false
        }
    }
}

#Preview {
    HomeView()
        .environmentObject(HomeViewModel())
        .environmentObject(UpcomingRideViewModel())
}
