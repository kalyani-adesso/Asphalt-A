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
            ScrollView {
                VStack(spacing: 30) {
                    SucessView(title:"Ride Created!", subtitle: "Share your ride with friends")
                    VStack(alignment: .leading, spacing: 15) {
                        Text("Share Link")
                            .font(KlavikaFont.medium.font(size: 16))
                            .foregroundColor(AppColor.black)
                        
                        HStack {
                            Text(viewModel.shareLink)
                                .font(KlavikaFont.regular.font(size: 14))
                                .foregroundColor(AppColor.stoneGray)
                                .lineLimit(1)
                                .truncationMode(.middle)
                            Spacer()
                            Button(action: {
                                UIPasteboard.general.string = viewModel.shareLink
                            }) {
                                AppIcon.CreateRide.copy
                            }
                        }
                        .padding()
                        .background(Color.white)
                        .cornerRadius(10)
                        Spacer() .frame(height: 0)
                  
                        Text("Share Via")
                            .font(KlavikaFont.medium.font(size: 16))
                            .foregroundColor(AppColor.black)
                        HStack(spacing: 15){
                            ShareIconButton(icon: AppIcon.CreateRide.whatsapp, color: AppColor.darkCyanLimeGreen)
                            ShareIconButton(icon: AppIcon.CreateRide.facebook, color: AppColor.skyBlue)
                            ShareIconButton(icon: AppIcon.CreateRide.twitter, color: AppColor.lightBlue)
                            ShareIconButton(icon: AppIcon.CreateRide.mail, color: AppColor.pink)
                        }
                    }
                }
                .frame(width: 343, height: 480)
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
                   UpcomingRideView()
                })
                
            }
            .padding()
        }
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
struct ShareIconButton: View {
    let icon: Image
    let color: Color
    
    var body: some View {
        Button(action: {}) {
            icon
                .foregroundColor(.white)
                .frame(width: 68, height: 56)
                .background(color)
                .cornerRadius(14)
        }
        
    }
}

#Preview {
    ShareView(viewModel: CreateRideViewModel.init())
}
