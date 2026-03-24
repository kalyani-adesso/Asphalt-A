//
//  InAppNavigationViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 24/03/26.
//

import Foundation
import CoreLocation
import MapKit

@MainActor
final class InAppNavigationViewModel: ObservableObject {
    @Published var routeCoordinates: [CLLocationCoordinate2D] = []
    @Published var routeTrafficSegments: [ColoredRouteSegment] = []
    @Published var steps: [String] = []
    @Published var stepCoords: [CLLocationCoordinate2D] = []
    @Published var stepDistances: [Double] = []
    @Published var currentStepIndex: Int = 0
    @Published var isCalculatingRoute: Bool = true
    @Published var routeDistance: Double = 0
    @Published var routeETA: TimeInterval = 0
    @Published var routeError: String?
    @Published var isRerouting: Bool = false
    @Published var fitRouteCounter: Int = 0
    @Published var isSimulating: Bool = false
    @Published var simulatedCoordinate: CLLocationCoordinate2D?
    @Published var simulationIndex: Int = 0

    private var lastRerouteAt: Date?
    private var lastOffRouteCheckAt: Date?
    private var simulationWorkItem: DispatchWorkItem?

    func calculateRoute(
        start: CLLocationCoordinate2D,
        end: CLLocationCoordinate2D,
        sourceOverride: CLLocationCoordinate2D? = nil,
        fitCameraToRoute: Bool = true
    ) {
        let isRerouteRequest = sourceOverride != nil
        let sourceCoord = sourceOverride ?? start
        if isRerouteRequest {
            guard !isCalculatingRoute, !isRerouting else { return }
            isRerouting = true
        } else {
            isCalculatingRoute = true
        }
        routeError = nil
        let request = MKDirections.Request()
        request.source = MKMapItem(placemark: MKPlacemark(coordinate: sourceCoord))
        request.destination = MKMapItem(placemark: MKPlacemark(coordinate: end))
        request.transportType = .automobile

        let directions = MKDirections(request: request)
        directions.calculate { [weak self] response, error in
            guard let self else { return }
            Task { @MainActor in
                self.isCalculatingRoute = false
                self.isRerouting = false
            }
            guard let route = response?.routes.first else {
                Task { @MainActor in
                    if let error = error {
                        self.routeError = "Unable to calculate route: \(error.localizedDescription)"
                    } else {
                        self.routeError = "Unable to calculate route between these points."
                    }
                }
                return
            }

            let polylineCoords = route.polyline.coordinates
            let stepSegments: [ColoredRouteSegment] = route.steps.compactMap { step -> ColoredRouteSegment? in
                let coords = step.polyline.coordinates
                guard coords.count >= 2 else { return nil }
                return ColoredRouteSegment(
                    coordinates: coords,
                    density: densityFor(stepDistanceMeters: step.distance, expectedTravelTime: estimatedTravelTimeForStep(step, route: route))
                )
            }
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

            Task { @MainActor in
                self.routeCoordinates = polylineCoords
                self.routeTrafficSegments = stepSegments.isEmpty
                    ? [ColoredRouteSegment(coordinates: polylineCoords, density: .unknown)]
                    : stepSegments
                self.steps = tempSteps
                self.stepCoords = tempCoords
                self.stepDistances = tempDistances
                self.currentStepIndex = 0
                self.routeDistance = route.distance
                self.routeETA = route.expectedTravelTime
                if fitCameraToRoute {
                    self.fitRouteCounter += 1
                }
            }
        }
    }

    func evaluateRerouteIfNeeded(current: CLLocation, isSimulating: Bool, start: CLLocationCoordinate2D, end: CLLocationCoordinate2D) {
        guard !isSimulating, !routeCoordinates.isEmpty, !isRerouting, !isCalculatingRoute else { return }
        let now = Date()
        if let t = lastOffRouteCheckAt,
           now.timeIntervalSince(t) < ConnectedRideReroutePolicy.navMinSecondsBetweenChecks { return }
        lastOffRouteCheckAt = now
        let deviation = crossTrackDistanceMeters(from: current.coordinate, along: routeCoordinates)
        guard deviation > ConnectedRideReroutePolicy.offRouteMeters else { return }
        if let t = lastRerouteAt,
           now.timeIntervalSince(t) < ConnectedRideReroutePolicy.minSecondsBetweenReroutes { return }
        lastRerouteAt = now
        calculateRoute(start: start, end: end, sourceOverride: current.coordinate, fitCameraToRoute: false)
    }

