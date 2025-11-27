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
    @State private var currentDate = Date()
    var body: some View {
        ZStack(alignment: .top) {
            ScrollView {
                VStack(spacing: 15){
                    TopNavBar()
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
            if viewModel.isRideLoading {
                ProgressViewReusable(title: "Loading ...")
            }
        }
        .task {
            viewModel.isRideLoading = true
            
            async let rides = viewModel.fetchAllUsers()
            async let allRides = viewModel.fetchAllRides()
            async let allInvites =  viewModel.getInvites()
            let month = Calendar.current.component(.month, from: currentDate)
            let year = Calendar.current.component(.year, from: currentDate)
            async let stats =  home.updateStatsFor(month: month, year: year)
            
            _ = await (rides, allRides, allInvites, stats)
            
            viewModel.isRideLoading = false
        }
        .refreshable {
            await viewModel.fetchAllRides()
            await viewModel.fetchAllUsers()
        }
    }
}

#Preview {
    HomeView()
        .environmentObject(HomeViewModel())
        .environmentObject(UpcomingRideViewModel())
}
