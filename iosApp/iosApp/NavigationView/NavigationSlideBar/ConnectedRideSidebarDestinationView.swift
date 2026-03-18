//
//  ConnectedRideSidebarDestinationView.swift
//  iosApp
//
//  Fetches active joined ride on open (same as Join Ride on Home) so Connected Ride
//  always shows loader → map when user has an ongoing ride.
//

import SwiftUI

struct ConnectedRideSidebarDestinationView: View {
    @EnvironmentObject private var createRideVM: CreateRideViewModel

    @State private var fetchDone = false

    var body: some View {
        Group {
            if !fetchDone {
                connectedRideStyleLoadingPlaceholder
            } else if createRideVM.activeRide?.rideJoined == true, let ride = createRideVM.activeRide {
                ConnectedRideView(
                    notificationTitle: AppStrings.JoinRide.rideActive,
                    title: AppStrings.ConnectedRide.startRideTitle,
                    subTitle: AppStrings.ConnectedRide.startRideSubtitle,
                    model: ride,
                    rideCompleteModel: []
                )
                .id(ride.rideId)
            } else {
                JoinRideView()
            }
        }
        .task {
            await createRideVM.getActiveJoinedRide()
            fetchDone = true
        }
    }

    /// Matches ConnectedRideView pre-map layout so the transition feels like Home → Join Ride.
    private var connectedRideStyleLoadingPlaceholder: some View {
        VStack(spacing: 0) {
            HStack(spacing: 10) {
                AppIcon.ConnectedRide.checkmark
                    .padding(.leading, 20)
                Text(AppStrings.JoinRide.rideActive)
                    .font(KlavikaFont.bold.font(size: 14))
                    .foregroundStyle(.spanishGreen)
                Spacer()
            }
            .frame(maxWidth: .infinity)
            .frame(height: 60)
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.lightGreen)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(AppColor.aeroGreen, lineWidth: 2)
            )
            .padding([.leading, .trailing, .top], 16)
            .padding(.bottom, 16)

            Spacer()

            AppImage.Logos.rider
                .resizable()
                .frame(width: 50, height: 60)
                .padding(.bottom, 16)
            Text(AppStrings.ConnectedRide.startRideTitle)
                .font(KlavikaFont.bold.font(size: 22))
                .foregroundStyle(AppColor.richBlack)
                .padding(.bottom, 6)
            Text(AppStrings.ConnectedRide.startRideSubtitle)
                .font(KlavikaFont.regular.font(size: 16))
                .foregroundStyle(AppColor.oldBurgundy)
                .padding(.bottom, 32)
            LoadingView()

            Spacer()
        }
        .navigationTitle(AppStrings.ConnectedRide.connectedRide)
        .navigationBarTitleDisplayMode(.inline)
    }
}
