//
//  RideDetailsView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 18/11/25.
//

import SwiftUI

struct RideDetailsView: View {
    @ObservedObject var viewModel: UpcomingRideViewModel
    @Binding var ride:RideModel
    @State private var startRide = false
    @State private var showJoinRide: Bool = false
    var body: some View {
        NavigationStack {
            SimpleCustomNavBar(title:ride.title, onBackToHome: {showJoinRide = true})
            ScrollView {
                VStack(spacing: 20) {
                    VStack(alignment: .leading, spacing: 22) {
                        HStack(spacing: 11) {
                            (rideIcon)
                                .resizable()
                                .modifier(RoundCorner(rideAction: ride.rideAction))
                                .frame(width: 30, height: 30)
                            VStack(alignment: .leading, spacing: 4) {
                                Text(ride.title)
                                    .font(KlavikaFont.bold.font(size: 16))
                                    .foregroundColor(AppColor.black)
                                Text("\(ride.routeStart) - \(ride.routeEnd)")
                                    .font(KlavikaFont.regular.font(size: 12))
                                    .foregroundColor(AppColor.stoneGray)
                            }
                            Spacer()
                        }
                        
                        HStack {
                            HStack(spacing: 5) {
                                AppIcon.UpcomingRide.calender
                                    .resizable()
                                    .frame(width: 16, height: 16)
                                Text(ride.date)
                                    .font(KlavikaFont.regular.font(size: 16))
                                    .foregroundStyle(AppColor.richBlack)
                            }
                            Spacer()
                            HStack(spacing: 5) {
                                AppIcon.UpcomingRide.group
                                    .resizable()
                                    .frame(width: 16, height: 16)
                                Text("\(ride.riderCount) rides")
                                    .font(KlavikaFont.regular.font(size: 16))
                                    .foregroundStyle(AppColor.richBlack)
                            }
                        }
                    }
                    .padding([.leading,.trailing,.top,.bottom],16)
                    .background(
                        RoundedRectangle(cornerRadius: 10)
                            .fill(AppColor.listGray)
                    )
                    .contentShape(Rectangle())
                    
                    HStack(spacing: 16) {
                        StatusCard(count: ride.participantAcceptedCount, title: "Confirmed", color: AppColor.spanishGreen)
                        StatusCard(count: ride.riderCount, title: "Pending", color: AppColor.lightOrange)
                        StatusCard(count: declinedCount , title: "Declined", color: AppColor.darkRed)
                    }
                    .frame(height: 63)
                    .padding([.top,.bottom],16)
                    if viewModel.rideDetails.count > 0 {
                        VStack {
                            ForEach(viewModel.rideDetails) { rideDetails in
                                ParticipantsStatusRow(rideDetails:rideDetails )
                            }
                        }
                        .padding([.leading,.trailing,.top,.bottom],16)
                        .background(
                            RoundedRectangle(cornerRadius: 10)
                                .fill(AppColor.listGray)
                        )
                        .contentShape(Rectangle())
                    }
                
                    ButtonView(
                        title:"START RIDE",
                        icon: AppIcon.JoinRide.nearMe,
                        fontSize: 16 ,
                        background: LinearGradient(
                            gradient: Gradient(colors: [AppColor.dimGreen, AppColor.dimGreen]),
                            startPoint: .leading,
                            endPoint: .trailing),
                        foregroundColor: AppColor.white,
                        showShadow: false,
                        onTap: {
                            startRide = true
                        }
                    )
                    .padding([.top,.bottom],16)
                    Spacer()
                }
                .padding(.all,16)
                .navigationBarBackButtonHidden(true)
                .navigationDestination(isPresented: $showJoinRide, destination: {
                    UpcomingRideView()
                })
                .navigationDestination(isPresented:$startRide,  destination: {
                    ConnectedRideView(notificationTitle: AppStrings.JoinRide.rideActive, title: AppStrings.ConnectedRide.startRideTitle, subTitle: AppStrings.ConnectedRide.startRideSubtitle, model: viewModel.joinRideModel)
                })
                .onAppear {
                    Task {
                        await  viewModel.fetchAllUsers()
                    }
                }
            }
        }
        
        var declinedCount:Int  {
            ride.participantAcceptedCount > 0 ? (ride.riderCount  - ride.participantAcceptedCount) : 0
        }
    }
    
    struct StatusCard: View {
        let count: Int
        let title: String
        let color: Color
        
        var body: some View {
            VStack(spacing: 13) {
                Text("\(count)")
                    .font(KlavikaFont.bold.font(size: 18))
                    .foregroundColor(color)
                
                Text(title)
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(color)
            }
            .frame(maxWidth: .infinity, minHeight: 90)
            .background(
                RoundedRectangle(cornerRadius: 16)
                    .stroke(AppColor.secondaryGray,lineWidth: 1)
            )
            .padding(.horizontal, 5)
        }
    }
    
    var rideIcon:Image {
        if ride.status == .upcoming {
            AppIcon.UpcomingRide.upcomingRide
        } else if ride.status == .queue {
            AppIcon.UpcomingRide.queueRide
        } else {
            AppIcon.UpcomingRide.completRide
        }
    }
}

struct ParticipantsStatusRow: View {
    let rideDetails: RideDetailsModel
    var body: some View {
        HStack(spacing: 12) {
            ZStack(alignment: .bottomTrailing) {
                AppImage.Profile.profile.resizable()
                    .frame(width: 37, height: 37)
                    .clipShape(Circle())
                    .overlay(
                        RoundedRectangle(cornerRadius: 32.5)
                            .stroke(rideDetails.status != "waiting for response." ? AppColor.green : AppColor.carrotOrange, lineWidth: 2.5)
                    )
                Circle()
                    .fill(rideDetails.status != "waiting for response." ? AppColor.green : AppColor.carrotOrange)
                    .frame(width: 13, height: 13)
                    .offset(x: 2, y: 2)
                    .overlay(
                        Circle()
                            .offset(x: 2, y: 2)
                            .stroke(Color.white, lineWidth: 1.5)
                    )
            }
            HStack {
                VStack(alignment: .leading, spacing: 4) {
                    Text(rideDetails.userName)
                        .font(KlavikaFont.bold.font(size: 16))
                        .foregroundColor(AppColor.black)
                    Text(rideDetails.status)
                        .font(KlavikaFont.regular.font(size: 13))
                        .foregroundColor(.gray)
                }
                
                Spacer()
                if rideDetails.status == "Ride Creator" {
                    HStack(spacing: 4) {
                        Text("Organizer")
                            .foregroundStyle(AppColor.celticBlue)
                            .font(KlavikaFont.regular.font(size: 12))
                    }
                    .padding(.horizontal, 8)
                    .padding(.vertical, 5)
                    .background(AppColor.secondaryBlue)
                    .cornerRadius(6)
                } else {
                    rideDetails.status == "confirmed" ? AppIcon.JoinRide.rideConfirmed : AppIcon.JoinRide.ridePending
                }
            }
        }
        .padding()
        .background(Color.white)
        .cornerRadius(10)
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.darkGray, lineWidth: 1)
        )
    }
}
