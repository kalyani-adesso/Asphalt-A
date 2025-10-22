//
//  JoinRideView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 17/10/25.
//

import Foundation
import SwiftUI

struct JoinRideView: View {
    @State private var searchQuery: String = ""
    @StateObject private var viewModel = JoinRideViewModel()
    @State private var showConnectedRides: Bool = false
    @State private var showHomeView:Bool = false
    @StateObject private var homeViewModel = HomeViewModel()
//    @Environment(\.dismiss) var dismiss
    var body: some View {
        VStack {
            searchBarView
                 .font(KlavikaFont.regular.font(size: 14))
                 .padding(.top)
            List(viewModel.rides, id: \.id) { ride in
                JoinRideRow(ride: ride, showConnectedRides: $showConnectedRides)
                    .listRowSeparator(.hidden)
            }
            .listStyle(.plain)
            Spacer()
        }
        .navigationTitle(AppStrings.HomeLabel.joinRide.localized)
        .navigationBarBackButtonHidden()
        .toolbar {
            ToolbarItem(placement: .topBarLeading, content: {
                Button(action: {
//                    dismiss()
                    showHomeView = true
                }, label:{
                    AppIcon.CreateRide.backButton
                })
            })
        }
        .navigationDestination(isPresented: $showHomeView, destination: {
            HomeView()
                .environmentObject(homeViewModel)
        })
        .navigationBarTitleDisplayMode(.inline)
        .navigationDestination(isPresented: $showConnectedRides, destination: {
            ConnectedRideView(notificationTitle: "Ride Started! Navigation active.", title: AppStrings.ConnectedRide.startRideTitle, subTitle: AppStrings.ConnectedRide.startRideSubtitle, model: JoinRideModel(
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
        })
        
    }
    @ViewBuilder var searchBarView: some View {
        HStack(spacing: 6) {
            AppIcon.JoinRide.search
                .resizable()
                .frame(width: 20, height: 20)
                .background(AppColor.whiteGray)
                .padding(.leading)
            TextField(AppStrings.JoinRide.searcRide, text: $searchQuery)
                .font(KlavikaFont.regular.font(size: 14))
                .foregroundColor(.stoneGray)
                .background(.clear)
                .background(AppColor.whiteGray)
        }
        .frame(maxWidth: .infinity)
        .frame(height: 50)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
        .padding([.leading, .trailing],16)
    }
}

struct JoinRideRow: View {
    var ride:JoinRideModel
    @Binding var showConnectedRides: Bool
    var body: some View {
        VStack(alignment: .leading, spacing: 22) {
            HStack(spacing: 11) {
                 AppIcon.Profile.profile
                    .resizable()
                    .overlay(
                        RoundedRectangle(cornerRadius: 15)
                            .stroke(AppColor.celticBlue.opacity(0.8), lineWidth: 2)
                    )
                    .frame(width: 30, height: 30)
                VStack(alignment: .leading, spacing: 4) {
                    Text(ride.title)
                        .font(KlavikaFont.bold.font(size: 16))
                        .foregroundColor(AppColor.black)
                    Text("By \(ride.organizer)")
                        .font(KlavikaFont.regular.font(size: 12))
                        .foregroundColor(AppColor.richBlack)
                }
                Spacer()
            }
            
            Text(ride.description)
                .font(KlavikaFont.regular.font(size: 14))
                .foregroundColor(AppColor.stoneGray)
            
            HStack {
                HStack(spacing: 5) {
                    AppIcon.JoinRide.calenderToday
                        .resizable()
                        .frame(width: 16, height: 16)
                    Text(ride.date)
                        .font(KlavikaFont.regular.font(size: 16))
                        .foregroundStyle(AppColor.richBlack)
                }
                Spacer()
                HStack(spacing: 5) {
                    AppIcon.JoinRide.joinGroup
                        .resizable()
                        .frame(width: 16, height: 16)
                    Text("\(ride.ridersCount)/\(ride.maxRiders) rides")
                        .font(KlavikaFont.regular.font(size: 16))
                        .foregroundStyle(AppColor.richBlack)
                }
            }
            
            HStack {
                HStack(spacing: 5) {
                    AppIcon.JoinRide.location
                        .resizable()
                        .frame(width: 16, height: 16)
                    Text(ride.route)
                        .font(KlavikaFont.regular.font(size: 16))
                        .foregroundStyle(AppColor.richBlack)
                }
                Spacer()
                HStack(spacing: 5) {
                    AppIcon.JoinRide.route
                        .resizable()
                        .frame(width: 16, height: 16)
                    Text(ride.distance)
                        .font(KlavikaFont.regular.font(size: 16))
                        .foregroundStyle(AppColor.richBlack)
                }
            }
            
            HStack(spacing: 15) {
                Button(action: {
                    
                }) {
                    HStack {
                        AppIcon.NavigationSlider.call
                            .resizable()
                            .frame(width: 20, height: 20)
                        Text(AppStrings.JoinRide.callRider.uppercased())
                            .font(KlavikaFont.bold.font(size: 14))
                            .foregroundStyle(AppColor.black)
                    }
                    .frame(maxWidth: .infinity, minHeight: 50)
                    .background(
                        RoundedRectangle(cornerRadius: 10)
                            .fill(AppColor.white)
                    )
                    .overlay(
                        RoundedRectangle(cornerRadius: 10)
                            .stroke(AppColor.darkGray, lineWidth: 2)
                    )
                  
                }
                .padding(.bottom,20)
                .buttonStyle(.plain)
                ButtonView(title: AppStrings.JoinRide.joinRide.uppercased(),icon: AppIcon.JoinRide.movedLocation,onTap: {
                    showConnectedRides = true
                })
                .frame(maxWidth: .infinity)
                .padding(.bottom,20)
            }
        }
        .padding([.leading,.trailing,.top],16)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
        .contentShape(Rectangle())
    }
}

#Preview {
    JoinRideView()
}
