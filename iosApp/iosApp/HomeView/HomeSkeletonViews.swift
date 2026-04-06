//
//  HomeSkeletonViews.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 06/04/26.
//
//  Layout-matched skeletons for Home: stats, upcoming ride cards, journey donut, places chart.
//  Uses `localizedSkeletonShimmer` (Twitter/Facebook-style horizontal sweep) only while loading.
//

import SwiftUI

// MARK: - Your Ride Stats

struct DashboardStatsSkeleton: View {
    var body: some View {
        VStack(spacing: 15) {
            HStack {
                VStack(alignment: .leading, spacing: 6) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(AppColor.whiteGray.opacity(0.72))
                        .frame(width: 140, height: 18)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(AppColor.whiteGray.opacity(0.72))
                        .frame(width: 180, height: 12)
                }
                Spacer()
                HStack(spacing: 20) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(AppColor.whiteGray.opacity(0.72))
                        .frame(width: 16, height: 16)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(AppColor.whiteGray.opacity(0.72))
                        .frame(width: 16, height: 16)
                }
            }
            .padding(.horizontal, 10)

            HStack(spacing: 15) {
                ForEach(0..<3, id: \.self) { _ in
                    VStack(alignment: .leading, spacing: 8) {
                        RoundedRectangle(cornerRadius: 4)
                            .fill(AppColor.whiteGray.opacity(0.72))
                            .frame(width: 28, height: 24)
                        VStack(alignment: .leading, spacing: 4) {
                            RoundedRectangle(cornerRadius: 4)
                                .fill(AppColor.whiteGray.opacity(0.72))
                                .frame(width: 44, height: 22)
                            RoundedRectangle(cornerRadius: 4)
                                .fill(AppColor.whiteGray.opacity(0.72))
                                .frame(width: 72, height: 12)
                        }
                    }
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 16)
                    .background(AppColor.mediumGray)
                    .cornerRadius(14)
                }
            }
        }
        .padding()
        .background(AppColor.backgroundLight)
        .cornerRadius(15)
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.darkGray, lineWidth: 2)
        )
        .localizedSkeletonShimmer(cornerRadius: 15, cycleDuration: 1.5)
    }
}

// MARK: - Upcoming Rides (horizontal cards)

struct UpcomingRidesSectionSkeleton: View {
    private let cardWidth: CGFloat = 290

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 25) {
                ForEach(0..<2, id: \.self) { _ in
                    card
                }
            }
        }
    }

    private var card: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack(spacing: 10) {
                Circle()
                    .fill(AppColor.whiteGray.opacity(0.72))
                    .frame(width: 29, height: 29)
                VStack(alignment: .leading, spacing: 6) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(AppColor.whiteGray.opacity(0.72))
                        .frame(height: 16)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(AppColor.whiteGray.opacity(0.72))
                        .frame(width: 160, height: 12)
                }
                Spacer()
            }
            HStack(spacing: 8) {
                RoundedRectangle(cornerRadius: 4)
                    .fill(AppColor.whiteGray.opacity(0.72))
                    .frame(width: 12, height: 12)
                RoundedRectangle(cornerRadius: 4)
                    .fill(AppColor.whiteGray.opacity(0.72))
                    .frame(width: 120, height: 12)
                Spacer()
            }
            HStack {
                RoundedRectangle(cornerRadius: 4)
                    .fill(AppColor.whiteGray.opacity(0.72))
                    .frame(width: 180, height: 12)
                Spacer()
                HStack(spacing: 4) {
                    ForEach(0..<3, id: \.self) { _ in
                        Circle()
                            .fill(AppColor.whiteGray.opacity(0.72))
                            .frame(width: 19, height: 19)
                    }
                }
            }
            HStack(spacing: 8) {
                RoundedRectangle(cornerRadius: 6)
                    .fill(AppColor.whiteGray.opacity(0.72))
                    .frame(width: 120, height: 32)
                RoundedRectangle(cornerRadius: 6)
                    .fill(AppColor.whiteGray.opacity(0.72))
                    .frame(width: 120, height: 32)
            }
            .padding(.vertical, 10)
        }
        .padding()
        .frame(width: cardWidth)
        .background(AppColor.backgroundLight)
        .cornerRadius(14)
        .overlay(
            RoundedRectangle(cornerRadius: 14)
                .stroke(AppColor.darkGray, lineWidth: 2)
        )
        .localizedSkeletonShimmer(cornerRadius: 14, cycleDuration: 1.5)
    }
}

