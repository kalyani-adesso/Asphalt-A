//
//  ButtonView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 30/09/25.
//

import SwiftUI

struct ButtonView<Destination: View>: View {
    
    let title: String
    @ViewBuilder let destination: () -> Destination
    
    var body: some View {
        NavigationLink(destination: destination) {
            Text(title)
                .frame(maxWidth: .infinity)
                .frame(height: 60)
                .background(
                    LinearGradient(
                        gradient: Gradient(colors: [
                            AppColor.royalBlue,
                            AppColor.pursianBlue,
                        ]),
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                )
                .cornerRadius(15)
                .shadow(color: Color.black.opacity(0.20), radius: 4, x: 0, y: 2)
                .foregroundStyle(AppColor.white)
                .font(KlavikaFont.bold.font(size: 18))
                .padding(.bottom, 21)
        }
    }
}

#Preview {
    NavigationStack {
        ButtonView(title: "Preview") {
            Text("Destination")
                .padding()
        }
        .padding()
    }
}
