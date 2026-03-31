//
//  ProgressView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 04/11/25.
//

import SwiftUI

enum ProgressLoaderStyle {
    case standard
    case connectedRide
}

private struct ShimmerModifier: ViewModifier {
    @State private var phase: CGFloat = -0.8

    func body(content: Content) -> some View {
        content
            .overlay {
                GeometryReader { proxy in
                    let w = proxy.size.width
                    Rectangle()
                        .fill(
                            LinearGradient(
                                gradient: Gradient(colors: [
                                    Color.clear,
                                    Color.white.opacity(0.70),
                                    Color.clear
                                ]),
                                startPoint: .top,
                                endPoint: .bottom
                            )
                        )
                        .frame(width: w * 0.55)
                        .rotationEffect(.degrees(18))
                        .offset(x: phase * w)
                        .blendMode(.screen)
                }
                .clipped()
            }
            .onAppear {
                withAnimation(.linear(duration: 1.05).repeatForever(autoreverses: false)) {
                    phase = 1.8
                }
            }
    }
}

private extension View {
    func shimmer() -> some View { modifier(ShimmerModifier()) }
}

/// A full-screen shimmer *overlay* meant to sit on top of an existing UI.
/// This avoids a generic "loading screen" by keeping the underlying UI visible and
/// drawing a dense skeleton layer above it, with an animated highlight pass.
private struct FullPageShimmerOverlay: View {
    @State private var highlightPhase: CGFloat = -1.2
    @State private var drift: CGFloat = 0

    var body: some View {
        GeometryReader { proxy in
            let size = proxy.size

            ZStack {
                // Clean highlight sweep across the existing UI.
                Rectangle()
                    .fill(
                        LinearGradient(
                            gradient: Gradient(stops: [
                                .init(color: .clear, location: 0.0),
                                .init(color: Color.white.opacity(0.78), location: 0.5),
                                .init(color: .clear, location: 1.0)
                            ]),
                            startPoint: .top,
                            endPoint: .bottom
                        )
                    )
                    .frame(width: size.width * 0.75)
                    .rotationEffect(.degrees(18))
                    .offset(x: (highlightPhase * size.width) + (drift * 10))
                    .blendMode(.screen)
                    .opacity(0.72)
                    .blur(radius: 0.6)
                    .ignoresSafeArea()
            }
            .ignoresSafeArea()
            .onAppear {
                withAnimation(.linear(duration: 1.15).repeatForever(autoreverses: false)) {
                    highlightPhase = 2.2
                }
                withAnimation(.easeInOut(duration: 2.6).repeatForever(autoreverses: true)) {
                    drift = 1
                }
            }
        }
        .allowsHitTesting(true)
    }

}

struct ProgressViewReusable: View {
    // Default empty; only Connected Ride should show visible loading text.
    var title: String = ""
    var color: Color = AppColor.celticBlue
    var style: ProgressLoaderStyle = .standard
    /// When `nil`, we choose a sensible default:
    /// - `.standard`: no black dim (keeps page visible)
    /// - `.connectedRide`: dim to match existing flow
    var dimsBackground: Bool? = nil

    var body: some View {
        ZStack {
            // Always block interaction with underlying UI while loader is visible.
            // (Even when we don't dim the background.)
            Color.clear
                .ignoresSafeArea()
                .contentShape(Rectangle())
                .onTapGesture { }

            let shouldDim = dimsBackground ?? (style == .connectedRide)
            if shouldDim {
                Color.black.opacity(style == .connectedRide ? 0.18 : 0.15)
                    .ignoresSafeArea()
            }

            Group {
                switch style {
                case .standard:
                    // Shimmer overlay on top of the existing UI (no generic skeleton page).
                    FullPageShimmerOverlay()

                case .connectedRide:
                    VStack(spacing: 8) {
                        HorizontalBouncingDotsLoader(
                            color: AppColor.celticBlue,
                            dotSize: 7,
                            spacing: 6,
                            travel: 5,
                            duration: 0.62,
                            stagger: 0.12
                        )
                        if !title.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                            Text(title)
                                .font(KlavikaFont.medium.font(size: 12))
                                .foregroundStyle(Color.white.opacity(0.88))
                                .lineLimit(1)
                        }
                    }
                    .padding(.vertical, 12)
                    .padding(.horizontal, 16)
                    .background(Color.black.opacity(0.72))
                    .clipShape(RoundedRectangle(cornerRadius: 14, style: .continuous))
                    .shadow(color: Color.black.opacity(0.20), radius: 10, x: 0, y: 5)
                    .padding(.horizontal, 24)
                }
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .ignoresSafeArea()
    }
}

#Preview {
    ProgressViewReusable(title: "", style: .connectedRide)
}
