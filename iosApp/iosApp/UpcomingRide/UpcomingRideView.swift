//
//  UpcomingRideView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 16/10/25.
//

import SwiftUI

struct UpcomingRideView: View {
    @StateObject private var viewModel = UpcomingRideViewModel()
    @State private var selectedStatus: String? = nil
    
    var body: some View {
        VStack {
            HStack(spacing: 12) {
                ForEach(viewModel.rideStatus, id: \.self) { status in
                    let isSelected = (selectedStatus ?? viewModel.rideStatus.first?.rawValue) == status.rawValue
                    SegmentButtonView(
                        rideStatus: status.rawValue,
                        isSelected: isSelected
                    ) {
                        selectedStatus = status.rawValue
                    }
                }
            }
            .frame(maxWidth: .infinity)
            .padding(16)
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.listGray)
            )
            .padding([.leading, .trailing])
            .contentShape(Rectangle())
            VStack {
                List {
                    ForEach($viewModel.rides.filter { $ride in
                        ride.rideAction.rawValue == selectedStatus
                    }, id: \.id) { $ride in
                        UpComingView(ride: $ride)
                            .listRowSeparator(.hidden)
                            .listRowInsets(EdgeInsets(top: 4, leading: 16, bottom: 4, trailing: 16))
                    }
                }
                .listStyle(.plain)
                .scrollContentBackground(.hidden)
            }
        }
        .navigationTitle(AppStrings.UpcomingRide.yourRide)
        .onAppear {
            selectedStatus = viewModel.rideStatus.first?.rawValue
        }
    }
}

struct SegmentButtonView: View {
    var rideStatus: String
    var isSelected: Bool = false
    var onTap: (() -> Void)? = nil
    var body: some View {
        Button(action: {
            onTap?()
        }) {
            Text(rideStatus)
                .frame(maxWidth: .infinity)
                .frame(height: 50)
                .background(
                    Group {
                        if isSelected {
                            LinearGradient(
                                gradient: Gradient(colors: [AppColor.royalBlue, AppColor.pursianBlue]),
                                startPoint: .leading,
                                endPoint: .trailing
                            )
                            .clipShape(RoundedRectangle(cornerRadius: 10))
                        } else {
                            RoundedRectangle(cornerRadius: 10)
                                .fill(AppColor.white)
                        }
                    }
                )
                .cornerRadius(10)
                .overlay(
                    RoundedRectangle(cornerRadius: 15)
                        .stroke(Color.gray.opacity(0.2), lineWidth: 1)
                )
                .shadow(color: isSelected ? Color.black.opacity(0.2) : .clear,
                        radius: isSelected ? 4 : 0,
                        x: 0, y: isSelected ? 2 : 0)
                .foregroundColor(isSelected ? AppColor.white : .black)
                .font(KlavikaFont.bold.font(size: 16))
        }
        .buttonStyle(.plain)
    }
}

