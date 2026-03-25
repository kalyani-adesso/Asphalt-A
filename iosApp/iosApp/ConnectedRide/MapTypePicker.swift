//
//  MapTypePicker.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 07/11/25.
//

import SwiftUI
import MapKit
import CoreLocation
import Combine

@available(iOS 17.0, *)
struct BikeRouteMapView: View {
    @Binding var position: MapCameraPosition
    @State private var fullRouteCoordinates: [CLLocationCoordinate2D] = []
    @State private var fullRouteSegments: [ColoredRouteSegment] = []
    @State private var isReroutingRoute: Bool = false
    @State private var lastBikeRerouteAt: Date?
    var currentMapStyle: MapStyle
    var rideModel: JoinRideModel
    var groupRiders: [Rider]
    @State var startLocation = CLLocationCoordinate2D(latitude: 0, longitude: 0)
    @State var endLocation = CLLocationCoordinate2D(latitude: 0, longitude: 0)
    @Binding var startTracking: Bool
    /// When non-nil and tracking, polyline shows only from user position to end (shortens as rider moves).
    var userLocation: CLLocationCoordinate2D?
    /// Called when the route is first fitted so the parent can restore this view on "refresh".
    var onRouteFitted: ((MKCoordinateRegion) -> Void)?
    
    /// Displayed route: trimmed from user to end when tracking with location; otherwise full route.
    private var displayedRouteCoordinates: [CLLocationCoordinate2D] {
        if startTracking, let user = userLocation, !fullRouteCoordinates.isEmpty {
            return trimRouteFromUserPosition(fullRouteCoordinates, user: user)
        }
        return fullRouteCoordinates
    }

    /// Segments currently shown on map with traffic colors.
    private var displayedTrafficSegments: [ColoredRouteSegment] {
        guard !fullRouteSegments.isEmpty else { return [] }
        if startTracking, let user = userLocation, !fullRouteCoordinates.isEmpty {
            let trimmed = trimRouteFromUserPosition(fullRouteCoordinates, user: user)
            if trimmed.count >= 2 {
                let perStep = trimColoredSegmentsFromUser(
                    fullRoute: fullRouteCoordinates,
                    segments: fullRouteSegments,
                    user: user
                )
                if !perStep.isEmpty { return perStep }
                let density = predominantDensity(fullRouteSegments)
                return [ColoredRouteSegment(coordinates: trimmed, density: density)]
            }
        }
        return fullRouteSegments
    }

    /// Arrow (start) icon position: moves to rider's current location when tracking; otherwise fixed at route start.
    private var startAnnotationCoordinate: CLLocationCoordinate2D {
        if startTracking, let user = userLocation {
            return user
        }
        return startLocation
    }

