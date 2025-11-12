//
//  LocationManager.swift
//  iosApp
//
//  Created by Lavanya Selvan on 28/10/25.
//

import Foundation
import CoreLocation

@MainActor
class LocationManager: NSObject, ObservableObject, @preconcurrency CLLocationManagerDelegate {
    let manager = CLLocationManager()
    private let geocoder = CLGeocoder()
    
    @Published var currentLocation: CLLocation?
    @Published var currentAddress: String = "Fetching location..."
    @Published var speedInKph: Double?
    @Published var lastLocation:CLLocation?

    override init() {
        super.init()
        manager.delegate = self
        manager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
    }
    
    func requestLocation() {
        let status = manager.authorizationStatus
        
        switch status {
        case .notDetermined:
            manager.requestWhenInUseAuthorization()
        case .authorizedWhenInUse, .authorizedAlways:
            manager.requestLocation()
        case .denied, .restricted:
            currentAddress = "Location access denied"
        default:
            break
        }
    }
    
    func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
        if manager.authorizationStatus == .authorizedWhenInUse || manager.authorizationStatus == .authorizedAlways {
            manager.requestLocation()
        } else if manager.authorizationStatus == .denied {
            currentAddress = "Location access denied"
        }
    }
    
    internal func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        if let location = locations.first {
            currentLocation = location
            reverseGeocode(location)
        }
        
        if let speed = locations.last?.speed, speed >= 0  {
            speedInKph = (speed * 3.6).rounded()
        }
       
        if let lastLocation = locations.last  {
            self.lastLocation = lastLocation
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        currentAddress = "Failed to get location"
        print("Location error:", error.localizedDescription)
    }
    
    private func reverseGeocode(_ location: CLLocation) {
        geocoder.reverseGeocodeLocation(location) { [weak self] placemarks, error in
            guard let placemark = placemarks?.first, error == nil else {
                self?.currentAddress = "Unknown location"
                return
            }
            
            let city = placemark.locality ?? ""
            let area = placemark.subLocality ?? ""
            self?.currentAddress = area.isEmpty ? city : "\(area), \(city)"
        }
    }
}
