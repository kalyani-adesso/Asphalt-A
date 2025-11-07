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
                Color.black.opacity(0.4)
                    .ignoresSafeArea()
                
                ProgressView(title)
                    .progressViewStyle(CircularProgressViewStyle(tint: color))
                    .foregroundColor(.white)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
}

#Preview {
    ProgressView()
}
