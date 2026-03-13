//
//  RidePhotosViewerView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 20/02/26.
//

import SwiftUI
import shared

/// One photo in the ride gallery: we need imageID for the delete API and url for display.
struct RidePhotoItem: Identifiable, Equatable {
    let id: String
    let url: String
}

struct RidePhotosViewerView: View {
    @EnvironmentObject var viewModel: UpcomingRideViewModel
    let rideId: String
    let rideTitle: String
    /// Only photos that have valid url and decode to a valid image. Each item has imageID for delete API.
    @State private var ridePhotos: [RidePhotoItem] = []
    @State private var isLoading = false
    @State private var showDeleteConfirmation = false
    @State private var photoToDelete: RidePhotoItem?
    @Binding var isPresented: Bool

    private let gridSpacing: CGFloat = 8

    var body: some View {
        ZStack {
            VStack(spacing: 0) {
                // Header
                HStack {
                    Text("Ride Photos")
                        .font(KlavikaFont.bold.font(size: 22))
                        .foregroundColor(AppColor.black)

                    Spacer()

                    Button(action: { isPresented = false }) {
                        Image(systemName: "xmark.circle.fill")
                            .font(.system(size: 28))
                            .foregroundStyle(AppColor.stoneGray.opacity(0.8))
                    }
                }
                .padding(.horizontal, 20)
                .padding(.top, 20)
                .padding(.bottom, 8)

                // Ride title
                Text(rideTitle)
                    .font(KlavikaFont.medium.font(size: 14))
                    .foregroundColor(AppColor.stoneGray)
                    .lineLimit(1)
                    .padding(.horizontal, 20)
                    .padding(.bottom, 16)

                // Content: grid or empty state
                if ridePhotos.isEmpty && !isLoading {
                    emptyState
                } else {
                    GeometryReader { geo in
                        let availableWidth = max(geo.size.width - 32, 0)
                        let cellSide = max((availableWidth - (gridSpacing * 2)) / 3, 80)
                        let columns = [
                            GridItem(.fixed(cellSide), spacing: gridSpacing),
                            GridItem(.fixed(cellSide), spacing: gridSpacing),
                            GridItem(.fixed(cellSide), spacing: gridSpacing)
                        ]
                        ScrollView {
                            LazyVGrid(columns: columns, spacing: gridSpacing) {
                                ForEach(ridePhotos) { item in
                                    photoCard(item: item, cellSize: cellSide)
                                }
                            }
                            .padding(.horizontal, 16)
                            .padding(.top, 8)
                            .padding(.bottom, 24)
                        }
                        .scrollIndicators(.hidden)
                    }
                    .frame(maxHeight: .infinity)
                }

                // Close button below content (no overlay)
                Button(action: { isPresented = false }) {
                    Text("CLOSE")
                        .font(KlavikaFont.bold.font(size: 16))
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .frame(height: 52)
                        .background(AppColor.celticBlue)
                        .cornerRadius(12)
                }
                .padding(.horizontal, 20)
                .padding(.top, 12)
                .padding(.bottom, 24)
            }
            .background(AppColor.listGray.opacity(0.3))
            .confirmationDialog("Delete Photo", isPresented: $showDeleteConfirmation, presenting: photoToDelete) { _ in
                Button("Delete", role: .destructive) {
                    if let photo = photoToDelete {
                        deletePhoto(photo)
                    }
                }
            } message: { _ in
                Text("Are you sure you want to delete this photo?")
            }
           

            if isLoading {
                Color.black.opacity(0.25)
                    .ignoresSafeArea()
                    .zIndex(1)
                ProgressViewReusable(title: "Loading photos...")
                    .zIndex(2)
            }
        }
        .onAppear {
            Task {
                await loadRidePhotos()
            }
        }
    }

    private var emptyState: some View {
        VStack(spacing: 16) {
            Image(systemName: "photo.on.rectangle.angled")
                .font(.system(size: 56))
                .foregroundColor(AppColor.stoneGray.opacity(0.6))
            Text("No photos yet")
                .font(KlavikaFont.medium.font(size: 17))
                .foregroundColor(AppColor.stoneGray)
            Text("Photos added to this ride will appear here.")
                .font(KlavikaFont.regular.font(size: 14))
                .foregroundColor(AppColor.stoneGray.opacity(0.8))
                .multilineTextAlignment(.center)
                .padding(.horizontal, 32)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    @ViewBuilder
    private func photoCard(item: RidePhotoItem, cellSize: CGFloat) -> some View {
        if let image = viewModel.decodeBase64ToImage(base64: item.url) {
            ZStack(alignment: .topTrailing) {
                Image(uiImage: image)
                    .resizable()
                    .scaledToFill()
                    .frame(width: cellSize, height: cellSize)
                    .clipped()
                    .clipShape(RoundedRectangle(cornerRadius: 10))

                Button(action: {
                    photoToDelete = item
                    showDeleteConfirmation = true
                }) {
                    Image(systemName: "xmark.circle.fill")
                        .font(.system(size: 22))
                        .foregroundStyle(.white)
                        .shadow(color: .black.opacity(0.4), radius: 2, x: 0, y: 1)
                }
                .padding(6)
            }
            .frame(width: cellSize, height: cellSize)
            .clipShape(RoundedRectangle(cornerRadius: 10))
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.mediumGray)
                    .shadow(color: .black.opacity(0.06), radius: 2, x: 0, y: 1)
            )
        }
    }

    private func loadRidePhotos() async {
        await MainActor.run { isLoading = true }
        await Task.yield()

        guard let rideIndex = viewModel.rides.firstIndex(where: { $0.id == rideId }) else {
            await MainActor.run { isLoading = false }
            return
        }
        let ride = viewModel.rides[rideIndex]
        guard let imageDataList = ride.imageData else {
            await MainActor.run { ridePhotos = []; isLoading = false }
            return
        }

        var validPhotos: [RidePhotoItem] = []
        for item in imageDataList {
            guard let img = item as? ImageData, !img.url.isEmpty else { continue }
            guard viewModel.decodeBase64ToImage(base64: img.url) != nil else { continue }
            validPhotos.append(RidePhotoItem(id: img.imageID, url: img.url))
        }

        await MainActor.run {
            ridePhotos = validPhotos
            isLoading = false
        }
    }

    private func deletePhoto(_ photo: RidePhotoItem) {
        Task {
            await MainActor.run { isLoading = true }
            do {
                try await viewModel.deleteRidePhoto(rideId: rideId, photoId: photo.id)
                await MainActor.run {
                    ridePhotos.removeAll { $0.id == photo.id }
                    showDeleteConfirmation = false
                    photoToDelete = nil
                }
                await viewModel.fetchAllRides()
                await loadRidePhotos()
            } catch {
                print("Error deleting photo: \(error)")
            }
            await MainActor.run { isLoading = false }
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
