//
//  ConnectedRideMapView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 21/10/25.
//

import SwiftUI
import MapKit
import Combine

@available(iOS 17.0, *)
struct ConnectedRideMapView: View {
    @StateObject private var viewModel = ConnectedRideViewModel()
    @StateObject private var joinRideVM = JoinRideViewModel()
    @StateObject var locationManager = LocationManager()
    @State private var rideComplted: Bool = false
    @State private var startTrack: Bool = false
    @State private var showToast: Bool = true
    @State var showJoinRideView:Bool = false
    @State var showMapViewFullScreen:Bool = false
    @State var showMessagePopup:Bool = false
    @State private var position: MapCameraPosition = .automatic
    @State private var elapsedSeconds = 0
    @State private var selectedRiderName: String = ""
    @State private var selectedRiderDelayText: String = ""
    @State var timer:Timer?
    @State private var index: Int = 0
    var rideModel: JoinRideModel
    
    var body: some View {
        
        ZStack{
            if showMessagePopup{
                MessagePopupView(viewModel: viewModel, isPresented: $showMessagePopup)
                    .transition(.scale)
                    .zIndex(1)
            }
            AppToolBar{
                VStack {
                    HStack {
                        HStack {
                            VStack(alignment: .leading, spacing: 5) {
                                Text(AppStrings.ConnectedRide.connectedRide)
                                    .font(KlavikaFont.bold.font(size: 16))
                                    .foregroundColor(AppColor.black)
                                Text(rideModel.title)
                                    .font(KlavikaFont.regular.font(size: 12))
                                    .foregroundColor(AppColor.black)
                            }
                        }
                        Spacer()
                        HStack {
                            Button {
                            } label: {
                                HStack(alignment:.center, spacing: 6) {
                                    Rectangle()
                                        .fill(startTrack ? .spanishGreen : .grayishBlue)
                                        .frame(width: 9, height: 9)
                                    
                                    Text(startTrack ? TrackingStatus.Live.rawValue.uppercased() : TrackingStatus.Paused.rawValue.uppercased())
                                        .font(KlavikaFont.regular.font(size: 12))
                                        .foregroundStyle(startTrack ? .spanishGreen : .grayishBlue)
                                        .frame(height: 9)
                                }
                                .frame(width: 69, height: 30)
                                .background(
                                    RoundedRectangle(cornerRadius: 10)
                                        .fill(AppColor.white)
                                )
                                .overlay(
                                    RoundedRectangle(cornerRadius: 10)
                                        .stroke(startTrack ? .spanishGreen : .grayishBlue, lineWidth: 1)
                                )
                            }
                            Button {
                                print("Menu tapped")
                            } label: {
                                HStack {
                                    AppIcon.ConnectedRide.rideDuration
                                        .resizable()
                                        .frame(width:12,height: 12)
                                    Text(formatTime(elapsedSeconds))
                                        .font(KlavikaFont.regular.font(size: 12))
                                        .foregroundStyle(AppColor.celticBlue)
                                }
                                .frame(width: 83, height: 30)
                                .background(
                                    RoundedRectangle(cornerRadius: 10)
                                        .fill(AppColor.white)
                                )
                                .overlay(
                                    RoundedRectangle(cornerRadius: 10)
                                        .stroke(AppColor.celticBlue, lineWidth: 1)
                                )
                            }
                        }
                    }
                    .padding(EdgeInsets(top: 25, leading: 16, bottom: 20, trailing: 16))
                    List {
                        Section {
                            VStack {
                                ZStack(alignment: .topLeading) {
                                    BikeRouteMapView(position: $position, currentMapStyle:viewModel.currentMapStyle, rideModel: rideModel, groupRiders: viewModel.groupRiders, startTracking: $startTrack)
                                        .cornerRadius(12)
                                        .ignoresSafeArea(edges: .top)
                                    VStack {
                                        ZStack{
                                            HStack {
                                                mapActionButton()
                                            }
                                            if showToast {
                                                showToast(title: AppStrings.ConnectedRide.rideStarted)
                                            }
                                            if startTrack && viewModel.showPopup {
                                                ConnectedRideOfflineView(title: viewModel.popupTitle, image: AppIcon.ConnectedRide.warning)
                                                    .padding(.horizontal, 16)
                                            }
                                        }
                                        
                                        Spacer()
                                        HStack {
                                            distanceAndETA()
                                            Spacer()
                                            floatingButton()
                                        }
                                    }
                                }
                                .frame(height: showMapViewFullScreen ? 760 : 534)
                                Spacer()
                            }
                        }
                        .listRowSeparator(.hidden)
                        Section {
                            VStack(spacing: 18) {
                                ConnectedRideHeaderView(title: AppStrings.ConnectedRide.rideInProgressTitle, subtitle:AppStrings.ConnectedRide.groupNavigationActiveSubtitle, image: AppIcon.Profile.profile)
                                
                                ActiveRiderView(title:  MBUserDefaults.userNameStatic ?? "", speed: "\(Int(locationManager.speedInKph ?? 0.0)) kph", rideModel: rideModel, startTrack:$startTrack , locationManager: locationManager, viewModel: viewModel)
                                
                                Button(action: {
                                    self.rideComplted = true
                                    if rideModel.userId != MBUserDefaults.userIdStatic {
                                        joinRideVM.changeRideInviteStatus(rideId: rideModel.rideId, userId:  MBUserDefaults.userIdStatic ?? "", inviteStatus: 4)
                                    } else {
                                        joinRideVM.updateOrganizerStatus(rideId: rideModel.rideId, rideStatus: 4)
                                    }
                                    
                                    viewModel.endRide(rideId:rideModel.rideId)
                                    viewModel.endRideSummary(ride: rideModel , userID: MBUserDefaults.userIdStatic ?? "")
                                    viewModel.getRideCompleteDetails(duration: formatTime(elapsedSeconds), distance: rideModel.distance, riders: "\(viewModel.groupRiders.count + 1)")
                                    stopTimer()
                                }, label: {
                                    Text(AppStrings.ConnectedRide.endRideButton)
                                        .frame(maxWidth: .infinity,minHeight: 60)
                                        .font(KlavikaFont.bold.font(size: 18))
                                        .foregroundColor(AppColor.white)
                                        .background(
                                            RoundedRectangle(cornerRadius: 10)
                                                .fill(AppColor.red)
                                        )
                                })
                                .padding([.leading,.trailing,.bottom],16)
                                .buttonStyle(.plain)
                            }
                            .background(
                                RoundedRectangle(cornerRadius: 10)
                                    .fill(AppColor.listGray)
                            )
                        }
                        .listRowSeparator(.hidden)
                        .listRowBackground(Color.clear)
                        
                        if viewModel.groupRiders.count >= 1 {
                            Section {
                                VStack(spacing: 18) {
                                    ConnectedRideHeaderView(title: "\(AppStrings.ConnectedRide.groupStatusTitle) (\(viewModel.groupRiders.count))", subtitle: "", image: AppIcon.ConnectedRide.groupStatus)
                                    
                                    ForEach(viewModel.groupRiders.indices, id: \.self) { index in
                                        let rider = viewModel.groupRiders[index]
                                        GroupRiderView(title: rider.name, status: rider.status.rawValue, speed: "\(rider.speed) km", subTitle: rider.timeSinceUpdate, index: index, showMessagePopup: $showMessagePopup,onMessageTap: { val in
                                            viewModel.messageIndex = val
                                        })
                                    }
                                }
                                .padding(.bottom,16)
                                .background(
                                    RoundedRectangle(cornerRadius: 10)
                                        .fill(AppColor.listGray)
                                )
                            }
                            .listRowSeparator(.hidden)
                            .listRowBackground(Color.clear)
                        }
                        Section {
                            VStack(spacing: 18) {
                                ConnectedRideHeaderView(title: AppStrings.ConnectedRide.emergencyActionsTitle, subtitle: "", image: AppIcon.ConnectedRide.emergency)
                                HStack(spacing: 16) {
                                    Button(action: {
                                        
                                    }) {
                                        HStack(alignment: .center,spacing: 5) {
                                            AppIcon.ConnectedRide.sos
                                            Text(AppStrings.ConnectedRide.emergencySOSButton)
                                                .font(KlavikaFont.bold.font(size: 16))
                                                .foregroundStyle(AppColor.black)
                                        }
                                        .padding()
                                        .frame( height: 50)
                                        .background(
                                            RoundedRectangle(cornerRadius: 10)
                                                .fill(AppColor.white)
                                        )
                                        .overlay(
                                            RoundedRectangle(cornerRadius: 10)
                                                .stroke(AppColor.darkGray, lineWidth: 2)
                                        )
                                    }
                                    Button(action: {
                                        
                                    }) {
                                        HStack(alignment: .center,spacing: 5) {
                                            AppIcon.ConnectedRide.shareLocation
                                            Text(AppStrings.ConnectedRide.shareLocationButton)
                                                .font(KlavikaFont.bold.font(size: 16))
                                                .foregroundStyle(AppColor.black)
                                        }
                                        .padding()
                                        .frame( height: 50)
                                        .background(
                                            RoundedRectangle(cornerRadius: 10)
                                                .fill(AppColor.white)
                                        )
                                        .overlay(
                                            RoundedRectangle(cornerRadius: 10)
                                                .stroke(AppColor.darkGray, lineWidth: 2)
                                        )
                                    }
                                }
                                .padding(.bottom)
                            }
                            .background(
                                RoundedRectangle(cornerRadius: 10)
                                    .fill(AppColor.listGray)
                            )
                        }
                        .listRowSeparator(.hidden)
                        .listRowBackground(Color.clear)
                    }.listStyle(.plain)
                        .listRowSeparator(.hidden)
                        .navigationBarBackButtonHidden()
                        .navigationDestination(isPresented: $rideComplted, destination: {
                            ConnectedRideView(notificationTitle: "Ride sucessfully completed", title: "Completing ride", subTitle: "Saving your ride data and generating summary", model: rideModel, rideCompleteModel: viewModel.rideCompleteModel)
                        })
                    
                }
                .onAppear() {
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                        showToast = false
                    }
                    if !rideModel.rideJoined {
                        viewModel.joinRide(rideId: rideModel.rideId, userId: MBUserDefaults.userIdStatic ?? "", currentLat: locationManager.lastLocation?.coordinate.latitude ?? 0.0, currentLong: locationManager.lastLocation?.coordinate.longitude ?? 0.0, speed: locationManager.speedInKph ?? 0.0)
                    } else {
                        startOngoingRideTimer()
                    }
                    viewModel.onLocationUpdate(lat:locationManager.lastLocation?.coordinate.latitude ?? 0.0 , long: locationManager.lastLocation?.coordinate.longitude ?? 0.0, speed: locationManager.speedInKph ?? 0.0)
                    timer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { _ in
                        self.elapsedSeconds += 1
                    }
                    
                }
                .task {
                    await viewModel.receiveMessage(rideId: rideModel.rideId)
                }
                .onChange(of: viewModel.ongoingRideId) { ride in
                    if !ride.isEmpty {
                        startOngoingRideTimer()
                    }
                }
                .navigationDestination(isPresented: $showJoinRideView, destination: {
                    JoinRideView()
                })
            }
        }
    }
    
    func startOngoingRideTimer() {
        // Invalidate any existing timer
        viewModel.ongoingRideTimer?.invalidate()
        // Schedule the timer to trigger every 2 min minutes (900 seconds)
        viewModel.ongoingRideTimer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) {  _ in
            Task {
                await viewModel.reJoinRide(rideId: rideModel.rideId, userId: MBUserDefaults.userIdStatic ?? "", currentLat: locationManager.lastLocation?.coordinate.latitude ?? 0.0, currentLong: locationManager.lastLocation?.coordinate.longitude ?? 0.0, speed: locationManager.speedInKph ?? 0.0)
                
            }
        }
    }
    
    func formatTime(_ totalSeconds: Int) -> String {
        let hours = totalSeconds / 3600
        let minutes = (totalSeconds % 3600) / 60
        let seconds = totalSeconds % 60
        return String(format: "%02d:%02d:%02d", hours, minutes, seconds)
    }
    
    func stopTimer() {
        viewModel.ongoingRideTimer?.invalidate()
        viewModel.ongoingRideTimer = nil
        self.timer?.invalidate()
        self.timer = nil
    }
    
    @ViewBuilder func mapActionButton() -> some View {
        Button(action: {
            showMapViewFullScreen.toggle()
        }) {
            AppIcon.ConnectedRide.zoom
                .resizable()
                .frame(width: 50, height: 50)
                .padding([.top, .leading],15)
        }
        .buttonStyle(.plain)
        Spacer()
        Menu {
            Picker("", selection: $viewModel.selectedType) {
                ForEach(MapType.allCases, id: \.self) { type in
                    Text(type.rawValue)
                }
            }
        } label: {
            HStack(alignment: .center) {
                Text(viewModel.selectedType.rawValue)
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(AppColor.stoneGray)
            }
            .frame(width: 69, height: 24)
            .background(
                RoundedRectangle(cornerRadius: 5)
                    .fill(AppColor.white)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 5)
                    .stroke(AppColor.darkGray, lineWidth: 1)
            )
        }
        .padding(.trailing,16)
    }
    
    @ViewBuilder func showToast(title:String) -> some View {
        HStack(spacing: 10) {
            AppIcon.ConnectedRide.checkmark
                .padding(.leading,20)
            Text(title)
                .font(KlavikaFont.bold.font(size: 14))
                .foregroundStyle(.spanishGreen)
            Spacer()
        }
        .frame(maxWidth: .infinity)
        .frame(height: 60)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.lightGreen)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.aeroGreen, lineWidth: 2)
        )
        .padding([.leading, .trailing,.top],16)
        .animation(.easeInOut(duration: 0.3), value: false)
    }
    
    @ViewBuilder func distanceAndETA() -> some View {
        VStack(alignment: .center) {
            Text("\(Int(locationManager.speedInKph ?? 0.0))")
                .font(KlavikaFont.bold.font(size: 20))
                .foregroundStyle(AppColor.black)
            Text("kph")
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundStyle(AppColor.stoneGray)
        }
        .frame(width: 58,height: 50)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.white)
        )
        .padding()
    }
    
    @ViewBuilder func floatingButton() -> some View {
        HStack(spacing: 10) {
            ButtonView(title: "", icon: AppIcon.ConnectedRide.refresh ,onTap: {
                recenterMap()
            })
            ButtonView(title: "", icon: AppIcon.ConnectedRide.nearMe,onTap: {
                updateCameraFollow()
            })
        }
        .frame(width: 198)
        .padding(.trailing)
    }
    
    func recenterMap() {
        if let userLocation = locationManager.manager.location?.coordinate {
            withAnimation {
                position = .region(MKCoordinateRegion(
                    center: userLocation,
                    span: MKCoordinateSpan(latitudeDelta: 0.01, longitudeDelta: 0.01)
                ))
            }
        }
    }
    
    func updateCameraFollow() {
        guard let userLocation = locationManager.lastLocation?.coordinate else { return }
        withAnimation(.easeInOut(duration: 0.4)) {
            position = .camera(MapCamera(centerCoordinate: userLocation, distance: 300))
        }
    }
}

