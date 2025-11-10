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
        NavigationStack {
            Header()
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
        .toolbar(.hidden, for: .navigationBar)
    }
}

#Preview {
    CreateRideView()
}
