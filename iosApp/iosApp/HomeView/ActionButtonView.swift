//
//  DashboardView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct ActionButtonView: View {
    @State private var isPresented: Bool = false
    @State private var showJoinRide: Bool = false
    
    var body: some View {
        HStack(spacing: 12) {
            
            ButtonView( title: AppStrings.HomeLabel.createRide.rawValue,
                        icon: AppIcon.Home.createRide,
                        fontSize: 16, showShadow: false,  onTap: {
                    isPresented = true
                }
            )
            ButtonView( title: AppStrings.HomeLabel.joinRide.rawValue,
                        icon: AppIcon.Home.group,
                        fontSize: 16, background: LinearGradient(
                            gradient: Gradient(colors: [.white, .white]),
                            startPoint: .leading,
                            endPoint: .trailing),
                        foregroundColor: AppColor.celticBlue,
                        showShadow: false,
                        borderColor: AppColor.celticBlue,onTap: {
               showJoinRide = true
            })
        }
        .padding(.vertical,10)
        .navigationDestination(isPresented: $isPresented, destination: {
            CreateRideView()
        })
        .navigationDestination(isPresented: $showJoinRide, destination: {
            JoinRideView()
        })
    }
}



#Preview {
    ActionButtonView()
}
