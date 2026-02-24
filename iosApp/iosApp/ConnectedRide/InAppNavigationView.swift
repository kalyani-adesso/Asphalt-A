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
    @State private var followUserState: Bool = true
    @State private var showInstructions: Bool = false
    private let synthesizer = AVSpeechSynthesizer()
    @StateObject private var speechDelegate = SpeechDelegate()

    var body: some View {
        VStack(spacing: 0) {
            HStack {
                Button(action: { dismiss() }) {
                    Text("Close")
                }
                Spacer()
                Text("Navigation")
                    .font(.headline)
                Spacer()
            }
            .padding()

            ZStack(alignment: .top) {
                InAppMapView(routeCoordinates: routeCoordinates,
                             startCoordinate: start,
                             endCoordinate: end,
                             followUser: followUserState,
                             cameraAltitude: 200,
                             mapType: mapType,
                             recenterCounter: recenterCounter,
                             focusCoordinate: focusCoordinate,
                             focusCounter: focusCounter)
                    .edgesIgnoringSafeArea(.all)

                // Top-left control column: floating buttons stacked vertically, with map-type as fourth
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
                                    .foregroundColor(.white)
                                    .frame(width: 44, height: 44)
                                    .background(
                                        steps.isEmpty ? Color.gray.opacity(0.4) : (speechDelegate.isSpeaking ? Color.red : Color.accentColor)
                                    )
                                    .clipShape(Circle())
                            }
                            .disabled(steps.isEmpty)

                        Button(action: { followUserState = true; recenterCounter += 1 }) {
                            Image(systemName: "location.fill")
                                .foregroundColor(.white)
                                .frame(width: 44, height: 44)
                                .background(Color.accentColor)
                                .clipShape(Circle())
                        }

                        Button(action: { withAnimation { showInstructions.toggle() } }) {
                            Image(systemName: "list.bullet")
                                .foregroundColor(.white)
                                .frame(width: 44, height: 44)
                                .background(Color.accentColor)
                                .clipShape(Circle())
                        }

                        // Map type menu integrated in the same column
                        Menu {
                            Button("Standard") { mapType = .standard }
                            Button("Satellite") { mapType = .satellite }
                            Button("Hybrid") { mapType = .hybrid }
                        } label: {
                            Image(systemName: "map")
                                .resizable()
                                .scaledToFit()
                                .frame(width: 18, height: 18)
                                .padding(12)
                                .background(.regularMaterial)
                                .clipShape(Circle())
                        }
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
                    // Ensure any speaking stops when leaving the navigation view
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

    func calculateRoute() {
        let request = MKDirections.Request()
        request.source = MKMapItem(placemark: MKPlacemark(coordinate: start))
        request.destination = MKMapItem(placemark: MKPlacemark(coordinate: end))
        request.transportType = .automobile

        let directions = MKDirections(request: request)
        directions.calculate { response, error in
            guard let route = response?.routes.first else { return }
            routeCoordinates = route.polyline.coordinates
            // extract step instructions and representative coordinate & distance for each step
            var tempSteps: [String] = []
            var tempCoords: [CLLocationCoordinate2D] = []
            var tempDistances: [Double] = []
            for step in route.steps {
                let instr = step.instructions
                if !instr.isEmpty {
                    tempSteps.append(instr)
                    tempDistances.append(step.distance)
                    // take first coordinate of the step's polyline if present
                    if step.polyline.pointCount > 0 {
                        let coords = step.polyline.coordinates
                        if let first = coords.first {
                            tempCoords.append(first)
                        } else {
                            tempCoords.append(route.polyline.coordinates.first ?? end)
                        }
                    } else {
                        tempCoords.append(route.polyline.coordinates.first ?? end)
                    }
                }
            }
            steps = tempSteps
            stepCoords = tempCoords
            stepDistances = tempDistances
            currentStepIndex = 0
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

    // Show a list of steps as an overlay
    @ViewBuilder func instructionList() -> some View {
        VStack(spacing: 0) {
            HStack {
                Text("Directions")
                    .font(.headline)
                Spacer()
                Button(action: { showInstructions.toggle() }) {
                    Image(systemName: showInstructions ? "chevron.down" : "chevron.up")
                }
            }
            .padding()

            if showInstructions {
                ScrollView {                
                    VStack(spacing: 8) {
                        ForEach(steps.indices, id: \.self) { i in
                                    let text = steps[i]
                            HStack {
                                VStack(alignment: .leading) {
                                    Text(text)
                                        .font(.subheadline)
                                        .foregroundColor(i == currentStepIndex ? .blue : .primary)
                                    Text(distanceText(i))
                                        .font(.caption)
                                        .foregroundColor(.secondary)
                                }
                                Spacer()
                                Button(action: {
                                    // disable follow-while focusing so the camera stays on the selected step
                                    followUserState = false
                                    // focus on this step coordinate
                                    if i < stepCoords.count {
                                        focusCoordinate = stepCoords[i]
                                        focusCounter += 1
                                    }
                                }) {
                                    Image(systemName: "location")
                                }
                            }
                            .padding(.horizontal)
                            .padding(.vertical, 6)
                            .background(Color.white.opacity(0.9))
                            .cornerRadius(8)
                            .shadow(radius: 1)
                        }
                    }
                    .padding()
                }
                .frame(maxHeight: 260)
            }
        }
        .background(.ultraThinMaterial)
        .cornerRadius(12)
        .padding()
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

// `MKPolyline.coordinates` extension already exists in `PolylineMapView.swift`.
@available(iOS 17.0, *)
struct InAppNavigationView_Previews: PreviewProvider {
    static var previews: some View {
        InAppNavigationView(start: CLLocationCoordinate2D(latitude: 19.0760, longitude: 72.8777), end: CLLocationCoordinate2D(latitude: 19.2183, longitude: 72.9781))
    }
}
