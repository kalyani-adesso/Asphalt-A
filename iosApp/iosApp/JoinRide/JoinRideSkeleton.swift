//
//  JoinRideSkeleton.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 31/03/26.
//
//  Mirrors `JoinRideRow`: header (avatar, title, organizer), description, date/riders row, route/distance row, CALL + JOIN buttons.
//

import SwiftUI

struct JoinRideSkeleton: View {
    private var skeletonFill: Color { AppColor.whiteGray.opacity(0.72) }

    var rowCount: Int = 6

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 14) {
                ForEach(0..<rowCount, id: \.self) { _ in
                    joinRideCardSkeleton
                }
            }
            .padding(.horizontal, 16)
            .padding(.bottom, 8)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .localizedSkeletonShimmer(cornerRadius: 12, cycleDuration: 1.5)
    }

    private var joinRideCardSkeleton: some View {
        VStack(alignment: .leading, spacing: 22) {
            HStack(spacing: 11) {
                Circle()
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
                        .frame(maxWidth: 140, alignment: .leading)
                }
                Spacer(minLength: 0)
            }

            RoundedRectangle(cornerRadius: 4)
                .fill(skeletonFill)
                .frame(height: 14)
                .padding(.trailing, 24)

            HStack {
                HStack(spacing: 5) {
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 16, height: 16)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(height: 14)
                        .frame(maxWidth: 160, alignment: .leading)
                }
                Spacer(minLength: 0)
                HStack(spacing: 5) {
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 16, height: 16)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 52, height: 14)
                }
            }

            HStack(alignment: .top) {
                HStack(alignment: .top, spacing: 5) {
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 16, height: 16)
                        .padding(.top, 2)
                    VStack(alignment: .leading, spacing: 4) {
                        RoundedRectangle(cornerRadius: 4)
                            .fill(skeletonFill)
                            .frame(height: 14)
                        RoundedRectangle(cornerRadius: 4)
                            .fill(skeletonFill)
                            .frame(height: 14)
                            .padding(.trailing, 40)
                    }
                }
                Spacer(minLength: 0)
                HStack(spacing: 5) {
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 16, height: 16)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 44, height: 14)
                }
            }

            HStack(spacing: 15) {
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.white)
                    .frame(maxWidth: .infinity)
                    .frame(minHeight: 50)
                    .overlay(
                        RoundedRectangle(cornerRadius: 10)
                            .stroke(AppColor.darkGray, lineWidth: 2)
                    )
                    .overlay(
                        HStack(spacing: 8) {
                            RoundedRectangle(cornerRadius: 3)
                                .fill(skeletonFill)
                                .frame(width: 20, height: 20)
                            RoundedRectangle(cornerRadius: 3)
                                .fill(skeletonFill)
                                .frame(width: 88, height: 14)
                        }
                        .frame(maxWidth: .infinity)
                    )
                    .padding(.bottom, 20)

                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.celticBlue.opacity(0.42))
                    .frame(maxWidth: .infinity)
                    .frame(minHeight: 50)
                    .overlay(
                        RoundedRectangle(cornerRadius: 4)
                            .fill(skeletonFill.opacity(0.85))
                            .frame(width: 96, height: 14)
                    )
                    .padding(.bottom, 20)
            }
        }
        .padding([.leading, .trailing, .top], 16)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
    }
}

#Preview {
    JoinRideSkeleton()
}