struct UpComingView: View {
    @Binding var ride:RideModel
    var body: some View {
        VStack(alignment: .leading, spacing: 22) {
            HStack(spacing: 11) {
                (ride.rideAction == .invities ? AppIcon.Profile.profile : rideIcon)
                    .resizable()
                    .modifier(RoundCorner(rideAction: ride.rideAction))
                    .frame(width: 30, height: 30)
                VStack(alignment: .leading, spacing: 4) {
                    Text(ride.title)
                        .font(KlavikaFont.bold.font(size: 16))
                        .foregroundColor(AppColor.black)
                    Text("\(ride.routeStart) - \(ride.routeEnd)")
                        .font(KlavikaFont.regular.font(size: 12))
                        .foregroundColor(AppColor.stoneGray)
                }
                Spacer()
                if ride.rideAction == .invities {
                    Button(action: {
                        
                    }) {
                        AppIcon.UpcomingRide.message
                            .resizable()
                            .frame(width:30, height:30)
                    }
                    .buttonStyle(.plain)
                } else {
                    statusText
                }
            }
            
            HStack {
                HStack(spacing: 5) {
                    AppIcon.UpcomingRide.calender
                        .resizable()
                        .frame(width: 16, height: 16)
                    Text(ride.date)
                        .font(KlavikaFont.regular.font(size: 16))
                        .foregroundStyle(AppColor.richBlack)
                }
                Spacer()
                HStack(spacing: 5) {
                    AppIcon.UpcomingRide.group
                        .resizable()
                        .frame(width: 16, height: 16)
                    Text("\(ride.riderCount) rides")
                        .font(KlavikaFont.regular.font(size: 16))
                        .foregroundStyle(AppColor.richBlack)
                }
            }
            
            HStack(spacing: 15) {
                if ride.rideAction == .history {
                    Button(action: {
                        
                    }) {
                        Text(AppStrings.UpcomingRide.viewPhotos.uppercased())
                            .frame(maxWidth: .infinity)
                            .frame(height: 50)
                            .background(AppColor.white)
                            .foregroundStyle(AppColor.celticBlue)
                            .font(KlavikaFont.bold.font(size: 14))
                            .overlay(
                                RoundedRectangle(cornerRadius: 10)
                                    .stroke(AppColor.celticBlue, lineWidth: 1)
                            )
                    }
                    .padding(.bottom,20)
                    .buttonStyle(.plain)
                } else {
                    ButtonView(title: ride.rideAction == .invities ? AppStrings.UpcomingRide.accept.uppercased() : AppStrings.UpcomingRide.share.uppercased(),onTap: {
                        
                    })
                    .modifier(ButtonWidth(rideAction: ride.rideAction))
                }
                
                Button(action: {
                    
                }) {
                    Text(ride.rideViewAction.rawValue.uppercased())
                        .frame(maxWidth: .infinity)
                        .frame(height: 50)
                        .modifier(ButtonBackground(rideAction: ride.rideAction))
                }
                .padding(.bottom,20)
                .buttonStyle(.plain)
            }
        }
        .padding([.leading,.trailing,.top],16)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
        .contentShape(Rectangle())
    }
    
    var rideIcon:Image {
        if ride.status == .upcoming {
            AppIcon.UpcomingRide.upcomingRide
        } else if ride.status == .queue {
            AppIcon.UpcomingRide.queueRide
        } else {
            AppIcon.UpcomingRide.completRide
        }
    }
    
    var rideColor:Color {
        if ride.status == .upcoming {
            AppColor.purple
        } else if ride.status == .queue {
            AppColor.lightOrange
        } else {
            AppColor.green
        }
    }
    
    @ViewBuilder var statusText: some View {
        Text(ride.status.rawValue.uppercased())
            .frame(width: 84, height: 30)
            .font(KlavikaFont.bold.font(size: 12))
            .foregroundColor(AppColor.white)
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(rideColor)
            )
    }
}

struct RoundCorner: ViewModifier {
    let rideAction: RideAction
    func body(content: Content) -> some View {
        if rideAction == .invities {
            content
                .overlay(
                    RoundedRectangle(cornerRadius: 15)
                        .stroke(AppColor.celticBlue.opacity(0.8), lineWidth: 2)
                )
        } else {
            content
        }
    }
}

struct ButtonWidth: ViewModifier {
    let rideAction: RideAction
    func body(content: Content) -> some View {
        if rideAction == .invities {
            content
                .frame(maxWidth: .infinity)
        } else {
            content
                .frame(width: 120)
        }
    }
}

struct ButtonBackground: ViewModifier {
    let rideAction: RideAction
    func body(content: Content) -> some View {
        if rideAction == .invities {
            content
                .background(AppColor.red)
                .foregroundStyle(AppColor.white)
                .font(KlavikaFont.bold.font(size: 14))
                .cornerRadius(10)
        } else {
            content
                .background(AppColor.white)
                .foregroundStyle(AppColor.celticBlue)
                .font(KlavikaFont.bold.font(size: 14))
                .overlay(
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(AppColor.celticBlue, lineWidth: 1)
                )
        }
    }
}

#Preview {
    UpcomingRideView()
}
