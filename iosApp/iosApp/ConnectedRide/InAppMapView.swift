//
//  InAppMapView.swift
//  iosApp
//
//  Created by GitHub Copilot on 23/02/2026.
//

import SwiftUI
import MapKit

/// Reusable map view used by the in‑app navigation screen. It displays a
/// polyline route and allows simple recentering/focusing behavior driven
/// by the parent view.

@available(iOS 17.0, *)
struct InAppMapView: View {
    var routeCoordinates: [CLLocationCoordinate2D]
    var startCoordinate: CLLocationCoordinate2D?
    var endCoordinate: CLLocationCoordinate2D?
    var followUser: Bool
    var cameraAltitude: Double
    var mapType: MKMapType
    var recenterCounter: Int
    var focusCoordinate: CLLocationCoordinate2D?
    var focusCounter: Int

    @State private var position: MapCameraPosition = .automatic

    var body: some View {
        Map(position: $position) {
            if !routeCoordinates.isEmpty {
                MapPolyline(coordinates: routeCoordinates)
                    .stroke(Color.blue, lineWidth: 5)
            }
            // start / end pins
            if let start = startCoordinate {
                Annotation("", coordinate: start) {
                    if let ui = AppIcon.ConnectedRide.startLocation {
                        Image(uiImage: ui)
                            .resizable()
                            .frame(width: 28, height: 28)
                            .clipShape(Circle())
                            .shadow(radius: 3)
                    }
                }
            }
            if let end = endCoordinate {
                Annotation("", coordinate: end) {
                    if let ui = AppIcon.ConnectedRide.endLocation {
                        Image(uiImage: ui)
                            .resizable()
                            .frame(width: 28, height: 28)
                    }
                }
            }
        }
        .mapStyle(style(for: mapType))
        .onChange(of: recenterCounter) { _ in
            recenter()
        }
        .onChange(of: focusCounter) { _ in
            if let coord = focusCoordinate {
                position = .camera(MapCamera(
                    centerCoordinate: coord,
                    distance: cameraAltitude,
                    heading: 0,
                    pitch: 0
                ))
            }
        }
    }

    private func recenter() {
        guard followUser, let center = routeCoordinates.first else { return }
        position = .camera(MapCamera(
            centerCoordinate: center,
            distance: cameraAltitude,
            heading: 0,
            pitch: 0
        ))
    }

    private func style(for mkType: MKMapType) -> MapStyle {
        switch mkType {
        case .standard:   return .standard
        case .hybrid:     return .hybrid
        case .satellite:  return .imagery
        default:          return .standard
        }
    }
}

#if DEBUG
@available(iOS 17.0, *)
struct InAppMapView_Previews: PreviewProvider {
    static var previews: some View {
        InAppMapView(routeCoordinates: [
            CLLocationCoordinate2D(latitude: 37.7749, longitude: -122.4194),
            CLLocationCoordinate2D(latitude: 37.7849, longitude: -122.4094)
        ], followUser: true, cameraAltitude: 200, mapType: .standard, recenterCounter: 0, focusCoordinate: nil, focusCounter: 0)
    }
}
#endif
