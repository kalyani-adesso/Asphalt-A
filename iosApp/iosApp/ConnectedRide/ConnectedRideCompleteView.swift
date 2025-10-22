//
//  ConnectedRideCompleteView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 19/10/25.
//

import Foundation
import SwiftUI

struct ConnectedRideCompleteView: View {
    var viewModel: JoinRideModel
    @StateObject var connectedRideViewModel = ConnectedRideViewModel()
    @StateObject private var homeViewModel = HomeViewModel()
    @State var rating: Int = 0
    @State var showHome: Bool = false
    @State var showMapView:Bool = false
//    @Environment(\.dismiss) var dismiss
    var body: some View {
        VStack(spacing: 16) {
            SucessView(title: viewModel.title,
                       subtitle: AppStrings.ConnectedRide.rideCompleted)
            
            RideDifficultyLevel
            RateThisRide
            RideButtonsView
        }
        .padding(.horizontal,16)
        .navigationDestination(isPresented: $showHome, destination: {
            HomeView()
                .environmentObject(homeViewModel)
        })
        .navigationTitle(AppStrings.ConnectedRide.connectedRide)
        .navigationBarBackButtonHidden()
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .topBarLeading, content: {
                Button(action: {
                    showMapView = true
                }, label:{
                    AppIcon.CreateRide.backButton
                })
            })
        }
        .navigationDestination(isPresented: $showMapView, destination: {
            ConnectedRideMapView()
        })
    }
    
    @ViewBuilder var RideDifficultyLevel: some View {
        VStack(alignment:.leading, spacing: 20) {
            Text(AppStrings.ConnectedRide.rideDifficultyLevel)
                .font(KlavikaFont.bold.font(size: 16))
                .foregroundStyle(AppColor.black)
                .padding(.top, 30)
                .padding(.leading,16)
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 18) {
                    ForEach(connectedRideViewModel.rideCompleteModel, id: \.id) { rideComplete in
                        RideDetails(
                            image: rideComplete.iconName,
                            title: rideComplete.value,
                            subTitle: rideComplete.label
                        )
                        .background(AppColor.white)
                        .cornerRadius(10)
                    }
                }
                .padding([.leading, .bottom], 16)
            }
            .scrollDisabled(true)
        }
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
    }
    
    @ViewBuilder var RateThisRide: some View {
        VStack(alignment:.leading, spacing: 20) {
            Text(AppStrings.ConnectedRide.rateRide)
                .font(KlavikaFont.bold.font(size: 16))
                .foregroundStyle(AppColor.black)
                .padding(.top, 30)
            RatingView(rating: $rating, iconRate: AppIcon.ConnectedRide.rate, iconNotRate: AppIcon.ConnectedRide.notRate)
        }
        .frame(maxWidth: .infinity)
        .padding()
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
    }
    
    @ViewBuilder var RideButtonsView: some View {
        HStack(spacing: 20) {
            Button(action: {
                
            }) {
                HStack {
                    Text(AppStrings.UpcomingRide.share.uppercased())
                        .font(KlavikaFont.bold.font(size: 14))
                        .foregroundStyle(AppColor.celticBlue)
                }
                .frame(maxWidth: .infinity, minHeight: 50)
                .background(
                    RoundedRectangle(cornerRadius: 10)
                        .fill(AppColor.white)
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(AppColor.celticBlue, lineWidth: 1)
                )
              
            }
            .buttonStyle(.plain)
            ButtonView(title: AppStrings.NavigationSlider.home.uppercased(),onTap: {
               showHome = true
            })
            .frame(maxWidth: .infinity)
        }
    }
}

struct  RideDetails:View {
    var image:Image
    var title:String
    var subTitle:String
    var body: some View {
        VStack(spacing: 8) {
            image
                .resizable()
                .frame(width: 22, height: 22)
                .padding(.top,14)
            Text(title)
                .font(KlavikaFont.bold.font(size: 16))
                .foregroundColor(AppColor.black)
            Text(subTitle)
                .font(KlavikaFont.regular.font(size: 16))
                .foregroundColor(AppColor.stoneGray)
                .padding(.bottom,14)
        }
        .frame(width: 100,height: 100)
    }
}

#Preview {
    ConnectedRideCompleteView(viewModel: JoinRideModel(
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
    ))
}