// MARK: - Adventure Journey (donut + legend)

struct JourneyCardSkeleton: View {
    var body: some View {
        HStack(alignment: .top, spacing: 12) {
            VStack(alignment: .leading, spacing: 20) {
                RoundedRectangle(cornerRadius: 4)
                    .fill(AppColor.whiteGray.opacity(0.72))
                    .frame(width: 150, height: 18)
                ZStack {
                    Circle()
                        .stroke(AppColor.whiteGray.opacity(0.72), lineWidth: 14)
                        .frame(width: 170, height: 170)
                    VStack(spacing: 6) {
                        RoundedRectangle(cornerRadius: 4)
                            .fill(AppColor.whiteGray.opacity(0.72))
                            .frame(width: 36, height: 36)
                        RoundedRectangle(cornerRadius: 4)
                            .fill(AppColor.whiteGray.opacity(0.72))
                            .frame(width: 88, height: 12)
                    }
                }
                .frame(width: 190, height: 190)
            }
            VStack(spacing: 36) {
                RoundedRectangle(cornerRadius: 5)
                    .fill(Color.white)
                    .frame(width: 111, height: 31)
                    .overlay(
                        RoundedRectangle(cornerRadius: 5)
                            .stroke(Color.black.opacity(0.10), lineWidth: 1)
                    )
                VStack(alignment: .leading, spacing: 10) {
                    ForEach(0..<4, id: \.self) { _ in
                        HStack(spacing: 6) {
                            RoundedRectangle(cornerRadius: 2)
                                .fill(AppColor.whiteGray.opacity(0.72))
                                .frame(width: 10, height: 10)
                            RoundedRectangle(cornerRadius: 4)
                                .fill(AppColor.whiteGray.opacity(0.72))
                                .frame(width: 100, height: 12)
                        }
                        .padding(.horizontal, 8)
                        .padding(.vertical, 5)
                        .background(
                            RoundedRectangle(cornerRadius: 5)
                                .fill(Color.white)
                                .overlay(
                                    RoundedRectangle(cornerRadius: 5)
                                        .stroke(Color.black.opacity(0.1), lineWidth: 1)
                                )
                        )
                    }
                }
                .frame(maxWidth: .infinity, alignment: .center)
            }
        }
        .padding()
        .background(AppColor.backgroundLight)
        .cornerRadius(15)
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.darkGray, lineWidth: 2)
        )
        .padding(.horizontal, 5)
        .localizedSkeletonShimmer(cornerRadius: 15, cycleDuration: 1.5)
    }
}

// MARK: - Places visited (header + bar chart area)

struct PlacesVisitedChartSkeleton: View {
    private let barHeights: [CGFloat] = [0.35, 0.55, 0.45, 0.7, 0.5, 0.62]

    var body: some View {
        VStack(alignment: .leading, spacing: 15) {
            HStack {
                VStack(alignment: .leading, spacing: 6) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(AppColor.whiteGray.opacity(0.72))
                        .frame(width: 220, height: 16)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(AppColor.whiteGray.opacity(0.72))
                        .frame(width: 180, height: 12)
                }
                Spacer()
                HStack(spacing: 10) {
                    RoundedRectangle(cornerRadius: 8)
                        .fill(AppColor.whiteGray.opacity(0.72))
                        .frame(width: 32, height: 32)
                    RoundedRectangle(cornerRadius: 8)
                        .fill(AppColor.whiteGray.opacity(0.72))
                        .frame(width: 32, height: 32)
                }
            }

            GeometryReader { geo in
                let h = geo.size.height
                let w = geo.size.width
                let barCount = barHeights.count
                let spacing: CGFloat = 12
                let barW = max((w - spacing * CGFloat(barCount - 1)) / CGFloat(barCount), 6)

                HStack(alignment: .bottom, spacing: spacing) {
                    ForEach(Array(barHeights.enumerated()), id: \.offset) { _, ratio in
                        RoundedRectangle(cornerRadius: 6)
                            .fill(AppColor.whiteGray.opacity(0.72))
                            .frame(width: barW, height: max(h * ratio, 8))
                    }
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottom)
            }
            .frame(height: 180)
            .padding(.top, 4)
        }
        .padding(.vertical, 4)
        .localizedSkeletonShimmer(cornerRadius: 12, cycleDuration: 1.5)
    }
}
