//
//  EditProfileSkeleton.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 01/04/26.
//
//  Layout-matched loading UI for Edit Profile (header, photo, fields, mechanic card, actions).
//

import SwiftUI

struct EditProfileSkeleton: View {
    private var skeletonFill: Color { AppColor.whiteGray.opacity(0.72) }

    var body: some View {
        VStack(spacing: 0) {
            headerRow
            avatarRow
            VStack(spacing: 20) {
                ForEach(0..<5, id: \.self) { _ in
                    fieldGroupSkeleton
                }
                mechanicCardSkeleton
                actionButtonsRow
            }
            .padding()
        }
        .background(AppColor.listGray)
        .cornerRadius(10)
        .localizedSkeletonShimmer(cornerRadius: 10, cycleDuration: 1.5)
    }

    private var headerRow: some View {
        HStack(alignment: .top) {
            HStack(spacing: 12) {
                RoundedRectangle(cornerRadius: 6)
                    .fill(skeletonFill)
                    .frame(width: 30, height: 30)
                VStack(alignment: .leading, spacing: 6) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 120, height: 18)
                    RoundedRectangle(cornerRadius: 4)
                        .fill(skeletonFill)
                        .frame(width: 220, height: 12)
                }
            }
            Spacer(minLength: 0)
            Color.clear
                .frame(width: 14, height: 14)
                .padding(.trailing, 15)
        }
        .padding(.horizontal, 15)
        .padding(.top, 20)
    }

    private var avatarRow: some View {
        ZStack(alignment: .bottomTrailing) {
            Ellipse()
                .fill(skeletonFill)
                .frame(width: 92, height: 73)
            Circle()
                .fill(AppColor.white)
                .overlay(Circle().stroke(AppColor.listGray, lineWidth: 1))
                .frame(width: 38, height: 38)
                .offset(x: -2, y: -2)
        }
        .padding([.top, .bottom], 20)
    }

    private var fieldGroupSkeleton: some View {
        VStack(alignment: .leading, spacing: 10) {
            RoundedRectangle(cornerRadius: 4)
                .fill(skeletonFill)
                .frame(width: 160, height: 16)
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.white)
                .frame(height: 52)
                .overlay(
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(AppColor.whiteGray.opacity(0.6), lineWidth: 1)
                )
        }
    }

    private var mechanicCardSkeleton: some View {
        HStack(spacing: 11) {
            RoundedRectangle(cornerRadius: 6)
                .fill(skeletonFill)
                .frame(width: 30, height: 30)
                .padding(.leading, 8)
            VStack(alignment: .leading, spacing: 4) {
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(width: 140, height: 16)
                RoundedRectangle(cornerRadius: 4)
                    .fill(skeletonFill)
                    .frame(width: 200, height: 12)
            }
            Spacer(minLength: 0)
            RoundedRectangle(cornerRadius: 12)
                .fill(skeletonFill)
                .frame(width: 48, height: 28)
                .padding(.trailing, 18)
        }
        .frame(height: 80)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(AppColor.white)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 8)
                .stroke(AppColor.whiteGray, lineWidth: 1)
        )
    }

    private var actionButtonsRow: some View {
        HStack(spacing: 19) {
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.white)
                .frame(height: 50)
                .overlay(
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(AppColor.darkRed.opacity(0.5), lineWidth: 1)
                )
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.celticBlue.opacity(0.4))
                .frame(height: 50)
        }
        .padding(.bottom, 21)
    }
}

#Preview {
    ZStack {
        AppColor.darkgray.ignoresSafeArea()
        ScrollView {
            EditProfileSkeleton()
                .padding(EdgeInsets(top: 15, leading: 15, bottom: 150, trailing: 15))
        }
    }
}
