//
//  CreateRideViewModel.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import Foundation

class CreateRideViewModel: ObservableObject {
      
    @Published var ride = Ride()
    @Published var currentStep = 1
    @Published var selectedParticipants: Set<UUID> = []
    
    let participants: [Participant] = [
            Participant(name: "Sooraj Rajan", role: "Mechanic", bike: "Harley Davidson 750", image: "avatar1", isOnline: true),
            Participant(name: "Abhishek", role: nil, bike: "Classic 350", image: "avatar1", isOnline: true),
            Participant(name: "Vyshnav", role: nil, bike: "Harley Davidson 750", image: "avatar1", isOnline: false),
            Participant(name: "Tony", role: nil, bike: "Harley Davidson 750", image: "avatar1", isOnline: true)
        ]
    
    func isStep1Valid() -> Bool {
        !ride.title.isEmpty && ride.type != .none && ride.date != nil && ride.time != nil
    }
    
    func isStep2Valid() -> Bool {
        !ride.startLocation.isEmpty && !ride.endLocation.isEmpty
    }
    
    func nextStep() {
        if currentStep < 5 { currentStep += 1 }
    }
    
    func previousStep() {
        if currentStep > 1 { currentStep -= 1 }
    }
}
