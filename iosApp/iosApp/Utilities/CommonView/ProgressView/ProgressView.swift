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

struct ProgressViewReusable: View {
    var title: String = "Loading..."
    var color: Color = AppColor.celticBlue
    var style: ProgressLoaderStyle = .standard
    var dimsBackground: Bool = true

    var body: some View {
        ZStack {
            if dimsBackground {
                Color.black.opacity(style == .connectedRide ? 0.18 : 0.15)
                    .ignoresSafeArea()
            }

            Group {
                switch style {
                case .standard:
                    HStack(spacing: 10) {
                        HorizontalBouncingDotsLoader(color: color, dotSize: 7, spacing: 6)
                        if !title.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                            Text(title)
                                .font(KlavikaFont.medium.font(size: 13))
                                .foregroundStyle(color)
                                .lineLimit(1)
                        }
                    }
                    .padding(.vertical, 10)
                    .padding(.horizontal, 14)
                    .background(Color.white)
                    .clipShape(Capsule())
                    .shadow(color: Color.black.opacity(0.08), radius: 12, x: 0, y: 4)
                    .padding(.horizontal, 24)

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
    }
}

#Preview {
    ProgressViewReusable(title: "Loading...")
}
