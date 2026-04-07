//
//  ConnectedRideMapView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 21/10/25.
//

import SwiftUI
import MapKit
import Combine
import CoreLocation

@available(iOS 17.0, *)
struct ConnectedRideMapView: View {
    @StateObject private var viewModel = ConnectedRideViewModel()
    @StateObject var locationManager = LocationManager()
    @StateObject private var sessionController = ConnectedRideSessionController()
    @State private var rideComplted: Bool = false
    @State private var startTrack: Bool = false
    @State private var showToast: Bool = true
    @State var showJoinRideView:Bool = false
    @State var showMapViewFullScreen:Bool = false
    @State var showMessagePopup:Bool = false
    @State var showMessageNotification: Bool = false
    @State private var position: MapCameraPosition = .automatic
    /// Saved when route is first fitted; used by refresh to restore original map view.
    @State private var initialMapRegion: MKCoordinateRegion?
    @State private var elapsedSeconds = 0
    /// Odometer: sum of GPS segment distances while on the connected ride screen (travelled distance).
    @State private var travelledDistanceMeters: Double = 0
    @State private var lastOdometerLocation: CLLocation?
    @State private var selectedRiderName: String = ""
    @State private var selectedRiderDelayText: String = ""
    @State private var index: Int = 0
    @State private var showNavigationOptions: Bool = false
    @State private var showInAppNavigation: Bool = false
    @State private var endRideErrorMessage: String?
    @State private var isEndingRide: Bool = false
    /// While `true`, Firebase message + ongoing-rider streams run; set `false` when ride ends.
    @State private var rideSessionActive: Bool = true
    // Smoothed speed & movement state for timer / UI
    @State private var speedSamples: [Double] = []
    @State private var isMoving: Bool = false
    @State private var movingTicks: Int = 0
    @State private var stoppedTicks: Int = 0
    var rideModel: JoinRideModel
    private var sessionVM: ConnectedRideSessionVM { ConnectedRideSessionVM(core: viewModel) }
    private var participantsVM: ConnectedRideParticipantsVM { ConnectedRideParticipantsVM(core: viewModel) }
    private var messagingVM: ConnectedRideMessagingVM { ConnectedRideMessagingVM(core: viewModel) }

    /// Display name for "Ride in Progress" card; fallback when userNameStatic is empty (e.g. after ride stopped).
    private var currentUserDisplayName: String {
        let name = MBUserDefaults.userNameStatic ?? ""
        if !name.isEmpty { return name }
        return rideModel.userId == MBUserDefaults.userIdStatic ? rideModel.organizer : AppStrings.ConnectedRide.riderFallbackName
    }

    /// keep both spped identical one in map and other in current user.
    private var displayedSpeedKph: Int {
        if !speedSamples.isEmpty {
            let avg = speedSamples.reduce(0, +) / Double(speedSamples.count)
            return Int(avg.rounded())
        }
        return Int(locationManager.speedInKph ?? 0.0)
    }

