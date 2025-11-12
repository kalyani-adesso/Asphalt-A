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
    @Binding  var position: MapCameraPosition
    @State private var routeCoordinates: [CLLocationCoordinate2D] = []
     var currentMapStyle:MapStyle
    var rideModel: JoinRideModel
    @State var startLocation = CLLocationCoordinate2D(latitude: 0, longitude: 0)
    @State var endLocation = CLLocationCoordinate2D(latitude: 0, longitude: 0)
   
    var body: some View {
        if #available(iOS 17.0, *) {
            Map(position: $position) {
              
                if !routeCoordinates.isEmpty {
                    MapPolyline(coordinates: routeCoordinates)
                        .stroke(.blue, lineWidth: 5)
                }
                Annotation("", coordinate: startLocation) {
                    if let image = AppIcon.ConnectedRide.startLocation {
                        Image(uiImage: image)
                            .resizable()
                            .frame(width: 40, height: 40)
                            .clipShape(Circle())
                            .shadow(radius: 3)
                    }
                }

                Annotation("", coordinate: endLocation) {
                    if let image = AppIcon.ConnectedRide.endLocation {
                        Image(uiImage: image)
                            .resizable()
                            .frame(width: 40, height: 40)
                            .clipShape(Circle())
                            .shadow(radius: 3)
                    }
                }
            }
            .mapStyle(currentMapStyle)
            .onAppear {
                startLocation = CLLocationCoordinate2D(latitude: rideModel.startLat, longitude: rideModel.startLong)
                endLocation = CLLocationCoordinate2D(latitude: rideModel.endLat, longitude:rideModel.endLong)
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


