//
//  RidePopupView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 06/11/25.
//

import SwiftUI

enum RidePopupType {
    case uploadOptions
    case previewSelected
}

struct RidePopupView: View {
    @Binding var activePopup: RidePopupType?
    @Binding var openGallery: Bool
    @Binding var selectedImages: [UIImage]
    @State private var showDelete = false
    
    var onUpload: (() -> Void)? = nil
    
    var body: some View {
        ZStack {
            if activePopup != nil {
                // Dim background
                Color.black.opacity(0.35)
                    .ignoresSafeArea()
                    .onTapGesture { withAnimation { activePopup = nil } }
                uploadContent
            }
        }
    }
    
    // MARK: Upload Content
    private var uploadContent: some View {
        VStack(spacing: 20) {
            Text(selectedImages.isEmpty ? "Add Ride Photos" : "Ride Photos")
                .font(KlavikaFont.bold.font(size: 20))
                .foregroundStyle(AppColor.richBlack)
            
            VStack(spacing: 20){
                if selectedImages.isEmpty{
                    AppIcon.UpcomingRide.imagePlaceholder
                        .resizable()
                        .frame(width: 64, height: 64)
                    
                    Text(AppStrings.RidePopup.photo)
                        .font(KlavikaFont.bold.font(size: 16))
                        .foregroundStyle(AppColor.richBlack)
                    
                    Text(AppStrings.RidePopup.share)
                        .font(KlavikaFont.regular.font(size: 14))
                        .foregroundStyle(AppColor.stoneGray)
                }
                if !selectedImages.isEmpty {
                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 10) {
                            ForEach(selectedImages.prefix(5), id: \.self) { img in
                                RideImageView(selectedImages: $selectedImages, img: img)
                            }
                            
                            // If more than 5, show +X
                            if selectedImages.count > 5 {
                                Text("+\(selectedImages.count - 5)")
                                    .font(KlavikaFont.medium.font(size: 14))
                                    .foregroundStyle(AppColor.richBlack)
                                    .frame(width: 52, height: 52)
                                    .background(AppColor.stoneGray.opacity(0.2))
                                    .clipShape(RoundedRectangle(cornerRadius: 8))
                            }
                        }
                        .padding(.top, 4)
                    }
                }
                
                
                footerButtons
            }
            .padding(20)
            .frame(width: 311, height: 266)
            .background(Color.white)
            .cornerRadius(16)
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                    .stroke(Color.gray.opacity(0.15), lineWidth: 1.74)
            )
            
            
        }
        .frame(width: 343, height: 362)
        .background(Color.white)
        .cornerRadius(16)
        .shadow(radius: 10)
    }
    struct RideImageView: View {
        @Binding var selectedImages: [UIImage]
        var img: UIImage
        @State private var showDelete = false
        
        var body: some View {
            ZStack(alignment: .center) {
                Image(uiImage: img)
                    .resizable()
                    .scaledToFill()
                    .frame(width: 131, height: 130)
                    .clipShape(RoundedRectangle(cornerRadius: 8))
                    .overlay(
                        Color.black.opacity(showDelete ? 0.35 : 0)
                            .blur(radius: showDelete ? 2 : 0)
                    )
                    .onTapGesture {
                        withAnimation {
                            showDelete.toggle()
                        }
                    }
                
                if showDelete {
                    Button(action: {
                        if let index = selectedImages.firstIndex(of: img) {
                            selectedImages.remove(at: index)
                        }
                    }) {
                        ZStack {
                            Circle()
                                .fill(AppColor.red)
                                .frame(width: 40, height: 40)
                            Image(systemName: "xmark")
                                .resizable()
                                .scaledToFit()
                                .frame(width: 16, height: 16)
                                .foregroundColor(.white)
                        }
                    }
                    .offset(x: 5, y: -5)
                    .transition(.scale)
                }
            }
            .animation(.easeInOut, value: showDelete)
        }
    }
    
    
    // MARK: Preview Content
    private var previewContent: some View {
        VStack(spacing: 12) {
            Text(AppStrings.RidePopup.photoTitle)
                .font(KlavikaFont.bold.font(size: 20))
                .foregroundStyle(AppColor.richBlack)
            
            if selectedImages.isEmpty {
                Text(AppStrings.RidePopup.noPhoto)
                    .font(.system(size: 14))
                    .foregroundColor(.gray)
            } else {
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 10) {
                        ForEach(selectedImages, id: \.self) { img in
                            Image(uiImage: img)
                                .resizable()
                                .scaledToFill()
                                .frame(width: 80, height: 80)
                                .clipShape(RoundedRectangle(cornerRadius: 8))
                        }
                    }
                    .padding(.horizontal, 8)
                }
                .frame(height: 90)
            }
        }
    }
    
    // MARK: Footer Buttons
    private var footerButtons: some View {
        HStack(spacing: 12) {
            ButtonView(
                title: AppStrings.RidePopup.cancel ,
                fontSize: 16 ,
                background: AppColor.white,
                foregroundColor: AppColor.black,
                showShadow: false,
                borderColor: AppColor.stoneGray.opacity(0.3),
                onTap: {
                    withAnimation { activePopup = nil }
                }
            )
            
            if activePopup == .previewSelected {
                ButtonView(
                    title: AppStrings.RidePopup.addMore,
                    fontSize: 16 ,
                    background: AppColor.celticBlue,
                    foregroundColor: AppColor.white,
                    showShadow: false,
                    borderColor: AppColor.stoneGray.opacity(0.3),
                    onTap: {
                        openGallery = true
                    }
                )
            }
            ButtonView(
                title: activePopup == .uploadOptions ? "SELECT PHOTOS" : "UPLOAD" ,
                fontSize: 16 ,
                background:  AppColor.celticBlue,
                foregroundColor: AppColor.white,
                showShadow: false,
                borderColor: AppColor.stoneGray.opacity(0.3),
                onTap: {
                    if activePopup == .uploadOptions {
                        openGallery = true
                    } else {
                        onUpload?()
                        withAnimation {
                            activePopup = nil
                            selectedImages = []
                        }
                    }
                }
            )
        }
    }
}

#Preview {
    RidePopupView(activePopup: .constant(.uploadOptions), openGallery: .constant(true), selectedImages: .constant([]))
    
}
