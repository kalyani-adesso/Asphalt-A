//
//  UpcomingRideView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 16/10/25.
//

import SwiftUI

struct UpcomingRideView: View {
    @EnvironmentObject var viewModel : UpcomingRideViewModel
    var currentSelected: RideAction { viewModel.selectedTab }
    var startingTab: RideAction = .upcoming 
    @State private var showHomeView:Bool = false
    @EnvironmentObject var homeViewModel : HomeViewModel
    @State var showHome: Bool = false
    @State var showpopup: Bool = false
    var onBackToHome: (() -> Void)? = nil
    var hasPendingInvites: Bool {
        viewModel.rides.contains { $0.rideAction == .invities }
    }
    @State private var activePopup: RidePopupType? = nil
    @State private var openGallery = false
    @State private var selectedImages: [UIImage] = []
    @State private var selectedRideId: String? = nil
    
    var body: some View {
        
        
        ZStack{
            NavigationStack {
                SimpleCustomNavBar(title: "Your Rides", onBackToHome: {
                    onBackToHome?()
                    showHome = true
                } )
                
                VStack {
                    HStack(spacing: 12) {
                        let rideStatuses = viewModel.rideStatus
                        
                        ForEach(rideStatuses, id: \.self) { status in
                            let isSelected = viewModel.selectedTab == status
                            let showDot = status == .invities && hasPendingInvites
                            SegmentButtonView(
                                rideStatus: status.rawValue,
                                isSelected: isSelected,
                                showNotificationDot: showDot
                            ) {
                                withAnimation {
                                    viewModel.selectedTab = status
                                }
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
                            ForEach($viewModel.rides.indices.filter { index in
                                viewModel.rides[index].rideAction.rawValue == viewModel.selectedTab.rawValue
                            }, id: \.self) { index in
                                UpComingView(viewModel: viewModel, ride: $viewModel.rides[index]){ rideId in
                                    selectedRideId = rideId  // open upload flow for this ride
                                    selectedImages = []  // clear previously selected images
                                    withAnimation(.easeInOut) { activePopup = .uploadOptions } 
                                }
                                .listRowSeparator(.hidden)
                                .listRowInsets(EdgeInsets(top: 4, leading: 16, bottom: 4, trailing: 16))
                            }
                        }
                        .listStyle(.plain)
                        .scrollContentBackground(.hidden)
                    }
                }
                .onAppear {
                    viewModel.selectedTab = .upcoming
                    if showpopup {
                        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                            withAnimation(.easeInOut) {
                                showpopup = false
                            }
                        }
                    }
                }
            }
            .navigationBarBackButtonHidden(true)
            .navigationDestination(isPresented: $showHome, destination: {
                BottomNavBar()
            })
            .onAppear {
                viewModel.selectedTab = startingTab
            }
            
            // Popup overlay (always stays above)
            if showpopup {
                // Dimmed background
                AppColor.backgroundLight.opacity(0.7)
                    .ignoresSafeArea()
                    .zIndex(1)
                    .onTapGesture {
                        withAnimation {
                            showpopup = false
                        }
                    }
                
                // Popup content
                VStack {
                    Snackbar(
                        message: "Ride Created Successfully",
                        subMessage: "Your ride has been created and is now live for other riders to join."
                    )
                    
                    Spacer()
                }
                .frame(width: 390, height: 620)
                .transition(.scale)
                .zIndex(2)
            }
            
            if viewModel.isRideLoading {
                ProgressViewReusable(title: "Loading Rides...")
            }
        }
        .overlay {
            if activePopup != nil {
                RidePopupView(
                    activePopup: $activePopup,
                    openGallery: $openGallery,
                    selectedImages: $selectedImages,
                    onUpload: {
                        handleUpload()
                    }
                )
                .transition(.opacity.combined(with: .scale))
                .zIndex(10)
            }
        }
        .sheet(isPresented: $openGallery, onDismiss: {
            if !selectedImages.isEmpty {
                withAnimation { activePopup = .previewSelected }
            }
        }) {
            //TODO: Check photo picker file is missing.
//            PhotoPicker(images: $selectedImages)
        }
        .zIndex(showpopup ? 2 : 0)
        .task{
            await viewModel.fetchAllRides()
            await viewModel.fetchAllUsers()
        }
        .refreshable {
            await viewModel.fetchAllRides()
            await viewModel.fetchAllUsers()
        }
    }
    
    
    private func handleUpload() {
        guard let rideId = selectedRideId else { return }
        
        if let index = viewModel.rides.firstIndex(where: { $0.id == rideId }) {
            var ride = viewModel.rides[index]
            ride.hasPhotos = true
            viewModel.rides[index] = ride
        }
        
        withAnimation {
            selectedRideId = nil
        }
    }
}

struct SegmentButtonView: View {
    var rideStatus: String
    var isSelected: Bool = false
    var showNotificationDot: Bool = false
    var onTap: (() -> Void)? = nil
    var body: some View {
        ZStack(alignment: .topTrailing){
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
            if showNotificationDot {
                ZStack {
                    Circle()
                        .fill(Color.white)
                        .frame(width: 19, height: 19)
                        .shadow(color: Color.black.opacity(0.2), radius: 3, x: 0, y: 1)
                    
                    // Inner blue circle
                    Circle()
                        .fill(AppColor.celticBlue)
                        .frame(width: 12, height: 12)
                }
                .offset(x: 8, y: -7)
            }
            
        }
    }
}

struct UpComingView: View {
    @ObservedObject var viewModel: UpcomingRideViewModel
    @Binding var ride:RideModel
    @State private var showUploadPopup = false
    @State private var showSelectedPopup = false
    @State private var selectedImages: [UIImage] = []
    @State private var openGallery = false
    var onAddPhotos: ((String) -> Void)? = nil
    
    var body: some View {
        ZStack{
            VStack(alignment: .leading, spacing: 22) {
                HStack(spacing: 11) {
                    (ride.rideAction == .invities ? AppIcon.Profile.profile : rideIcon)
                        .resizable()
                        .modifier(RoundCorner(rideAction: ride.rideAction))
                        .frame(width: 30, height: 30)
                    VStack(alignment: .leading, spacing: 4) {
                        Text(ride.rideAction == .invities ? "Invite from \(viewModel.usersById[ride.createdBy] ?? "Unknown")" : ride.title)
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
                            if ride.hasPhotos {
                                //open local manager
                            } else {
                                onAddPhotos?(ride.id)
                            }
                        }) {
                            Text(ride.hasPhotos ? AppStrings.UpcomingRide.viewPhotos.uppercased() : AppStrings.UpcomingRide.addPhotos.uppercased())
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
                        ButtonView(title: ride.rideAction == .invities ? AppStrings.UpcomingRide.accept.uppercased() : AppStrings.UpcomingRide.share.uppercased(), fontSize: 14,onTap: {
                            Task {
                                await viewModel.changeRideInviteStatus(rideId: ride.id, accepted: true)
                            }
                        })
                        .modifier(ButtonWidth(rideAction: ride.rideAction))
                        .padding(.bottom,20)
                    }
                    
                    Button(action: {
                        Task {
                            await viewModel.changeRideInviteStatus(rideId: ride.id, accepted: false)
                        }
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
        .environmentObject(HomeViewModel())
        .environmentObject(UpcomingRideViewModel())
}
