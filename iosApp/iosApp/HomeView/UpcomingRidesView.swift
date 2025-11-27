//
//  UpcomingRides.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct UpcomingRidesView: View {
    @EnvironmentObject var home: HomeViewModel
    @EnvironmentObject var viewModel : UpcomingRideViewModel
    @State private var showAllRides: Bool = false
    
    var body: some View {
        VStack(alignment: .leading, spacing: 20) {
            HStack {
                Text( AppStrings.HomeLabel.upcomingRides.rawValue)
                    .font(KlavikaFont.bold.font(size: 18))
                Spacer()
                Button("View All") {
                    showAllRides = true
                }
                .font(KlavikaFont.bold.font(size: 13))
            }
           
            if viewModel.upcomingInvitesRide.isEmpty {
                emptyStateView
            }
            else{
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 25) {
                        ForEach($viewModel.upcomingInvitesRide, id: \.id) { $ride in
                            UpcomingRideCard(viewModel: viewModel, ride: $ride)
                        }
                    }
                }
            }
        }
        .padding(.top,20)
        .navigationDestination(isPresented:$showAllRides , destination: {
            UpcomingRideView(startingTab: .invities)
                .environmentObject(viewModel)
                .environmentObject(home)
        })
        .task{
            await viewModel.fetchAllRides()
            await viewModel.fetchAllUsers()
        }
    }
}
struct UpcomingRideCard: View {
    @ObservedObject var viewModel: UpcomingRideViewModel
    @Binding var ride:RideModel
    var hostName: String {
        viewModel.usersById[ride.createdBy] ?? "Unknown"
    }
    var isMyRide: Bool {
        ride.createdBy == MBUserDefaults.userIdStatic
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack {
                AppImage.Profile.profile.resizable()
                    .frame(width: 29, height: 29)
                    .clipShape(Circle())
                    .overlay(
                        RoundedRectangle(cornerRadius: 32.5)
                            .stroke(AppColor.celticBlue, lineWidth: 2.5)
                    )
                    .overlay(Text(initials(from: hostName)).font(.headline))
                VStack(alignment: .leading, spacing: 4) {
                    Text(isMyRide ? ride.title : "Invite from  \(hostName)")
                        .font(KlavikaFont.bold.font(size: 16))
                    Text("\(ride.routeStart)")
                        .font(KlavikaFont.regular.font(size: 12))
                        .foregroundColor(AppColor.stoneGray)
                }
                Spacer()
                AppIcon.Home.message
            }
            HStack(spacing: 8) {
                AppIcon.Home.calender
                Text(ride.startDateStr)
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(AppColor.stoneGray)
                Spacer()
            }
            HStack{
                HStack(spacing: 8) {
                    AppIcon.Home.group.resizable()
                        .frame(width: 15, height: 15)
                    Text("\(ride.participantAcceptedCount) people joined this ride")
                        .font(KlavikaFont.regular.font(size: 12))
                        .foregroundColor(AppColor.stoneGray)
                }
                Spacer()
                HStack{
                    let displayCount = min(ride.participantAcceptedCount, 3)
                    ForEach(0..<displayCount, id: \.self) { index in
                        AppImage.Profile.profile.resizable()
                            .frame(width: 19, height: 19)
                            .clipShape(Circle())
                            .overlay(
                                RoundedRectangle(cornerRadius: 32.5)
                                    .stroke(index == 0 ? AppColor.green : AppColor.grayishBlue, lineWidth: 1.5)
                            )
                    }
                }
                if ride.participantAcceptedCount > 3 {
                    ZStack {
                        Circle()
                            .fill(AppColor.celticBlue)
                            .frame(width: 19, height: 19)
                            .overlay(
                                RoundedRectangle(cornerRadius: 32.5)
                                    .stroke(AppColor.grayishBlue, lineWidth: 1.5)
                            )
                        Text("+\(ride.participantAcceptedCount - 3)")
                            .font(KlavikaFont.bold.font(size: 12))
                            .foregroundColor(AppColor.white)
                    }
                }
                
            }
            if isMyRide {
                HStack {
                    ButtonView(title: "VIEW DETAILS", fontSize: 14, onTap :{
                        Task {
                            await viewModel.changeRideInviteStatus(rideId: ride.id, accepted: true)
                        }
                    }, height: 32)
                    ButtonView(title: "CANCEL RIDE",  fontSize: 14,  background: AppColor.darkRed, onTap :{
                        Task {
                            await viewModel.changeRideInviteStatus(rideId: ride.id, accepted: false)
                        }
                    },height: 32)
                    
                }
                .padding(.vertical,10)
            }
            else{
                HStack {
                    ButtonView(title: AppStrings.HomeButton.accept.rawValue, fontSize: 14, onTap :{
                        Task {
                            await viewModel.changeRideInviteStatus(rideId: ride.id, accepted: true)
                        }
                    }, height: 32)
                    ButtonView(title: AppStrings.HomeButton.decline.rawValue,  fontSize: 14,  background: AppColor.darkRed, onTap :{
                        Task {
                            await viewModel.changeRideInviteStatus(rideId: ride.id, accepted: false)
                        }
                    },height: 32)
                    
                }
                .padding(.vertical,10)
            }
            
        }
        .padding()
        .frame(width: 290)
        .background(AppColor.backgroundLight)
        .cornerRadius(14)
        .overlay(
            RoundedRectangle(cornerRadius: 14)
                .stroke(AppColor.darkGray, lineWidth: 2)
        )
    }
    
    private func initials(from name: String) -> String {
        name.split(separator: " ").compactMap { $0.first }.map { String($0) }.joined()
    }
}
@ViewBuilder
var emptyStateView: some View {
    VStack(spacing: 12) {
        HStack{
            AppIcon.UpcomingRide.message
                .resizable()
                .frame(width: 25, height: 25)
            
            Text("No Upcoming Rides !!!")
                .font(KlavikaFont.bold.font(size: 16))
                .foregroundColor(AppColor.richBlack)
            
        }
        Text("You’ll see your ride invites here once someone adds you.")
            .font(KlavikaFont.regular.font(size: 12))
            .foregroundColor(AppColor.stoneGray)
    }
    .frame(maxWidth: .infinity)
    .padding(.vertical, 30)
}

#Preview {
    UpcomingRidesView()
        .environmentObject(HomeViewModel())
        .environmentObject(UpcomingRideViewModel())
}
