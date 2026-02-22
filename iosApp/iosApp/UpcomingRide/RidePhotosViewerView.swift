//
//  RidePhotosViewerView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 20/02/26.
//

import SwiftUI
import shared

struct RidePhotosViewerView: View {
    @EnvironmentObject var viewModel: UpcomingRideViewModel
    let rideId: String
    let rideTitle: String
    @State private var ridePhotos: [String] = [] // Array of base64 encoded images
    @State private var isLoading = false
    @State private var showDeleteConfirmation = false
    @State private var photoToDelete: String?
    @Binding var isPresented: Bool
    
    var body: some View {
        ZStack {
            VStack(spacing: 16) {
                // Header
                HStack {
                    Text("Ride Photos")
                        .font(KlavikaFont.bold.font(size: 22))
                        .foregroundColor(AppColor.black)
                    
                    Spacer()
                    
                    Button(action: { isPresented = false }) {
                        Image(systemName: "xmark")
                            .resizable()
                            .frame(width: 14, height: 14)
                            .foregroundStyle(AppColor.richBlack)
                    }
                }
                .padding()
                
                // Ride Title
                Text(rideTitle)
                    .font(KlavikaFont.medium.font(size: 14))
                    .foregroundColor(AppColor.stoneGray)
                    .padding(.horizontal)
                
                // Photos Grid
                ZStack {
                    Group {
                        if ridePhotos.isEmpty {
                            VStack(spacing: 12) {
                                Image(systemName: "photo")
                                    .resizable()
                                    .frame(width: 64, height: 64)
                                    .foregroundColor(AppColor.stoneGray)

                                Text("No photos available")
                                    .font(KlavikaFont.regular.font(size: 16))
                                    .foregroundColor(AppColor.stoneGray)
                            }
                            .frame(maxHeight: .infinity)
                            .frame(maxWidth: .infinity)
                        } else {
                            ScrollView {
                                LazyVGrid(columns: [
                                    GridItem(.flexible(), spacing: 12),
                                    GridItem(.flexible(), spacing: 12)
                                ], spacing: 12) {
                                    ForEach(ridePhotos, id: \.self) { photoBase64 in
                                        VStack {
                                            ZStack(alignment: .topTrailing) {
                                                if let image = viewModel.decodeBase64ToImage(base64: photoBase64) {
                                                    Image(uiImage: image)
                                                        .resizable()
                                                        .scaledToFill()
                                                        .frame(height: 120)
                                                        .clipShape(RoundedRectangle(cornerRadius: 8))
                                                } else {
                                                    RoundedRectangle(cornerRadius: 8)
                                                        .fill(AppColor.listGray)
                                                        .frame(height: 120)
                                                }

                                                // Delete button
                                                Button(action: {
                                                    photoToDelete = photoBase64
                                                    showDeleteConfirmation = true
                                                }) {
                                                    ZStack {
                                                        Circle()
                                                            .fill(AppColor.red)
                                                            .frame(width: 22, height: 22)

                                                        Image(systemName: "xmark")
                                                            .resizable()
                                                            .scaledToFit()
                                                            .frame(width: 8, height: 8)
                                                            .foregroundColor(.white)
                                                    }
                                                }
                                                .padding(4)
                                            }
                                        }
                                    }
                                }
                                .padding(12)
                            }
                            .frame(maxHeight: .infinity)
                            .frame(maxWidth: .infinity)
                        }
                    }

                    // Close Button overlaid at bottom
                    VStack {
                        Spacer()
                        ButtonView(
                            title: "CLOSE",
                            fontSize: 16,
                            onTap: { isPresented = false }
                        )
                        .padding()
                    }
                }
                .background(AppColor.white)
                .cornerRadius(16)
            }
            .confirmationDialog(
                "Delete Photo",
                isPresented: $showDeleteConfirmation,
                presenting: photoToDelete
            ) { photo in
                Button("Delete", role: .destructive) {
                    if let photo = photoToDelete {
                        deletePhoto(photoBase64: photo)
                    }
                }
            } message: { photo in
                Text("Are you sure you want to delete this photo?")
            }
            .task {
                await loadRidePhotos()
            }
            if isLoading {
                ProgressViewReusable(title: "Loading photos...")
            }
        }
    }
    
    private func loadRidePhotos() async {
        isLoading = true
        if let rideIndex = viewModel.rides.firstIndex(where: { $0.id == rideId }) {
            let ride = viewModel.rides[rideIndex]
            
            // Simulate async work or perform actual network calls here
            let photos = ride.imageData?.compactMap { item in
                (item as? ImageData)?.url.isEmpty == false ? (item as? ImageData)?.url : nil
            } ?? []
            
            // Update on main thread
            await MainActor.run {
                ridePhotos = photos
                isLoading = false
            }
        } else {
            isLoading = false
        }
    }

    private func deletePhoto(photoBase64: String) {
        Task {
            isLoading = true
            do {
                let photoId = photoBase64
                try await viewModel.deleteRidePhoto(rideId: rideId, photoId: photoId)
                ridePhotos.removeAll { $0 == photoBase64 }
                showDeleteConfirmation = false
                photoToDelete = nil
            } catch {
                print("Error deleting photo: \(error)")
            }
            isLoading = false
            await loadRidePhotos()
        }
    }
}

#Preview {
    RidePhotosViewerView(
        rideId: "ride_123",
        rideTitle: "Mountain Trail Ride",
        isPresented: .constant(true)
    )
    .environmentObject(UpcomingRideViewModel())
}
