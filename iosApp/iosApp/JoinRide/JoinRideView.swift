//
//  JoinRideView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 17/10/25.
//

import Foundation
import SwiftUI

struct JoinRideView: View {
    @State private var searchQuery: String = ""
    @StateObject private var viewModel = JoinRideViewModel()
    @State private var showConnectedRides: Bool = false
    @State private var showHomeView:Bool = false
    @StateObject private var homeViewModel = HomeViewModel()
    @State private var selectedRide: JoinRideModel? = nil
    @Environment(\.dismiss) private var dismiss
    var body: some View {
        AppToolBar{
            ZStack {
                NavigationStack {
                    if #available(iOS 17.0, *) {
                        VStack {
                            searchBarView
                                .font(KlavikaFont.regular.font(size: 14))
                                .padding(.top)
                            if viewModel.filteredRides.isEmpty {
                                VStack(spacing: 12) {
                                    Text(AppStrings.JoinRide.noActiveRidesFound)
                                        .font(KlavikaFont.bold.font(size: 18))
                                        .foregroundColor(.gray)
                                }
                                .frame(maxWidth: .infinity, minHeight: 400)
                            } else {
                                List(viewModel.filteredRides.indices, id: \.self) { index in
                                    let ride = viewModel.filteredRides[index]
                                    
                                    JoinRideRow(
                                        ride: ride,
                                        index: index,
                                        viewModel: viewModel, selectedRide: $selectedRide
                                    )
                                    .listRowSeparator(.hidden)
                                }
                                .listStyle(.plain)
                            }
                            Spacer()
                        }
                        .navigationBarBackButtonHidden(true)
                        .navigationDestination(isPresented: $showHomeView, destination: {
                            BottomNavBar()
                        })
                        .navigationDestination(item: $selectedRide, destination: { ride in
                            ConnectedRideView(notificationTitle: AppStrings.JoinRide.rideActive, title: AppStrings.ConnectedRide.startRideTitle, subTitle: AppStrings.ConnectedRide.startRideSubtitle, model: ride, rideCompleteModel: [])
                        })
                        .task {
                            await viewModel.getRides()
                        }
                        .refreshable {
                            Task {
                                await viewModel.getRides()
                            }
                        }
                    } else {
                        // Fallback on earlier versions
                    }
                }
                if viewModel.isRideLoading {
                    Color.black.opacity(0.5)
                        .ignoresSafeArea()
                    ProgressView(AppStrings.JoinRide.loading)
                        .progressViewStyle(CircularProgressViewStyle())
                        .padding(.top, 100)
                        .foregroundColor(.white)
                }
            }
        }
        
    }
    @ViewBuilder var searchBarView: some View {
        HStack(spacing: 6) {
            AppIcon.JoinRide.search
                .resizable()
                .frame(width: 20, height: 20)
                .background(AppColor.whiteGray)
                .padding(.leading)
            TextField(AppStrings.JoinRide.searcRide, text: $viewModel.searchQuery)
                .font(KlavikaFont.regular.font(size: 14))
                .foregroundColor(.stoneGray)
                .background(.clear)
                .background(AppColor.whiteGray)
        }
        .frame(maxWidth: .infinity)
        .frame(height: 50)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
        .padding([.leading, .trailing],16)
    }
}

