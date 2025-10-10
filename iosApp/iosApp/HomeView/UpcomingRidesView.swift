//
//  UpcomingRides.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct UpcomingRidesView: View {
    @StateObject private var home = HomeViewModel()
    var body: some View {
        VStack(alignment: .leading, spacing: 20) {
            HStack {
                Text( AppStrings.HomeLabel.upcomingRides.rawValue)
                    .font(KlavikaFont.bold.font(size: 18))
                Spacer()
                Button("View All") { }
                    .font(KlavikaFont.bold.font(size: 13))
            }
            
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 25) {
                    ForEach(home.upcomingRides) { ride in
                        UpcomingRideCard(ride: ride)
                    }
                }
            }
        }
        .padding(.top,20)
    }
}

struct UpcomingRideCard: View {
    let ride: UpcomingRide
    let totalCount = 5
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
                    .overlay(Text(initials(from: ride.hostName)).font(.headline))
                VStack(alignment: .leading, spacing: 4) {
                    Text("Invite from \(ride.hostName)")
                        .font(KlavikaFont.bold.font(size: 16))
                    Text(ride.route)
                        .font(KlavikaFont.regular.font(size: 12))
                        .foregroundColor(AppColor.stoneGray)
                }
                Spacer()
                AppIcon.Home.message
            }
            HStack(spacing: 8) {
                AppIcon.Home.calender
                Text(ride.date, style: .date)
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(AppColor.stoneGray)
                Text("-")
                    .foregroundColor(AppColor.stoneGray)
                Text(ride.date, style: .time)
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(AppColor.stoneGray)
                Spacer()
            }
            HStack{
                HStack(spacing: 8) {
                    AppIcon.Home.group.resizable()
                        .frame(width: 15, height: 15)
                    Text("5 people joined this ride")
                        .font(KlavikaFont.regular.font(size: 12))
                        .foregroundColor(AppColor.stoneGray)
                }
                Spacer()
                HStack{
                    AppImage.Profile.profile.resizable()
                        .frame(width: 19, height: 19)
                        .clipShape(Circle())
                        .overlay(
                            RoundedRectangle(cornerRadius: 32.5)
                                .stroke(AppColor.green, lineWidth: 1.5)
                        )
                    AppImage.Profile.profile.resizable()
                        .frame(width: 19, height: 19)
                        .clipShape(Circle())
                        .overlay(
                            RoundedRectangle(cornerRadius: 32.5)
                                .stroke(AppColor.grayishBlue, lineWidth: 1.5)
                        )
                    AppImage.Profile.profile.resizable()
                        .frame(width: 19, height: 19)
                        .clipShape(Circle())
                        .overlay(
                            RoundedRectangle(cornerRadius: 32.5)
                                .stroke(AppColor.grayishBlue, lineWidth: 1.5)
                        )
                }
                if totalCount > 3 {
                    ZStack {
                        Circle()
                            .fill(Color.celticBlue)
                            .frame(width: 19, height: 19)
                            .overlay(
                                RoundedRectangle(cornerRadius: 32.5)
                                    .stroke(AppColor.grayishBlue, lineWidth: 1.5)
                            )
                        Text("+\(totalCount - 3)")
                            .font(KlavikaFont.bold.font(size: 12))
                            .foregroundColor(AppColor.white)
                    }
                }
                
            }
            HStack {
                ButtonView(title: AppStrings.HomeButton.accept.rawValue, height: 50)
                ButtonView(title: AppStrings.HomeButton.decline.rawValue,   background: LinearGradient(
                    gradient: Gradient(colors: [AppColor.darkRed, AppColor.darkRed]),
                    startPoint: .leading,
                    endPoint: .trailing), height: 50)
                
            }
            .padding(.bottom,-15)
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


#Preview {
    UpcomingRidesView()
}
