//
//  HeaderViewHome.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import Foundation
import SwiftUI

struct HeaderViewHome: View {
    let userName: String
    let location: String

    var body: some View {
        HStack {
            Button {
                // menu action
            } label: {
                Image(systemName: "line.horizontal.3")
                    .font(.title2)
                    .foregroundColor(.primary)
            }

            Spacer()

            VStack(spacing: 2) {
                Text("Hello \(userName)")
                    .font(.system(size: 18, weight: .semibold))
                HStack(spacing: 6) {
                    Circle().fill(Color.green).frame(width: 6, height: 6)
                    Text(location)
                        .font(.footnote)
                        .foregroundColor(.secondary)
                }
            }

            Spacer()

            Button {
                // notifications
            } label: {
                ZStack(alignment: .topTrailing) {
                    Image(systemName: "bell")
                        .font(.title2)
                        .foregroundColor(.primary)
                    Circle().fill(Color.red).frame(width: 8, height: 8).offset(x: 6, y: -6)
                }
            }
        }
        .padding(.vertical, 6)
    }
}
#Preview {
    HeaderViewHome(userName: "Lavanya", location: "Chennai")
}
