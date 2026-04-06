//
//  HistoryRideCardSkeleton.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 04/04/26.
//
//  Mirrors `UpComingView` for `.history`: header + COMPLETED badge, date/riders row, ADD PHOTOS, rating row.
//

import SwiftUI

struct HistoryRideCardSkeleton: View {
    private var skeletonFill: Color { AppColor.whiteGray.opacity(0.72) }

    var body: some View {
        VStack(alignment: .leading, spacing: 22) {
            HStack(spacing: 11) {
                RoundedRectangle(cornerRadius: 6)
                    .fill(skeletonFill)
                    .frame(width: 30, height: 30)
                VStack(alignment: .leading, spacing: 4) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(height: 16)
                        .frame(maxWidth: 200, alignment: .leading)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(height: 12)
                        .frame(maxWidth: 260, alignment: .leading)
                }
                Spacer(minLength: 0)
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.green.opacity(0.4))
                    .frame(width: 84, height: 30)
            }

            HStack {
                HStack(spacing: 5) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 20, height: 20)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(height: 14)
                        .frame(maxWidth: 160, alignment: .leading)
                }
                Spacer(minLength: 0)
                HStack(spacing: 5) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 16, height: 16)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 64, height: 16)
                }
            }

            VStack(alignment: .leading, spacing: 20) {
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.white)
                    .frame(maxWidth: .infinity)
                    .frame(height: 50)
                    .overlay(
                        RoundedRectangle(cornerRadius: 10)
                            .stroke(AppColor.celticBlue.opacity(0.45), lineWidth: 1)
                    )
                    .overlay(
                        HStack(spacing: 8) {
                            RoundedRectangle(cornerRadius: 3)
                                .fill(skeletonFill)
                                .frame(width: 20, height: 20)
                            RoundedRectangle(cornerRadius: 3)
                                .fill(skeletonFill)
                                .frame(width: 120, height: 14)
                        }
                        .frame(maxWidth: .infinity)
                    )

                HStack(spacing: 10) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 88, height: 16)
                    ForEach(0..<5, id: \.self) { _ in
                        RoundedRectangle(cornerRadius: 4)
                            .fill(skeletonFill)
                            .frame(width: 18, height: 18)
                    }
                }
            }
        }
        .padding([.leading, .trailing, .top], 16)
        .padding(.bottom, 20)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
        .localizedSkeletonShimmer(cornerRadius: 10, cycleDuration: 1.5)
    }
}

#Preview {
    List {
        HistoryRideCardSkeleton()
            .listRowSeparator(.hidden)
            .listRowInsets(EdgeInsets(top: 4, leading: 16, bottom: 4, trailing: 16))
    }
    .listStyle(.plain)
}