    var body: some View {
        Map(position: $position) {
                
                ForEach(displayedTrafficSegments) { segment in
                    if segment.coordinates.count >= 2 {
                        MapPolyline(coordinates: segment.coordinates)
                            .stroke(segment.density.color, lineWidth: 6)
                    }
                }
                
                // Start / rider position annotation (arrow moves with rider when tracking)
                Annotation("", coordinate: startAnnotationCoordinate) {
                    if startTracking {
                        Image(systemName: "location.north.fill")
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(.white)
                            .frame(width: 30, height: 30)
                            .background(AppColor.celticBlue)
                            .clipShape(Circle())
                            .overlay(Circle().stroke(Color.white, lineWidth: 2))
                            .shadow(color: .black.opacity(0.25), radius: 4, x: 0, y: 2)
                    } else if let image = AppIcon.ConnectedRide.startLocation {
                        Image(uiImage: image)
                            .resizable()
                            .frame(width: 32, height: 32)
                            .clipShape(Circle())
                            .shadow(color: .black.opacity(0.25), radius: 4, x: 0, y: 2)
                    } else {
                        Image(systemName: "flag.checkered.circle.fill")
                            .symbolRenderingMode(.palette)
                            .foregroundStyle(.white, AppColor.celticBlue)
                            .font(.system(size: 32))
                            .shadow(color: .black.opacity(0.25), radius: 4, x: 0, y: 2)
                    }
                }
                
                // End location annotation
                Annotation("", coordinate: endLocation) {
                    if let image = AppIcon.ConnectedRide.endLocation {
                        Image(uiImage: image)
                            .resizable()
                            .frame(width: 32, height: 32)
                            .shadow(color: .black.opacity(0.25), radius: 4, x: 0, y: 2)
                    } else {
                        Image(systemName: "mappin.circle.fill")
                            .symbolRenderingMode(.palette)
                            .foregroundStyle(.white, AppColor.red)
                            .font(.system(size: 32))
                            .shadow(color: .black.opacity(0.25), radius: 4, x: 0, y: 2)
                    }
                }
                
                if startTracking {
                    // Annotations for group riders — avatar + status-colored pin
                    ForEach(groupRiders, id: \.name) { rider in
                        Annotation("", coordinate: CLLocationCoordinate2D(latitude: rider.currentLat, longitude: rider.currentLong)) {
                            VStack(spacing: 1) {
                                ProfileImageView(profileImageName: rider.profileImageName, size: CGSize(width: 26, height: 26))
                                    .overlay(
                                        Circle()
                                            .stroke(Color.white, lineWidth: 1)
                                    )
                                HStack {
                                    Spacer()
                                    (rider.status == .connected ? AppIcon.JoinRide.greenPin
                                        : rider.status == .delayed ? AppIcon.JoinRide.yellowPin
                                        : AppIcon.JoinRide.orangePin)
                                    .resizable()
                                    .frame(width: 20, height: 20)
                                }
                                .frame(width: 32)
                            }
                            .frame(width: 32)
                            .background(.clear)
                            .clipShape(RoundedRectangle(cornerRadius: 8))
                            .shadow(radius: 2)
                        }
                    }
                }
            }
            .mapStyle(currentMapStyle)
            .onAppear {
                // When ride has assembly point, draw route assembly → end; otherwise start → end.
                if rideModel.hasAssemblyPoint, let aLat = rideModel.assemblyLat, let aLon = rideModel.assemblyLon {
                    startLocation = CLLocationCoordinate2D(latitude: aLat, longitude: aLon)
                } else {
                    startLocation = CLLocationCoordinate2D(latitude: rideModel.startLat, longitude: rideModel.startLong)
                }
                endLocation = CLLocationCoordinate2D(latitude: rideModel.endLat, longitude: rideModel.endLong)
                fetchBikeRoute(source: nil, fitCamera: true)
            }
            .onReceive(Timer.publish(every: ConnectedRideReroutePolicy.bikeMapCheckSeconds, on: .main, in: .common).autoconnect()) { _ in
                evaluateBikeMapReroute()
            }
    }

    private func predominantDensity(_ segments: [ColoredRouteSegment]) -> TrafficDensity {
        let counts = Dictionary(grouping: segments, by: \.density).mapValues(\.count)
        return counts.max(by: { $0.value < $1.value })?.key ?? .unknown
    }

    private func evaluateBikeMapReroute() {
        guard startTracking,
              let user = userLocation,
              !fullRouteCoordinates.isEmpty,
              !isReroutingRoute else { return }
        let deviation = crossTrackDistanceMeters(from: user, along: fullRouteCoordinates)
        guard deviation > ConnectedRideReroutePolicy.offRouteMeters else { return }
        let now = Date()
        if let last = lastBikeRerouteAt,
           now.timeIntervalSince(last) < ConnectedRideReroutePolicy.minSecondsBetweenReroutes { return }
        lastBikeRerouteAt = now
        fetchBikeRoute(source: user, fitCamera: false)
    }

    func fetchBikeRoute(source: CLLocationCoordinate2D?, fitCamera: Bool) {
        let sourceCoord = source ?? startLocation
        let request = MKDirections.Request()
        request.source = MKMapItem(placemark: MKPlacemark(coordinate: sourceCoord))
        request.destination = MKMapItem(placemark: MKPlacemark(coordinate: endLocation))
        request.transportType = .automobile
        let isReroute = source != nil
        if isReroute { isReroutingRoute = true }
        let directions = MKDirections(request: request)
        directions.calculate { response, error in
            defer {
                if isReroute {
                    DispatchQueue.main.async { isReroutingRoute = false }
                }
            }
            guard let route = response?.routes.first else {
                print(" No route found: \(error?.localizedDescription ?? "Unknown error")")
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
            
            let region = MKCoordinateRegion(route.polyline.boundingMapRect)
            let adjustedRegion = MKCoordinateRegion(
                center: region.center,
                span: MKCoordinateSpan(
                    latitudeDelta: region.span.latitudeDelta * 1.3,
                    longitudeDelta: region.span.longitudeDelta * 1.3
                )
            )
            DispatchQueue.main.async {
                fullRouteCoordinates = polylineCoords
                fullRouteSegments = stepSegments.isEmpty
                    ? [ColoredRouteSegment(coordinates: polylineCoords, density: .unknown)]
                    : stepSegments
                if fitCamera {
                    withAnimation {
                        position = .region(adjustedRegion)
                    }
                    onRouteFitted?(adjustedRegion)
                }
            }
        }
    }
}
