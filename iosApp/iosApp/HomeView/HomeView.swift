//
//  HomeView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct ActiveChat {
    let id: String
    let name: String
    let chatType: MessagesViewModel.ChatType
    let memberList: [String]?
    let rideTitle: String?
}

struct HomeView: View {
    @StateObject var home =  HomeViewModel()
    @StateObject var viewModel  =  UpcomingRideViewModel()
    @StateObject var profileVM = ProfileViewModel()
    @StateObject var createRideVM = CreateRideViewModel()
    @State private var currentDate = Date()
    @State private var activeChat: ActiveChat? = nil
    @StateObject private var messagesVM = MessagesViewModel(currentUserId: MBUserDefaults.userIdStatic ?? "", recipientId: "", chatType: .private)
    
    var body: some View {
        ZStack(alignment: .top) {
            ScrollView {
                VStack(spacing: 15){
                    TopNavBar(viewModel: profileVM)
                    ActionButtonView(viewModel: createRideVM, upcomingRideViewModel: viewModel, homeViewModel: home)
                    DashboardView()
                    UpcomingRidesView { rideId in
                        
                        guard let ride = viewModel.upcomingInvitesRide.first(where: { $0.id == rideId }) else {
                            return
                        }
                        
                        let currentUserID = MBUserDefaults.userIdStatic ?? ""
                        print("my id from home :\(currentUserID)")
                        
                        var chatType: MessagesViewModel.ChatType
                        var chatName: String
                        var rideTitle: String? = nil
                        
                        if ride.createdBy == currentUserID {
                            //  GROUP CHAT
                            
                            chatType = .group
                            rideTitle = ride.title
                            chatName = ride.title
                            
                            Task {
                                let members = (ride.participants ?? []).map { $0.userId }
                                    var allMembers = members
                                    if !allMembers.contains(ride.createdBy) {
                                        allMembers.append(ride.createdBy)
                                    }
                                print("members: \(allMembers)")
                                
                                await MainActor.run {
                                    withAnimation(.easeInOut) {
                                        activeChat = ActiveChat(
                                            id: ride.id,
                                            name: chatName,
                                            chatType: chatType,
                                            memberList: allMembers,
                                            rideTitle: rideTitle
                                        )
                                    }
                                }
                            }
                            
                        } else {
                            
                            chatType = .private
                            rideTitle = ride.title
                            chatName = viewModel.usersById[ride.createdBy] ?? "Unknown"
                            let members = [currentUserID, ride.createdBy]
                            withAnimation(.easeInOut) {
                                activeChat = ActiveChat(
                                    id: ride.createdBy,
                                    name: chatName,
                                    chatType: chatType,
                                    memberList: members,
                                    rideTitle: rideTitle
                                )
                            }
                        }
                    }
                    
                    .environmentObject(home)
                    .environmentObject(viewModel)
                    JourneyCardView()
                    PlacesVisitedView()
                }
                .padding()
            }
            if  createRideVM.isRideLoading {
                ProgressViewReusable(title: "Loading ...")
            }
            if activeChat != nil {
                ChatOverlayView(activeChat: $activeChat)
            }
        }
        .task {
            
            viewModel.isRideLoading = true
            async let rides = viewModel.fetchAllUsers()
            async let allRides = viewModel.fetchAllRides()
            let month = Calendar.current.component(.month, from: currentDate)
            let year = Calendar.current.component(.year, from: currentDate)
            async let stats =  home.updateStatsFor(month: month, year: year)
            _ = await (rides, allRides, stats)
            viewModel.isRideLoading = false
        }
        .task {
            await profileVM.fetchProfile(userId: MBUserDefaults.userIdStatic ?? "")
        }
        .refreshable {
            await viewModel.fetchAllRides()
            await viewModel.fetchAllUsers()
        }
    }
}

