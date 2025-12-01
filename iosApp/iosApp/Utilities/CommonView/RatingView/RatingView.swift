//
//  RatingView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 19/10/25.
//


import SwiftUI

struct RatingView: View {
    @Binding var rating: Int
    let maxRating: Int = 5
    var height: CGFloat? = 48
    var width: CGFloat? = 51
    let iconRate: Image
    let iconNotRate: Image
    var didSelect: (() -> Void)? = nil
    var body: some View {
        HStack(spacing: 15) {
            ForEach(1...maxRating, id: \.self) { index in
                Button {
                    rating = index
                    didSelect?()
                } label: {
                    (index <= rating ? iconRate : iconNotRate)
                        .resizable()
                        .scaledToFit()
                        .frame(width: width, height: height)
                        .animation(.easeInOut(duration: 0.2), value: rating)
                }
                .buttonStyle(.plain)
            }
        }
        
        .frame(maxWidth: .infinity)
    }
}