struct JoinRideRow: View {
    var ride:JoinRideModel?
    let index:Int
    @ObservedObject var viewModel:JoinRideViewModel
    @Binding var selectedRide: JoinRideModel?
    var body: some View {
            VStack(alignment: .leading, spacing: 22) {
                HStack(spacing: 11) {
                    AppIcon.Profile.profile
                        .resizable()
                        .overlay(
                            RoundedRectangle(cornerRadius: 15)
                                .stroke(AppColor.celticBlue.opacity(0.8), lineWidth: 2)
                        )
                        .frame(width: 30, height: 30)
                    VStack(alignment: .leading, spacing: 4) {
                        Text(ride?.title ?? "")
                            .font(KlavikaFont.bold.font(size: 16))
                            .foregroundColor(AppColor.black)
                        Text("By \(ride?.organizer ?? "")")
                            .font(KlavikaFont.regular.font(size: 12))
                            .foregroundColor(AppColor.richBlack)
                    }
                    Spacer()
                }
                
                Text(ride?.description ?? "")
                    .font(KlavikaFont.regular.font(size: 14))
                    .foregroundColor(AppColor.stoneGray)
                
                HStack {
                    HStack(spacing: 5) {
                        AppIcon.JoinRide.calenderToday
                            .resizable()
                            .frame(width: 16, height: 16)
                        Text(ride?.date ?? "")
                            .font(KlavikaFont.regular.font(size: 16))
                            .foregroundStyle(AppColor.richBlack)
                    }
                    Spacer()
                    HStack(spacing: 5) {
                        AppIcon.JoinRide.joinGroup
                            .resizable()
                            .frame(width: 16, height: 16)
                        Text("\(ride?.ridersCount ?? "")/\(ride?.maxRiders ?? "") rides")
                            .font(KlavikaFont.regular.font(size: 16))
                            .foregroundStyle(AppColor.richBlack)
                    }
                }
                
                HStack {
                    HStack(spacing: 5) {
                        AppIcon.JoinRide.location
                            .resizable()
                            .frame(width: 16, height: 16)
                        Text(ride?.route ?? "")
                            .font(KlavikaFont.regular.font(size: 16))
                            .foregroundStyle(AppColor.richBlack)
                    }
                    Spacer()
                    HStack(spacing: 5) {
                        AppIcon.JoinRide.route
                            .resizable()
                            .frame(width: 16, height: 16)
                        Text(ride?.distance ?? "")
                            .font(KlavikaFont.regular.font(size: 16))
                            .foregroundStyle(AppColor.richBlack)
                    }
                }
                
                HStack(spacing: 15) {
                    Button(action: {
                        viewModel.callToRider(contactNumber: ride?.contactNumber ?? "")
                    }) {
                        HStack {
                            AppIcon.NavigationSlider.call
                                .resizable()
                                .frame(width: 20, height: 20)
                            Text(AppStrings.JoinRide.callRider.uppercased())
                                .font(KlavikaFont.bold.font(size: 14))
                                .foregroundStyle(AppColor.black)
                        }
                        .frame(maxWidth: .infinity, minHeight: 50)
                        .background(
                            RoundedRectangle(cornerRadius: 10)
                                .fill(AppColor.white)
                        )
                        .overlay(
                            RoundedRectangle(cornerRadius: 10)
                                .stroke(AppColor.darkGray, lineWidth: 2)
                        )
                    }
                    .padding(.bottom,20)
                    .buttonStyle(.plain)
                    
                    if #available(iOS 17.0, *) {
                        ButtonView(title: (ride?.rideJoined ?? false) ? AppStrings.JoinRide.reJoinRideTitle.uppercased() : AppStrings.JoinRide.joinRide.uppercased(),icon: AppIcon.JoinRide.movedLocation, background: (ride?.rideJoined ?? false) ? AppColor.vividGreen
                                   :  AppColor.celticBlue ,onTap: {
                            viewModel.tappedIndex = index
                            Task {
                                if let ride = ride,
                                  let selected = await viewModel.handleJoin(for: ride) {
                                        selectedRide = selected
                                    }
                            }
                        })
                        .frame(maxWidth: .infinity)
                        .padding(.bottom,20)
                    }
                }
            }
            .padding([.leading,.trailing,.top],16)
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.listGray)
            )
            .contentShape(Rectangle())
            .alert(AppStrings.JoinRide.rideActive, isPresented: $viewModel.showRideAlreadyActivePopup) {
                Button(AppStrings.JoinRide.no, role: .cancel) { }
                Button(AppStrings.JoinRide.yes) {
                    Task {
                        await viewModel.endActiveRide()
                        await viewModel.joinRide(viewModel.filteredRides[viewModel.tappedIndex ?? 0])
                        selectedRide = viewModel.filteredRides[viewModel.tappedIndex ?? 0]
                    }
                }
            } message: {
                Text(AppStrings.JoinRide.confirmEndCurrentRide)
            }
    }
}

#Preview {
    JoinRideView()
}


