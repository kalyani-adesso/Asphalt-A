//
//  ParticipantsSkeleton.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 02/04/26.
//
//  Mirrors `ParticipantsView` + `ParticipantsRow`: header, search, contact cards with avatar, name/tag, bike, radio.
//

import SwiftUI

struct ParticipantsSkeleton: View {
    private var skeletonFill: Color { AppColor.whiteGray.opacity(0.72) }

    var rowCount: Int = 8

    var body: some View {
        VStack(alignment: .leading, spacing: 20) {
            HStack {
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(width: 140, height: 18)
                Spacer(minLength: 0)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(width: 88, height: 14)
            }

            searchFieldSkeleton

            ScrollView {
                VStack(spacing: 10) {
                    ForEach(0..<rowCount, id: \.self) { _ in
                        participantRowSkeleton
                    }
                }
            }
        }
        .padding()
        .background(AppColor.backgroundLight)
        .cornerRadius(10)
        .localizedSkeletonShimmer(cornerRadius: 10, cycleDuration: 1.5)
    }

    private var searchFieldSkeleton: some View {
        VStack(alignment: .leading, spacing: 10) {
            Color.clear.frame(height: 16)

            HStack(spacing: 12) {
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(width: 20, height: 20)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(height: 16)
                    .frame(maxWidth: .infinity, alignment: .leading)
            }
            .padding()
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.white)
            )
        }
    }

    private var participantRowSkeleton: some View {
        HStack(spacing: 12) {
            ZStack(alignment: .bottomTrailing) {
                Circle()
                    .fill(skeletonFill)
                    .frame(width: 37, height: 37)
                Circle()
                    .fill(AppColor.spanishGreen.opacity(0.55))
                    .frame(width: 13, height: 13)
                    .offset(x: 2, y: 2)
            }

            VStack(alignment: .leading, spacing: 4) {
                HStack(spacing: 8) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 120, height: 16)
                    RoundedRectangle(cornerRadius: 6)
                        .fill(skeletonFill)
                        .frame(width: 76, height: 24)
                }

                HStack(spacing: 4) {
                    RoundedRectangle(cornerRadius: 2)
                        .fill(skeletonFill)
                        .frame(width: 12, height: 10)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 140, height: 13)
                }
            }

            Spacer(minLength: 0)

            Circle()
                .stroke(skeletonFill, lineWidth: 1)
                .frame(width: 24, height: 24)
        }
        .padding()
        .background(Color.white)
        .cornerRadius(10)
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.darkGray, lineWidth: 1)
        )
    }
}

#Preview {
    ParticipantsSkeleton()
        .padding()
        .background(AppColor.backgroundLight)
}
