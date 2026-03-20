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

// MARK: - Coordinate sanity (pickup / drop / assembly)

/// `true` when lat/lon are in range and not the common "missing data" default **(0, 0)** (Gulf of Guinea).
/// KMP `RidesData` defaults missing Firebase fields to `0.0`; routing from (0,0) yields Atlantic pin + no usable polyline.
func isPlausibleRoutingCoordinate(latitude: Double, longitude: Double) -> Bool {
    if abs(latitude) < 1e-6 && abs(longitude) < 1e-6 { return false }
    return (-90.0...90.0).contains(latitude) && (-180.0...180.0).contains(longitude)
}

/// Route line on the connected-ride map: assembly → end when assembly is valid; otherwise ride start → end.
func resolvedRideRouteEndpoints(for ride: JoinRideModel) -> (start: CLLocationCoordinate2D, end: CLLocationCoordinate2D) {
    let end = CLLocationCoordinate2D(latitude: ride.endLat, longitude: ride.endLong)
    let start: CLLocationCoordinate2D
    if ride.hasAssemblyPoint,
       let aLat = ride.assemblyLat,
       let aLon = ride.assemblyLon,
       isPlausibleRoutingCoordinate(latitude: aLat, longitude: aLon) {
        start = CLLocationCoordinate2D(latitude: aLat, longitude: aLon)
    } else {
        start = CLLocationCoordinate2D(latitude: ride.startLat, longitude: ride.startLong)
    }
    return (start, end)
}

// MARK: - Route trimming (polyline shortens as rider moves, like Apple/Google Maps)
func trimRouteFromUserPosition(_ route: [CLLocationCoordinate2D], user: CLLocationCoordinate2D) -> [CLLocationCoordinate2D] {
    guard route.count >= 2 else { return route }
    var bestDistSq = Double.infinity
    var bestPoint = route[0]
    var bestEndIndex = 0
    for i in 0..<(route.count - 1) {
        let a = route[i]
        let b = route[i + 1]
        let closest = closestPointOnSegment(point: user, segmentStart: a, segmentEnd: b)
        let dLat = closest.latitude - user.latitude
        let dLon = closest.longitude - user.longitude
        let distSq = dLat * dLat + dLon * dLon
        if distSq < bestDistSq {
            bestDistSq = distSq
            bestPoint = closest
            bestEndIndex = i + 1
        }
    }
    if bestEndIndex >= route.count { return [route.last!] }
    var result = [bestPoint]
    result.append(contentsOf: route[bestEndIndex...])
    return result
}

func closestPointOnSegment(point: CLLocationCoordinate2D, segmentStart: CLLocationCoordinate2D, segmentEnd: CLLocationCoordinate2D) -> CLLocationCoordinate2D {
    let dx = segmentEnd.longitude - segmentStart.longitude
    let dy = segmentEnd.latitude - segmentStart.latitude
    let lenSq = dx * dx + dy * dy
    if lenSq == 0 { return segmentStart }
    var t = ((point.longitude - segmentStart.longitude) * dx + (point.latitude - segmentStart.latitude) * dy) / lenSq
    t = max(0, min(1, t))
    return CLLocationCoordinate2D(
        latitude: segmentStart.latitude + t * dy,
        longitude: segmentStart.longitude + t * dx
    )
}

// MARK: - Off-route detection (rerouting)

/// Shortest distance in meters from `user` to the polyline (piecewise linear segments).
func crossTrackDistanceMeters(from user: CLLocationCoordinate2D, along route: [CLLocationCoordinate2D]) -> Double {
    guard route.count >= 2 else { return 0 }
    let userLoc = CLLocation(latitude: user.latitude, longitude: user.longitude)
    var best = Double.greatestFiniteMagnitude
    for i in 0..<(route.count - 1) {
        let c = closestPointOnSegment(point: user, segmentStart: route[i], segmentEnd: route[i + 1])
        let d = userLoc.distance(from: CLLocation(latitude: c.latitude, longitude: c.longitude))
        best = min(best, d)
    }
    return best
}

// MARK: - User-facing route errors

