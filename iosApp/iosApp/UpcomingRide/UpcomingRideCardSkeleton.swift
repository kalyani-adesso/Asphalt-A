//
//  UpcomingRideCardSkeleton.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 06/04/26.
//
//  Mirrors `UpComingView` for `.upcoming`: header, date/time rows, rider count, two outline buttons.
//

import SwiftUI

struct UpcomingRideCardSkeleton: View {
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
                    .fill(AppColor.purple.opacity(0.35))
                    .frame(width: 84, height: 30)
            }

            VStack(alignment: .leading, spacing: 10) {
                VStack(alignment: .leading, spacing: 5) {
                    HStack {
                        HStack(spacing: 5) {
                            RoundedRectangle(cornerRadius: 4)
                                .fill(skeletonFill)
                                .frame(width: 20, height: 20)
                            RoundedRectangle(cornerRadius: 4)
                                .fill(skeletonFill)
                                .frame(width: 150, height: 14)
                        }
                        Spacer(minLength: 0)
                        HStack(spacing: 5) {
                            RoundedRectangle(cornerRadius: 4)
                                .fill(skeletonFill)
                                .frame(width: 20, height: 20)
                            RoundedRectangle(cornerRadius: 4)
                                .fill(skeletonFill)
                                .frame(width: 72, height: 14)
                        }
                    }
                    HStack {
                        HStack(spacing: 5) {
                            RoundedRectangle(cornerRadius: 4)
                                .fill(skeletonFill)
                                .frame(width: 20, height: 20)
                            RoundedRectangle(cornerRadius: 4)
                                .fill(skeletonFill)
                                .frame(width: 140, height: 14)
                        }
                        Spacer(minLength: 0)
                        HStack(spacing: 5) {
                            RoundedRectangle(cornerRadius: 4)
                                .fill(skeletonFill)
                                .frame(width: 20, height: 20)
                            RoundedRectangle(cornerRadius: 4)
                                .fill(skeletonFill)
                                .frame(width: 72, height: 14)
                        }
                    }
                }
                .padding(.horizontal, 10)

                HStack(spacing: 5) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 16, height: 16)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 64, height: 16)
                }
            }

            HStack(spacing: 15) {
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.white)
                    .frame(height: 50)
                    .frame(maxWidth: .infinity)
                    .overlay(
                        RoundedRectangle(cornerRadius: 10)
                            .stroke(AppColor.celticBlue.opacity(0.45), lineWidth: 1)
                    )
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.white)
                    .frame(height: 50)
                    .frame(maxWidth: .infinity)
                    .overlay(
                        RoundedRectangle(cornerRadius: 10)
                            .stroke(AppColor.celticBlue.opacity(0.45), lineWidth: 1)
                    )
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
        UpcomingRideCardSkeleton()
            .listRowSeparator(.hidden)
            .listRowInsets(EdgeInsets(top: 4, leading: 16, bottom: 4, trailing: 16))
    }
    .listStyle(.plain)
}