    var body: some View {
        
        ZStack{
            if showMessagePopup{
                MessagePopupView(viewModel: viewModel, isPresented: $showMessagePopup, showMessageNotification: $showMessageNotification, riderName: $selectedRiderName)
                    .transition(.scale)
                    .zIndex(1)
            }
            AppToolBar {
                VStack(spacing: 0) {
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
                    if showMessageNotification {
                        showToast(title: "\(AppStrings.ConnectedRide.messageSentToPrefix) \(selectedRiderName)")
                            .transition(.move(edge: .top).combined(with: .opacity))
                    }
                    if viewModel.showRecieveMessagePopup {
                        showToast(title: "\(AppStrings.ConnectedRide.newMessageFromPrefix) \(viewModel.latestIncomingSenderName)")
                            .transition(.move(edge: .top).combined(with: .opacity))
                    }
                    List {
                        Section {
                            VStack {
                                ZStack(alignment: .topLeading) {
                                    BikeRouteMapView(position: $position, currentMapStyle: viewModel.currentMapStyle, rideModel: rideModel, groupRiders: viewModel.groupRiders, startTracking: $startTrack, userLocation: startTrack ? locationManager.lastLocation?.coordinate : nil, onRouteFitted: { initialMapRegion = $0 })
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
                        Section { rideProgressSection() }
                        .listRowSeparator(.hidden)
                        .listRowBackground(Color.clear)
                        
                        if viewModel.groupRiders.count >= 1 {
                            Section { groupStatusSection() }
                            .listRowSeparator(.hidden)
                            .listRowBackground(Color.clear)
                        }
                        Section { emergencyActionsSection() }
                        .listRowSeparator(.hidden)
                        .listRowBackground(Color.clear)
                        .listRowInsets(EdgeInsets(top: 0, leading: 16, bottom: 0, trailing: 16))
                    }.listStyle(.plain)
                        .listRowSeparator(.hidden)
                        .navigationBarBackButtonHidden()
                        .navigationDestination(isPresented: $rideComplted, destination: {
                            ConnectedRideView(
                                notificationTitle: AppStrings.ConnectedRide.rideCompletedTitle,
                                title: AppStrings.ConnectedRide.rideCompletionProgressTitle,
                                subTitle: AppStrings.ConnectedRide.rideCompletionProgressSubtitle,
                                model: rideModel,
                                rideCompleteModel: viewModel.rideCompleteModel
                            )
                        })
                        .onAppear() {
                            viewModel.activeRide = rideModel
                            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                                showToast = false
                            }
                            guard rideSessionActive else { return }
                            locationManager.requestLocation()
                            locationManager.startUpdatingLocation()

                            if !rideModel.rideJoined {
                                tryJoinWhenLocationReady()
                            } else {
                                startOngoingRideTimer()
                            }
                            let rawSpeed = locationManager.speedInKph ?? 0.0
                            viewModel.onLocationUpdate(lat: locationManager.lastLocation?.coordinate.latitude ?? 0.0,
                                                       long: locationManager.lastLocation?.coordinate.longitude ?? 0.0,
                                                       speed: rawSpeed)
                            sessionController.startUiTimer {
                                updateSpeedAndTimer()
                            }
                        }
                        .onChange(of: locationManager.lastLocation) { _, newLoc in
                            accumulateTravelledDistance(newLoc)
                        }
                        .onChange(of: showMessageNotification) { _, isShowing in
                            if isShowing {
                                NotificationStore.shared.add(
                                    title: AppStrings.NavigationSlider.message,
                                    message: "\(AppStrings.ConnectedRide.messageSentToPrefix) \(selectedRiderName)",
                                    type: .message
                                )
                                DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                                    withAnimation {
                                        showMessageNotification = false
                                    }
                                }
                            }
                        }
                        .onChange(of: viewModel.showRecieveMessagePopup) { _, isShowingMessage in
                            if isShowingMessage {
                                DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                                    withAnimation {
                                        viewModel.showRecieveMessagePopup = false
                                    }
                                }
                            }
                        }
                        .task(id: rideSessionActive) {
                            guard rideSessionActive else { return }
                            await messagingVM.receiveMessage(rideId: rideModel.rideId)
                        }
                        .task(id: rideSessionActive) {
                            guard rideSessionActive else { return }
                            await participantsVM.getOnGoingRides(rideId: rideModel.rideId)
                        }
                        .onChange(of: viewModel.ongoingRideId) { _, ride in
                            if !ride.isEmpty, rideSessionActive, !rideComplted, !isEndingRide {
                                startOngoingRideTimer()
                            }
                        }
                        .navigationDestination(isPresented: $showJoinRideView, destination: {
                            JoinRideView()
                        })
                }
            }
        }
        .animation(.easeInOut, value: showMessageNotification)
    }
    
