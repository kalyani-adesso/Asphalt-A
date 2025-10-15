//
//  RouteView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import SwiftUI

struct RouteView: View {
    @ObservedObject var viewModel: CreateRideViewModel
    var body: some View {
        
        VStack(spacing: 20) {
            stepIndicator
            
            VStack(alignment: .leading, spacing: 20) {
                
                FormFieldView(
                    label: "Starting Point",
                    icon:  AppIcon.CreateRide.route,
                    placeholder: "Enter starting location",
                    iconColor: AppColor.darkLime,
                    value: $viewModel.ride.startLocation,
                    isValidEmail: .constant(false),
                    backgroundColor: AppColor.white)
                FormFieldView(
                    label: "Destination",
                    icon:  AppIcon.CreateRide.route,
                    placeholder: "Enter destination",
                    iconColor: AppColor.darkRed,
                    value: $viewModel.ride.endLocation,
                    isValidEmail: .constant(false),
                    backgroundColor: AppColor.white)
            }
            .frame(width: 343, height: 241)
            .padding()
            .background(AppColor.backgroundLight)
            .cornerRadius(10)
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
                        borderColor: AppColor.celticBlue ,
            onTap: {
                viewModel.previousStep()
            })
            
            ButtonView( title: AppStrings.CreateRide.next.rawValue,
                        showShadow: false ,
            onTap: {
                viewModel.nextStep()
            }
            )
        }
        .padding()
    }
    
    var stepIndicator: some View {
        HStack(spacing: 32) {
            StepIndicator(icon: AppIcon.Home.createRide, title: "Details", isActive: true, isCurrentPage: false)
            StepIndicator(icon: AppIcon.CreateRide.route, title: "Route", isActive: true, isCurrentPage: true)
            StepIndicator(icon: AppIcon.Home.group, title: "Participants")
            StepIndicator(icon: AppIcon.CreateRide.review, title: "Review")
            StepIndicator(icon: AppIcon.CreateRide.share, title: "Share")
        }
    }
}


#Preview {
    RouteView(viewModel: CreateRideViewModel.init())
}
