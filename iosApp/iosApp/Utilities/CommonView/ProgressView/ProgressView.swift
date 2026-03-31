//
//  ProgressView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 04/11/25.
//

import SwiftUI

struct ProgressViewReusable: View {
    var title: String = "Loading..."
    var color: Color = AppColor.white

    var body: some View {
        ZStack {
            // Subtle dim (no heavy overlay).
            Color.black.opacity(0.18)
                .ignoresSafeArea()

            HStack(spacing: 10) {
                ProgressView()
                    .progressViewStyle(.circular)
                    .tint(color)
                    .scaleEffect(0.85)

                if !title.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                    Text(title)
                        .font(KlavikaFont.medium.font(size: 13))
                        .foregroundStyle(Color.white.opacity(0.92))
                        .lineLimit(1)
                }
            }
            .padding(.vertical, 10)
            .padding(.horizontal, 14)
            .background(Color.black.opacity(0.72))
            .clipShape(Capsule())
            .shadow(color: Color.black.opacity(0.20), radius: 10, x: 0, y: 6)
            .padding(.horizontal, 24)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

#Preview {
    ProgressViewReusable(title: "Loading...")
}
