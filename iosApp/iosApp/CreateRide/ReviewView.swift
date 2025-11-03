//
//  ReviewView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import SwiftUI

struct ReviewView: View {
    @ObservedObject var viewModel: CreateRideViewModel
    
    var body: some View {
        ZStack{
            VStack(spacing: 20) {
                stepIndicator
                VStack(alignment: .leading, spacing: 20) {
                    HStack {
                        Text("Review Your Ride")
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
                        title: "Date and Time",
                        subtitle: formattedDateTime)
                    ReviewCard(
                        icon: AppIcon.CreateRide.route,
                        iconColor: AppColor.purple,
                        title: "Route",
                        subtitle: "\(viewModel.ride.startLocation) - \(viewModel.ride.endLocation)")
                    ReviewCard(
                        icon: AppIcon.Home.group,
                        iconColor: AppColor.lightOrange,
                        title: "Participants",
                        subtitle:"\(viewModel.selectedParticipants.count) riders selected")
                }
                .frame(width: 343, height: 430)
                .padding()
                .background(AppColor.backgroundLight)
                .cornerRadius(10)
            }
            if viewModel.isRideLoading {
                Color.black.opacity(0.5)
                    .ignoresSafeArea()
                ProgressView("Loading...")
                    .progressViewStyle(CircularProgressViewStyle())
                    .padding(.top, 100)
                    .foregroundColor(.white)
            }
        }
        
        Spacer()
        HStack(spacing: 15) {
            ButtonView( title: AppStrings.CreateRide.previous.rawValue,
                        background: LinearGradient(
                            gradient: Gradient(colors: [.white, .white]),
                            startPoint: .leading,
                            endPoint: .trailing),
                        foregroundColor: AppColor.celticBlue,
                        showShadow: false ,
                        borderColor: AppColor.celticBlue , onTap: {
                viewModel.previousStep()
            })
            
            ButtonView( title: AppStrings.CreateRide.create.rawValue,
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
    var stepIndicator: some View {
        HStack(spacing: 32) {
            StepIndicator(icon: AppIcon.Home.createRide, title: "Details", isActive: true, isCurrentPage: false)
            StepIndicator(icon: AppIcon.CreateRide.route, title: "Route", isActive: true, isCurrentPage: false)
            StepIndicator(icon: AppIcon.Home.group, title: "Participants",isActive: true, isCurrentPage: false)
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
        if viewModel.selectedParticipants.count > 1{
            return "Group ride"
        }
        else{
            return "Solo ride"
        }
    }
}

#Preview {
    ReviewView(viewModel: CreateRideViewModel.init())
}
