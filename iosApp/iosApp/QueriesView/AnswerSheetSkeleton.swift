//
//  AnswerSheetSkeleton.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 04/04/26.
//
//  Mirrors `AnswerSheetView`: header, question card, answer blocks, bottom composer — same shimmer as `QueriesSkeleton`.
//

import SwiftUI

struct AnswerSheetSkeleton: View {
    private var skeletonFill: Color { AppColor.whiteGray.opacity(0.72) }

    var body: some View {
        VStack(spacing: 20) {
            headerSkeleton

            ScrollView {
                VStack(alignment: .leading, spacing: 16) {
                    questionCardSkeleton

                    answerBlockSkeleton
                    answerBlockSkeleton
                }
                .padding(.horizontal, 35)
            }

            bottomInputSkeleton
        }
        .background(
            LinearGradient(
                gradient: Gradient(stops: [
                    .init(color: AppColor.white.opacity(0.8), location: 0.2),
                    .init(color: Color.clear, location: 1.0)
                ]),
                startPoint: .top,
                endPoint: .bottom
            )
        )
        .localizedSkeletonShimmer(cornerRadius: 12, cycleDuration: 1.5)
    }

    private var headerSkeleton: some View {
        HStack {
            RoundedRectangle(cornerRadius: 4)
                .fill(skeletonFill)
                .frame(width: 168, height: 22)
            Spacer()
            Circle()
                .fill(skeletonFill)
                .frame(width: 28, height: 28)
        }
        .padding()
    }

    private var questionCardSkeleton: some View {
        VStack(alignment: .leading, spacing: 12) {
            RoundedRectangle(cornerRadius: 4)
                .fill(skeletonFill)
                .frame(height: 18)
                .padding(.trailing, 32)

            HStack(spacing: 8) {
                RoundedRectangle(cornerRadius: 6)
                    .fill(skeletonFill)
                    .frame(width: 56, height: 24)
                RoundedRectangle(cornerRadius: 6)
                    .fill(skeletonFill)
                    .frame(width: 72, height: 24)
            }

            VStack(alignment: .leading, spacing: 6) {
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(height: 14)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(height: 14)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(height: 14)
                    .padding(.trailing, 20)
            }

            HStack {
                Circle()
                    .fill(skeletonFill)
                    .frame(width: 28, height: 28)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(width: 100, height: 14)
                Spacer(minLength: 0)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(width: 56, height: 12)
            }

            HStack(spacing: 16) {
                HStack(spacing: 4) {
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 16, height: 16)
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 14, height: 14)
                }
                HStack(spacing: 4) {
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 16, height: 16)
                    RoundedRectangle(cornerRadius: 3)
                        .fill(skeletonFill)
                        .frame(width: 14, height: 14)
                }
            }
        }
        .padding()
        .background(AppColor.white)
        .cornerRadius(10)
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.darkGray, lineWidth: 1)
        )
    }

    private var answerBlockSkeleton: some View {
        VStack(alignment: .leading, spacing: 10) {
            HStack(alignment: .top) {
                Circle()
                    .fill(skeletonFill)
                    .frame(width: 36, height: 36)
                VStack(alignment: .leading, spacing: 6) {
                    HStack {
                        RoundedRectangle(cornerRadius: 4)
                            .fill(skeletonFill)
                            .frame(width: 110, height: 16)
                        Spacer(minLength: 0)
                        RoundedRectangle(cornerRadius: 4)
                            .fill(skeletonFill)
                            .frame(width: 52, height: 12)
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
                    .padding(.trailing, 28)
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

    private var bottomInputSkeleton: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack(alignment: .top, spacing: 12) {
                Circle()
                    .fill(skeletonFill)
                    .frame(width: 44, height: 44)

                VStack(alignment: .leading, spacing: 10) {
                    RoundedRectangle(cornerRadius: 10)
                        .fill(skeletonFill)
                        .frame(height: 100)
                        .overlay(
                            RoundedRectangle(cornerRadius: 10)
                                .stroke(AppColor.darkGray, lineWidth: 1)
                        )

                    HStack(spacing: 12) {
                        RoundedRectangle(cornerRadius: 8)
                            .fill(skeletonFill)
                            .frame(width: 87, height: 36)
                        Spacer()
                        RoundedRectangle(cornerRadius: 8)
                            .fill(skeletonFill)
                            .frame(width: 132, height: 36)
                    }
                    .padding(.top, 10)
                }
            }
            .padding(.top, 60)
            .padding(.horizontal, 30)
            .padding(.bottom, 50)
        }
        .frame(width: 400, height: 189)
        .background(Color.white)
    }
}

#Preview {
    AnswerSheetSkeleton()
}
