//
//  DashboardView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct ActionButtonView: View {
    @State private var isPresented: Bool = false
    @State private var showJoinRide: Bool = false
    @ObservedObject var viewModel: CreateRideViewModel
    @ObservedObject var upcomingRideViewModel: UpcomingRideViewModel
    @ObservedObject var homeViewModel: HomeViewModel
    @State private var showConnectedRide: Bool = false
    /// When true, show shimmer placeholders instead of buttons.
    let showSkeleton: Bool
    var body: some View {
        ZStack {
            if showSkeleton {
                actionButtonsSkeleton
            } else {
                HStack(spacing: 12) {
                    ButtonView( title: AppStrings.HomeLabel.createRide.rawValue,
                                icon: AppIcon.Home.createRide,
                                fontSize: 16, showShadow: false,  onTap: {
                        isPresented = true
                    }
                    )
                    ButtonView( title: AppStrings.HomeLabel.joinRide.rawValue,
                                icon: AppIcon.Home.group,
                                fontSize: 16,background: AppColor.white,
                                foregroundColor: AppColor.celticBlue,
                                showShadow: false,
                                borderColor: AppColor.celticBlue,onTap: {
                        // Use cached activeRide for instant navigation; keep background refresh elsewhere.
                        if viewModel.activeRide?.rideJoined == true {
                            showConnectedRide = true
                        } else {
                            showJoinRide = true
                        }
                    })
                }
            }
        }
        .padding(.vertical,10)
        .navigationDestination(isPresented: $isPresented, destination: {
            CreateRideView(upcomingRideVM: upcomingRideViewModel, homeVM: homeViewModel)
                .environmentObject(viewModel)
        })
        .navigationDestination(isPresented: $showJoinRide, destination: {
            JoinRideView()
        })
        .navigationDestination(isPresented: $showConnectedRide, destination: {
            if let ride = viewModel.activeRide {
                ConnectedRideView(
                    notificationTitle: AppStrings.JoinRide.rideActive,
                    title: AppStrings.ConnectedRide.startRideTitle,
                    subTitle: AppStrings.ConnectedRide.startRideSubtitle,
                    model: ride,
                    rideCompleteModel: [],
                    upcomingViewModel: upcomingRideViewModel, homeViewModel: homeViewModel
                )
            }
        })
       
    }

    private var actionButtonsSkeleton: some View {
        let fill = AppColor.whiteGray.opacity(0.72)
        return HStack(spacing: 12) {
            RoundedRectangle(cornerRadius: 15)
                .fill(fill)
                .frame(height: 51)
            RoundedRectangle(cornerRadius: 15)
                .fill(fill)
                .frame(height: 51)
        }
        .localizedSkeletonShimmer(cornerRadius: 15, cycleDuration: 1.5)
    }
}
