//
//  QueriesListSkeleton.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 03/04/26.
//
//  List-only skeleton for `QueriesView` (cards area only).
//

import SwiftUI

struct QueriesListSkeleton: View {
    private var skeletonFill: Color { AppColor.whiteGray.opacity(0.72) }

    var cardCount: Int = 3

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 20) {
                ForEach(0..<cardCount, id: \.self) { _ in
                    queryCardSkeleton
                }
            }
            .padding(.horizontal, 20)
            .padding(.bottom, 24)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .localizedSkeletonShimmer(cornerRadius: 12, cycleDuration: 1.5)
    }

    private var queryCardSkeleton: some View {
        VStack(alignment: .leading, spacing: 15) {
            RoundedRectangle(cornerRadius: 4)
                .fill(skeletonFill)
                .frame(height: 18)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.trailing, 40)

            HStack(spacing: 8) {
                RoundedRectangle(cornerRadius: 6)
                    .fill(skeletonFill)
                    .frame(width: 52, height: 26)
                RoundedRectangle(cornerRadius: 6)
                    .fill(skeletonFill)
                    .frame(width: 76, height: 26)
            }

            VStack(alignment: .leading, spacing: 6) {
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(height: 14)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(height: 14)
                    .padding(.trailing, 24)
            }

            HStack {
                Circle()
                    .fill(skeletonFill)
                    .frame(width: 30, height: 30)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(width: 120, height: 16)
                Spacer(minLength: 0)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(width: 72, height: 12)
            }

            Divider()

            answerNestedSkeleton

            Divider()

            HStack(spacing: 20) {
                HStack(spacing: 6) {
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 18, height: 18)
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 14, height: 14)
                }
                HStack(spacing: 6) {
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 18, height: 18)
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 14, height: 14)
                }
                Spacer(minLength: 0)
                RoundedRectangle(cornerRadius: 2)
                    .fill(skeletonFill)
                    .frame(width: 72, height: 24)
            }
            .padding(.top, 4)
        }
        .padding()
        .background(AppColor.listGray)
        .cornerRadius(12)
    }

    private var answerNestedSkeleton: some View {
        VStack(alignment: .leading, spacing: 10) {
            HStack(alignment: .top) {
                Circle()
                    .fill(skeletonFill)
                    .frame(width: 36, height: 36)
                VStack(alignment: .leading, spacing: 6) {
                    HStack {
                        RoundedRectangle(cornerRadius: 4)
                            .fill(skeletonFill)
                            .frame(width: 100, height: 16)
                        Spacer(minLength: 0)
                        RoundedRectangle(cornerRadius: 4)
                            .fill(skeletonFill)
                            .frame(width: 56, height: 12)
                    }
                }
            }

            VStack(alignment: .leading, spacing: 6) {
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(height: 14)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(height: 14)
                    .padding(.trailing, 32)
            }

            HStack(spacing: 16) {
                HStack(spacing: 6) {
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 18, height: 18)
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 14, height: 14)
                }
                HStack(spacing: 6) {
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 18, height: 18)
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 14, height: 14)
                }
                Spacer(minLength: 0)
            }
            .padding(.top, 4)
        }
        .padding()
        .background(AppColor.lightGreen)
        .cornerRadius(10)
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.darkGray, lineWidth: 1)
        )
    }
}