    func tryJoinWhenLocationReady() {
        sessionController.tryJoinWhenLocationReady(
            locationProvider: {
                let lat = locationManager.lastLocation?.coordinate.latitude ?? (rideModel.hasAssemblyPoint ? rideModel.assemblyLat : rideModel.startLat) ?? 0
                let long = locationManager.lastLocation?.coordinate.longitude ?? (rideModel.hasAssemblyPoint ? rideModel.assemblyLon : rideModel.startLong) ?? 0
                let isValid = (lat != 0 || long != 0)
                return (lat, long, isValid)
            },
            joinAction: { lat, long in
                sessionVM.joinRide(
                    rideId: rideModel.rideId,
                    userId: MBUserDefaults.userIdStatic ?? "",
                    currentLat: lat,
                    currentLong: long,
                    speed: locationManager.speedInKph ?? 0.0
                )
            },
            onRetriesExhaustedWithoutValidLocation: {
                endRideErrorMessage = AppStrings.ConnectedRide.locationUnavailableJoinFailed
            }
        )
    }

    func startOngoingRideTimer() {
        guard rideSessionActive, !rideComplted, !isEndingRide else { return }
        sessionController.startHeartbeat(interval: 10) {
            if MBUserDefaults.userIdStatic == nil || MBUserDefaults.userIdStatic?.isEmpty == true {
                stopTimer()
                return
            }
            Task { @MainActor in
                let lat = locationManager.lastLocation?.coordinate.latitude ?? 0.0
                let long = locationManager.lastLocation?.coordinate.longitude ?? 0.0
                let speed = locationManager.speedInKph ?? 0.0
                sessionVM.sendHeartbeatIfNeeded(
                    rideId: rideModel.rideId,
                    userId: MBUserDefaults.userIdStatic ?? "",
                    currentLat: lat,
                    currentLong: long,
                    speed: speed
                )
            }
        }
    }
    
    func formatTime(_ totalSeconds: Int) -> String {
        let hours = totalSeconds / 3600
        let minutes = (totalSeconds % 3600) / 60
        let seconds = totalSeconds % 60
        return String(format: "%02d:%02d:%02d", hours, minutes, seconds)
    }

    /// Distance shown on the ride-complete screen (GPS odometer, km).
    private func formatTravelledDistanceKm() -> String {
        String(format: "%.1f", travelledDistanceMeters / 1000.0)
    }

    /// Adds segment length between consecutive fixes; ignores jitter and single-point GPS spikes.
    private func accumulateTravelledDistance(_ newLoc: CLLocation?) {
        guard let newLoc else { return }
        guard newLoc.horizontalAccuracy > 0, newLoc.horizontalAccuracy <= 80 else { return }
        guard let prev = lastOdometerLocation else {
            lastOdometerLocation = newLoc
            return
        }
        guard prev.horizontalAccuracy > 0, prev.horizontalAccuracy <= 80 else {
            lastOdometerLocation = newLoc
            return
        }
        let delta = newLoc.distance(from: prev)
        if delta >= 2, delta < 500 {
            travelledDistanceMeters += delta
        }
        lastOdometerLocation = newLoc
    }
    
    func stopTimer() {
        sessionController.stopAllTimers()
    }

    /// Stops heartbeat + location updates immediately after a successful end ride.
    private func stopSessionAfterRideEnded() {
        stopTimer()
        viewModel.stopOngoingRideTimer()
        viewModel.endRide()
        rideSessionActive = false
        viewModel.ongoingRideId = ""
        MBUserDefaults.isRideJoinedID = nil
        MBUserDefaults.rideIdStatic = nil
        locationManager.stopUpdatingLocation()
    }

