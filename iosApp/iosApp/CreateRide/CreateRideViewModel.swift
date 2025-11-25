//
//  CreateRideViewModel.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import Foundation
import MapKit
import shared

class CreateRideViewModel: NSObject, ObservableObject {
    
    @Published var ride = Ride()
    @Published var currentStep = 1
    @Published var selectedParticipants: [Participant] = []
    @Published var shareLink = "https://adessoriderclub.app/12121312"
    @Published var selectedStartTime: Date? = nil
    @Published var selectedStartDate: Date? = nil
    @Published var selectedEndTime: Date? = nil
    @Published var selectedEndDate: Date? = nil
    @Published var isRideLoading = false
    private var currentUserId = MBUserDefaults.userIdStatic ?? ""
    
    @Published var query = "" {
        didSet {
            completer.queryFragment = query
        }
    }
    @Published var results: [MKLocalSearchCompletion] = []
    
    private var completer: MKLocalSearchCompleter
    
    var isSelectingStart = true
    
    @Published  var participants: [Participant] = []
    
    private var userAPIService: UserAPIService
    private var userRepository: UserRepository
    
    private var rideAPIService: RidesApIService
    private var rideRepository: RidesRepository
    
    override init() {
        completer = MKLocalSearchCompleter()
        
        userAPIService = UserAPIServiceImpl(client: KtorClient())
        userRepository = UserRepository(apiService: userAPIService)
        
        rideAPIService = RidesApiServiceImpl(client: KtorClient())
        rideRepository = RidesRepository(apiService: rideAPIService)
        
        super.init()
        completer.delegate = self
    }
    
    func nextStep() {
        DispatchQueue.main.async {
            if self.currentStep < 5 { self.currentStep += 1 }
        }
    }
    
    func previousStep() {
        if currentStep > 1 { currentStep -= 1 }
    }
    func isSelected(_ id: String) -> Bool {
        selectedParticipants.contains(where: { $0.id == id })
    }
    
    func toggle(_ id: String) {
        if let index = selectedParticipants.firstIndex(where: { $0.id == id }) {
            selectedParticipants.remove(at: index)
        } else if let participant = participants.first(where: { $0.id == id }) {
            selectedParticipants.append(participant)
        }
    }
}

// MARK: - Search Place Delegates
extension CreateRideViewModel: MKLocalSearchCompleterDelegate {
    func selectPlace(_ completion: MKLocalSearchCompletion, isStart: Bool) {
        let searchRequest = MKLocalSearch.Request(completion: completion)
        let search = MKLocalSearch(request: searchRequest)
        
        search.start { [weak self] response, error in
            guard let self = self, let item = response?.mapItems.first else { return }
            
            DispatchQueue.main.async {
                let placeName = item.name ?? completion.title
                
                if isStart {
                    self.ride.startLocation = placeName
                    self.ride.startLat = item.placemark.coordinate.latitude
                    self.ride.startLng = item.placemark.coordinate.longitude
                } else {
                    self.ride.endLocation = placeName
                    self.ride.endLat = item.placemark.coordinate.latitude
                    self.ride.endLng = item.placemark.coordinate.longitude
                }
                
                if let coordinate = item.placemark.coordinate as CLLocationCoordinate2D? {
                    print("Selected place coordinate: \(coordinate.latitude), \(coordinate.longitude)")
                }
                
                print("Selected place: \(placeName)")
            }
        }
    }
    
    func completerDidUpdateResults(_ completer: MKLocalSearchCompleter) {
        DispatchQueue.main.async {
            self.results = completer.results
        }
    }
    
    func completer(_ completer: MKLocalSearchCompleter, didFailWithError error: Error) {
        print("Error: \(error.localizedDescription)")
    }
    
    func getDistance() {
        if let startLat = self.ride.startLat,
           let startLng = self.ride.startLng,
           let endLat = self.ride.endLat,
           let endLng = self.ride.endLng {
            
            let startPlacemark = MKPlacemark(coordinate: CLLocationCoordinate2D(latitude: startLat, longitude: startLng))
            let endPlacemark = MKPlacemark(coordinate: CLLocationCoordinate2D(latitude: endLat, longitude: endLng))
            
            let request = MKDirections.Request()
            request.source = MKMapItem(placemark: startPlacemark)
            request.destination = MKMapItem(placemark: endPlacemark)
            request.transportType = .automobile
            
            let directions = MKDirections(request: request)
            directions.calculate { response, error in
                if let route = response?.routes.first {
                    let distanceKm = route.distance / 1000.0
                    print("Bike traveling distance: \(String(format: "%.2f", distanceKm)) km")
                    self.ride.rideDistance = distanceKm.rounded()
                } else if let error = error {
                    print("Error calculating route distance: \(error.localizedDescription)")
                }
            }
        }
    }
}