struct ConnectedRideHeaderView: View {
    let title: String
    let subtitle: String
    let image: Image
    
    var body: some View {
        HStack(spacing: 11) {
            image
                .resizable()
                .scaledToFill()
                .frame(width: 30, height: 30)
                .modifier(CustomCornerRadius(text: subtitle))
            
            VStack(alignment: .leading, spacing: 5) {
                Text(title)
                    .font(KlavikaFont.bold.font(size: 16))
                    .foregroundColor(AppColor.black)
                if !subtitle.isEmpty {
                    Text(subtitle)
                        .font(KlavikaFont.regular.font(size: 12))
                        .foregroundColor(AppColor.black)
                }
            }
            Spacer()
        }
        .padding([.top,.leading],16)
    }
}

struct ConnectedRideOfflineView: View {
    let title: String
    let image: Image
    
    var body: some View {
        HStack(spacing: 11) {
            image
                .resizable()
                .scaledToFill()
                .frame(width: 32, height: 32)
            
            VStack(alignment: .leading, spacing: 5) {
                Text(title)
                    .font(KlavikaFont.bold.font(size: 14))
                    .foregroundColor(AppColor.yellow)
            }
            Spacer()
        }
        .padding([.top,.leading,.bottom],12)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.pastelYellow)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.yellow, lineWidth: 2)
        )
    }
}

