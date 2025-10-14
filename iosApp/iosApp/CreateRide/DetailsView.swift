//
//  DetailsView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import SwiftUI

struct DetailsView: View {
    
    @ObservedObject var viewModel: CreateRideViewModel
    @State private var showDatePicker = false
    @State private var showTimePicker = false
    @State private var isPresented: Bool = false
    @State private var selectedTime : Date? = nil
    @State private var selectedDate : Date? = nil
    
    var body: some View {
        VStack(spacing: 20) {
            stepIndicator
            ScrollView {
                VStack(alignment: .leading, spacing: 20) {
                    
                    VStack(alignment: .leading, spacing: 10) {
                        Text("Ride Type")
                            .font(KlavikaFont.medium.font(size: 16))
                            .foregroundColor(AppColor.black)

                        Menu {
                            ForEach(RideType.allCases) { type in
                                Button(action: {
                                    viewModel.ride.type = type
                                }) {
                                    Text(type.rawValue)
                                        .font(KlavikaFont.regular.font(size: 14))
                                        .foregroundColor(AppColor.stoneGray)
                                }
                            }
                        } label: {
                            HStack {
                                Text(viewModel.ride.type.rawValue.isEmpty ? "Select ride type" : viewModel.ride.type.rawValue)
                                    .foregroundColor(viewModel.ride.type.rawValue.isEmpty ? AppColor.stoneGray : AppColor.stoneGray)
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


                    Text("Ride Title").font(KlavikaFont.medium.font(size: 16))
                        .foregroundColor(AppColor.black)
                    TextField("Enter ride name...", text: $viewModel.ride.title)
                        .font(KlavikaFont.regular.font(size: 16))
                        .foregroundColor(AppColor.stoneGray)
                        .padding()
                        .background(Color.white)
                        .cornerRadius(10)

                    Text("Description").font(KlavikaFont.medium.font(size: 16))
                        .foregroundColor(AppColor.black)
                    TextField("Describe the vibe...", text: $viewModel.ride.description, axis: .vertical)
                        .font(KlavikaFont.regular.font(size: 16))
                        .foregroundColor(AppColor.stoneGray)
                        .padding()
                        .padding(.bottom,50)
                        .frame(height: 100)
                        .background(Color.white)
                        .cornerRadius(10)

                    HStack {
                        VStack(alignment: .leading) {
                            Text("Date").font(KlavikaFont.medium.font(size: 16))
                                .foregroundColor(AppColor.black)
                            Button {
                                showDatePicker.toggle()
                            } label: {
                                HStack{
                                    AppIcon.CreateRide.calendar_month
                                        .renderingMode(.template)
                                        .foregroundColor(AppColor.stoneGray)
                                        .frame(width: 24, height: 24)
                                    Text(selectedDate != nil ? DateFormatter.displayDate.string(from: selectedDate!): "Pick date")
                                        .font(KlavikaFont.medium.font(size: 14))
                                            .foregroundColor(AppColor.stoneGray)
                                }
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(Color.white)
                                .cornerRadius(10)
                            }
                        }
                        VStack(alignment: .leading) {
                            Text("Time").font(KlavikaFont.medium.font(size: 16))
                                .foregroundColor(AppColor.black)
                            Button {
                                showTimePicker.toggle()
                            } label: {
                                HStack{
                                    AppIcon.CreateRide.timePicker
                                        .renderingMode(.template)
                                        .foregroundColor(AppColor.stoneGray)
                                        .frame(width: 24, height: 24)
                                    Text(selectedTime != nil ? DateFormatter.displayTime.string(from: selectedTime!): "Pick time")
                                        .font(KlavikaFont.medium.font(size: 14))
                                            .foregroundColor(AppColor.stoneGray)
                                }
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(Color.white)
                                .cornerRadius(10)
                            }
                        }
                    }
                }
                .frame(width: 343, height: 452)
                .padding()
                .background(AppColor.backgroundLight)
                .cornerRadius(10)
                
            }
            Spacer()
            ButtonView( title: AppStrings.CreateRide.nextStep.rawValue,
                        showShadow: false , onTap: {
                if viewModel.isStep1Valid() {
                    viewModel.nextStep()
                }
                    isPresented = true
                }
            ).navigationDestination(isPresented: $isPresented, destination: {
                RouteView(viewModel: viewModel)
            })
            .padding()
        }
        
        .sheet(isPresented: $showDatePicker) {
        CustomDatePicker(selectedDate: Binding(
            get: { selectedDate ?? Date() },
            set: { selectedDate = $0 }), onDismiss:
                            {
            if selectedDate == nil {
                selectedDate = Date()
            }
                showDatePicker = false
            })
        }
        .sheet(isPresented: $showTimePicker) {
            CustomTimePicker(selectedTime: Binding(
                get: { selectedTime ?? Date() },
                set: { selectedTime = $0 }), onDismiss:
                                {
                if selectedTime == nil {
                    selectedTime = Date()
                }
                showTimePicker = false
                })
        }
    }


    private var stepIndicator: some View {
        HStack(spacing: 32) {
            StepIndicator(icon: AppIcon.Home.createRide, title: "Details", isActive: true, isCurrentPage: true)
            StepIndicator(icon: AppIcon.CreateRide.route, title: "Route")
            StepIndicator(icon: AppIcon.Home.group, title: "Participants")
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
    DetailsView(viewModel: CreateRideViewModel.init())
}
