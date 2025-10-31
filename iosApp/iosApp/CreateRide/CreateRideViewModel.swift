//
//  CreateRideViewModel.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//


import Foundation
import MapKit
import shared

class CreateRideViewModel: NSObject, ObservableObject, MKLocalSearchCompleterDelegate {
    
    @Published var ride = Ride()
    @Published var currentStep = 1
    @Published var selectedParticipants: Set<UUID> = []
    @Published var shareLink = "https://adessoriderclub.app/12121312"
    @Published var selectedTime: Date? = nil
    @Published var selectedDate: Date? = nil
    @Published var startLocation: String = ""
    @Published var endLocation: String = ""
    
    // --- Place search properties ---
    @Published var query = "" {
        didSet {
            completer.queryFragment = query
        }
    }
    @Published var results: [MKLocalSearchCompletion] = []
    
    private var completer: MKLocalSearchCompleter
    
    // Track which field is active (start or end)
    var isSelectingStart = true
    
    let participants: [Participant] = [
        Participant(name: "Sooraj Rajan", role: "Mechanic", bike: "Harley Davidson 750", image: "avatar1", isOnline: true),
        Participant(name: "Abhishek", role: nil, bike: "Classic 350", image: "avatar1", isOnline: true),
        Participant(name: "Vyshnav", role: nil, bike: "Harley Davidson 750", image: "avatar1", isOnline: false),
        Participant(name: "Tony", role: nil, bike: "Harley Davidson 750", image: "avatar1", isOnline: true)
    ]
    
    func nextStep() {
        if currentStep < 5 { currentStep += 1 }
    }
    
    func previousStep() {
        if currentStep > 1 { currentStep -= 1 }
    }
    
    private var userAPIService: UserAPIService
    private var userRepository: UserRepository
    

    override init() {
        completer = MKLocalSearchCompleter()
        userAPIService = UserAPIServiceImpl(client: KtorClient())
        userRepository = UserRepository(apiService: userAPIService)
        super.init()
        completer.delegate = self
    }
    
    // MARK: - MKLocalSearchCompleter Delegate
    func completerDidUpdateResults(_ completer: MKLocalSearchCompleter) {
        DispatchQueue.main.async {
            self.results = completer.results
        }
    }
    
    func completer(_ completer: MKLocalSearchCompleter, didFailWithError error: Error) {
        print("Error: \(error.localizedDescription)")
    }
    
    // MARK: - Select Place
    func selectPlace(_ completion: MKLocalSearchCompletion, isStart: Bool) {
        let searchRequest = MKLocalSearch.Request(completion: completion)
        let search = MKLocalSearch(request: searchRequest)
        
        search.start { [weak self] response, error in
            guard let self = self, let item = response?.mapItems.first else { return }
            
            DispatchQueue.main.async {
                let placeName = item.name ?? completion.title
                
                if isStart {
                    self.startLocation = placeName
                    self.ride.startLocation = placeName
                } else {
                    self.endLocation = placeName
                    self.ride.endLocation = placeName
                }
                
                if let coordinate = item.placemark.coordinate as CLLocationCoordinate2D? {
                    print("Selected place coordinate: \(coordinate.latitude), \(coordinate.longitude)")
                }
                
                print("Selected place: \(placeName)")
            }
        }
    }
}

extension CreateRideViewModel {
    func getAllUsers()  {
        userRepository.getAllUsers { result,error  in
            if let success = result as? APIResultSuccess<AnyObject>,
               let domain = success.data as? UserData {
                print("User name:\(domain.name)")
            }
        }
    }
}