/// Short, friendly copy for MKDirections failures (keeps raw system errors out of the UI).
func friendlyDirectionsErrorMessage(error: Error?, isReroute: Bool) -> String {
    if let mk = error as? MKError {
        switch mk.code {
        case .directionsNotFound:
            return isReroute
                ? "Couldn't find a new route from here. Your last line stays on the map—try Apple or Google Maps if needed."
                : "Driving directions aren't available for this trip. Use Open in Apple Maps or Google Maps from the menu."
        @unknown default:
            break
        }
    }
    let lower = (error?.localizedDescription ?? "").lowercased()
    if lower.contains("directions not available") {
        return isReroute
            ? "Directions aren't available for an updated path. Keep following the last route or open Apple Maps."
            : "Directions aren't available between these points. Try another map app or check the ride locations."
    }
    if lower.contains("network") || lower.contains("internet") || lower.contains("connection") {
        return "Check your internet connection and try again."
    }
    return isReroute
        ? "Couldn't update the route. The previous line is still shown if we had one."
        : "We couldn't draw a route. Check start and end locations, or open directions in another app."
}

/// Shared thresholds for connected-ride map + in-app navigation rerouting.
enum ConnectedRideReroutePolicy {
    /// If the rider is farther than this from the current polyline, consider a reroute.
    static let offRouteMeters: Double = 85
    /// Avoid hammering MKDirections.
    static let minSecondsBetweenReroutes: TimeInterval = 18
    /// How often we evaluate off-route while tracking (connected ride map).
    static let bikeMapCheckSeconds: TimeInterval = 5
    /// Throttle off-route checks from GPS callbacks (in-app navigation).
    static let navMinSecondsBetweenChecks: TimeInterval = 4
}

@available(iOS 17.0, *)
struct BikeRouteMapView: View {
    @Binding var position: MapCameraPosition
    @State private var fullRouteCoordinates: [CLLocationCoordinate2D] = []
    @State private var isReroutingRoute: Bool = false
    @State private var lastBikeRerouteAt: Date?
    @State private var routeBannerMessage: String?
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

    /// Arrow (start) icon position: moves to rider's current location when tracking; otherwise fixed at route start.
    private var startAnnotationCoordinate: CLLocationCoordinate2D {
        if startTracking, let user = userLocation {
            return user
        }
        return startLocation
    }

