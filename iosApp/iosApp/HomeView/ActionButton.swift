//
//  DashboardView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct ActionButton: View {
    var body: some View {
        HStack(spacing: 12) {
            
            ButtonView( title: AppStrings.HomeLabel.createRide.rawValue,
                        icon: AppIcon.Home.createRide,
                        showShadow: false )
            
            ButtonView( title: AppStrings.HomeLabel.joinRide.rawValue,
                        icon: AppIcon.Home.group,
                        background: LinearGradient(
                                    gradient: Gradient(colors: [.white, .white]),
                                    startPoint: .leading,
                                    endPoint: .trailing),
                        foregroundColor: AppColor.celticBlue,
                        showShadow: false ,
                        borderColor: AppColor.celticBlue)
        }
        .padding(.top,20)
    }
}



#Preview {
    ActionButton()
}