struct ActiveRiderView: View {
    let title: String
    let speed: String
    var rideModel:JoinRideModel
    @Binding var startTrack:Bool
    @ObservedObject var locationManager:LocationManager
    @ObservedObject var viewModel:ConnectedRideViewModel
    var body: some View {
        HStack {
            HStack(spacing: 16) {
                AppIcon.Profile.profile
                    .resizable()
                    .clipShape(Circle())
                    .frame(width: 37, height: 37)
                    .overlay(Circle().stroke(AppColor.green, lineWidth: 1.5))
                    .padding(.leading, 18)
                
                VStack(alignment: .leading, spacing: 5) {
                    Text(title)
                        .font(KlavikaFont.bold.font(size: 16))
                        .foregroundColor(AppColor.black)
                    HStack(spacing: 3) {
                        AppIcon.ConnectedRide.speed
                        Text(speed)
                            .font(KlavikaFont.regular.font(size: 12))
                            .foregroundColor(.stoneGray)
                    }
                }
            }
            Spacer()
            Button(action: {
                startTrack.toggle()
            }) {
                Text(startTrack ? AppStrings.ConnectedRide.stopTrackingButton.uppercased() : AppStrings.ConnectedRide.startTrackingButton.uppercased())
                    .font(KlavikaFont.bold.font(size: 12))
                    .foregroundColor(AppColor.white)
                    .frame(width: 120, height: 30)
                    .background(
                        RoundedRectangle(cornerRadius: 10)
                            .fill(!startTrack ? AppColor.green : AppColor.red)
                    )
            }
            .padding(.trailing, 16)
        }
        .frame(maxWidth: .infinity, minHeight: 60)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.white)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.darkGray, lineWidth: 2)
        )
        .padding([.leading,.trailing],16)
        .task {
            await viewModel.getOnGoingRides(rideId: rideModel.rideId)
        }
    }
}

