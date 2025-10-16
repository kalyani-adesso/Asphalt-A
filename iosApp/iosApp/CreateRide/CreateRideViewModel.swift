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
    @Published var shareLink = "https://adessoriderclub.app/12121312"
    @Published  var selectedTime : Date? = nil
    @Published  var selectedDate : Date? = nil
    
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
}
