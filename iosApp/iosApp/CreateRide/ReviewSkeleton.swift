//
//  ReviewSkeleton.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 02/04/26.
//
//  Mirrors `ReviewView` + `ReviewCard`: subtitle, four icon rows (ride, date, route, participants), footer buttons.
//

import SwiftUI

struct ReviewSkeleton: View {
    private var skeletonFill: Color { AppColor.whiteGray.opacity(0.72) }

    var body: some View {
        VStack(alignment: .leading, spacing: 20) {
            RoundedRectangle(cornerRadius: 4)
                .fill(skeletonFill)
                .frame(width: 180, height: 18)

            ForEach(0..<4, id: \.self) { index in
                reviewRowSkeleton(isFirst: index == 0, subtitleLines: index == 2 ? 2 : 1)
            }
        }
        .frame(width: 343)
        .padding()
        .background(AppColor.backgroundLight)
        .cornerRadius(10)
        .localizedSkeletonShimmer(cornerRadius: 10, cycleDuration: 1.5)
    }

    private func reviewRowSkeleton(isFirst: Bool, subtitleLines: Int) -> some View {
        HStack(alignment: .top, spacing: 12) {
            RoundedRectangle(cornerRadius: 5)
                .fill(skeletonFill)
                .frame(width: 36, height: 36)

            VStack(alignment: .leading, spacing: 4) {
                HStack(alignment: .center) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(height: 16)
                        .frame(maxWidth: isFirst ? 160 : .infinity, alignment: .leading)
                    if isFirst {
                        Spacer(minLength: 8)
                        RoundedRectangle(cornerRadius: 6)
                            .fill(skeletonFill)
                            .frame(width: 76, height: 26)
                    }
                }
                ForEach(0..<subtitleLines, id: \.self) { line in
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(height: 14)
                        .padding(.trailing, line == 0 ? 24 : 48)
                }
            }
            Spacer(minLength: 0)
        }
        .padding()
        .background(Color.white)
        .cornerRadius(10)
    }
}

struct ReviewFooterSkeleton: View {
    private var skeletonFill: Color { AppColor.whiteGray.opacity(0.72) }

    var body: some View {
        HStack(spacing: 15) {
            RoundedRectangle(cornerRadius: 15)
                .fill(AppColor.white)
                .frame(height: 51)
                .frame(maxWidth: .infinity)
                .overlay(
                    RoundedRectangle(cornerRadius: 15)
                        .stroke(AppColor.celticBlue, lineWidth: 1)
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(height: 14)
                        .padding(.horizontal, 28)
                )

            RoundedRectangle(cornerRadius: 15)
                .fill(AppColor.celticBlue.opacity(0.42))
                .frame(height: 51)
                .frame(maxWidth: .infinity)
                .overlay(
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill.opacity(0.9))
                        .frame(height: 14)
                        .padding(.horizontal, 32)
                )
        }
        .localizedSkeletonShimmer(cornerRadius: 15, cycleDuration: 1.5)
    }
}

#Preview {
    VStack {
        ReviewSkeleton()
        ReviewFooterSkeleton()
            .padding()
    }
}