struct GroupRiderView: View {
    let title: String
    let status:String
    let speed: String
    let subTitle:String
    let index: Int
    @Binding var showMessagePopup: Bool
    var onMessageTap: (Int) -> Void
    var body: some View {
        HStack {
            HStack(spacing: 16) {
                AppIcon.Profile.profile
                    .resizable()
                    .clipShape(Circle())
                    .frame(width: 37, height: 37)
                    .overlay(Circle().stroke(AppColor.green, lineWidth: 1.5))
                    .padding(.leading, 18)
                
                VStack(alignment: .leading, spacing: 5) {
                    HStack(spacing: 6) {
                        Text(title)
                            .font(KlavikaFont.bold.font(size: 16))
                            .foregroundColor(AppColor.black)
                        Text(status)
                            .padding(6)
                            .font(KlavikaFont.regular.font(size: 12))
                            .foregroundColor(statusTextColor)
                            .background(statusBgColor)
                            .frame(height: 16)
                            .cornerRadius(10)
                    }
                    HStack(spacing: 3) {
                        AppIcon.ConnectedRide.speed
                        Text(speed)
                            .font(KlavikaFont.regular.font(size: 12))
                            .foregroundColor(.stoneGray)
                        Text("• \(subTitle)")
                            .font(KlavikaFont.regular.font(size: 12))
                            .foregroundColor(.stoneGray)
                    }
                }
                Spacer()
                HStack {
                    Button(action: {
                        
                    }, label: {
                        AppIcon.ConnectedRide.call
                    })
                    .buttonStyle(.plain)
                    Button(action: {
                        showMessagePopup = true
                        onMessageTap(index)
                    }, label: {
                        AppIcon.Home.message
                    })
                    .buttonStyle(.plain)
                }
            }
            Spacer()
        }
        .frame(maxWidth: .infinity, minHeight: 60)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.white)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.darkGray, lineWidth: 2)
        )
        .padding([.leading,.trailing],16)
    }
    
    var statusBgColor:Color {
        if status == RiderStatus.connected.rawValue {
            return AppColor.lightGreen
        } else if status == RiderStatus.delayed.rawValue {
            return AppColor.pastelYellow
        } else if status == RiderStatus.stopped.rawValue {
            return AppColor.blushPink
        } else {
            return AppColor.pink
        }
    }
    
    var statusTextColor:Color {
        if status == RiderStatus.connected.rawValue {
            return AppColor.darkGreen
        } else if status == RiderStatus.delayed.rawValue {
            return AppColor.deepOrange
        } else if status == RiderStatus.stopped.rawValue {
            return AppColor.crimsonRed
        } else {
            return AppColor.pink
        }
    }
}

struct CustomCornerRadius: ViewModifier {
    let text:String
    func body(content: Content) -> some View {
        if text.isEmpty {
            content
        } else {
            content
                .clipShape(Circle())
                .overlay(Circle().stroke(AppColor.aeroGreen, lineWidth: 1.5))
        }
    }
}
