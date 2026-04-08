//
//  RidePhotosPopupSkeleton.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 06/04/26.
//
//  Mirrors `RidePopupView` when photos are selected: title, thumbnail strip, CANCEL / ADD MORE / UPLOAD row.
//

import SwiftUI

struct RidePhotosPopupSkeleton: View {
    private var skeletonFill: Color { AppColor.whiteGray.opacity(0.72) }

    var body: some View {
        VStack(spacing: 20) {
            RoundedRectangle(cornerRadius: 4)
                .fill(skeletonFill)
                .frame(width: 140, height: 22)

            VStack(spacing: 20) {
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 10) {
                        ForEach(0..<3, id: \.self) { _ in
                            RoundedRectangle(cornerRadius: 8)
                                .fill(skeletonFill)
                                .frame(width: 131, height: 130)
                        }
                    }
                    .padding(.top, 4)
                }

                HStack(spacing: 12) {
                    RoundedRectangle(cornerRadius: 10)
                        .fill(AppColor.white)
                        .frame(height: 50)
                        .frame(maxWidth: .infinity)
                        .overlay(
                            RoundedRectangle(cornerRadius: 10)
                                .stroke(AppColor.stoneGray.opacity(0.3), lineWidth: 1)
                        )
                    RoundedRectangle(cornerRadius: 10)
                        .fill(AppColor.celticBlue.opacity(0.38))
                        .frame(height: 50)
                        .frame(maxWidth: .infinity)
                    RoundedRectangle(cornerRadius: 10)
                        .fill(AppColor.celticBlue.opacity(0.38))
                        .frame(height: 50)
                        .frame(maxWidth: .infinity)
                }
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
        .localizedSkeletonShimmer(cornerRadius: 16, cycleDuration: 1.5)
    }
}

#Preview {
    ZStack {
        Color.black.opacity(0.35)
        RidePhotosPopupSkeleton()
    }
}
