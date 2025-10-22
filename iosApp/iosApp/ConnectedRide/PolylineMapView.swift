//
//  PolylineMapView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 22/10/25.
//

import UIKit
import MapKit
import SwiftUI


struct PolylineMapView: UIViewRepresentable {
    var coordinates: [CLLocationCoordinate2D]

    func makeUIView(context: Context) -> MKMapView {
        let mapView = MKMapView()
        mapView.delegate = context.coordinator
        mapView.showsUserLocation = true
        mapView.isZoomEnabled = true
        mapView.isScrollEnabled = true
        return mapView
    }

    func updateUIView(_ mapView: MKMapView, context: Context) {
        mapView.removeOverlays(mapView.overlays)
        mapView.removeAnnotations(mapView.annotations)

        // Add annotations
        for (index, coordinate) in coordinates.enumerated() {
            let annotation = MKPointAnnotation()
            annotation.coordinate = coordinate
            annotation.title = "Stop \(index + 1)"
            annotation.subtitle = "\(index)" // pass index to coordinator
            mapView.addAnnotation(annotation)
        }

        // Add polyline
        let polyline = MKPolyline(coordinates: coordinates, count: coordinates.count)
        mapView.addOverlay(polyline)

        // Fit map
        if !coordinates.isEmpty {
            let rect = polyline.boundingMapRect
            let padding = UIEdgeInsets(top: 60, left: 60, bottom: 60, right: 60)
            mapView.setVisibleMapRect(rect, edgePadding: padding, animated: true)
        }
    }

    func makeCoordinator() -> Coordinator {
        Coordinator(coordinates: coordinates)
    }

    class Coordinator: NSObject, MKMapViewDelegate {
        let coordinates: [CLLocationCoordinate2D]  // store a reference to parent’s coordinates

        init(coordinates: [CLLocationCoordinate2D]) {
            self.coordinates = coordinates
        }

        func mapView(_ mapView: MKMapView, rendererFor overlay: MKOverlay) -> MKOverlayRenderer {
            if let polyline = overlay as? MKPolyline {
                let renderer = MKPolylineRenderer(polyline: polyline)
                renderer.strokeColor = UIColor.systemBlue
                renderer.lineWidth = 5
                return renderer
            }
            return MKOverlayRenderer()
        }

        func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
            if annotation is MKUserLocation { return nil }

            let identifier = "RideStopPin"
            var annotationView = mapView.dequeueReusableAnnotationView(withIdentifier: identifier)

            if annotationView == nil {
                annotationView = MKAnnotationView(annotation: annotation, reuseIdentifier: identifier)
                annotationView?.canShowCallout = true
            } else {
                annotationView?.annotation = annotation
            }

            // Use index from subtitle to determine pin type
            if let subtitle = annotation.subtitle, let index = Int(subtitle ?? "0") {
                if index == 0 {
                    annotationView?.image = AppIcon.ConnectedRide.startLocation
                } else if index == coordinates.count - 1 {
                    annotationView?.image = AppIcon.ConnectedRide.endLocation
                } else {
                    annotationView?.image = AppIcon.ConnectedRide.intermediate
                }
            }

            return annotationView
        }
    }
}
