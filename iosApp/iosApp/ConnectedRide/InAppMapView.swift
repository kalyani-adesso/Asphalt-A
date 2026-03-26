//
//  InAppMapView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 23/02/2026.
//

import SwiftUI
import MapKit

/// Reusable map view used by the in‑app navigation screen. It displays a
/// polyline route, user location, and allows recentering/focusing behavior driven
/// by the parent view.

@available(iOS 17.0, *)
struct InAppMapView: View {
    var routeCoordinates: [CLLocationCoordinate2D]
    var trafficSegments: [ColoredRouteSegment] = []
    var startCoordinate: CLLocationCoordinate2D?
    var endCoordinate: CLLocationCoordinate2D?
    var userCoordinate: CLLocationCoordinate2D?
    /// Other riders participating in the connected ride (for pins on the navigation map).
    var participants: [Rider]
    var followUser: Bool
    var cameraAltitude: Double
    var mapType: MKMapType
    var recenterCounter: Int
    var focusCoordinate: CLLocationCoordinate2D?
    var focusCounter: Int
    /// Increment to force re-fitting camera to route (reliable on first load across devices).
    var fitRouteCounter: Int

    @State private var position: MapCameraPosition = .automatic
    @State private var hasFittedRoute = false

    var body: some View {
        Map(position: $position) {
            if !trafficSegments.isEmpty {
                ForEach(trafficSegments) { segment in
                    if segment.coordinates.count >= 2 {
                        MapPolyline(coordinates: segment.coordinates)
                            .stroke(segment.density.color, lineWidth: 6)
                    }
                }
            } else if !routeCoordinates.isEmpty {
                MapPolyline(coordinates: routeCoordinates)
                    .stroke(AppColor.celticBlue, lineWidth: 6)
            }
            // Start pin
            if let start = startCoordinate {
                Annotation("", coordinate: start) {
                    Group {
                        if let ui = AppIcon.ConnectedRide.startLocation {
                            Image(uiImage: ui)
                                .resizable()
                                .frame(width: 32, height: 32)
                                .clipShape(Circle())
                                .shadow(color: .black.opacity(0.25), radius: 4, x: 0, y: 2)
                        } else {
                            // Asset `icon-startLocation` missing from catalog → UIImage is nil; show fallback until PDF is added to imageset.
                            Image(systemName: "flag.checkered.circle.fill")
                                .symbolRenderingMode(.palette)
                                .foregroundStyle(.white, AppColor.celticBlue)
                                .font(.system(size: 32))
                                .shadow(color: .black.opacity(0.25), radius: 4, x: 0, y: 2)
                        }
                    }
                }
            }
            if let end = endCoordinate {
                Annotation("", coordinate: end) {
                    Group {
                        if let ui = AppIcon.ConnectedRide.endLocation {
                            Image(uiImage: ui)
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
                }
            }
            // Participant pins
            ForEach(participants) { rider in
                Annotation("", coordinate: CLLocationCoordinate2D(latitude: rider.currentLat, longitude: rider.currentLong)) {
                    VStack(spacing: 1) {
                        ProfileImageView(profileImageName: rider.profileImageName, size: CGSize(width: 24, height: 24))
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
                            .frame(width: 18, height: 18)
                        }
                        .frame(width: 28)
                    }
                    .frame(width: 28)
                    .background(.clear)
                    .clipShape(RoundedRectangle(cornerRadius: 6))
                    .shadow(radius: 2)
                }
            }
            // User / navigate location marker (drawn last so it appears on top of polyline)
            if let user = userCoordinate {
                Annotation("", coordinate: user) {
                    ZStack {
                        Circle()
                            .fill(AppColor.celticBlue)
                            .frame(width: 34, height: 34)
                            .overlay(Circle().stroke(Color.white, lineWidth: 2))
                        Image(systemName: "location.north.fill")
                            .font(.system(size: 13, weight: .bold))
                            .foregroundColor(.white)
                    }
                    .shadow(color: .black.opacity(0.35), radius: 4, x: 0, y: 2)
                }
            }
        }
        .mapStyle(style(for: mapType))
        .mapControls {
            MapCompass()
            MapScaleView()
        }
        .onChange(of: routeCoordinates.count) { _, _ in
            if !routeCoordinates.isEmpty && !hasFittedRoute {
                fitMapToRoute()
                hasFittedRoute = true
            }
        }
        .onChange(of: fitRouteCounter) { _, _ in
            guard !routeCoordinates.isEmpty else { return }
            fitMapToRoute()
            hasFittedRoute = true
        }
        .onChange(of: recenterCounter) { _, _ in
            recenter()
        }
        .onChange(of: focusCounter) { _, _ in
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

    private func fitMapToRoute() {
        var allCoords = routeCoordinates
        if let start = startCoordinate { allCoords.append(start) }
        if let end = endCoordinate { allCoords.append(end) }
        if let user = userCoordinate { allCoords.append(user) }
        guard !allCoords.isEmpty else { return }
        let minLat = allCoords.map(\.latitude).min() ?? 0
        let maxLat = allCoords.map(\.latitude).max() ?? 0
        let minLon = allCoords.map(\.longitude).min() ?? 0
        let maxLon = allCoords.map(\.longitude).max() ?? 0
        let center = CLLocationCoordinate2D(
            latitude: (minLat + maxLat) / 2,
            longitude: (minLon + maxLon) / 2
        )
        let span = MKCoordinateSpan(
            latitudeDelta: max((maxLat - minLat) * 1.4, 0.008),
            longitudeDelta: max((maxLon - minLon) * 1.4, 0.008)
        )
        position = .region(MKCoordinateRegion(center: center, span: span))
    }

    private func recenter() {
        if followUser, let user = userCoordinate {
            position = .camera(MapCamera(
                centerCoordinate: user,
                distance: cameraAltitude,
                heading: 0,
                pitch: 0
            ))
        } else if let center = routeCoordinates.first {
            position = .camera(MapCamera(
                centerCoordinate: center,
                distance: cameraAltitude,
                heading: 0,
                pitch: 0
            ))
        }
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

