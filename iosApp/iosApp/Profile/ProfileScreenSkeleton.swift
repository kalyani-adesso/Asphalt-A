//
//  ProfileScreenSkeleton.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 01/04/26.
//
//  Layout-matched loading placeholders for Profile (header, garage, stats, achievements).
//  Twitter/Facebook-style horizontal sweep via `localizedSkeletonShimmer`.
//

import SwiftUI

struct ProfileScreenSkeleton: View {
    private var skeletonFill: Color { AppColor.whiteGray.opacity(0.72) }

    var body: some View {
        VStack(spacing: 20) {
            profileHeaderCard
            vehiclesSectionCard
            statisticsSectionCard
            achievementsSectionCard
        }
        .padding(.bottom, 32)
    }

    // MARK: - Profile header (avatar, name, email, badge pills)

    private var profileHeaderCard: some View {
        HStack(alignment: .center, spacing: 20) {
            Circle()
                .fill(skeletonFill)
                .frame(width: 73, height: 73)
            VStack(alignment: .leading, spacing: 8) {
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(height: 20)
                    .frame(maxWidth: 200, alignment: .leading)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(height: 16)
                    .frame(maxWidth: 240, alignment: .leading)
                HStack(spacing: 8) {
                    RoundedRectangle(cornerRadius: 5)
                        .fill(AppColor.white)
                        .frame(width: 88, height: 26)
                        .overlay(RoundedRectangle(cornerRadius: 5).stroke(AppColor.listGray.opacity(0.5), lineWidth: 1))
                    RoundedRectangle(cornerRadius: 5)
                        .fill(AppColor.white)
                        .frame(width: 130, height: 26)
                        .overlay(RoundedRectangle(cornerRadius: 5).stroke(AppColor.listGray.opacity(0.5), lineWidth: 1))
                }
            }
            Spacer(minLength: 0)
        }
        .padding()
        .frame(maxWidth: .infinity)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
        .padding(.horizontal, 16)
        .localizedSkeletonShimmer(cornerRadius: 10, cycleDuration: 1.5)
    }

    // MARK: - Your Vehicles (title, garage row, add bike)

    private var vehiclesSectionCard: some View {
        VStack(spacing: 0) {
            HStack(spacing: 20) {
                RoundedRectangle(cornerRadius: 6)
                    .fill(skeletonFill)
                    .frame(width: 30, height: 30)
                VStack(alignment: .leading, spacing: 5) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 130, height: 16)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 220, height: 12)
                }
                Spacer(minLength: 0)
            }
            .padding([.top, .bottom], 21)
            .padding(.horizontal, 16)

            HStack {
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(width: 150, height: 16)
                Spacer()
            }
            .padding(.horizontal, 16)
            .padding(.bottom, 12)

            VStack(alignment: .leading, spacing: 0) {
                HStack(alignment: .center, spacing: 12) {
                    RoundedRectangle(cornerRadius: 6)
                        .fill(skeletonFill)
                        .frame(width: 36, height: 36)
                    VStack(alignment: .leading, spacing: 6) {
                        RoundedRectangle(cornerRadius: 4)
                            .fill(skeletonFill)
                            .frame(width: 110, height: 16)
                        RoundedRectangle(cornerRadius: 4)
                            .fill(skeletonFill)
                            .frame(width: 170, height: 12)
                    }
                    Spacer(minLength: 0)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 22, height: 22)
                }
                .padding()
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .fill(AppColor.white)
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(AppColor.listGray, lineWidth: 1)
                )
            }
            .padding(.horizontal, 16)

            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.celticBlue.opacity(0.35))
                .frame(height: 50)
                .padding(.horizontal, 16)
                .padding(.top, 16)
                .padding(.bottom, 21)
        }
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.whiteGray, lineWidth: 1)
        )
        .padding(.horizontal, 16)
        .localizedSkeletonShimmer(cornerRadius: 10, cycleDuration: 1.5)
    }

    // MARK: - Total statistics (two columns)

    private var statisticsSectionCard: some View {
        VStack(alignment: .leading, spacing: 16) {
            HStack(spacing: 20) {
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(width: 30, height: 30)
                VStack(alignment: .leading, spacing: 5) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 130, height: 16)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 200, height: 12)
                }
                Spacer(minLength: 0)
            }
            .padding([.top], 21)
            .padding(.horizontal, 16)

            HStack(spacing: 20) {
                statTileSkeleton
                statTileSkeleton
            }
            .padding([.leading, .trailing, .bottom])
        }
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.whiteGray, lineWidth: 1)
        )
        .padding(.horizontal, 16)
        .localizedSkeletonShimmer(cornerRadius: 10, cycleDuration: 1.5)
    }

    private var statTileSkeleton: some View {
        VStack(spacing: 10) {
            RoundedRectangle(cornerRadius: 6)
                .fill(skeletonFill)
                .frame(width: 32, height: 32)
            RoundedRectangle(cornerRadius: 4)
                .fill(skeletonFill)
                .frame(width: 72, height: 18)
            RoundedRectangle(cornerRadius: 4)
                .fill(skeletonFill)
                .frame(width: 88, height: 12)
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, 17)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(AppColor.white)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 8)
                .stroke(AppColor.listGray, lineWidth: 1)
        )
    }

    // MARK: - Achievements

    private var achievementsSectionCard: some View {
        VStack(spacing: 0) {
            HStack(spacing: 20) {
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(width: 30, height: 30)
                VStack(alignment: .leading, spacing: 5) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 120, height: 16)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 260, height: 12)
                }
                Spacer(minLength: 0)
            }
            .padding([.top, .bottom], 21)
            .padding(.horizontal, 16)

            VStack(spacing: 15) {
                RoundedRectangle(cornerRadius: 6)
                    .fill(skeletonFill)
                    .frame(width: 44, height: 44)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(width: 200, height: 16)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(height: 12)
                    .frame(maxWidth: 280)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(height: 12)
                    .frame(maxWidth: 240)
                RoundedRectangle(cornerRadius: 10)
                    .fill(skeletonFill)
                    .frame(width: 171, height: 50)
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 24)
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .fill(AppColor.white)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(AppColor.listGray, lineWidth: 1)
            )
            .padding(.horizontal, 16)
            .padding(.bottom, 21)
        }
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.whiteGray, lineWidth: 1)
        )
        .padding(.horizontal, 16)
        .localizedSkeletonShimmer(cornerRadius: 10, cycleDuration: 1.5)
    }
}

#Preview {
    ScrollView {
        ProfileScreenSkeleton()
            .padding(.top, 20)
    }
    .background(AppColor.white)
}
