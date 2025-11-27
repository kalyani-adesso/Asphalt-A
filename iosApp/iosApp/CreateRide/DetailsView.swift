//
//  DetailsView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import SwiftUI

struct DetailsView: View {
    
    @EnvironmentObject var viewModel: CreateRideViewModel
    @State private var showDatePicker = false
    @State private var showTimePicker = false
    @State private var activePicker: PickerType?
    enum PickerType: String, Identifiable {
        case startDate, endDate, startTime, endTime
        var id: String { rawValue }
    }
    
    
    var body: some View {
        VStack(spacing: 20) {
            stepIndicator
            ScrollView {
                VStack(alignment: .leading, spacing: 20) {
                    VStack(alignment: .leading, spacing: 10) {
                        Text(AppStrings.CreateRide.rideType)
                            .font(KlavikaFont.medium.font(size: 16))
                            .foregroundColor(AppColor.black)
                        
                        Menu {
                            ForEach(RideType.allCases) { type in
                                Button(action: {
                                    viewModel.ride.type = type
                                }) {
                                    Text(type.rawValue)
                                        .font(KlavikaFont.regular.font(size: 14))
                                        .foregroundColor(AppColor.richBlack)
                                }
                            }
                        } label: {
                            HStack {
                                Text(viewModel.ride.type?.rawValue ?? "Select ride type")
                                    .foregroundColor( viewModel.ride.type == nil ? Color(.placeholderText) : AppColor.richBlack)
                                    .font(KlavikaFont.regular.font(size: 16))
                                Spacer()
                                Image(systemName: "chevron.down")
                                    .resizable()
                                    .frame(width: 10, height: 6)
                                    .foregroundColor(AppColor.stoneGray)
                            }
                            .padding()
                            .background(Color.white)
                            .cornerRadius(10)
                        }
                    }
                    
                    Text(AppStrings.CreateRide.rideTitle).font(KlavikaFont.medium.font(size: 16))
                        .foregroundColor(AppColor.black)
                    TextField(AppStrings.CreateRide.rideTitleLabel, text: $viewModel.ride.title)
                        .font(KlavikaFont.regular.font(size: 16))
                        .foregroundColor(AppColor.richBlack)
                        .padding()
                        .background(Color.white)
                        .cornerRadius(10)
                    Text(AppStrings.CreateRide.description).font(KlavikaFont.medium.font(size: 16))
                        .foregroundColor(AppColor.black)
                    TextField(AppStrings.CreateRide.descriptionLabel, text: $viewModel.ride.description, axis: .vertical)
                        .font(KlavikaFont.regular.font(size: 16))
                        .foregroundColor(AppColor.richBlack)
                        .padding(.horizontal, 12)
                        .padding(.vertical, 8)
                        .frame(minHeight: 100, alignment: .topLeading)
                        .background(Color.white)
                        .cornerRadius(10)
                    
                    HStack {
                        VStack(alignment: .leading) {
                            Text(AppStrings.CreateRide.startDate).font(KlavikaFont.medium.font(size: 16))
                                .foregroundColor(AppColor.black)
                            Button {
                                activePicker = .startDate
                                showDatePicker.toggle()
                            } label: {
                                HStack{
                                    AppIcon.CreateRide.calendar_month
                                        .renderingMode(.template)
                                        .foregroundColor(viewModel.selectedStartDate != nil ? AppColor.richBlack : AppColor.stoneGray)
                                        .frame(width: 24, height: 24)
                                    Text(viewModel.selectedStartDate != nil ? DateFormatter.displayDate.string(from: viewModel.selectedStartDate!): "select")
                                        .font(KlavikaFont.regular.font(size: 14))
                                        .foregroundColor(viewModel.selectedStartDate != nil ? AppColor.richBlack : AppColor.stoneGray)
                                }
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding()
                                .background(Color.white)
                                .cornerRadius(10)
                            }
                        }
                        VStack(alignment: .leading) {
                            Text(AppStrings.CreateRide.startTime).font(KlavikaFont.medium.font(size: 16))
                                .foregroundColor(AppColor.black)
                            Button {
                                activePicker = .startTime
                                showTimePicker.toggle()
                            } label: {
                                HStack{
                                    AppIcon.CreateRide.timePicker
                                        .renderingMode(.template)
                                        .foregroundColor(viewModel.selectedStartTime != nil ? AppColor.richBlack : AppColor.stoneGray)
                                        .frame(width: 24, height: 24)
                                    Text(viewModel.selectedStartTime != nil ? DateFormatter.displayTime.string(from: viewModel.selectedStartTime!): "select")
                                        .font(KlavikaFont.regular.font(size: 14))
                                        .foregroundColor(viewModel.selectedStartTime != nil ? AppColor.richBlack : AppColor.stoneGray)
                                }
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding()
                                .background(Color.white)
                                .cornerRadius(10)
                            }
                        }
                    }
                    HStack {
                        VStack(alignment: .leading) {
                            Text(AppStrings.CreateRide.endDate).font(KlavikaFont.medium.font(size: 16))
                                .foregroundColor(AppColor.black)
                            Button {
                                activePicker = .endDate
                                showDatePicker.toggle()
                            } label: {
                                HStack{
                                    AppIcon.CreateRide.calendar_month
                                        .renderingMode(.template)
                                        .foregroundColor(viewModel.selectedEndDate != nil ? AppColor.richBlack : AppColor.stoneGray)
                                        .frame(width: 24, height: 24)
                                    Text(viewModel.selectedEndDate != nil ? DateFormatter.displayDate.string(from: viewModel.selectedEndDate!): "select")
                                        .font(KlavikaFont.regular.font(size: 14))
                                        .foregroundColor(viewModel.selectedEndDate != nil ? AppColor.richBlack : AppColor.stoneGray)
                                }
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding()
                                .background(Color.white)
                                .cornerRadius(10)
                            }
                        }
                        VStack(alignment: .leading) {
                            Text(AppStrings.CreateRide.endTime).font(KlavikaFont.medium.font(size: 16))
                                .foregroundColor(AppColor.black)
                            Button {
                                activePicker = .endTime
                                showTimePicker.toggle()
                            } label: {
                                HStack{
                                    AppIcon.CreateRide.timePicker
                                        .renderingMode(.template)
                                        .foregroundColor(viewModel.selectedEndTime != nil ? AppColor.richBlack : AppColor.stoneGray)
                                        .frame(width: 24, height: 24)
                                    Text(viewModel.selectedEndTime != nil ? DateFormatter.displayTime.string(from: viewModel.selectedEndTime!): "select")
                                        .font(KlavikaFont.regular.font(size: 14))
                                        .foregroundColor(viewModel.selectedEndTime != nil ? AppColor.richBlack : AppColor.stoneGray)
                                }
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding()
                                .background(Color.white)
                                .cornerRadius(10)
                            }
                        }
                    }
                }
                .frame(width: 343)
                .padding()
                .background(AppColor.backgroundLight)
                .cornerRadius(10)
                
            }
       
            ButtonView( title: AppStrings.CreateRideButton.nextStep.rawValue,
                        showShadow: false , onTap: {
                viewModel.nextStep()
            }
            )
            .disabled(!viewModel.isDetailsValid)
            .padding()
        }
        .sheet(item: $activePicker) { picker in
            switch picker {
                
            case .startDate:
                CustomDatePicker(
                    selectedDate: Binding(
                        get: { viewModel.selectedStartDate ?? Date() },
                        set: { viewModel.selectedStartDate = $0 }
                    ),
                    onDismiss: {
                        if viewModel.selectedStartDate == nil {
                            viewModel.selectedStartDate = Date()
                        }
                        activePicker = nil
                    }
                )
                
            case .endDate:
                CustomDatePicker(
                    selectedDate: Binding(
                        get: { viewModel.selectedEndDate ?? Date() },
                        set: { viewModel.selectedEndDate = $0 }
                    ),
                    onDismiss: {
                        if viewModel.selectedEndDate == nil {
                            viewModel.selectedEndDate = Date()
                        }
                        activePicker = nil
                    }
                )
                
            case .startTime:
                CustomTimePicker(
                    selectedTime: Binding(
                        get: { viewModel.selectedStartTime ?? Date() },
                        set: { viewModel.selectedStartTime = $0 }
                    ),
                    onDismiss: {
                        activePicker = nil
                    }
                )
                
            case .endTime:
                CustomTimePicker(
                    selectedTime: Binding(
                        get: { viewModel.selectedEndTime ?? Date() },
                        set: { viewModel.selectedEndTime = $0 }
                    ),
                    onDismiss: {
                        activePicker = nil
                    }
                )
            }
        }
    }
    
    private var stepIndicator: some View {
        HStack(spacing: 32) {
            StepIndicator(icon: AppIcon.Home.createRide, title: "Details", isActive: true, isCurrentPage: true)
            StepIndicator(icon: AppIcon.CreateRide.route, title: "Route")
            if viewModel.ride.type?.rawValue != "Solo Ride" {
                StepIndicator(icon: AppIcon.Home.group, title: "Participants")
            }
            StepIndicator(icon: AppIcon.CreateRide.review, title: "Review")
            StepIndicator(icon: AppIcon.CreateRide.share, title: "Share")
        }
    }
}

extension DateFormatter {
    static let displayDate: DateFormatter = {
        let df = DateFormatter()
        df.dateStyle = .medium
        return df
    }()
    
    static let displayTime: DateFormatter = {
        let df = DateFormatter()
        df.dateFormat = "hh:mm a"
        return df
    }()
}

#Preview {
    DetailsView()
        .environmentObject(CreateRideViewModel())
}
