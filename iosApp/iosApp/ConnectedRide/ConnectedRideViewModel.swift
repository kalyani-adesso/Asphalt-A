//
//  ConnectedRideViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 18/10/25.
//

import Foundation
import SwiftUI

struct RideCompleteModel: Identifiable {
    let id = UUID()
    let iconName: Image
    let value: String
    let label: String
}

import Foundation

final class ConnectedRideViewModel: ObservableObject {
    @Published var rideCompleteModel: [RideCompleteModel] = []
    
    init () {
        loadData()
    }
    
    func loadData() {
        rideCompleteModel =   [
            RideCompleteModel(iconName: AppIcon.ConnectedRide.rideDuration, value: "09:30:23", label: "Duration"),
            RideCompleteModel(iconName: AppIcon.ConnectedRide.rideDistance, value: "78.5", label: "KM"),
            RideCompleteModel(iconName: AppIcon.ConnectedRide.riders, value: "3", label: "Riders")
       ]
    }
}
