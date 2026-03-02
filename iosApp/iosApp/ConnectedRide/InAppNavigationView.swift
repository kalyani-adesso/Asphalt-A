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
    @Environment(\.dismiss) private var dismiss
    @State private var routeCoordinates: [CLLocationCoordinate2D] = []
    @State private var steps: [String] = []
    @State private var stepCoords: [CLLocationCoordinate2D] = []
    @State private var stepDistances: [Double] = []
    @State private var currentStepIndex: Int = 0
    @StateObject private var locationManager = LocationManager()
    @State private var mapType: MKMapType = .standard
    @State private var recenterCounter: Int = 0
    @State private var focusCoordinate: CLLocationCoordinate2D? = nil
    @State private var focusCounter: Int = 0
    /// When focusing on start (after simulation stopped), use larger distance to zoom out.
    @State private var focusCameraDistance: Double? = nil
    @State private var followUserState: Bool = true
    @State private var showInstructions: Bool = false
    @State private var isCalculatingRoute: Bool = true
    @State private var routeDistance: Double = 0
    @State private var routeETA: TimeInterval = 0
    private let synthesizer = AVSpeechSynthesizer()
    @StateObject private var speechDelegate = SpeechDelegate()
    // Route simulation demo
    @State private var isSimulating: Bool = false
    @State private var simulatedCoordinate: CLLocationCoordinate2D? = nil
    @State private var simulationIndex: Int = 0

    /// User position shown on map: simulated during demo, or endpoint after demo ends, otherwise real location.
    private var displayUserCoordinate: CLLocationCoordinate2D? {
        if isSimulating { return simulatedCoordinate }
        if let sim = simulatedCoordinate { return sim } // show endpoint after simulation ends
        return locationManager.lastLocation?.coordinate
    }

    var body: some View {
        VStack(spacing: 0) {
            // Header with app styling
            HStack {
                Button(action: { dismiss() }) {
                    HStack(spacing: 6) {
                        Image(systemName: "chevron.left")
                            .font(.system(size: 16, weight: .semibold))
                        Text("Close")
                            .font(KlavikaFont.medium.font(size: 16))
                    }
                    .foregroundColor(AppColor.celticBlue)
                }
                Spacer()
                VStack(spacing: 2) {
                    Text("Navigation")
                        .font(KlavikaFont.bold.font(size: 18))
                        .foregroundColor(AppColor.black)
                    if !isCalculatingRoute && routeDistance > 0 {
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
                InAppMapView(routeCoordinates: routeCoordinates,
                             startCoordinate: start,
                             endCoordinate: end,
                             userCoordinate: displayUserCoordinate,
                             participants: connectedRideViewModel.groupRiders,
                             followUser: followUserState,
                             cameraAltitude: 200,
                             mapType: mapType,
                             recenterCounter: recenterCounter,
                             focusCoordinate: focusCoordinate,
                             focusCounter: focusCounter)
                    .edgesIgnoringSafeArea(.all)

                if isCalculatingRoute {
                    VStack {
                        Spacer()
                        HStack {
                            Spacer()
                            ProgressView()
                                .scaleEffect(1.2)
                                .tint(AppColor.celticBlue)
                            Text("Calculating route...")
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

                // Top-left control column
                HStack {
                        VStack(spacing: 12) {
                            Button(action: {
                                if speechDelegate.isSpeaking {
                                    synthesizer.stopSpeaking(at: .immediate)
                                } else {
                                    startVoiceGuidance()
                                }
                            }) {
                                Image(systemName: speechDelegate.isSpeaking ? "speaker.slash.fill" : "speaker.wave.2.fill")
                                    .font(.system(size: 18))
                                    .foregroundColor(.white)
                                    .frame(width: 44, height: 44)
                                    .background(
                                        steps.isEmpty ? AppColor.stoneGray.opacity(0.5) : (speechDelegate.isSpeaking ? Color.red : AppColor.celticBlue)
                                    )
                                    .clipShape(Circle())
                            }
                            .disabled(steps.isEmpty)

                        Button(action: {
                            if isSimulating {
                                followUserState = true
                                recenterCounter += 1
                            } else if simulatedCoordinate != nil {
                                // Simulation stopped – show start point (default zoom)
                                followUserState = false
                                focusCoordinate = start
                                focusCameraDistance = nil
                                focusCounter += 1
                            } else {
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
                            Button("Standard") { mapType = .standard }
                            Button("Satellite") { mapType = .satellite }
                            Button("Hybrid") { mapType = .hybrid }
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

                        Button(action: { toggleRouteSimulation() }) {
                            Image(systemName: isSimulating ? "stop.circle.fill" : "play.circle.fill")
                                .font(.system(size: 22))
                                .foregroundColor(.white)
                                .frame(width: 44, height: 44)
                                .background(isSimulating ? Color.red : AppColor.celticBlue)
                                .clipShape(Circle())
                        }
                        .disabled(routeCoordinates.isEmpty)
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
                    calculateRoute()
                    // start receiving updates
                    locationManager.requestLocation()
                    locationManager.manager.startUpdatingLocation()
                    // wire delegate so we can update UI when speech starts/stops
                    synthesizer.delegate = speechDelegate
                }
                .onDisappear {
                    isSimulating = false
                    simulatedCoordinate = nil
                    if synthesizer.isSpeaking {
                        synthesizer.stopSpeaking(at: .immediate)
                    }
                }
                .onChange(of: locationManager.lastLocation) { newLoc in
                    guard let loc = newLoc else { return }
                    checkProgress(current: loc)
                }
        }
    }

    private var routeSummaryText: String {
        var parts: [String] = []
        if routeDistance >= 1000 {
            parts.append(String(format: "%.1f km", routeDistance / 1000))
        } else if routeDistance > 0 {
            parts.append(String(format: "%.0f m", routeDistance))
        }
        if routeETA > 0 {
            let mins = Int(routeETA / 60)
            if mins >= 60 {
                parts.append(String(format: "%d hr %d min", mins / 60, mins % 60))
            } else {
                parts.append("\(mins) min")
            }
        }
        return parts.joined(separator: " · ")
    }

    func calculateRoute() {
        isCalculatingRoute = true
        let request = MKDirections.Request()
        request.source = MKMapItem(placemark: MKPlacemark(coordinate: start))
        request.destination = MKMapItem(placemark: MKPlacemark(coordinate: end))
        request.transportType = .automobile

        let directions = MKDirections(request: request)
        directions.calculate { response, error in
            DispatchQueue.main.async {
                isCalculatingRoute = false
            }
            guard let route = response?.routes.first else { return }
            let polylineCoords = route.polyline.coordinates
            let tempSteps: [String] = route.steps.compactMap { step in
                let instr = step.instructions
                return instr.isEmpty ? nil : instr
            }
            var tempCoords: [CLLocationCoordinate2D] = []
            var tempDistances: [Double] = []
            for step in route.steps where !step.instructions.isEmpty {
                tempDistances.append(step.distance)
                if step.polyline.pointCount > 0, let first = step.polyline.coordinates.first {
                    tempCoords.append(first)
                } else {
                    tempCoords.append(polylineCoords.first ?? end)
                }
            }
            let totalDistance = route.distance
            let eta = route.expectedTravelTime

            DispatchQueue.main.async {
                routeCoordinates = polylineCoords
                steps = tempSteps
                stepCoords = tempCoords
                stepDistances = tempDistances
                currentStepIndex = 0
                routeDistance = totalDistance
                routeETA = eta
            }
        }
    }

    func startVoiceGuidance() {
        guard !steps.isEmpty else { return }
        // speak the first step immediately
        speakStep(index: currentStepIndex)
    }

    func speakStep(index: Int) {
        guard index >= 0 && index < steps.count else { return }
        let text = steps[index]
        if synthesizer.isSpeaking { synthesizer.stopSpeaking(at: .immediate) }
        let utterance = AVSpeechUtterance(string: text)
        utterance.voice = AVSpeechSynthesisVoice(language: Locale.current.languageCode ?? "en-US")
        synthesizer.speak(utterance)
    }

    func checkProgress(current: CLLocation) {
        guard !isSimulating else { return }
        guard currentStepIndex < stepCoords.count else { return }
        let target = stepCoords[currentStepIndex]
        let targetLoc = CLLocation(latitude: target.latitude, longitude: target.longitude)
        let distance = current.distance(from: targetLoc)
        // when within 30 meters, speak next step and advance
        if distance <= 30 {
            speakStep(index: currentStepIndex)
            currentStepIndex += 1
        }
    }

    // MARK: - Route simulation demo
    func toggleRouteSimulation() {
        if isSimulating {
            stopRouteSimulation()
        } else {
            startRouteSimulation()
        }
    }

    func startRouteSimulation() {
        guard !routeCoordinates.isEmpty else { return }
        simulationIndex = 0
        currentStepIndex = 0
        simulatedCoordinate = routeCoordinates[0]
        isSimulating = true
        followUserState = true
        // Speak first instruction when simulation starts
        if !steps.isEmpty {
            speakStep(index: 0)
        }
        advanceSimulationStep(index: 0)
    }

    private func advanceSimulationStep(index: Int) {
        guard isSimulating else { return }
        let count = routeCoordinates.count
        guard count > 0 else { stopRouteSimulation(); return }
        if index >= count {
            // Ensure we end exactly at the end point
            simulatedCoordinate = routeCoordinates[count - 1]
            simulationIndex = count - 1
            recenterCounter += 1
            stopRouteSimulation()
            return
        }
        simulationIndex = index
        simulatedCoordinate = routeCoordinates[index]
        recenterCounter += 1
        // Voice over: when simulated position is near next step, speak it
        if currentStepIndex < stepCoords.count, let currentSim = simulatedCoordinate {
            let target = stepCoords[currentStepIndex]
            let targetLoc = CLLocation(latitude: target.latitude, longitude: target.longitude)
            let simLoc = CLLocation(latitude: currentSim.latitude, longitude: currentSim.longitude)
            if simLoc.distance(from: targetLoc) <= 50 {
                speakStep(index: currentStepIndex)
                currentStepIndex += 1
            }
        }
        // 1 second per point
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
            advanceSimulationStep(index: index + 1)
        }
    }

    func stopRouteSimulation() {
        isSimulating = false
        // Leave dot at endpoint when simulation ends (reached end or user tapped stop)
        if !routeCoordinates.isEmpty {
            simulatedCoordinate = routeCoordinates[routeCoordinates.count - 1]
        } else {
            simulatedCoordinate = nil
        }
    }

    @ViewBuilder func instructionList() -> some View {
        VStack(spacing: 0) {
            HStack {
                Text("Directions")
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
                        ForEach(steps.indices, id: \.self) { i in
                            let text = steps[i]
                            let isCurrent = i == currentStepIndex
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
                                    if i < stepCoords.count {
                                        focusCoordinate = stepCoords[i]
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
        guard index < stepDistances.count else { return "" }
        let dist = stepDistances[index]
        if dist >= 1000 {
            return String(format: "%.1f km", dist/1000)
        } else {
            return String(format: "%.0f m", dist)
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
