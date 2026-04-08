//
//  ForgotPasswordSkeleton.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 31/03/26.
//

import SwiftUI

struct ForgotPasswordSkeleton: View {
    private var skeletonFill: Color { AppColor.whiteGray.opacity(0.72) }

    var body: some View {
        VStack(spacing: 0) {
            VStack(spacing: 21) {
                fieldPlaceholder
            }
            .padding(.bottom, 18)

            RoundedRectangle(cornerRadius: 15)
                .fill(skeletonFill)
                .frame(height: 51)
                .padding(.bottom, 20)
        }
        .localizedSkeletonShimmer(cornerRadius: 12, cycleDuration: 1.5)
    }

    private var fieldPlaceholder: some View {
        VStack(alignment: .leading, spacing: 10) {
            RoundedRectangle(cornerRadius: 4)
                .fill(skeletonFill)
                .frame(width: 160, height: 16)
            RoundedRectangle(cornerRadius: 10)
                .fill(skeletonFill)
                .frame(height: 50)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

#Preview {
    ForgotPasswordSkeleton()
        .padding(.horizontal, 24)
}