    func checkProgress(current: CLLocation, isSimulating: Bool, isVoiceEnabled: Bool, speakStep: (Int) -> Void) {
        guard !isSimulating else { return }
        guard currentStepIndex < stepCoords.count else { return }
        let target = stepCoords[currentStepIndex]
        let targetLoc = CLLocation(latitude: target.latitude, longitude: target.longitude)
        let distance = current.distance(from: targetLoc)
        if distance <= 30, isVoiceEnabled {
            speakStep(currentStepIndex)
            currentStepIndex += 1
        }
    }

    // MARK: - Route simulation
    func toggleRouteSimulation(
        isVoiceEnabled: Bool,
        speakStep: @escaping (Int) -> Void,
        onRecenterTick: @escaping () -> Void
    ) {
        if isSimulating {
            pauseRouteSimulation()
        } else if simulatedCoordinate != nil {
            startRouteSimulation(from: simulationIndex, isVoiceEnabled: isVoiceEnabled, speakStep: speakStep, onRecenterTick: onRecenterTick)
        } else {
            startRouteSimulation(from: 0, isVoiceEnabled: isVoiceEnabled, speakStep: speakStep, onRecenterTick: onRecenterTick)
        }
    }

    func startRouteSimulation(
        from startIndex: Int,
        isVoiceEnabled: Bool,
        speakStep: @escaping (Int) -> Void,
        onRecenterTick: @escaping () -> Void
    ) {
        guard !routeCoordinates.isEmpty else { return }
        let clampedIndex = max(0, min(startIndex, routeCoordinates.count - 1))
        simulationIndex = clampedIndex
        if clampedIndex == 0 {
            currentStepIndex = 0
        }
        simulatedCoordinate = routeCoordinates[clampedIndex]
        isSimulating = true
        if isVoiceEnabled, !steps.isEmpty && currentStepIndex == 0 {
            speakStep(0)
        }
        advanceSimulationStep(index: clampedIndex, isVoiceEnabled: isVoiceEnabled, speakStep: speakStep, onRecenterTick: onRecenterTick)
    }

    func pauseRouteSimulation() {
        isSimulating = false
        simulationWorkItem?.cancel()
        simulationWorkItem = nil
    }

    func stopRouteSimulation() {
        isSimulating = false
        simulationWorkItem?.cancel()
        simulationWorkItem = nil
        if !routeCoordinates.isEmpty {
            simulatedCoordinate = routeCoordinates[routeCoordinates.count - 1]
        } else {
            simulatedCoordinate = nil
        }
    }

    private func advanceSimulationStep(
        index: Int,
        isVoiceEnabled: Bool,
        speakStep: @escaping (Int) -> Void,
        onRecenterTick: @escaping () -> Void
    ) {
        guard isSimulating else { return }
        let count = routeCoordinates.count
        guard count > 0 else { stopRouteSimulation(); return }
        if index >= count {
            simulatedCoordinate = routeCoordinates[count - 1]
            simulationIndex = count - 1
            onRecenterTick()
            stopRouteSimulation()
            return
        }
        simulationIndex = index
        simulatedCoordinate = routeCoordinates[index]
        onRecenterTick()
        if isVoiceEnabled, currentStepIndex < stepCoords.count, let currentSim = simulatedCoordinate {
            let target = stepCoords[currentStepIndex]
            let targetLoc = CLLocation(latitude: target.latitude, longitude: target.longitude)
            let simLoc = CLLocation(latitude: currentSim.latitude, longitude: currentSim.longitude)
            if simLoc.distance(from: targetLoc) <= 50 {
                speakStep(currentStepIndex)
                currentStepIndex += 1
            }
        }
        let work = DispatchWorkItem { [weak self] in
            self?.advanceSimulationStep(index: index + 1, isVoiceEnabled: isVoiceEnabled, speakStep: speakStep, onRecenterTick: onRecenterTick)
        }
        simulationWorkItem = work
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0, execute: work)
    }
}
