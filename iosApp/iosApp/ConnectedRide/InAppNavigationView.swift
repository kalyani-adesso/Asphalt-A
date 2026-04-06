//  InAppNavigationView.swift
//  iosApp
//
//  Basic in-app routing with polyline, traffic and voice guidance (simple proof-of-concept)
//

import SwiftUI
import MapKit
import AVFoundation

@available(iOS 17.0, *)
struct InAppNavigationView: View {
    let start: CLLocationCoordinate2D
    let end: CLLocationCoordinate2D
    /// Shared ConnectedRideViewModel so we can show live participant pins during navigation.
    @ObservedObject var connectedRideViewModel: ConnectedRideViewModel
    @ObservedObject var locationManager: LocationManager
    @Environment(\.dismiss) private var dismiss
    @StateObject private var viewModel = InAppNavigationViewModel()
    @State private var mapType: MKMapType = .standard
    @State private var recenterCounter: Int = 0
    @State private var focusCoordinate: CLLocationCoordinate2D? = nil
    @State private var focusCounter: Int = 0
    /// When focusing on start (after simulation stopped), use larger distance to zoom out.
    @State private var focusCameraDistance: Double? = nil
    @State private var followUserState: Bool = true
    @State private var showInstructions: Bool = false
    private let synthesizer = AVSpeechSynthesizer()
    @StateObject private var speechDelegate = SpeechDelegate()
    /// Whether voice guidance is enabled; when off, no new instructions are spoken.
    @State private var isVoiceEnabled: Bool = true
    /// User position shown on map: simulated during demo; otherwise real location so the navigate icon stays visible when recentering.
    private var displayUserCoordinate: CLLocationCoordinate2D? {
        if viewModel.isSimulating { return viewModel.simulatedCoordinate }
        // Prefer real location when not simulating so the blue dot appears on the visible map when user taps navigate.
        if let loc = locationManager.lastLocation?.coordinate { return loc }
        return viewModel.simulatedCoordinate
    }

    /// Route to draw: trimmed from user position to end when user position is known (polyline shortens as rider moves).
    private var displayedRouteCoordinates: [CLLocationCoordinate2D] {
        guard !viewModel.routeCoordinates.isEmpty else { return [] }
        // In simulation demo, when paused or finished (isSimulating == false but we still have a simulatedCoordinate),
        // show the full route from START → END so the user clearly sees the entire navigation path.
        if !viewModel.isSimulating, viewModel.simulatedCoordinate != nil {
            return viewModel.routeCoordinates
        }
        guard let user = displayUserCoordinate else { return viewModel.routeCoordinates }
        return trimRouteFromUserPosition(viewModel.routeCoordinates, user: user)
    }

