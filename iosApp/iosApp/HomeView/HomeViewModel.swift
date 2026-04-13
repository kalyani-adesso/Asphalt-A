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
    @Published  var currentSlices: [JourneySlice] = []
    @Published var journeySlices: [JourneySlice] = []
    @Published var stats: [RideStat] = []
    @Published var userName: String = ""
    @Published var userCreatedDate: Date? = nil
    /// True while ride summary / dashboard API is in flight (journey donut, stats, places chart).
    @Published var isRideSummaryLoading = true

    // MARK: - Bar chart
    @Published var selectedMonth: PlacesMonth? = nil
    @Published var placesByMonth: [PlacesMonth] = []
    
    struct PlacesMonth: Identifiable, Equatable {
        var id = UUID()
        var month: String
        var year: Int
        var placesCount: Int
        var monthIndex: Int
    }
    
    
    init() {
        rideAPIService = RidesApiServiceImpl(client: KtorClient())
        rideRepository = RidesRepository(apiService: rideAPIService)
        loadUserName()

        // Observe location updates
        Task {
            for await address in locationManager.$currentAddress.values {
                self.location = address
            }
        }
    }

    /// Starts location resolution for the main shell (Home or active ride). Do not call from `init()` — many screens create a `HomeViewModel()` and would re-trigger the permission flow on every launch.
    func requestLocationForMainShell() {
        locationManager.requestLocation()
    }

    @MainActor
    func loadUserName() {
        Task {
            do {
                if let currentUser = try await userRepo.getUserDetails() {
                    self.userName = currentUser.name ?? ""
                    MBUserDefaults.userNameStatic = currentUser.name ?? ""
                    let createdMillis = currentUser.accountCreatedDate
                    if  createdMillis > 0 {
                        self.userCreatedDate = Date(timeIntervalSince1970: TimeInterval(createdMillis) / 1000)
                    }
                    
                }
            } catch {
                print("Error fetching user: \(error)")
            }
        }
    }
    func getRideSummary(userID: String, range: String) {
        Task { @MainActor in
            isRideSummaryLoading = true
        }
        rideRepository.getRideSummary(userID: userID) { result, error in
            Task { @MainActor in
                defer { self.isRideSummaryLoading = false }
                if let success = result as? APIResultSuccess<AnyObject>,
                   let rideArray = success.data as? [DashboardDomain] {
                    self.dashboardData = rideArray
                    let month = Calendar.current.component(.month, from: Date())
                    let year = Calendar.current.component(.year, from: Date())
                    self.updateStatsFor(month: month, year: year)
                    self.journeySlices = self.aggregateSlices(from: self.dashboardData)
                    self.currentSlices = self.getJourneySlices(for: range)
                    self.generatePlacesByMonth()
                    NotificationCenter.default.post(name: .placesDataUpdated, object: nil)
                    print("----Rides---\(rideArray)")
                } else {
                    print("error")
                }
            }
        }
    }
    
    
    
    @MainActor
    func updateStatsFor(month: Int, year: Int) {
        
        guard let selectedDash = dashboardData.first(where: {
            $0.monthYear.month == month && $0.monthYear.year == year
        }) else {
            
            self.stats = [
                RideStat(title: "Total Rides", value: "0", icon: AppIcon.Home.createRide),
                RideStat(title: "Locations", value: "0",  icon: AppIcon.Home.location),
                RideStat(title: "Total KMs", value: "0", icon: AppIcon.Home.nearMe)
            ]
            return
        }
        
        let rides = selectedDash.perMonthData
        
        let totalRides = rides.count
        let rawDistance = rides.reduce(0) { $0 + Int(truncating: $1.rideDistance ?? 0) }
        let totalDistance = formatDistance(rawDistance)
        let locations = Set(rides.map { $0.endLocation ?? "" }).count
        // MARK: - Dashboard Stat Cards
        self.stats = [
            RideStat(title: "Total Rides", value: "\(totalRides)", icon: AppIcon.Home.createRide),
            RideStat(title: "Locations", value: "\(locations)", icon: AppIcon.Home.location),
            RideStat(title: "Total KMs", value: totalDistance,  icon: AppIcon.Home.nearMe)
        ]
    }
    // MARK: - Donut chart
    func getDashboardRange(_ label: String) -> [DashboardDomain] {
        let calendar = Calendar.current
        let now = Date()
        switch label {
            
        case "This month":
            let m = calendar.component(.month, from: now)
            let y = calendar.component(.year, from: now)
            return dashboardData.filter { $0.monthYear.month == m && $0.monthYear.year == y }
            
            
        case "Last month":
            let lastMonth = calendar.date(byAdding: .month, value: -1, to: now)!
            let m = calendar.component(.month, from: lastMonth)
            let y = calendar.component(.year, from: lastMonth)
            return dashboardData.filter { $0.monthYear.month == m && $0.monthYear.year == y }
            
        case "Last 4 months":
            let start = calendar.date(byAdding: .month, value: -4, to: now)!
            return dashboardData.filter {
                guard let date = calendar.date(from: DateComponents(year: Int($0.monthYear.year),
                                                                    month: Int($0.monthYear.month))) else { return false }
                return date >= start
            }
            
        case "Last 6 months":
            let start = calendar.date(byAdding: .month, value: -6, to: now)!
            return dashboardData.filter {
                guard let date = calendar.date(from: DateComponents(year: Int($0.monthYear.year),
                                                                    month: Int($0.monthYear.month))) else { return false }
                return date >= start
            }
            
        case "Last year":
            let start = calendar.date(byAdding: .month, value: -12, to: now)!
            return dashboardData.filter {
                guard let date = calendar.date(from: DateComponents(year: Int($0.monthYear.year),
                                                                    month: Int($0.monthYear.month))) else { return false }
                return date >= start
            }
            
        default:
            return []
        }
    }
    func formatDistance(_ meters: Int) -> String {
        if meters >= 1000 {
            let kValue = meters / 1000
            let isExact = meters % 1000 == 0
            
            return isExact ? "\(kValue)K" : "\(kValue)K+"
        } else {
            return "\(meters)"
        }
    }
    
    
    
    func getJourneySlices(for range: String) -> [JourneySlice] {
        let selected = getDashboardRange(range)
        
        if selected.isEmpty {
            return []
        }
        
        return aggregateSlices(from: selected)
    }
    func aggregateSlices(from data: [DashboardDomain]) -> [JourneySlice] {
        let rides = data.flatMap { $0.perMonthData }
        
        let totalRides = rides.count
        let locations = Set(rides.map { $0.endLocation ?? "" }).count
        let organiser = rides.filter { $0.isOrganiserGroupRide == true }.count
        let participant = rides.filter { $0.isParticipantGroupRide == true }.count
        
        return [
            JourneySlice(category: "Total Rides", value: Double(totalRides), color: AppColor.strongCyan),
            JourneySlice(category: "Places Explored", value: Double(locations), color: AppColor.purple),
            JourneySlice(category: "Ride Groups", value: Double(participant), color: AppColor.lightCyan),
            JourneySlice(category: "Ride Invites", value: Double(organiser), color: AppColor.vividBlue)
        ]
    }
    // MARK: - Bar chart
    func generatePlacesByMonth() {
        var result: [PlacesMonth] = []
        
        for item in dashboardData {
            let m = Int(item.monthYear.month)
            let y = Int(item.monthYear.year)
            
            let monthName = DateFormatter().monthSymbols[m - 1].prefix(3).capitalized
            
            let uniquePlaces = item.perMonthData.filter {
                !($0.endLocation ?? "").trimmingCharacters(in: .whitespaces).isEmpty
            }.count
            
            
            result.append(
                PlacesMonth(
                    month: String(monthName),
                    year: y,
                    placesCount: uniquePlaces,
                    monthIndex: m
                )
            )
        }
        // Sort chronologically
        self.placesByMonth = result.sorted {
            if $0.year == $1.year {
                return $0.monthIndex < $1.monthIndex
            }
            return $0.year < $1.year
        }
    }
    var firstAvailableMonth: Date? {
        guard let created = userCreatedDate else { return nil }
        
        let calendar = Calendar.current
        let components = calendar.dateComponents([.year, .month], from: created)
        
        return calendar.date(from: components)
    }
    
    
}
extension Notification.Name {
    static let placesDataUpdated = Notification.Name("placesDataUpdated")
}
