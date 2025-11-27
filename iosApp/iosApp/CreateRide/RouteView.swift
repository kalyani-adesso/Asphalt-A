//
//  RouteView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import SwiftUI

struct RouteView: View {
    @EnvironmentObject var viewModel: CreateRideViewModel
    @State private var showLocationPicker = false
    @State private var searchText = ""
    @State private var isAssemblySameAsStart = false // checkbox
    @State private var isSelectingStart = true
    @State private var isSelectingAssembly = false
    @State private var isSelectingDestination = false
    
    
    var body: some View {
        VStack(spacing: 20) {
            stepIndicator
            
            VStack(alignment: .leading, spacing: 30) {
                
                LocationFieldView(
                    label: "Starting Point",
                    icon: AppIcon.CreateRide.route,
                    placeholder: "Select",
                    iconColor: AppColor.darkLime,
                    value: $viewModel.ride.startLocation,
                    backgroundColor: AppColor.white
                ) {
                    isSelectingStart = true
                    isSelectingAssembly = false
                    isSelectingDestination = false
                    showLocationPicker = true
                    
                }
                
                LocationFieldView(
                    label: "Destination",
                    icon: AppIcon.CreateRide.route,
                    placeholder: "Select",
                    iconColor: AppColor.darkRed,
                    value: $viewModel.ride.endLocation,
                    backgroundColor: AppColor.white
                ) {
                    isSelectingStart = false
                    isSelectingAssembly = false
                    isSelectingDestination = true
                    showLocationPicker = true
                }
                Button(action: {
                    isAssemblySameAsStart.toggle()
                    if !isAssemblySameAsStart {
                        viewModel.ride.assemblyPoint = viewModel.ride.assemblyPoint
                            viewModel.ride.assemblyLat = viewModel.ride.assemblyLat
                            viewModel.ride.assemblyLon = viewModel.ride.assemblyLon
                        } else {
                            viewModel.ride.assemblyPoint = ""
                            viewModel.ride.assemblyLat = 0.0
                            viewModel.ride.assemblyLon = 0.0
                        }
                }) {
                    HStack {
                        Image(systemName: isAssemblySameAsStart ? "checkmark.square.fill" : "square")
                            .foregroundColor(isAssemblySameAsStart ? AppColor.celticBlue : AppColor.celticBlue)
                        Text("Assembly point same as starting location")
                            .foregroundColor(AppColor.stoneGray)
                            .font(KlavikaFont.regular.font(size: 16))
                    }
                }
                .buttonStyle(PlainButtonStyle())
                .padding()
                
                if !isAssemblySameAsStart {
                    LocationFieldView(
                        label: "Assembly Point",
                        icon: AppIcon.CreateRide.route,
                        placeholder: "Select",
                        iconColor: AppColor.darkLime,
                        value: $viewModel.ride.assemblyPoint,
                        backgroundColor: AppColor.white
                    ) {
                        isSelectingStart = false
                        isSelectingAssembly = true
                        isSelectingDestination = false
                        showLocationPicker = true
                    }
                }
            }
            .sheet(isPresented: $showLocationPicker) {
                PlaceSearchView(
                    searchText: searchText,
                    isSelectingStart: isSelectingStart,
                    isSelectingDestination: isSelectingDestination,
                    isSelectingAssembly: isSelectingAssembly,
                    isAssemblySameAsStart : isAssemblySameAsStart,
                    dismiss: { showLocationPicker = false }
                )
            }
            .frame(width: 343)
            .padding()
            .background(AppColor.backgroundLight)
            .cornerRadius(10)
        }
        
        Spacer()
        HStack(spacing: 15) {
            ButtonView(
                title: AppStrings.CreateRideButton.previous.rawValue,
                background:AppColor.white,
                foregroundColor: AppColor.celticBlue,
                showShadow: false,
                borderColor: AppColor.celticBlue,
                onTap: {
                    viewModel.previousStep()
                }
            )
            
            ButtonView(
                title: AppStrings.CreateRideButton.next.rawValue,
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
            if viewModel.ride.type?.rawValue != "Solo Ride" {
                StepIndicator(icon: AppIcon.Home.group, title: "Participants")
            }
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
    RouteView()
        .environmentObject(CreateRideViewModel())
}
