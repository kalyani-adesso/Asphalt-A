//
//  ShareView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 14/10/25.
//

import SwiftUI

struct ShareView: View {
    @ObservedObject var viewModel: CreateRideViewModel
    @State private var isPresented: Bool = false
    var body: some View {
        VStack(spacing: 20) {
            stepIndicator
            VStack(alignment: .leading, spacing: 20) {
                VStack{
                VStack(spacing: 15){
                    Image(systemName:"checkmark.circle.fill")
                        .resizable()
                        .frame(width: 100, height: 100)
                        .foregroundColor(AppColor.darkCyanLimeGreen)
                    Text("Ride Created!")
                        .font(KlavikaFont.bold.font(size: 16))
                        .foregroundColor(AppColor.black)
                    Text("Share your ride with friends")
                        .font(KlavikaFont.regular.font(size: 12))
                        .foregroundColor(AppColor.stoneGray)
                    
                }
                FormFieldView(
                    label: "Share Link",
                    icon:  AppIcon.CreateRide.copy,
                    placeholder: "https://adessoriderclub.app/12121312",
                    iconColor: AppColor.darkRed,
                    value: .constant("https://adessoriderclub.app/12121312"),
                    isValidEmail: .constant(false),
                    backgroundColor: AppColor.white)
            }
            .padding()
                VStack(alignment: .leading){
                Text("Share Via")
                    .font(KlavikaFont.medium.font(size: 16))
                    .foregroundColor(AppColor.black)
                HStack(spacing: 20){
                    AppIcon.CreateRide.whatsapp
                        .resizable()
                        .renderingMode(.template)
                        .foregroundColor(AppColor.white)
                        .frame(width: 15, height: 15)
                        .padding(8)
                        .background(AppColor.darkCyanLimeGreen)
                        .cornerRadius(5)
                    AppIcon.CreateRide.facebook
                        .resizable()
                        .renderingMode(.template)
                        .foregroundColor(AppColor.white)
                        .frame(width: 20, height: 20)
                        .padding(8)
                        .background(AppColor.skyBlue)
                        .cornerRadius(5)
                    AppIcon.CreateRide.twitter
                        .resizable()
                        .renderingMode(.template)
                        .foregroundColor(AppColor.white)
                        .frame(width: 20, height: 20)
                        .padding(8)
                        .background(AppColor.lightBlue)
                        .cornerRadius(5)
                    AppIcon.CreateRide.mail
                        .resizable()
                        .renderingMode(.template)
                        .foregroundColor(AppColor.white)
                        .frame(width: 20, height: 20)
                        .padding(8)
                        .background(AppColor.pink)
                        .cornerRadius(5)
                }
            }
            .padding()
        }
            .frame(width: 343, height: 500)
            .padding()
            .background(AppColor.backgroundLight)
            .cornerRadius(10)
           
        }
        
      
        HStack(spacing: 15) {
            
            ButtonView( title: AppStrings.CreateRide.done.rawValue,
                        showShadow: false , onTap: {
                isPresented = true
            }
            ).navigationDestination(isPresented: $isPresented, destination: {
                YourRideScreen()
            })
            
        }
        .padding()
    }
    var stepIndicator: some View {
        HStack(spacing: 32) {
            StepIndicator(icon: AppIcon.Home.createRide, title: "Details", isActive: true, isCurrentPage: false)
            StepIndicator(icon: AppIcon.CreateRide.route, title: "Route", isActive: true, isCurrentPage: false)
            StepIndicator(icon: AppIcon.Home.group, title: "Participants",isActive: true, isCurrentPage: false)
            StepIndicator(icon: AppIcon.CreateRide.review, title: "Review",isActive: true, isCurrentPage: false)
            StepIndicator(icon: AppIcon.CreateRide.share, title: "Share",isActive: true, isCurrentPage: true)
        }
    }
}

#Preview {
    ShareView(viewModel: CreateRideViewModel.init())
}
