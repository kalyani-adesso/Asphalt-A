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
    @Published var selectedParticipants: Set<UUID> = []
    @Published var shareLink = "https://adessoriderclub.app/12121312"
    @Published var selectedTime: Date? = nil
    @Published var selectedDate: Date? = nil

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
        if currentStep < 5 { currentStep += 1 }
    }
    
    func previousStep() {
        if currentStep > 1 { currentStep -= 1 }
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
}

//MARK: - Create Ride API -
extension CreateRideViewModel {
    func getAllUsers() {
        userRepository.getAllUsers { result, error in
            if let success = result as? APIResultSuccess<AnyObject>,
               let domainList = success.data as? [UserDomain] {
                
                let participants = domainList.map { user in
                    Participant(
                        name: user.name,
                        role: user.isMechanic ? "Mechanic" : nil,
                        bike: user.primaryBike,
                        image: user.profilePic.isEmpty ? "avatar1" : user.profilePic,
                        isOnline: true,
                        userId: user.uid
                    )
                }
                
                DispatchQueue.main.async {
                    self.participants = participants
                }
                
            } else if let error = error {
                print("Error fetching users: \(error)")
            } else {
                print("Unexpected data format")
            }
        }
    }
    
    func createRide() {
        //TODO: You have to store all the particpants map it according to the structure
        
        // TODO: convert date and time to long
       
        let userInvites = UserInvites(acceptInvite: 0)
        let createRideRoot = CreateRideRoot(userID: MBUserDefaults.userIdStatic, rideType: ride.type?.rawValue ?? "", rideTitle: ride.title, description: ride.description, startDate: <#T##KotlinLong?#>, startLocation: ride.startLocation, endLocation: ride.endLocation, createdDate: <#T##KotlinLong?#>, participants: , startLatitude: ride.startLat ?? 0.0, startLongitude: ride.startLng ?? 0.0, endLatitude: ride.endLat ?? 0.0, endLongitude: ride.endLng ?? 0.0)
        rideRepository.createRide(createRideRoot: createRideRoot, completionHandler: { rideResult, error in
            //TODO: Display the response.
            
        })
    }
}
