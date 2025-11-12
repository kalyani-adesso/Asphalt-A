//
//  ConnectedRideView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 18/10/25.
//

import SwiftUI

struct ConnectedRideView: View {
    let notificationTitle:String
    let title:String
    let subTitle:String
    let model:JoinRideModel
    @State var showView = false
    var body: some View {
        VStack {
            HStack(spacing: 10) {
                AppIcon.ConnectedRide.checkmark
                    .padding(.leading,20)
                Text(notificationTitle)
                    .font(KlavikaFont.bold.font(size: 14))
                    .foregroundStyle(.spanishGreen)
                Spacer()
            }
            .frame(maxWidth: .infinity)
            .frame(height: 60)
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.lightGreen)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(AppColor.aeroGreen, lineWidth: 2)
            )
            .padding([.leading, .trailing,.top],16)
            .padding(.bottom,183)
            DisplayView
                .padding(.bottom,32)
            LoadingView()
            Spacer()
        }
        .navigationTitle(AppStrings.ConnectedRide.connectedRide)
        .navigationBarBackButtonHidden()
        .navigationBarTitleDisplayMode(.inline)
        .navigationDestination(isPresented: $showView, destination: {
            if title == AppStrings.ConnectedRide.rideMessage {
                ConnectedRideCompleteView(viewModel:model )
            } else {
                if #available(iOS 17.0, *) {
                    ConnectedRideMapView(rideModel: model)
                }
            }
        })
        .onAppear {
            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                self.showView = true
            }
        }
        .toolbar {
            ToolbarItem(placement: .topBarLeading, content: {
                Button(action: {
               //    dismiss()
                }, label:{
                    AppIcon.CreateRide.backButton
                })
            })
        }
    }
    
    @ViewBuilder var DisplayView: some View {
        VStack {
            AppImage.Logos.rider
                .resizable()
                .frame(width: 50,height: 60)
                .padding(.bottom,16)
            Text(title)
                .font(KlavikaFont.bold.font(size: 22))
                .foregroundStyle(AppColor.richBlack)
                .padding(.bottom,6)
            Text(subTitle)
                .font(KlavikaFont.regular.font(size: 16))
                .foregroundStyle(AppColor.oldBurgundy)
        }
    }
}
