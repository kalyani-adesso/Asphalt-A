//
//  ToastView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 03/10/25.
//


import SwiftUI

struct ToastView: View {
    let message: String
    @Binding var isShowing: Bool
    var body: some View {
        VStack {
            Spacer()
            if isShowing {
                Text(message)
                    .multilineTextAlignment(.center)
                    .fixedSize(horizontal: false, vertical: true)
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(Color.black.opacity(0.8))
                    .foregroundColor(.white)
                    .cornerRadius(8)
                    .padding(.horizontal, 16)
                    .transition(.opacity.combined(with: .move(edge: .bottom)))
                    .onAppear {
                        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                            withAnimation {
                                isShowing = false
                            }
                        }
                    }
            }
        }
        .padding(.bottom,20)
        
    }
}

