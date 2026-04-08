//
//  ProgressView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 04/11/25.
//

import SwiftUI

enum ProgressLoaderStyle {
    /// List / forms / details — light frosted layer + soft white → brand blue sweep.
    case standard
    /// Connected ride flows — slightly stronger scrim + tighter, brighter blue shimmer.
    case connectedRide
}

/// Full-screen shimmer that reads clearly on device (no dot animation).
private struct DesignAlignedShimmerOverlay: View {
    let style: ProgressLoaderStyle
    let accent: Color

    private var baseFillOpacity: CGFloat {
        switch style {
        case .standard: return 0.42
        case .connectedRide: return 0.32
        }
    }

    private var bandWidthFraction: CGFloat {
        switch style {
        case .standard: return 0.62
        case .connectedRide: return 0.48
        }
    }

    /// Seconds for one full sweep (standard = calmer; connected = snappier).
    private var cycleDuration: Double {
        switch style {
        case .standard: return 1.55
        case .connectedRide: return 1.12
        }
    }

    private var centerOpacity: CGFloat {
        switch style {
        case .standard: return 0.55
        case .connectedRide: return 0.72
        }
    }

    var body: some View {
        TimelineView(.animation(minimumInterval: 1.0 / 60.0, paused: false)) { timeline in
            GeometryReader { geo in
                let w = geo.size.width
                let h = geo.size.height
                let t = timeline.date.timeIntervalSinceReferenceDate
                let progress = CGFloat((t.truncatingRemainder(dividingBy: cycleDuration)) / cycleDuration)
                // Sweep band fully across screen once per cycle.
                let phase = -1.2 + progress * 2.8

                ZStack {
                    AppColor.backgroundLight
                        .opacity(baseFillOpacity)
                        .ignoresSafeArea()

                    Rectangle()
                        .fill(
                            LinearGradient(
                                stops: [
                                    .init(color: .white.opacity(0.05), location: 0.0),
                                    .init(color: .white.opacity(0.55), location: 0.42),
                                    .init(color: accent.opacity(centerOpacity), location: 0.50),
                                    .init(color: .white.opacity(0.45), location: 0.58),
                                    .init(color: .white.opacity(0.06), location: 1.0)
                                ],
                                startPoint: .topLeading,
                                endPoint: .bottomTrailing
                            )
                        )
                        .frame(width: max(w, h) * bandWidthFraction, height: max(w, h) * 1.35)
                        .rotationEffect(.degrees(22))
                        .offset(x: phase * w * 1.25, y: phase * h * 0.08)
                        .blendMode(.plusLighter)
                }
                .frame(width: w, height: h)
            }
        }
        .allowsHitTesting(true)
    }
}

struct ProgressViewReusable: View {
    var title: String = ""
    var color: Color = AppColor.celticBlue
    var style: ProgressLoaderStyle = .standard
    /// When `nil`, a sensible default scrim is used per style.
    /// Pass `false` for a lighter overlay (e.g. over tab bar) while shimmer stays visible.
    var dimsBackground: Bool? = nil

    private var scrimOpacity: CGFloat {
        switch style {
        case .connectedRide:
            if let dims = dimsBackground { return dims ? 0.22 : 0.12 }
            return 0.22
        case .standard:
            if let dims = dimsBackground { return dims ? 0.26 : 0.12 }
            return 0.20
        }
    }

    var body: some View {
        ZStack {
            Color.black.opacity(scrimOpacity)
                .ignoresSafeArea()
                .contentShape(Rectangle())

            DesignAlignedShimmerOverlay(style: style, accent: color)

            if !title.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                VStack {
                    Spacer()
                    Text(title)
                        .font(KlavikaFont.medium.font(size: 13))
                        .foregroundStyle(Color.primary.opacity(0.85))
                        .padding(.horizontal, 20)
                        .padding(.vertical, 10)
                        .background(
                            RoundedRectangle(cornerRadius: 12, style: .continuous)
                                .fill(AppColor.backgroundLight.opacity(0.94))
                                .shadow(color: .black.opacity(0.08), radius: 8, x: 0, y: 2)
                        )
                        .padding(.bottom, 36)
                }
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .ignoresSafeArea()
        .accessibilityElement(children: .combine)
        .accessibilityLabel(title.isEmpty ? "Loading" : title)
    }
}

#Preview {
    ProgressViewReusable(title: "Loading…", style: .standard)
}

#Preview("Connected") {
    ProgressViewReusable(title: "", style: .connectedRide)
}
