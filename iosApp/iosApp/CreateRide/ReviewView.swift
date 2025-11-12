//
//  ReviewView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import SwiftUI

struct ReviewView: View {
    @EnvironmentObject var viewModel: CreateRideViewModel
    
    var body: some View {
        ZStack{
            VStack{
                VStack(spacing: 20) {
                    stepIndicator
                    VStack(alignment: .leading, spacing: 20) {
                        HStack {
                            Text(AppStrings.CreateRide.reviewSubTitle)
                                .font(KlavikaFont.medium.font(size: 16))
                        }
                        ReviewCard(
                            icon: AppIcon.Home.nearMe,
                            iconColor: AppColor.lightBlue,
                            title: viewModel.ride.title,
                            subtitle: viewModel.ride.description,
                            tag: tagValue)
                        ReviewCard(
                            icon: AppIcon.Home.calender,
                            iconColor: AppColor.darkOrange,
                            title: AppStrings.CreateRide.reviewDate,
                            subtitle: formattedDateTime)
                        ReviewCard(
                            icon: AppIcon.CreateRide.route,
                            iconColor: AppColor.purple,
                            title:AppStrings.CreateRide.reviewRoute,
                            subtitle: "\(viewModel.ride.startLocation) - \(viewModel.ride.endLocation)")
                        ReviewCard(
                            icon: AppIcon.Home.group,
                            iconColor: AppColor.lightOrange,
                            title: AppStrings.CreateRide.reviewParticipants,
                            subtitle:"\(viewModel.selectedParticipants.count) riders selected")
                    }
                    .frame(width: 343, height: 430)
                    .padding()
                    .background(AppColor.backgroundLight)
                    .cornerRadius(10)
                }

                Spacer()

                HStack(spacing: 15) {
                    ButtonView( title: AppStrings.CreateRideButton.previous.rawValue,
                                background: LinearGradient(
                                    gradient: Gradient(colors: [.white, .white]),
                                    startPoint: .leading,
                                    endPoint: .trailing),
                                foregroundColor: AppColor.celticBlue,
                                showShadow: false ,
                                borderColor: AppColor.celticBlue , onTap: {
                        viewModel.previousStep()
                    })

                    ButtonView( title: AppStrings.CreateRideButton.create.rawValue,
                                showShadow: false , onTap: {
                        viewModel.createRide(completion: {success in
                            if success {
                                viewModel.nextStep()
                            }
                        })
                    }
                    )
                }
                .padding()
            }
            
            if viewModel.isRideLoading {
                ProgressViewReusable(title: "Loading ...")
            }
        }

    }
    var stepIndicator: some View {
        HStack(spacing: 32) {
            StepIndicator(icon: AppIcon.Home.createRide, title: "Details", isActive: true, isCurrentPage: false)
            StepIndicator(icon: AppIcon.CreateRide.route, title: "Route", isActive: true, isCurrentPage: false)
            if viewModel.ride.type?.rawValue != "Solo Ride" {
                StepIndicator(icon: AppIcon.Home.group, title: "Participants",isActive: true, isCurrentPage: false)
            }
            StepIndicator(icon: AppIcon.CreateRide.review, title: "Review",isActive: true, isCurrentPage: true)
            StepIndicator(icon: AppIcon.CreateRide.share, title: "Share")
        }
    }
    // MARK: Date formatting helper
    var formattedDateTime: String {
        let date = viewModel.ride.date
        let formatter = DateFormatter()
        formatter.dateFormat = "E, MMM d - hh:mm a"
        return formatter.string(from: date ?? Date())
    }
    
    var tagValue: String{
        if viewModel.selectedParticipants.count > 0{
            return "Group ride"
        }
        else{
            return "Solo ride"
        }
    }
}

#Preview {
    ReviewView()
}
