//
//  RouteView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import SwiftUI

struct RouteView: View {
    @ObservedObject var viewModel: CreateRideViewModel
    @State private var showLocationPicker = false
    @State private var searchText = ""
    @State private var isSelectingStart = true

    var body: some View {
        VStack(spacing: 20) {
            stepIndicator
            
            VStack(alignment: .leading, spacing: 20) {
                
                LocationFieldView(
                    label: "Starting Point",
                    icon: AppIcon.CreateRide.route,
                    placeholder: "Enter starting location",
                    iconColor: AppColor.darkLime,
                    value: $viewModel.ride.startLocation,
                    backgroundColor: AppColor.white
                ) {
                    isSelectingStart = true
                    showLocationPicker = true
                }
    
                LocationFieldView(
                    label: "Destination",
                    icon: AppIcon.CreateRide.route,
                    placeholder: "Enter destination",
                    iconColor: AppColor.darkRed,
                    value: $viewModel.ride.endLocation,
                    backgroundColor: AppColor.white
                ) {
                    isSelectingStart = false
                    searchText = ""
                    showLocationPicker = true
                }
            }
            .sheet(isPresented: $showLocationPicker) {
                PlaceSearchView(
                    viewModel: viewModel,
                    searchText: searchText,
                    isSelectingStart: isSelectingStart,
                    dismiss: { showLocationPicker = false }
                )
            }
            .frame(width: 343, height: 241)
            .padding()
            .background(AppColor.backgroundLight)
            .cornerRadius(10)
        }
        
        Spacer()
        HStack(spacing: 15) {
            ButtonView(
                title: AppStrings.CreateRide.previous.rawValue,
                background: LinearGradient(
                    gradient: Gradient(colors: [.white, .white]),
                    startPoint: .leading,
                    endPoint: .trailing
                ),
                foregroundColor: AppColor.celticBlue,
                showShadow: false,
                borderColor: AppColor.celticBlue,
                onTap: {
                    viewModel.previousStep()
                }
            )
            
            ButtonView(
                title: AppStrings.CreateRide.next.rawValue,
                showShadow: false,
                onTap: {
                    viewModel.getDistance()
                    viewModel.nextStep()
                }
            )
           .disabled(!viewModel.isRouteValid)
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

struct LocationFieldView: View {
    let label: String
    let icon: Image?
    let placeholder: String
    var iconColor: Color? = nil
    
    @Binding var value: String
    
    var backgroundColor: Color = AppColor.backgroundLight
    var onTap: (() -> Void)? = nil

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text(label)
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundStyle(AppColor.black)
            
            HStack {
                if let icon = icon {
                    icon
                        .renderingMode(iconColor == nil ? .original : .template)
                        .foregroundColor(iconColor ?? .primary)
                        .frame(width: 20, height: 20)
                }
                
                ZStack {
                    TextField(placeholder, text: $value)
                        .font(KlavikaFont.regular.font(size: 16))
                        .foregroundStyle(AppColor.richBlack)
                        .disabled(true)
                        
                    Rectangle()
                        .foregroundColor(.clear)
                        .contentShape(Rectangle())
                        .onTapGesture {
                            onTap?()
                        }
                        .frame(height:20)
                }
            }
            .padding()
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(backgroundColor)
            )
            .cornerRadius(10)
        }
    }
}

#Preview {
    RouteView(viewModel: CreateRideViewModel.init())
}
