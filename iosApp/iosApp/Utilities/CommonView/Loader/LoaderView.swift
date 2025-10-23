//
//  LoaderView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 18/10/25.
//

import SwiftUI

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


