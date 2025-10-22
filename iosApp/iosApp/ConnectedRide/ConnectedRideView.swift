//
//  ConnectedRideView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 18/10/25.
//

import SwiftUI

struct ConnectedRideView: View {
    let notificationTitle:String
    let title:String
    let subTitle:String
    let model:JoinRideModel
    @State var showView = false
    var body: some View {
        VStack {
            HStack(spacing: 10) {
                AppIcon.ConnectedRide.checkmark
                    .padding(.leading,20)
                Text(notificationTitle)
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
            .padding([.leading, .trailing,.top],16)
            .padding(.bottom,183)
            DisplayView
                .padding(.bottom,32)
            LoadingView()
            Spacer()
        }
        .navigationTitle(AppStrings.ConnectedRide.connectedRide)
        .navigationBarBackButtonHidden()
        .navigationDestination(isPresented: $showView, destination: {
            if title == "Completing ride" {
                ConnectedRideCompleteView(viewModel:model )
            } else {
                ConnectedRideMapView()
            }
        })
        .onAppear {
            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                self.showView = true
            }
        }
    }
    
    @ViewBuilder var DisplayView: some View {
        VStack {
            AppImage.Logos.rider
                .resizable()
                .frame(width: 50,height: 60)
                .padding(.bottom,16)
            Text(title)
                .font(KlavikaFont.bold.font(size: 22))
                .foregroundStyle(AppColor.richBlack)
                .padding(.bottom,6)
            Text(subTitle)
                .font(KlavikaFont.regular.font(size: 16))
                .foregroundStyle(AppColor.oldBurgundy)
        }
    }
}

#Preview {
    ConnectedRideView(notificationTitle: AppStrings.ConnectedRide.rideCompleted, title: AppStrings.ConnectedRide.startRideTitle, subTitle: AppStrings.ConnectedRide.startRideSubtitle, model:JoinRideModel(
        title: "Weekend Coast Ride",
        organizer: "Sooraj",
        description: "Join us for a beautiful sunrise ride along the coastal highway",
        route: "Kochi - Kanyakumari",
        distance: "280km",
        date: "Sun, Oct 21",
        time: "09:00 AM",
        ridersCount: "3",
        maxRiders: "8",
        riderImage: "rider_avatar"
    ) )
}
