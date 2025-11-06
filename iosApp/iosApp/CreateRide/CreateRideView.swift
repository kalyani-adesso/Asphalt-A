//
//  CreateRideView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import SwiftUI

struct CreateRideView: View {
    @StateObject private var viewModel = CreateRideViewModel()
    var body: some View {
        NavigationStack {
            Header(viewModel: viewModel)
            Group {
                switch viewModel.currentStep {
                case 1:
                    DetailsView(viewModel: viewModel)
                case 2:
                    RouteView(viewModel: viewModel)
                case 3:
                    AnyView(
                        viewModel.ride.type?.rawValue == "Solo Ride"
                        ? AnyView(ReviewView(viewModel: viewModel))
                        : AnyView(ParticipantsView(viewModel: viewModel))
                    )
                case 4:
                    ReviewView(viewModel: viewModel)
                case 5:
                    ShareView(viewModel: viewModel)
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
