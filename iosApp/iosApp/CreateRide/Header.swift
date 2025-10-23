//
//  HeaderView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 15/10/25.
//

import SwiftUI

struct Header: View {
    @ObservedObject var viewModel: CreateRideViewModel
    @Environment(\.presentationMode) var presentationMode
    @State private var goToHome = false
    var body: some View {
        VStack(spacing: 0) {
            HStack {
                Button {
                    if viewModel.currentStep > 1 {
                        viewModel.previousStep()
                    } else {
                        goToHome = true
                    }
                } label: {
                    AppIcon.CreateRide.backButton
                        .frame(width: 24, height: 24)
                }
                
                Spacer()
                
                Text("Create a Ride")
                    .font(KlavikaFont.bold.font(size: 19))
                    .foregroundColor(AppColor.black)
                
                Spacer()
                
                Text("\(viewModel.currentStep)/5")
                    .font(KlavikaFont.medium.font(size: 16))
                    .foregroundColor(AppColor.black)
            }
            .padding(.horizontal)
            .padding(.vertical, 12)
            .background(AppColor.white)
            .shadow(color: Color.black.opacity(0.08), radius: 4, x: 0, y: 2)
            .navigationBarBackButtonHidden(true)
            .navigationDestination(isPresented: $goToHome, destination: {
                BottomNavBar()
            })
        }
        .padding(.bottom,15)
    }
}

#Preview {
    Header(viewModel: CreateRideViewModel.init())
}
