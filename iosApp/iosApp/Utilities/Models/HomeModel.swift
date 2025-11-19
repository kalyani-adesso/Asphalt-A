//
//  HomeViewModel.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import Foundation
import SwiftUI

struct RideStat: Identifiable {
    let id = UUID()
    let title: String
    let value: Int
    let color: Color
    let icon: Image
}

struct UpcomingRide: Identifiable {
    let id = UUID()
    let hostName: String
    let route: String
    let date: Date
    let joinedAvatars: [String]
    let joinedCount: Int
}

struct JourneySlice: Identifiable {
    let id = UUID()
    let category: String
    let value: Double
    let color: Color
}

