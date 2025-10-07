//
//  HomeViewModel.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import Foundation
import SwiftUI

class HomeViewModel: ObservableObject {
    // Header / Greeting data
    @Published var userName: String = "Aromal"
    @Published var location: String = "Kochi, Infopark"

    // Stats
    @Published var stats: [RideStat] = [
        RideStat(title: "Total Rides", value: 25, color: AppColor.totalrides, icon: AppIcon.Home.createRide),
        RideStat(title: "Locations", value: 12, color: AppColor.location, icon: AppIcon.Home.location),
        RideStat(title: "Total KMs", value: 1850, color: AppColor.totalKms, icon: AppIcon.Home.nearMe)
    ]

    // Upcoming rides
    @Published var upcomingRides: [UpcomingRide] = [
        UpcomingRide(hostName: "Sooraj", route: "Kochi - Kanyakumari", date: Calendar.current.date(byAdding: .day, value: 20, to: Date())!, joinedAvatars: ["SR","AL","VM"], joinedCount: 5),
        UpcomingRide(hostName: "Neha", route: "Kochi - Munnar", date: Calendar.current.date(byAdding: .day, value: 35, to: Date())!, joinedAvatars: ["NN","AR"], joinedCount: 3)
    ]

    // Journey pie/donut
    @Published var journeySlices: [JourneySlice] = [
        JourneySlice(category: "Total Rides", value: 25, color: Color("SliceBlue")),
        JourneySlice(category: "Distance", value: 40, color: Color("SliceTeal")),
        JourneySlice(category: "Places Explored", value: 15, color: Color("SlicePurple")),
        JourneySlice(category: "Ride Groups", value: 10, color: Color("SliceLightBlue")),
        JourneySlice(category: "Ride Invites", value: 10, color: Color("SliceDeepBlue"))
    ]

    // Places visited bar data (last 6 months)
    @Published var placesByMonth: [PlacesMonth] = [
        PlacesMonth(month: "Mar", placesCount: 5),
        PlacesMonth(month: "Apr", placesCount: 2),
        PlacesMonth(month: "May", placesCount: 5),
        PlacesMonth(month: "Jun", placesCount: 7),
        PlacesMonth(month: "Jul", placesCount: 4),
        PlacesMonth(month: "Aug", placesCount: 5)
    ]

    init() {
        // can fetch remote data or simulate delay here
    }
}
