//
//  MapTypePicker.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 07/11/25.
//

import SwiftUI
import MapKit
import CoreLocation

@available(iOS 17.0, *)
struct BikeRouteMapView: View {
    @Binding var position: MapCameraPosition
    @State private var routeCoordinates: [CLLocationCoordinate2D] = []
    var currentMapStyle: MapStyle
    var rideModel: JoinRideModel
    var groupRiders: [Rider]
    @State var startLocation = CLLocationCoordinate2D(latitude: 0, longitude: 0)
    @State var endLocation = CLLocationCoordinate2D(latitude: 0, longitude: 0)
    @Binding var startTracking: Bool
    /// Called when the route is first fitted so the parent can restore this view on "refresh".
    var onRouteFitted: ((MKCoordinateRegion) -> Void)?
    
    var body: some View {
        if #available(iOS 17.0, *) {
            Map(position: $position) {
                
                if !routeCoordinates.isEmpty {
                    MapPolyline(coordinates: routeCoordinates)
                        .stroke(AppColor.celticBlue, lineWidth: 6)
                }
                
                // Start location annotation
                Annotation("", coordinate: startLocation) {
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
                                Group {
                                    if let imageName = rider.profileImageName, !imageName.isEmpty {
                                        Image(imageName)
                                            .resizable()
                                    } else {
                                        AppIcon.Profile.profile
                                            .resizable()
                                    }
                                }
                                .frame(width: 26, height: 26)
                                .overlay(
                                    Circle()
                                        .stroke(Color.white, lineWidth: 1)
                                )
                                .clipShape(Circle())
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
                startLocation = CLLocationCoordinate2D(latitude: rideModel.startLat, longitude: rideModel.startLong)
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
            routeCoordinates = polyline.coordinates
            
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


