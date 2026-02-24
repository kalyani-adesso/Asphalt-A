//
//  ShareView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 14/10/25.
//

import SwiftUI

struct ShareView: View {
    @EnvironmentObject var viewModel: CreateRideViewModel
    @ObservedObject var upcomingViewModel: UpcomingRideViewModel
    @ObservedObject var home: HomeViewModel
    @State private var isPresented: Bool = false
    var body: some View {
        VStack(spacing: 20) {
            stepIndicator
            ScrollView {
                VStack(spacing: 30) {
                    SucessView(title:"Ride Created!", subtitle: "Share your ride with friends")
                    VStack(alignment: .leading, spacing: 15) {
                        Text(AppStrings.CreateRide.shareTitle)
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
                        
                        Text(AppStrings.CreateRide.shareSubTitle)
                            .font(KlavikaFont.medium.font(size: 16))
                            .foregroundColor(AppColor.black)
                        HStack(spacing: 15){
                            ShareIconButton(shareURL: viewModel.shareLink, platform: .whatsApp, icon: AppIcon.CreateRide.whatsapp, color: AppColor.darkCyanLimeGreen)
                            ShareIconButton(shareURL: viewModel.shareLink, platform: .facebook, icon: AppIcon.CreateRide.facebook, color: AppColor.skyBlue)
                            ShareIconButton(shareURL: viewModel.shareLink, platform: .twitter, icon: AppIcon.CreateRide.twitter, color: AppColor.lightBlue)
                            ShareIconButton(shareURL: viewModel.shareLink, platform: .mail, icon: AppIcon.CreateRide.mail, color: AppColor.pink)
                        }
                    }
                }
                .frame(width: 343, height: 480)
                .padding()
                .background(AppColor.backgroundLight)
                .cornerRadius(10)
                
            }
            
            
            HStack(spacing: 15) {
                
                ButtonView( title: AppStrings.CreateRideButton.done.rawValue,
                            showShadow: false , onTap: {
                    isPresented = true
                })
                
            }
            .padding()
        }
        .navigationDestination(isPresented: $isPresented, destination: {
            UpcomingRideView(viewModel: upcomingViewModel, showpopup: true, navigationDone: true, rideIdToOpen: .constant(nil))
//                .environmentObject(viewModel)
//                .environmentObject(UpcomingViewModel)
//                .environmentObject(home)
            
        })
    }
    var stepIndicator: some View {
        HStack(spacing: 32) {
            StepIndicator(icon: AppIcon.Home.createRide, title: "Details", isActive: true, isCurrentPage: false)
            StepIndicator(icon: AppIcon.CreateRide.route, title: "Route", isActive: true, isCurrentPage: false)
            if viewModel.ride.type?.rawValue != "Solo Ride" {
                StepIndicator(icon: AppIcon.Home.group, title: "Participants",isActive: true, isCurrentPage: false)
            }
            StepIndicator(icon: AppIcon.CreateRide.review, title: "Review",isActive: true, isCurrentPage: false)
            StepIndicator(icon: AppIcon.CreateRide.share, title: "Share",isActive: true, isCurrentPage: true)
        }
    }
    
}
struct ShareIconButton: View {
    let shareURL: String
    let platform: SharePlatform
    let icon: Image
    let color: Color
    
    var body: some View {
        Button(action: { shareDeepLink(shareURL, platform: platform) }) {
            icon
                .foregroundColor(.white)
                .frame(width: 68, height: 56)
                .background(color)
                .cornerRadius(14)
        }
        .disabled(shareURL.isEmpty)
    }
}

enum SharePlatform {
    case whatsApp, facebook, twitter, mail
}

func shareDeepLink(_ urlString: String, platform: SharePlatform) {
    guard !urlString.isEmpty, let url = URL(string: urlString) else { return }
    let encoded = urlString.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? urlString
    
    switch platform {
    case .whatsApp:
        if let u = URL(string: "https://wa.me/?text=\(encoded)") {
            UIApplication.shared.open(u)
        }
    case .facebook:
        if let u = URL(string: "https://www.facebook.com/sharer/sharer.php?u=\(encoded)") {
            UIApplication.shared.open(u)
        }
    case .twitter:
        if let u = URL(string: "https://twitter.com/intent/tweet?url=\(encoded)") {
            UIApplication.shared.open(u)
        }
    case .mail:
        guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
              let rootVC = windowScene.windows.first?.rootViewController else { return }
        let activity = UIActivityViewController(activityItems: [url], applicationActivities: nil)
        rootVC.present(activity, animated: true)
    }
}
