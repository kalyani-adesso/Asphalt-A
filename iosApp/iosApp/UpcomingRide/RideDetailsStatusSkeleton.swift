//
//  RideDetailsStatusSkeleton.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 05/04/26.
//

import SwiftUI

struct RideDetailsStatusSkeleton: View {
    private var skeletonFill: Color { AppColor.whiteGray.opacity(0.72) }

    var body: some View {
        HStack(spacing: 16) {
            statusCardPlaceholder()
            statusCardPlaceholder()
            statusCardPlaceholder()
        }
        .frame(height: 63)
        .padding([.top, .bottom], 16)
        .localizedSkeletonShimmer(cornerRadius: 12, cycleDuration: 1.5)
    }

    private func statusCardPlaceholder() -> some View {
        VStack(spacing: 13) {
            RoundedRectangle(cornerRadius: 6)
                .fill(skeletonFill)
                .frame(width: 28, height: 18)
            RoundedRectangle(cornerRadius: 6)
                .fill(skeletonFill)
                .frame(width: 72, height: 14)
        }
        .frame(maxWidth: .infinity)
        .frame(height: 63)
        .background(
            RoundedRectangle(cornerRadius: 12)
                .fill(AppColor.white)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(AppColor.darkGray, lineWidth: 1)
        )
    }
}

#Preview {
    RideDetailsStatusSkeleton()
        .padding()
}

