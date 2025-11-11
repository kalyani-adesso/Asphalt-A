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
                            Text(AppStrings.CreateRide.date).font(KlavikaFont.medium.font(size: 16))
                                .foregroundColor(AppColor.black)
                            Button {
                                showDatePicker.toggle()
                            } label: {
                                HStack{
                                    AppIcon.CreateRide.calendar_month
                                        .renderingMode(.template)
                                        .foregroundColor(viewModel.selectedDate != nil ? AppColor.richBlack : AppColor.stoneGray)
                                        .frame(width: 24, height: 24)
                                    Text(viewModel.selectedDate != nil ? DateFormatter.displayDate.string(from: viewModel.selectedDate!): "Pick date")
                                        .font(KlavikaFont.regular.font(size: 14))
                                        .foregroundColor(viewModel.selectedDate != nil ? AppColor.richBlack : AppColor.stoneGray)
                                }
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(Color.white)
                                .cornerRadius(10)
                            }
                        }
                        VStack(alignment: .leading) {
                            Text(AppStrings.CreateRide.time).font(KlavikaFont.medium.font(size: 16))
                                .foregroundColor(AppColor.black)
                            Button {
                                showTimePicker.toggle()
                            } label: {
                                HStack{
                                    AppIcon.CreateRide.timePicker
                                        .renderingMode(.template)
                                        .foregroundColor(viewModel.selectedTime != nil ? AppColor.richBlack : AppColor.stoneGray)
                                        .frame(width: 24, height: 24)
                                    Text(viewModel.selectedTime != nil ? DateFormatter.displayTime.string(from: viewModel.selectedTime!): "Pick time")
                                        .font(KlavikaFont.regular.font(size: 14))
                                        .foregroundColor(viewModel.selectedTime != nil ? AppColor.richBlack : AppColor.stoneGray)
                                }
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(Color.white)
                                .cornerRadius(10)
                            }
                        }
                    }
                }
                .frame(width: 343, height: 440)
                .padding()
                .background(AppColor.backgroundLight)
                .cornerRadius(10)
                
            }
            Spacer()
            ButtonView( title: AppStrings.CreateRideButton.nextStep.rawValue,
                        showShadow: false , onTap: {
                viewModel.nextStep()
            }
            )
            .disabled(!viewModel.isDetailsValid)
            .padding()
        }
        
        .sheet(isPresented: $showDatePicker) {
            CustomDatePicker(selectedDate: Binding(
                get: { viewModel.selectedDate ?? Date() },
                set: { viewModel.selectedDate = $0 }), onDismiss:
                                {
                if viewModel.selectedDate == nil {
                    viewModel.selectedDate = Date()
                }
                showDatePicker = false
            })
        }
        .sheet(isPresented: $showTimePicker) {
            CustomTimePicker(selectedTime: Binding(
                get: { viewModel.selectedTime ?? Date() },
                set: { viewModel.selectedTime = $0 }), onDismiss:
                                {
                showTimePicker = false
            })
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
}
