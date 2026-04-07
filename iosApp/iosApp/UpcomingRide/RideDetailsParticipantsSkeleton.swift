//
//  RideDetailsParticipantsSkeleton.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 05/04/26.
//
//  Participant list area only (`ParticipantsStatusRow` + gray container) — same shimmer as full layout.
//

import SwiftUI

struct RideDetailsParticipantsSkeleton: View {
    private var skeletonFill: Color { AppColor.whiteGray.opacity(0.72) }

    var rowCount: Int = 6

    var body: some View {
        VStack(spacing: 8) {
            ForEach(0..<rowCount, id: \.self) { index in
                participantRowPlaceholder(isFirst: index == 0)
            }
        }
        .padding(16)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
        .localizedSkeletonShimmer(cornerRadius: 10, cycleDuration: 1.5)
    }

    private func participantRowPlaceholder(isFirst: Bool) -> some View {
        HStack(spacing: 12) {
            Circle()
                .fill(skeletonFill)
                .frame(width: 37, height: 37)
            VStack(alignment: .leading, spacing: 4) {
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(height: 16)
                    .frame(maxWidth: 160, alignment: .leading)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(height: 13)
                    .frame(maxWidth: 220, alignment: .leading)
            }
            Spacer(minLength: 0)
            Group {
                if isFirst {
                    RoundedRectangle(cornerRadius: 6)
                        .fill(skeletonFill)
                        .frame(width: 76, height: 28)
                } else {
                    Circle()
                        .fill(skeletonFill)
                        .frame(width: 24, height: 24)
                }
            }
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
    RideDetailsParticipantsSkeleton()
        .padding()
}