//MARK: - Create Ride API
extension CreateRideViewModel {
    func getAllUsers() {
        userRepository.getAllUsers { result, error in
            if let success = result as? APIResultSuccess<AnyObject>,
               let domainList = success.data as? [UserDomain] {
                
                let filteredParticpants = domainList
                    .filter { $0.uid != self.currentUserId }
                    .filter {$0.primaryBike != ""}
                    .map { user in
                        Participant(
                            id: user.uid,
                            name: user.name,
                            role: user.isMechanic ? "Mechanic" : nil,
                            bike: user.primaryBike,
                            image: user.profilePic.isEmpty ? "avatar1" : user.profilePic,
                            isOnline: true,
                            userId: user.uid
                        )
                    }
                
                DispatchQueue.main.async {
                    self.participants = filteredParticpants
                }
                
            } else if let error = error {
                print("Error fetching users: \(error)")
            } else {
                print("Unexpected data format")
            }
        }
    }
    
    func createRide(completion: @escaping (Bool) -> Void) {
        self.isRideLoading = true
        let participantDict: [String: UserInvites] = Dictionary(
            uniqueKeysWithValues: selectedParticipants.map { participant in
                (participant.userId, UserInvites(acceptInvite: 0))
            }
        )
        
        let createdDateLong = Int64(Date().timeIntervalSince1970 * 1000)
        var startDateLong: Int64 = 0
        if let merged = combine(date: selectedStartDate, time: selectedStartTime) {
            startDateLong = Int64(merged.timeIntervalSince1970 * 1000)
        }
        var endDateLong: Int64 = 0
        if let merged = combine(date: selectedEndDate, time: selectedEndTime) {
            endDateLong = Int64(merged.timeIntervalSince1970 * 1000)
        }
        
        let createRideRoot = CreateRideRoot(userID: MBUserDefaults.userIdStatic, rideType: ride.type?.rawValue ?? "", rideTitle: ride.title, description: ride.description, startDate: KotlinLong(value: startDateLong), startLocation: ride.startLocation, endLocation: ride.endLocation, createdDate: KotlinLong(value: createdDateLong) , participants:participantDict, startLatitude: ride.startLat ?? 0.0, startLongitude: ride.startLng ?? 0.0, endLatitude: ride.endLat ?? 0.0, endLongitude: ride.endLng ?? 0.0, distance: ride.rideDistance ?? 0.0, rideStatus: 0, endDate: KotlinLong(value: endDateLong))
        
        rideRepository.createRide(createRideRoot: createRideRoot, completionHandler: { rideResult, error in
            if let success = rideResult as? APIResultSuccess<AnyObject>,
               let data = success.data as? GenericResponse {
                Task { @MainActor in
                    self.isRideLoading = false
                    self.nextStep()
                    print("ride id:\(data.name)")
                    MBUserDefaults.rideIdStatic = data.name
                    completion(true)
                }
            }
        })
    }
    
    // MARK: - Epoch converter
    
    func combine(date: Date?, time: Date?) -> Date? {
        guard let date = date, let time = time else { return nil }
        
        let calendar = Calendar.current
        let dateComponents = calendar.dateComponents([.year, .month, .day], from: date)
        let timeComponents = calendar.dateComponents([.hour, .minute], from: time)
        
        var merged = DateComponents()
        merged.year = dateComponents.year
        merged.month = dateComponents.month
        merged.day = dateComponents.day
        merged.hour = timeComponents.hour
        merged.minute = timeComponents.minute
        
        return calendar.date(from: merged)
    }
    
    // MARK: - Details View validation
    var isDetailsValid: Bool {
        guard let type = ride.type,
              !ride.title.trimmingCharacters(in: .whitespaces).isEmpty,
              !ride.description.trimmingCharacters(in: .whitespaces).isEmpty,
              selectedStartDate != nil,
              selectedStartTime != nil,
              selectedEndDate != nil,
              selectedEndTime != nil
        else { return false }
        return true
    }
    
    // MARK: - Route View validation
    var isRouteValid: Bool {
        !ride.startLocation.trimmingCharacters(in: .whitespaces).isEmpty &&
        !ride.endLocation.trimmingCharacters(in: .whitespaces).isEmpty
    }
}
