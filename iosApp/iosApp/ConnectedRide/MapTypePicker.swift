//
//  MapTypePicker.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 07/11/25.
//

import SwiftUI
import MapKit
import CoreLocation

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

@available(iOS 17.0, *)
struct BikeRouteMapView: View {
    @Binding var position: MapCameraPosition
    @State private var fullRouteCoordinates: [CLLocationCoordinate2D] = []
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
            .onAppear {
                // When ride has assembly point, draw route assembly → end; otherwise start → end.
                if rideModel.hasAssemblyPoint, let aLat = rideModel.assemblyLat, let aLon = rideModel.assemblyLon {
                    startLocation = CLLocationCoordinate2D(latitude: aLat, longitude: aLon)
                } else {
                    startLocation = CLLocationCoordinate2D(latitude: rideModel.startLat, longitude: rideModel.startLong)
                }
                endLocation = CLLocationCoordinate2D(latitude: rideModel.endLat, longitude: rideModel.endLong)
                fetchBikeRoute()
            }
        }
    }
    
    func fetchBikeRoute() {
        let request = MKDirections.Request()
        request.source = MKMapItem(placemark: MKPlacemark(coordinate: startLocation))
        request.destination = MKMapItem(placemark: MKPlacemark(coordinate: endLocation))
        request.transportType = .automobile
        
        let directions = MKDirections(request: request)
        directions.calculate { response, error in
            guard let route = response?.routes.first else {
                print(" No route found: \(error?.localizedDescription ?? "Unknown error")")
                return
            }
            let polyline = route.polyline
            fullRouteCoordinates = polyline.coordinates
            
            let region = MKCoordinateRegion(polyline.boundingMapRect)
            let adjustedRegion = MKCoordinateRegion(
                center: region.center,
                span: MKCoordinateSpan(
                    latitudeDelta: region.span.latitudeDelta * 1.3,
                    longitudeDelta: region.span.longitudeDelta * 1.3
                )
            )
            DispatchQueue.main.async {
                withAnimation {
                    position = .region(adjustedRegion)
                }
                onRouteFitted?(adjustedRegion)
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


