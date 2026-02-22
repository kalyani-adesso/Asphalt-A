//
//  ImagePicker.swift
//  iosApp
//
//  Created by Lavanya Selvan on 06/11/25.
//

import SwiftUI
import PhotosUI

import PhotosUI

struct PhotoPicker: UIViewControllerRepresentable{
    @Binding var images: [UIImage]
    let maxImages: Int = 6
    
    func makeUIViewController(context: Context) -> PHPickerViewController {
        var config = PHPickerConfiguration()
        // Calculate remaining slots
        let remainingSlots = max(0, maxImages - images.count)
        config.selectionLimit = remainingSlots
        config.filter = .images
        let picker = PHPickerViewController(configuration: config)
        picker.delegate = context.coordinator
        return picker
    }
    
    func updateUIViewController(_ uiViewController: PHPickerViewController, context: Context) {}
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    class Coordinator: NSObject, PHPickerViewControllerDelegate {
        let parent: PhotoPicker
        init(_ parent: PhotoPicker) { self.parent = parent }
        
        func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
            picker.dismiss(animated: true)
            
            for result in results {
                if result.itemProvider.canLoadObject(ofClass: UIImage.self) {
                    // Check if we haven't exceeded max images
                    if self.parent.images.count < self.parent.maxImages {
                        result.itemProvider.loadObject(ofClass: UIImage.self) { object, _ in
                            if let img = object as? UIImage {
                                DispatchQueue.main.async {
                                    // Double-check before adding
                                    if self.parent.images.count < self.parent.maxImages {
                                        self.parent.images.append(img)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