    var body: some View {
        VStack(spacing: 0) {
            // Header with app styling
            HStack {
                Button(action: { dismiss() }) {
                    HStack(spacing: 6) {
                        Image(systemName: "chevron.left")
                            .font(.system(size: 16, weight: .semibold))
                        Text(AppStrings.ConnectedRide.navClose)
                            .font(KlavikaFont.medium.font(size: 16))
                    }
                    .foregroundColor(AppColor.celticBlue)
                }
                Spacer()
                VStack(spacing: 2) {
                    Text(AppStrings.ConnectedRide.navTitle)
                        .font(KlavikaFont.bold.font(size: 18))
                        .foregroundColor(AppColor.black)
                    if !viewModel.isCalculatingRoute && viewModel.routeDistance > 0 {
                        Text(routeSummaryText)
                            .font(KlavikaFont.regular.font(size: 12))
                            .foregroundColor(AppColor.stoneGray)
                    }
                }
                Spacer()
                Color.clear.frame(width: 60, height: 44)
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
            .background(Color(UIColor.systemBackground))

            ZStack(alignment: .top) {
                InAppMapView(routeCoordinates: displayedRouteCoordinates,
                             trafficSegments: viewModel.routeTrafficSegments,
                             startCoordinate: start,
                             endCoordinate: end,
                             userCoordinate: displayUserCoordinate,
                             participants: connectedRideViewModel.groupRiders,
                             followUser: followUserState,
                             cameraAltitude: 200,
                             mapType: mapType,
                             recenterCounter: recenterCounter,
                             focusCoordinate: focusCoordinate,
                             focusCounter: focusCounter,
                             fitRouteCounter: viewModel.fitRouteCounter)
                    .edgesIgnoringSafeArea(.all)

                if viewModel.isCalculatingRoute {
                    VStack {
                        Spacer()
                        HStack {
                            Spacer()
                            HorizontalBouncingDotsLoader(color: AppColor.celticBlue, dotSize: 7, spacing: 6)
                            Text(AppStrings.ConnectedRide.navCalculatingRoute)
                                .font(KlavikaFont.medium.font(size: 14))
                                .foregroundColor(AppColor.stoneGray)
                            Spacer()
                        }
                        .padding(.vertical, 16)
                        .background(.ultraThinMaterial)
                        .cornerRadius(12)
                        .padding(.horizontal, 40)
                        Spacer().frame(height: 120)
                    }
                }
                if viewModel.isRerouting && !viewModel.isCalculatingRoute {
                    VStack {
                        Spacer()
                        HStack(spacing: 8) {
                            HorizontalBouncingDotsLoader(color: AppColor.celticBlue, dotSize: 6, spacing: 5)
                            Text(AppStrings.ConnectedRide.navUpdatingRoute)
                                .font(KlavikaFont.medium.font(size: 13))
                                .foregroundColor(AppColor.stoneGray)
                        }
                        .padding(.horizontal, 14)
                        .padding(.vertical, 10)
                        .background(.ultraThinMaterial)
                        .cornerRadius(10)
                        .padding(.bottom, 100)
                    }
                }

                if let errorText = viewModel.routeError {
                    VStack {
                        Spacer()
                        HStack(spacing: 10) {
                            Image(systemName: "exclamationmark.triangle.fill")
                                .foregroundColor(.yellow)
                            Text(errorText)
                                .font(KlavikaFont.regular.font(size: 13))
                                .foregroundColor(AppColor.black)
                            Spacer()
                        }
                        .padding(.horizontal, 12)
                        .padding(.vertical, 10)
                        .background(.ultraThinMaterial)
                        .cornerRadius(12)
                        .shadow(color: Color.black.opacity(0.18), radius: 6, x: 0, y: 3)
                        .padding(.horizontal, 16)
                        .padding(.bottom, 28)
                    }
                    .transition(.move(edge: .bottom).combined(with: .opacity))
                    .animation(.easeInOut(duration: 0.25), value: viewModel.routeError)
                }

                // Top-left control column
                HStack {
                        VStack(spacing: 12) {
                            Button(action: {
                                // Toggle voice guidance on/off.
                                isVoiceEnabled.toggle()
                                if !isVoiceEnabled, synthesizer.isSpeaking {
                                    synthesizer.stopSpeaking(at: .immediate)
                                } else if isVoiceEnabled {
                                    // Optional: speak current step once when turning on.
                                    startVoiceGuidance()
                                }
                            }) {
                                Image(systemName: isVoiceEnabled ? "speaker.wave.2.fill" : "speaker.slash.fill")
                                    .font(.system(size: 18))
                                    .foregroundColor(.white)
                                    .frame(width: 44, height: 44)
                                    .background(
                                        viewModel.steps.isEmpty
                                        ? AppColor.stoneGray.opacity(0.5)
                                        : (isVoiceEnabled ? AppColor.celticBlue : AppColor.stoneGray)
                                    )
                                    .clipShape(Circle())
                            }
                            .disabled(viewModel.steps.isEmpty)

                        Button(action: {
                            // In simulation demo mode, recenter to the route START so the user always sees navigation from the beginning.
                            if !viewModel.routeCoordinates.isEmpty, viewModel.simulatedCoordinate != nil {
                                followUserState = false
                                focusCoordinate = viewModel.routeCoordinates.first ?? start
                                focusCounter += 1
                            } else {
                                // Normal behavior: recenter on live user location.
                                followUserState = true
                                recenterCounter += 1
                            }
                        }) {
                            Image(systemName: "location.fill")
                                .font(.system(size: 18))
                                .foregroundColor(.white)
                                .frame(width: 44, height: 44)
                                .background(AppColor.celticBlue)
                                .clipShape(Circle())
                        }

                        Button(action: { withAnimation(.easeInOut(duration: 0.25)) { showInstructions.toggle() } }) {
                            Image(systemName: "list.bullet")
                                .font(.system(size: 18))
                                .foregroundColor(.white)
                                .frame(width: 44, height: 44)
                                .background(AppColor.celticBlue)
                                .clipShape(Circle())
                        }

                        Menu {
                            Button(AppStrings.ConnectedRide.navMapStandard) { mapType = .standard }
                            Button(AppStrings.ConnectedRide.navMapSatellite) { mapType = .satellite }
                            Button(AppStrings.ConnectedRide.navMapHybrid) { mapType = .hybrid }
                        } label: {
                            Image(systemName: "map")
                                .resizable()
                                .scaledToFit()
                                .frame(width: 18, height: 18)
                                .foregroundColor(AppColor.celticBlue)
                                .padding(12)
                                .background(.regularMaterial)
                                .clipShape(Circle())
                        }

                        Button(action: {
                            viewModel.toggleRouteSimulation(
                                isVoiceEnabled: isVoiceEnabled,
                                speakStep: { idx in speakStep(index: idx) },
                                onRecenterTick: { recenterCounter += 1 }
                            )
                            if viewModel.isSimulating { followUserState = true }
                        }) {
                            Image(systemName: viewModel.isSimulating ? "stop.circle.fill" : "play.circle.fill")
                                .font(.system(size: 22))
                                .foregroundColor(.white)
                                .frame(width: 44, height: 44)
                                .background(viewModel.isSimulating ? Color.red : AppColor.celticBlue)
                                .clipShape(Circle())
                        }
                        .disabled(viewModel.routeCoordinates.isEmpty)
                    }
                    .padding(8)
                    .background(.ultraThinMaterial)
                    .cornerRadius(16)
                    .shadow(color: Color.black.opacity(0.12), radius: 6, x: 0, y: 3)
                    .padding(.leading, 12)
                    .padding(.top, 64)
                    .zIndex(2)

                    Spacer()
                }

                // Instruction list pinned at bottom
                VStack {
                    Spacer()
                    instructionList()
                }
            }
                .edgesIgnoringSafeArea(.all)
                .onAppear {
                    viewModel.calculateRoute(start: start, end: end)
                    // start receiving updates
                    locationManager.requestLocation()
                    locationManager.startUpdatingLocation()
                    // wire delegate so we can update UI when speech starts/stops
                    synthesizer.delegate = speechDelegate
                }
                .onDisappear {
                    viewModel.pauseRouteSimulation()
                    viewModel.simulatedCoordinate = nil
                    if synthesizer.isSpeaking {
                        synthesizer.stopSpeaking(at: .immediate)
                    }
                }
                .onChange(of: locationManager.lastLocation) { _, newLoc in
                    guard let loc = newLoc else { return }
                    viewModel.checkProgress(current: loc, isSimulating: viewModel.isSimulating, isVoiceEnabled: isVoiceEnabled) { idx in
                        speakStep(index: idx)
                    }
                    viewModel.evaluateRerouteIfNeeded(current: loc, isSimulating: viewModel.isSimulating, start: start, end: end)
                }
        }
    }

    private var routeSummaryText: String {
        var parts: [String] = []
        if viewModel.routeDistance >= 1000 {
            parts.append(String(format: AppStrings.ConnectedRide.navDistanceKmFormat, viewModel.routeDistance / 1000))
        } else if viewModel.routeDistance > 0 {
            parts.append(String(format: AppStrings.ConnectedRide.navDistanceMFormat, viewModel.routeDistance))
        }
        if viewModel.routeETA > 0 {
            let mins = Int(viewModel.routeETA / 60)
            if mins >= 60 {
                parts.append(String(format: AppStrings.ConnectedRide.navDurationHrMinFormat, mins / 60, mins % 60))
            } else {
                parts.append("\(mins) \(AppStrings.ConnectedRide.navDurationMinSuffix)")
            }
        }
        return parts.joined(separator: " · ")
    }

    func startVoiceGuidance() {
        guard isVoiceEnabled else { return }
        guard !viewModel.steps.isEmpty else { return }
        // speak the first step immediately
        speakStep(index: viewModel.currentStepIndex)
    }

    func speakStep(index: Int) {
        guard isVoiceEnabled else { return }
        guard index >= 0 && index < viewModel.steps.count else { return }
        let text = viewModel.steps[index]
        if synthesizer.isSpeaking { synthesizer.stopSpeaking(at: .immediate) }
        let utterance = AVSpeechUtterance(string: text)
        let languageId = Locale.current.language.languageCode?.identifier ?? "en-US"
        utterance.voice = AVSpeechSynthesisVoice(language: languageId)
        synthesizer.speak(utterance)
    }

    @ViewBuilder func instructionList() -> some View {
        VStack(spacing: 0) {
            HStack {
                Text(AppStrings.ConnectedRide.navDirections)
                    .font(KlavikaFont.bold.font(size: 17))
                    .foregroundColor(AppColor.black)
                Spacer()
                Button(action: { withAnimation(.easeInOut(duration: 0.25)) { showInstructions.toggle() } }) {
                    Image(systemName: showInstructions ? "chevron.down" : "chevron.up")
                        .font(.system(size: 14, weight: .semibold))
                        .foregroundColor(AppColor.celticBlue)
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 12)

            if showInstructions {
                ScrollView {
                    VStack(spacing: 6) {
                        ForEach(viewModel.steps.indices, id: \.self) { i in
                            let text = viewModel.steps[i]
                            let isCurrent = i == viewModel.currentStepIndex
                            HStack(alignment: .top, spacing: 12) {
                                Text("\(i + 1)")
                                    .font(KlavikaFont.bold.font(size: 12))
                                    .foregroundColor(isCurrent ? .white : AppColor.stoneGray)
                                    .frame(width: 22, height: 22)
                                    .background(isCurrent ? AppColor.celticBlue : AppColor.mediumGray)
                                    .clipShape(Circle())
                                VStack(alignment: .leading, spacing: 4) {
                                    Text(text)
                                        .font(KlavikaFont.regular.font(size: 14))
                                        .foregroundColor(isCurrent ? AppColor.celticBlue : AppColor.black)
                                    Text(distanceText(i))
                                        .font(KlavikaFont.regular.font(size: 12))
                                        .foregroundColor(AppColor.stoneGray)
                                }
                                Spacer()
                                Button(action: {
                                    followUserState = false
                                    focusCameraDistance = nil
                                    if i < viewModel.stepCoords.count {
                                        focusCoordinate = viewModel.stepCoords[i]
                                        focusCounter += 1
                                    }
                                }) {
                                    Image(systemName: "location")
                                        .font(.system(size: 14))
                                        .foregroundColor(AppColor.celticBlue)
                                }
                            }
                            .padding(.horizontal, 12)
                            .padding(.vertical, 10)
                            .background(isCurrent ? AppColor.celticBlue.opacity(0.08) : Color.white.opacity(0.95))
                            .cornerRadius(10)
                        }
                    }
                    .padding(.horizontal, 16)
                    .padding(.bottom, 16)
                }
                .frame(maxHeight: 280)
            }
        }
        .background(.ultraThinMaterial)
        .cornerRadius(16)
        .padding(.horizontal, 12)
        .padding(.bottom, 8)
    }

    func distanceText(_ index: Int) -> String {
        guard index < viewModel.stepDistances.count else { return "" }
        let dist = viewModel.stepDistances[index]
        if dist >= 1000 {
            return String(format: AppStrings.ConnectedRide.navDistanceKmFormat, dist/1000)
        } else {
            return String(format: AppStrings.ConnectedRide.navDistanceMFormat, dist)
        }
    }
}

// Observable delegate to track speech synthesizer state
class SpeechDelegate: NSObject, ObservableObject, AVSpeechSynthesizerDelegate {
    @Published var isSpeaking: Bool = false

    func speechSynthesizer(_ synthesizer: AVSpeechSynthesizer, didStart utterance: AVSpeechUtterance) {
        DispatchQueue.main.async { self.isSpeaking = true }
    }

    func speechSynthesizer(_ synthesizer: AVSpeechSynthesizer, didFinish utterance: AVSpeechUtterance) {
        DispatchQueue.main.async { self.isSpeaking = false }
    }

    func speechSynthesizer(_ synthesizer: AVSpeechSynthesizer, didCancel utterance: AVSpeechUtterance) {
        DispatchQueue.main.async { self.isSpeaking = false }
    }
}
