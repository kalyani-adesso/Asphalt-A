//
//  StepIndicator.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import SwiftUI

struct StepIndicator: View {
    let icon: Image
    let title: String
    var isActive: Bool = false
    var isCurrentPage: Bool = false

    var body: some View {
        VStack {
           icon
                .renderingMode(.template)
                .foregroundColor(isActive ? AppColor.white: AppColor.stoneGray)
                .font(.system(size: 20))
                .padding(10)
                .background(
                    isActive
                           ? LinearGradient(
                               gradient: Gradient(colors: [
                                   AppColor.royalBlue,
                                   AppColor.pursianBlue
                               ]),
                               startPoint: .leading,
                               endPoint: .trailing
                             )
                           : LinearGradient(
                            gradient: Gradient(colors: [
                                Color.clear,
                                Color.clear,
                            ]),
                            startPoint: .leading,
                            endPoint: .trailing
                          )
                )
                .clipShape(RoundedRectangle(cornerRadius: 10))

            Text(title)
                .font(isCurrentPage ? KlavikaFont.bold.font(size: 12) : KlavikaFont.regular.font(size: 12))
                .foregroundColor(isCurrentPage ? AppColor.black : AppColor.stoneGray)
        }
    }
}

#Preview {
    StepIndicator(icon: AppIcon.Home.createRide, title: "Details", isActive: true)
}
