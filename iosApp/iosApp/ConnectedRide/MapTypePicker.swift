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
    
    var body: some View {
        if #available(iOS 17.0, *) {
            Map(position: $position) {
                
                if !routeCoordinates.isEmpty {
                    MapPolyline(coordinates: routeCoordinates)
                        .stroke(.blue, lineWidth: 5)
                }
                
                // Start location annotation
                Annotation("", coordinate: startLocation) {
                    if let image = AppIcon.ConnectedRide.startLocation {
                        Image(uiImage: image)
                            .resizable()
                            .frame(width: 28, height: 28)
                            .clipShape(Circle())
                            .shadow(radius: 3)
                    }
                }
                
                // End location annotation
                Annotation("", coordinate: endLocation) {
                    if let image = AppIcon.ConnectedRide.endLocation {
                        Image(uiImage: image)
                            .resizable()
                            .frame(width: 28, height: 28)
                    }
                }
                
                if startTracking {
                    // Annotations for group riders
                    ForEach(groupRiders, id: \.name) { rider in  // Assuming 'name' is unique; use a unique ID if available
                        Annotation("", coordinate: CLLocationCoordinate2D(latitude: rider.currentLat, longitude: rider.currentLong)) {
                            // Customize the annotation view for riders
                            VStack(spacing: 1) {
                                AppIcon.Profile.profile
                                    .resizable()
                                    .frame(width: 26, height: 26)
                                    .overlay(
                                        Circle()
                                            .stroke(Color.white, lineWidth: 1)
                                    )
                                    .clipShape(Circle())
                                HStack {
                                    Spacer()
                                    let randomImages = [AppIcon.JoinRide.greenPin, AppIcon.JoinRide.yellowPin, AppIcon.JoinRide.orangePin]  // Array of possible SF Symbol names
                                    randomImages.randomElement()?
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
            
            withAnimation {
                let region = MKCoordinateRegion(polyline.boundingMapRect)
                let adjustedRegion = MKCoordinateRegion(
                    center: region.center,
                    span: MKCoordinateSpan(
                        latitudeDelta: region.span.latitudeDelta * 1.3,
                        longitudeDelta: region.span.longitudeDelta * 1.3
                    )
                )
                position = .region(adjustedRegion)
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


