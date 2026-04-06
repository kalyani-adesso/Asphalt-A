//
//  ListShimmerPlaceholder.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 03/04/26.
//  Localized skeleton + sweep (profile garage pattern). Use for list/grid loading
//  instead of full-screen `ProgressViewReusable`.
//

import SwiftUI

// MARK: - Sweep modifier (shared timeline)

private struct ListShimmerSweepModifier: ViewModifier {
    var cornerRadius: CGFloat = 10
    /// Seconds for one full pass (feed-style loaders often use ~1.4–1.6s).
    var cycleDuration: Double = 1.25

    func body(content: Content) -> some View {
        TimelineView(.animation(minimumInterval: 1.0 / 60.0, paused: false)) { timeline in
            let t = timeline.date.timeIntervalSinceReferenceDate
            let cycle = max(cycleDuration, 0.2)
            let progress = CGFloat((t.truncatingRemainder(dividingBy: cycle)) / cycle)
            let phase = -0.85 + progress * 2.2

            content
                .overlay {
                    GeometryReader { geo in
                        let w = geo.size.width
                        LinearGradient(
                            colors: [
                                .clear,
                                .white.opacity(0.55),
                                AppColor.celticBlue.opacity(0.22),
                                .white.opacity(0.45),
                                .clear
                            ],
                            startPoint: .leading,
                            endPoint: .trailing
                        )
                        .frame(width: w * 0.5)
                        .offset(x: phase * w)
                        .blendMode(.plusLighter)
                    }
                    .clipShape(RoundedRectangle(cornerRadius: cornerRadius))
                }
        }
    }
}

// MARK: - List / card rows

/// Skeleton rows with the same shimmer treatment as `ProfileGarageLoadingPlaceholder`.
struct ListShimmerPlaceholder: View {
    var rowCount: Int = 4
    var rowHeight: CGFloat = 54
    var rowSpacing: CGFloat = 12
    var rowCornerRadius: CGFloat = 8
    var horizontalPadding: CGFloat = 16
    var bottomPadding: CGFloat = 8
    /// When true, rows sit in a white bordered card (garage style).
    var embeddedInCard: Bool = false
    var cardCornerRadius: CGFloat = 8
    var cardInteriorPadding: CGFloat = 16
    /// When false, static gray rows only (no horizontal sweep). Default off so Home/dashboard never picks up accidental Timeline shimmer.
    var showsSweep: Bool = false

    var body: some View {
        let inner = VStack(alignment: .leading, spacing: rowSpacing) {
            ForEach(0..<rowCount, id: \.self) { _ in
                RoundedRectangle(cornerRadius: rowCornerRadius)
                    .fill(AppColor.whiteGray.opacity(0.72))
                    .frame(height: rowHeight)
            }
        }
        .padding(embeddedInCard ? cardInteriorPadding : 0)

        Group {
            if embeddedInCard {
                inner
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(
                        RoundedRectangle(cornerRadius: cardCornerRadius)
                            .fill(AppColor.white)
                    )
                    .overlay(
                        RoundedRectangle(cornerRadius: cardCornerRadius)
                            .stroke(AppColor.listGray, lineWidth: 1)
                    )
            } else {
                inner
                    .frame(maxWidth: .infinity, alignment: .leading)
            }
        }
        .modifier(ConditionalShimmerModifier(
            showsSweep: showsSweep,
            cornerRadius: embeddedInCard ? cardCornerRadius : max(10, rowCornerRadius + 2),
            cycleDuration: 1.25
        ))
        .padding(.horizontal, horizontalPadding)
        .padding(.bottom, bottomPadding)
    }
}

/// Applies `ListShimmerSweepModifier` only when `showsSweep` is true.
private struct ConditionalShimmerModifier: ViewModifier {
    var showsSweep: Bool
    var cornerRadius: CGFloat
    var cycleDuration: Double = 1.25

    @ViewBuilder
    func body(content: Content) -> some View {
        if showsSweep {
            content.modifier(ListShimmerSweepModifier(cornerRadius: cornerRadius, cycleDuration: cycleDuration))
        } else {
            content
        }
    }
}

// MARK: - Grid (e.g. photo gallery)

struct GridShimmerPlaceholder: View {
    let columns: [GridItem]
    let cellSide: CGFloat
    let cellCount: Int
    var spacing: CGFloat = 8
    var cellCornerRadius: CGFloat = 8
    var horizontalPadding: CGFloat = 16
    var containerCornerRadius: CGFloat = 12
    var showsSweep: Bool = false

    var body: some View {
        LazyVGrid(columns: columns, spacing: spacing) {
            ForEach(0..<cellCount, id: \.self) { _ in
                RoundedRectangle(cornerRadius: cellCornerRadius)
                    .fill(AppColor.whiteGray.opacity(0.72))
                    .frame(width: cellSide, height: cellSide)
            }
        }
        .padding(.horizontal, horizontalPadding)
        .modifier(ConditionalShimmerModifier(showsSweep: showsSweep, cornerRadius: containerCornerRadius, cycleDuration: 1.25))
    }
}

// MARK: - Reuse sweep on custom dashboard / section skeletons

extension View {
    /// Horizontal sweep over arbitrary skeleton shapes (same treatment as list shimmer).
    /// Use a slightly longer `cycleDuration` (e.g. 1.5) for Twitter/Facebook-style feed skeletons.
    func localizedSkeletonShimmer(cornerRadius: CGFloat = 12, cycleDuration: Double = 1.25) -> some View {
        modifier(ListShimmerSweepModifier(cornerRadius: cornerRadius, cycleDuration: cycleDuration))
    }
}
