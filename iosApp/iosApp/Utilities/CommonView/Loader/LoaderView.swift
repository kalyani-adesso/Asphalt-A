//
//  LoaderView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 18/10/25.
//

import SwiftUI

/// Three small dots that bounce horizontally (side-to-side), staggered.
struct HorizontalBouncingDotsLoader: View {
    var color: Color = AppColor.celticBlue
    var dotSize: CGFloat = 7
    var spacing: CGFloat = 6
    var travel: CGFloat = 5
    var duration: Double = 0.62
    var stagger: Double = 0.12

    @State private var bounce = false

    var body: some View {
        HStack(spacing: spacing) {
            ForEach(0..<3, id: \.self) { index in
                Circle()
                    .fill(color)
                    .frame(width: dotSize, height: dotSize)
                    .offset(x: bounce ? travel : -travel)
                    .opacity(bounce ? 1.0 : 0.78)
                    .scaleEffect(bounce ? 1.0 : 0.92)
                    .animation(
                        .interactiveSpring(response: duration, dampingFraction: 0.82, blendDuration: 0.1)
                            .repeatForever(autoreverses: true)
                            .delay(Double(index) * stagger),
                        value: bounce
                    )
            }
        }
        .onAppear { bounce = true }
    }
}

struct LoadingView: View {
    @State private var animate = false
    let color: Color = AppColor.celticBlue

    var body: some View {
        HStack(spacing: 12) {
            ForEach(0..<3, id: \.self) { index in
                Circle()
                    .fill(color)
                    .frame(width: animate ? 32 : 21, height: animate ? 32 : 21)
                    .animation(
                        .easeInOut(duration: 0.6)
                            .repeatForever(autoreverses: true)
                            .delay(Double(index) * 0.2),
                        value: animate
                    )
            }
        }
        .frame(width: 103, height: 32, alignment: .center)
        .onAppear { animate = true }
    }
}

#Preview {
    LoadingView()
}


