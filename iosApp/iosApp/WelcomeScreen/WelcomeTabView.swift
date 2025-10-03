//
//  WelcomeTabView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 26/09/25.
//

import SwiftUI

public struct WelcomeTabView: View {
    let image: Image
    let title: String
    let subtitle: String
    @Binding var currentPage: Int
    let totalPages: Int
    
    public var body: some View {
        VStack(spacing: 20) {
            
            ZStack {
                image
                    .resizable()
                    .scaledToFill()
                    .frame(width: 296, height: 307)
                    .clipShape(RoundedRectangle(cornerRadius: 40))
                    .clipped()
                
                RoundedRectangle(cornerRadius: 40)
                    .stroke(Color.white, lineWidth: 2)
                    .frame(width: 296, height: 307)
            }
            
            HStack(spacing: 8) {
                ForEach(0..<totalPages, id: \.self) { index in
                    Circle()
                        .fill(index == currentPage ? Color.white : Color.white.opacity(0.4))
                        .frame(width: 8, height: 8)
                }
            }

            Text(title)
                .font(.custom("Klavika-Bold", size: 26))
                .foregroundColor(.white)
                .multilineTextAlignment(.center)
                .frame(width: 227, height: 62)
                .fixedSize(horizontal: false, vertical: true)

            Text(subtitle)
                .font(.custom("Klavika-Regular", size: 17))
                .foregroundColor(.white.opacity(0.9))
                .multilineTextAlignment(.center)
                .frame(width: 290, height: 62)
                .fixedSize(horizontal: false, vertical: true)
            
        }
    }
}

