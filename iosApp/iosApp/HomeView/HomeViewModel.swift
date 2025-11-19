//
//  HomeViewModel.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import Foundation
import SwiftUI
import shared

@MainActor
class HomeViewModel: ObservableObject {
    @Published var locationManager = LocationManager()
    @Published var location: String = "Kochi, Infopark"
    private var rideAPIService: RidesApIService
    private var rideRepository: RidesRepository
    private let userRepo = UserRepoImpl()
    @Published var dashboardData: [DashboardDomain] = []
    
    // MARK: - Dashboard
    @Published var stats: [RideStat] = []
    
    // MARK: - Upcoming Rides
    @Published var upcomingRides: [UpcomingRide] = [
        UpcomingRide(hostName: "Sooraj", route: "Kochi - Kanyakumari", date: Calendar.current.date(byAdding: .day, value: 20, to: Date())!, joinedAvatars: ["SR","AL","VM"], joinedCount: 5),
        UpcomingRide(hostName: "Neha", route: "Kochi - Munnar", date: Calendar.current.date(byAdding: .day, value: 35, to: Date())!, joinedAvatars: ["NN","AR"], joinedCount: 3),
        UpcomingRide(hostName: "Aromal", route: "Kochi - Kashmir", date: Calendar.current.date(byAdding: .day, value: 35, to: Date())!, joinedAvatars: ["NN","AR"], joinedCount: 3)
    ]
    
    // MARK: - Donut chart
    @Published var journeySlices: [JourneySlice] = []
    
    func getJourneySlices(for range: String) -> [JourneySlice] {
        switch range {
        case "This month":
            return journeySlices
            
        case "Last month":
            return [
                JourneySlice(category: "Total Rides", value: 18, color: AppColor.strongCyan),
                JourneySlice(category: "Places Explored", value: 6, color: AppColor.purple),
                JourneySlice(category: "Ride Groups", value: 3, color: AppColor.lightCyan),
                JourneySlice(category: "Ride Invites", value: 9, color: AppColor.vividBlue)
            ]
            
        case "Last 4 months":
            return [
                JourneySlice(category: "Total Rides", value: 75, color: AppColor.strongCyan),
                JourneySlice(category: "Places Explored", value: 18, color: AppColor.purple),
                JourneySlice(category: "Ride Groups", value: 9, color: AppColor.lightCyan),
                JourneySlice(category: "Ride Invites", value: 30, color: AppColor.vividBlue)
            ]
            
        case "Last 6 months":
            return [
                JourneySlice(category: "Total Rides", value: 120, color: AppColor.strongCyan),
                JourneySlice(category: "Places Explored", value: 28, color: AppColor.purple),
                JourneySlice(category: "Ride Groups", value: 15, color: AppColor.lightCyan),
                JourneySlice(category: "Ride Invites", value: 55, color: AppColor.vividBlue)
            ]
            
        case "Last year":
            return [
                JourneySlice(category: "Total Rides", value: 240, color: AppColor.strongCyan),
                JourneySlice(category: "Places Explored", value: 60, color: AppColor.purple),
                JourneySlice(category: "Ride Groups", value: 35, color: AppColor.lightCyan),
                JourneySlice(category: "Ride Invites", value: 110, color: AppColor.vividBlue)
            ]
            
        default:
            return journeySlices
        }
    }
    
    
    // MARK: - Bar chart
    @Published var selectedMonth: PlacesMonth? = nil
    
    
    @Published var placesByMonth: [PlacesMonth] = [
        PlacesMonth(month: "Mar", placesCount: 5),
        PlacesMonth(month: "Apr", placesCount: 2),
        PlacesMonth(month: "May", placesCount: 5),
        PlacesMonth(month: "Jun", placesCount: 7),
        PlacesMonth(month: "Jul", placesCount: 4),
        PlacesMonth(month: "Aug", placesCount: 5),
        PlacesMonth(month: "Sept", placesCount: 0)
    ]
    
    struct PlacesMonth: Identifiable, Equatable {
        var id = UUID()
        var month: String
        var placesCount: Int
    }
    
    
    init() {
        rideAPIService = RidesApiServiceImpl(client: KtorClient())
        rideRepository = RidesRepository(apiService: rideAPIService)
        loadUserName()
        locationManager.requestLocation()
        
        // Observe location updates
        Task {
            for await address in locationManager.$currentAddress.values {
                self.location = address
            }
        }
    }
    @MainActor
    func loadUserName() {
        Task {
            do {
                if let currentUser = try await userRepo.getUserDetails() {
                    DispatchQueue.main.async {
                        MBUserDefaults.userNameStatic = currentUser.name!
                    }
                }
            } catch {
                print("Error fetching user: \(error)")
            }
        }
    }
    func getRideSummary(userID: String, range: String) {
        rideRepository.getRideSummary(userID: userID) { result, error in
            if let success = result as? APIResultSuccess<AnyObject>,
               let rideArray = success.data as? [DashboardDomain] {
                Task { @MainActor in
                    self.dashboardData = rideArray
                }
                print("----Rides---\(rideArray)")
            }  else {
                print("error")
            }
        }
    }
    
    @MainActor
    func updateStatsFor(month: Int, year: Int) {
        
        guard let selectedDash = dashboardData.first(where: {
            $0.monthYear.month == month && $0.monthYear.year == year
        }) else {
            
            self.stats = [
                RideStat(title: "Total Rides", value: 0, color: AppColor.vividBlue, icon: AppIcon.Home.createRide),
                RideStat(title: "Locations", value: 0, color: AppColor.skyBlue, icon: AppIcon.Home.location),
                RideStat(title: "Total KMs", value: 0, color: AppColor.strongCyan, icon: AppIcon.Home.nearMe)
            ]
            return
        }
        
        let rides = selectedDash.perMonthData
        
        let totalRides = rides.count
        let totalDistance = Int(rides.reduce(0) { $0 + Int($1.rideDistance ?? 0) })
        let locations = Set(rides.map { $0.endLocation ?? "" }).count
        let organiser = rides.filter { $0.isOrganiserGroupRide == true }.count
        let participant = rides.filter { $0.isParticipantGroupRide == true }.count
        
        
        self.stats = [
            RideStat(title: "Total Rides", value: totalRides, color: AppColor.vividBlue, icon: AppIcon.Home.createRide),
            RideStat(title: "Locations", value: locations, color: AppColor.skyBlue, icon: AppIcon.Home.location),
            RideStat(title: "Total KMs", value: totalDistance, color: AppColor.strongCyan, icon: AppIcon.Home.nearMe)
        ]
        
        self.journeySlices = [
            JourneySlice(category: "Total Rides", value: Double(totalRides), color: AppColor.strongCyan),
            JourneySlice(category: "Places Explored", value: Double(locations), color: AppColor.purple),
            JourneySlice(category: "Ride Groups", value: Double(participant), color: AppColor.lightCyan),
            JourneySlice(category: "Ride Invites", value: Double(organiser), color: AppColor.vividBlue)
        ]
    }
    
    
}
