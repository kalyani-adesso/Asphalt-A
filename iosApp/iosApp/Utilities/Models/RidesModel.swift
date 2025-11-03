//
//  RidesModel.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import Foundation


struct Ride: Identifiable {
    let id = UUID()
    var type: RideType? = nil
    var title: String = ""
    var description: String = ""
    var date: Date?
    var time: Date?
    var startLocation: String = ""
    var endLocation: String = ""
    var startLat: Double?
    var startLng: Double?
    var endLat: Double?
    var endLng: Double?
}

enum RideType: String, CaseIterable, Identifiable {
    case none = "Select ride type"
    case solo = "Solo Ride"
    case group = "Group Ride"
    case open = "Open Event"

    var id: String { rawValue }
}

struct Participant: Identifiable {
    let id : String
    let name: String
    let role: String?
    let bike: String
    let image: String
    let isOnline: Bool
    let userId: String
}