    /// Updates smoothed speed and controls when the ride timer should advance.
    /// - Uses a small rolling window and hysteresis to avoid flicker when GPS jitter or stop‑and‑go traffic occur.
    private func updateSpeedAndTimer() {
        // 1. Read raw speed from location manager
        let rawSpeed = locationManager.speedInKph ?? 0.0

        // 2. Apply a deadband: treat very low speeds as 0 to ignore jitter
        let speedWithDeadband = rawSpeed < 3.0 ? 0.0 : rawSpeed

        // 3. Maintain a short rolling window (last 5 samples)
        speedSamples.append(speedWithDeadband)
        if speedSamples.count > 5 {
            speedSamples.removeFirst(speedSamples.count - 5)
        }

        let avgSpeed = speedSamples.isEmpty ? 0.0 : speedSamples.reduce(0, +) / Double(speedSamples.count)

        // 4. Hysteresis for movement state:
        //    - require 3 consecutive "moving" ticks to start
        //    - require 5 consecutive "stopped" ticks to stop
        if avgSpeed > 0 {
            movingTicks += 1
            stoppedTicks = 0
            if !isMoving, movingTicks >= 3 {
                isMoving = true
            }
        } else {
            stoppedTicks += 1
            movingTicks = 0
            if isMoving, stoppedTicks >= 5 {
                isMoving = false
            }
        }

        // 5. Advance timer only while considered moving
        if isMoving {
            elapsedSeconds += 1
        }
    }
    
