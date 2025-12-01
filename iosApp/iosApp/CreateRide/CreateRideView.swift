//
//  CreateRideView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import SwiftUI

struct CreateRideView: View {
    @EnvironmentObject var viewModel: CreateRideViewModel
    var body: some View {
        
        AppToolBar{
            ReusableHeader {
                Text(AppStrings.CreateRide.createRide)
                    .font(KlavikaFont.bold.font(size: 22))
                    .foregroundColor(AppColor.black)
            } trailing: {
                Text("Step \(viewModel.currentStep)/5")
                    .font(KlavikaFont.regular.font(size: 16))
                    .foregroundColor(AppColor.black)
            }
            Group {
                switch viewModel.currentStep {
                case 1:
                    DetailsView()
                case 2:
                    RouteView()
                case 3:
                    AnyView(
                        viewModel.ride.type?.rawValue == "Solo Ride"
                        ? AnyView(ReviewView())
                        : AnyView(ParticipantsView())
                    )
                case 4:
                    ReviewView()
                case 5:
                    ShareView()
                default:
                    Text("Coming Soon...")
                        .font(.title)
                }
            }
            .animation(.easeInOut, value: viewModel.currentStep)
        }
        .navigationBarBackButtonHidden(true)
        .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
    CreateRideView()
}