    var body: some View {
        if #available(iOS 17.0, *) {
            Map(position: $position) {
                
                if !displayedRouteCoordinates.isEmpty {
                    MapPolyline(coordinates: displayedRouteCoordinates)
                        .stroke(AppColor.celticBlue, lineWidth: 6)
                }
                
                // Start / rider position annotation (arrow moves with rider when tracking)
                Annotation("", coordinate: startAnnotationCoordinate) {
                    if let image = AppIcon.ConnectedRide.startLocation {
                        Image(uiImage: image)
                            .resizable()
                            .frame(width: 32, height: 32)
                            .clipShape(Circle())
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
            .overlay(alignment: .bottom) {
                if let msg = routeBannerMessage {
                    HStack(alignment: .top, spacing: 10) {
                        Image(systemName: "exclamationmark.triangle.fill")
                            .foregroundStyle(.yellow)
                            .font(.system(size: 16))
                        Text(msg)
                            .font(KlavikaFont.regular.font(size: 12))
                            .foregroundColor(AppColor.black)
                            .fixedSize(horizontal: false, vertical: true)
                        Spacer(minLength: 0)
                        Button {
                            routeBannerMessage = nil
                        } label: {
                            Image(systemName: "xmark.circle.fill")
                                .font(.system(size: 18))
                                .foregroundStyle(.secondary)
                        }
                        .buttonStyle(.plain)
                    }
                    .padding(12)
                    .background(.ultraThinMaterial)
                    .cornerRadius(12)
                    .shadow(color: Color.black.opacity(0.12), radius: 5, x: 0, y: 2)
                    .padding(.horizontal, 10)
                    .padding(.bottom, 10)
                    .transition(.move(edge: .bottom).combined(with: .opacity))
                }
            }
            .animation(.easeInOut(duration: 0.2), value: routeBannerMessage)
            .onAppear {
                let endpoints = resolvedRideRouteEndpoints(for: rideModel)
                startLocation = endpoints.start
                endLocation = endpoints.end
                #if DEBUG
                print("""
                [BikeRouteMap] route endpoints — start: \(startLocation.latitude), \(startLocation.longitude) \
                end: \(endLocation.latitude), \(endLocation.longitude) \
                hasAssembly=\(rideModel.hasAssemblyPoint) assembly=(\(rideModel.assemblyLat.map { String($0) } ?? "nil"),\(rideModel.assemblyLon.map { String($0) } ?? "nil")) \
                rideStart=(\(rideModel.startLat),\(rideModel.startLong))
                """)
                #endif
                fetchBikeRoute(source: nil, fitCamera: true)
            }
            .onReceive(Timer.publish(every: ConnectedRideReroutePolicy.bikeMapCheckSeconds, on: .main, in: .common).autoconnect()) { _ in
                evaluateBikeMapReroute()
            }
        }
    }

    private func showRouteBanner(_ message: String, autoDismissAfter seconds: TimeInterval = 10) {
        routeBannerMessage = message
        DispatchQueue.main.asyncAfter(deadline: .now() + seconds) {
            routeBannerMessage = nil
        }
    }

    /// When tracking, periodically checks cross-track distance; if off route, requests new directions from GPS → destination.
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
        guard isPlausibleRoutingCoordinate(latitude: user.latitude, longitude: user.longitude) else { return }
        lastBikeRerouteAt = now
        #if DEBUG
        print("[BikeRouteMap] rerouting — deviation \(Int(deviation)) m from polyline")
        #endif
        fetchBikeRoute(source: user, fitCamera: false)
    }

    /// - Parameters:
    ///   - source: Pass user coordinate to reroute from current GPS; `nil` uses planned ride start (assembly or start).
    ///   - fitCamera: `false` on reroute to avoid jumping the map while riding.
    func fetchBikeRoute(source: CLLocationCoordinate2D?, fitCamera: Bool) {
        let sourceCoord = source ?? startLocation
        guard isPlausibleRoutingCoordinate(latitude: sourceCoord.latitude, longitude: sourceCoord.longitude),
              isPlausibleRoutingCoordinate(latitude: endLocation.latitude, longitude: endLocation.longitude) else {
            print("""
            [BikeRouteMap] skipping MKDirections — invalid start/end. \
            start=(\(sourceCoord.latitude),\(sourceCoord.longitude)) \
            end=(\(endLocation.latitude),\(endLocation.longitude))
            """)
            DispatchQueue.main.async {
                showRouteBanner("Start or end location looks invalid, so the route line can't be drawn.")
            }
            return
        }
        let isReroute = source != nil
        if isReroute {
            isReroutingRoute = true
        }
        let request = MKDirections.Request()
        request.source = MKMapItem(placemark: MKPlacemark(coordinate: sourceCoord))
        request.destination = MKMapItem(placemark: MKPlacemark(coordinate: endLocation))
        request.transportType = .automobile

        let directions = MKDirections(request: request)
        directions.calculate { response, error in
            defer {
                if isReroute {
                    DispatchQueue.main.async {
                        isReroutingRoute = false
                    }
                }
            }
            guard let route = response?.routes.first else {
                print("[BikeRouteMap] No route found: \(error?.localizedDescription ?? "Unknown error") | start=(\(sourceCoord.latitude),\(sourceCoord.longitude)) end=(\(endLocation.latitude),\(endLocation.longitude))")
                DispatchQueue.main.async {
                    showRouteBanner(friendlyDirectionsErrorMessage(error: error, isReroute: isReroute))
                }
                return
            }
            let polyline = route.polyline
            let coords = polyline.coordinates
            let region = MKCoordinateRegion(polyline.boundingMapRect)
            let adjustedRegion = MKCoordinateRegion(
                center: region.center,
                span: MKCoordinateSpan(
                    latitudeDelta: region.span.latitudeDelta * 1.3,
                    longitudeDelta: region.span.longitudeDelta * 1.3
                )
            )
            DispatchQueue.main.async {
                routeBannerMessage = nil
                fullRouteCoordinates = coords
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

extension MKPolyline {
    var coordinates: [CLLocationCoordinate2D] {
        var coords = [CLLocationCoordinate2D](repeating: kCLLocationCoordinate2DInvalid, count: pointCount)
        getCoordinates(&coords, range: NSRange(location: 0, length: pointCount))
        return coords
    }
}


