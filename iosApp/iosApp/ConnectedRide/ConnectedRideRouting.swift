//
//  ConnectedRideRouting.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 24/03/26.
//

import SwiftUI
import MapKit
import CoreLocation

enum TrafficDensity {
    case low
    case medium
    case high
    case unknown

    var color: Color {
        switch self {
        case .low: return AppColor.green
        case .medium: return AppColor.lightOrange
        case .high: return AppColor.red
        case .unknown: return AppColor.stoneGray
        }
    }
}

struct ColoredRouteSegment: Identifiable {
    let id = UUID()
    let coordinates: [CLLocationCoordinate2D]
    let density: TrafficDensity
}

enum ConnectedRideReroutePolicy {
    /// Cross-track distance above this triggers reroute. Lower = more sensitive (10 m is aggressive; GPS jitter is often ~5–20 m).
    static let offRouteMeters: Double = 10
    static let minSecondsBetweenReroutes: TimeInterval = 18
    static let bikeMapCheckSeconds: TimeInterval = 5
    static let navMinSecondsBetweenChecks: TimeInterval = 4
}

/// Closest point on the polyline to `user`, and the index of the first vertex after that point.
func trimRouteMetadata(_ route: [CLLocationCoordinate2D], user: CLLocationCoordinate2D) -> (bestPoint: CLLocationCoordinate2D, firstVertexIndex: Int)? {
    guard route.count >= 2 else { return nil }
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
    if bestEndIndex >= route.count { return nil }
    return (bestPoint, bestEndIndex)
}

func trimRouteFromUserPosition(_ route: [CLLocationCoordinate2D], user: CLLocationCoordinate2D) -> [CLLocationCoordinate2D] {
    guard route.count >= 2 else { return route }
    guard let meta = trimRouteMetadata(route, user: user) else {
        return [route.last!]
    }
    var result = [meta.bestPoint]
    result.append(contentsOf: route[meta.firstVertexIndex...])
    return result
}

func coordinatesNearlyEqual(_ a: CLLocationCoordinate2D, _ b: CLLocationCoordinate2D) -> Bool {
    abs(a.latitude - b.latitude) < 1e-5 && abs(a.longitude - b.longitude) < 1e-5
}

/// While tracking, trim each colored segment so traffic colors stay aligned with the remaining path.
func trimColoredSegmentsFromUser(
    fullRoute: [CLLocationCoordinate2D],
    segments: [ColoredRouteSegment],
    user: CLLocationCoordinate2D
) -> [ColoredRouteSegment] {
    guard let meta = trimRouteMetadata(fullRoute, user: user), !segments.isEmpty else { return [] }
    var cursor = 0
    var out: [ColoredRouteSegment] = []
    var started = false
    for seg in segments {
        let c = seg.coordinates
        guard c.count >= 2 else { continue }
        guard cursor < fullRoute.count, coordinatesNearlyEqual(fullRoute[cursor], c[0]) else {
            return []
        }
        let segStartIndex = cursor
        let segEndIndex = cursor + c.count - 1
        if !started {
            if meta.firstVertexIndex <= segEndIndex {
                started = true
                let k = meta.firstVertexIndex - segStartIndex
                guard k >= 0, k < c.count else { return [] }
                let newCoords = [meta.bestPoint] + Array(c[k...])
                guard newCoords.count >= 2 else { return [] }
                out.append(ColoredRouteSegment(coordinates: newCoords, density: seg.density))
            }
        } else {
            out.append(seg)
        }
        cursor += c.count - 1
    }
    guard cursor == fullRoute.count - 1 else { return [] }
    return out
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

func crossTrackDistanceMeters(from user: CLLocationCoordinate2D, along route: [CLLocationCoordinate2D]) -> Double {
    guard route.count >= 2 else { return 0 }
    let userLoc = CLLocation(latitude: user.latitude, longitude: user.longitude)
    var best = Double.greatestFiniteMagnitude
    for i in 0..<(route.count - 1) {
        let closest = closestPointOnSegment(point: user, segmentStart: route[i], segmentEnd: route[i + 1])
        let d = userLoc.distance(from: CLLocation(latitude: closest.latitude, longitude: closest.longitude))
        best = min(best, d)
    }
    return best
}

func densityFor(stepDistanceMeters: CLLocationDistance, expectedTravelTime: TimeInterval) -> TrafficDensity {
    guard expectedTravelTime > 0 else { return .unknown }
    let speedKph = (stepDistanceMeters / expectedTravelTime) * 3.6
    if speedKph >= 40 { return .low }
    if speedKph >= 20 { return .medium }
    return .high
}

/// `MKRoute.Step` has no per-step ETA; split the route's `expectedTravelTime` by distance.
func estimatedTravelTimeForStep(_ step: MKRoute.Step, route: MKRoute) -> TimeInterval {
    guard route.distance > 0 else { return 0 }
    return route.expectedTravelTime * (step.distance / route.distance)
}

extension MKPolyline {
    var coordinates: [CLLocationCoordinate2D] {
        var coords = [CLLocationCoordinate2D](repeating: kCLLocationCoordinate2DInvalid, count: pointCount)
        getCoordinates(&coords, range: NSRange(location: 0, length: pointCount))
        return coords
    }
}