    @ViewBuilder func mapActionButton() -> some View {
        HStack {
            Button(action: { showMapViewFullScreen.toggle() }) {
                AppIcon.ConnectedRide.zoom
                    .resizable()
                    .frame(width: 50, height: 50)
                    .padding([.top, .leading], 15)
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
            .padding(.trailing, 16)
        }
    }

    @ViewBuilder private func rideProgressSection() -> some View {
        VStack(spacing: 18) {
            ConnectedRideHeaderView(title: AppStrings.ConnectedRide.rideInProgressTitle, subtitle:AppStrings.ConnectedRide.groupNavigationActiveSubtitle, image: AppIcon.Profile.profile)
            ActiveRiderView(title: currentUserDisplayName, speed: "\(displayedSpeedKph) \(AppStrings.ConnectedRide.speedUnitKph)", startTrack:$startTrack)
            Button(action: endRideTapped) {
                Text(isEndingRide ? AppStrings.ConnectedRide.endRideInProgress : AppStrings.ConnectedRide.endRideButton)
                    .frame(maxWidth: .infinity,minHeight: 60)
                    .font(KlavikaFont.bold.font(size: 18))
                    .foregroundColor(AppColor.white)
                    .background(
                        RoundedRectangle(cornerRadius: 10)
                            .fill(AppColor.red)
                    )
            }
            .padding([.leading,.trailing,.bottom],16)
            .buttonStyle(.plain)
            .disabled(isEndingRide)
        }
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
    }

    @ViewBuilder private func groupStatusSection() -> some View {
        VStack(spacing: 18) {
            ConnectedRideHeaderView(title: "\(AppStrings.ConnectedRide.groupStatusTitle) (\(viewModel.groupRiders.count))", subtitle: "", image: AppIcon.ConnectedRide.groupStatus)
            ForEach(viewModel.groupRiders.indices, id: \.self) { index in
                let rider = viewModel.groupRiders[index]
                let _ = viewModel.groupStatusTick
                GroupRiderView(profileImageName: rider.profileImageName, title: rider.name, status: rider.status.rawValue, speed: "\(rider.speed) km", subTitle: participantsVM.formatTime(from: rider.statusSinceEpochMillis), index: index, showMessagePopup: $showMessagePopup,onMessageTap: { val in
                    selectedRiderName = viewModel.groupRiders[index].name
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

    @ViewBuilder private func emergencyActionsSection() -> some View {
        VStack(spacing: 18) {
            ConnectedRideHeaderView(title: AppStrings.ConnectedRide.emergencyActionsTitle, subtitle: "", image: AppIcon.ConnectedRide.emergency)
            HStack(spacing: 16) {
                emergencyActionButton(icon: AppIcon.ConnectedRide.sos, title: AppStrings.ConnectedRide.emergencySOSButton) {
                    participantsVM.sendEmergencySOS()
                }
                emergencyActionButton(icon: AppIcon.ConnectedRide.shareLocation, title: AppStrings.ConnectedRide.shareLocationButton) {
                    participantsVM.shareLocation()
                }
            }
            .padding(.bottom)
        }
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
    }

    @ViewBuilder private func emergencyActionButton(icon: Image, title: String, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            HStack(alignment: .center, spacing: 5) {
                icon
                Text(title)
                    .font(KlavikaFont.bold.font(size: 16))
                    .foregroundStyle(AppColor.black)
            }
            .padding()
            .frame(maxWidth: .infinity)
            .frame(height: 50)
            .contentShape(Rectangle())
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.white)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(AppColor.darkGray, lineWidth: 2)
            )
        }
        .buttonStyle(.plain)
    }

    private func endRideTapped() {
        guard !isEndingRide else { return }
        isEndingRide = true
        sessionVM.endRide(rideId: rideModel.rideId) { success in
            guard success else {
                DispatchQueue.main.async {
                    endRideErrorMessage = AppStrings.ConnectedRide.endRideFailed
                    isEndingRide = false
                }
                return
            }
            // Stop any background heartbeats immediately so no more PATCH updates go out after ride ends.
            DispatchQueue.main.async {
                stopSessionAfterRideEnded()
                // Mark ride ended in the main ride node immediately (source of truth for "active ride").
                // Do NOT depend on end-ride summary success for this, otherwise the app can treat the ride as still active
                // on next refresh and re-join in background.
                viewModel.endActiveRide(rideId: rideModel.rideId, rideCreatedBy: rideModel.userId)
            }
            sessionVM.endRideSummary(
                ride: rideModel,
                userID: MBUserDefaults.userIdStatic ?? "",
                travelledDistanceKm: travelledDistanceMeters / 1000.0
            ) { summarySaved in
                DispatchQueue.main.async {
                    guard summarySaved else {
                        endRideErrorMessage = AppStrings.ConnectedRide.endRideSummaryFailed
                        isEndingRide = false
                        return
                    }
                    viewModel.getRideCompleteDetails(
                        duration: formatTime(elapsedSeconds),
                        distance: formatTravelledDistanceKm(),
                        riders: "\(viewModel.groupRiders.count + 1)"
                    )

                    NotificationStore.shared.add(
                        title: AppStrings.NavigationSlider.connectedRide,
                        message: AppStrings.ConnectedRide.rideCompleted,
                        type: .rideUpdate
                    )
                    isEndingRide = false
                    rideComplted = true
                }
            }
        }
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
            Text("\(displayedSpeedKph)")
                .font(KlavikaFont.bold.font(size: 20))
                .foregroundStyle(AppColor.black)
            Text(AppStrings.ConnectedRide.speedUnitKph)
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
            ButtonView(title: "", icon: AppIcon.ConnectedRide.refresh, onTap: resetMapToInitial)
            ButtonView(title: "", icon: AppIcon.ConnectedRide.nearMe, onTap: {
                showNavigationOptions = true
            })
            .confirmationDialog(AppStrings.ConnectedRide.navDialogTitle, isPresented: $showNavigationOptions, titleVisibility: .visible) {
                Button(AppStrings.ConnectedRide.navOpenInApp) {
                    showInAppNavigation = true
                }
                Button(AppStrings.ConnectedRide.navOpenAppleMaps) {
                    openInAppleMaps(start: navigationStartCoordinate(), end: navigationEndCoordinate())
                }
                Button(AppStrings.ConnectedRide.navOpenGoogleMaps) {
                    openInGoogleMaps(start: navigationStartCoordinate(), end: navigationEndCoordinate())
                }
                Button(AppStrings.ConnectedRide.navCancel, role: .cancel) { }
            }
            .sheet(isPresented: $showInAppNavigation) {
                InAppNavigationView(
                    start: navigationStartCoordinate(),
                    end: navigationEndCoordinate(),
                    connectedRideViewModel: viewModel,
                    locationManager: locationManager
                )
            }
        }
        .frame(width: 130)
        .padding(.trailing)
    }
    
    func resetMapToInitial() {
        guard let region = initialMapRegion else { return }
        withAnimation(.easeInOut(duration: 0.35)) {
            position = .region(region)
        }
    }

    func recenterMap() {
        if let userLocation = locationManager.lastLocation?.coordinate {
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

    // MARK: - Navigation coordinates (assembly point → end when present)
    /// Start for navigation: assembly point when ride has one, else user location or ride start.
    private func navigationStartCoordinate() -> CLLocationCoordinate2D {
        if startTrack, let current = locationManager.lastLocation?.coordinate {
            return current
        }
        if rideModel.hasAssemblyPoint,
           let lat = rideModel.assemblyLat,
           let lon = rideModel.assemblyLon {
            return CLLocationCoordinate2D(latitude: lat, longitude: lon)
        }
        return locationManager.lastLocation?.coordinate ?? CLLocationCoordinate2D(latitude: rideModel.startLat, longitude: rideModel.startLong)
    }

    private func navigationEndCoordinate() -> CLLocationCoordinate2D {
        CLLocationCoordinate2D(latitude: rideModel.endLat, longitude: rideModel.endLong)
    }

    // MARK: - Navigation option handlers
    func openInAppleMaps(start: CLLocationCoordinate2D, end: CLLocationCoordinate2D) {
        let startItem = MKMapItem(placemark: MKPlacemark(coordinate: start))
        let endItem = MKMapItem(placemark: MKPlacemark(coordinate: end))
        MKMapItem.openMaps(with: [startItem, endItem], launchOptions: [MKLaunchOptionsDirectionsModeKey: MKLaunchOptionsDirectionsModeDriving])
    }

    func openInGoogleMaps(start: CLLocationCoordinate2D, end: CLLocationCoordinate2D) {
        // Prefer comgooglemaps URL scheme if available, fallback to web directions
        let urlScheme = "comgooglemaps://"
        let directions = "?saddr=\(start.latitude),\(start.longitude)&daddr=\(end.latitude),\(end.longitude)&directionsmode=driving"
        if let schemeURL = URL(string: urlScheme), UIApplication.shared.canOpenURL(schemeURL) {
            if let url = URL(string: "comgooglemaps://\(directions)") {
                UIApplication.shared.open(url)
            }
        } else {
            if let webURL = URL(string: "https://www.google.com/maps/dir/\(start.latitude),\(start.longitude)/\(end.latitude),\(end.longitude)/") {
                UIApplication.shared.open(webURL)
            }
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
    @Binding var startTrack:Bool
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
    }
}

struct GroupRiderView: View {
    let profileImageName: String?
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
                ProfileImageView(profileImageName: profileImageName, size: CGSize(width: 37, height: 37))
                    .overlay(Circle().stroke(statusPinColor, lineWidth: 1.5))
                    .padding(.leading, 18)
                
                VStack(alignment: .leading, spacing: 5) {
                    HStack(spacing: 6) {
                        Text(title.isEmpty ? AppStrings.ConnectedRide.riderFallbackName : title)
                            .lineLimit(1)
                            .minimumScaleFactor(0.7)
                            .font(KlavikaFont.bold.font(size: 16))
                            .foregroundColor(AppColor.black)
                        Text(status)
                            .padding(.horizontal, 8)
                            .padding(.vertical, 4)
                            .font(KlavikaFont.regular.font(size: 12))
                            .foregroundColor(statusTextColor)
                            .background(statusBgColor)
                            .fixedSize(horizontal: true, vertical: false)
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
    
    var statusPinColor: Color {
        if status == RiderStatus.connected.rawValue {
            return AppColor.green
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
