//
//  RatingView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 29/10/25.
//

import SwiftUI

struct RatingSheetView: View {
    
    @Binding var isPresented: Bool
    @State private var rating: Int = 0
    @State private var feedback: String = ""
    @State var showHome: Bool = false
    @EnvironmentObject var home: HomeViewModel
    @EnvironmentObject var viewModel : UpcomingRideViewModel
    
    var ratingText: String {
        switch rating {
        case 1: return "Needs improvement"
        case 2: return "Fair"
        case 3: return "Good"
        case 4: return "Great"
        case 5: return "Excellent"
        default: return ""
        }
    }
    
    var body: some View {
        ZStack {
            Color.black.opacity(0.4)
                .ignoresSafeArea()
                .onTapGesture {
                    withAnimation { isPresented = false }
                }
            VStack(spacing: 25) {
               
                VStack(spacing: 10) {
                    AppIcon.ConnectedRide.rateLogo
               
                    Text("Rate Your Ride")
                        .font(KlavikaFont.bold.font(size: 16))
                    
                    Text("How was your experience on this ride?")
                        .foregroundColor(AppColor.grey)
                        .font(KlavikaFont.regular.font(size: 16))
                }
              
                Spacer()
                // Rating Stars
                RateThisRide
                
                Text(ratingText)
                    .font(KlavikaFont.bold.font(size: 18))
                    .foregroundColor(AppColor.celticBlue)
                Spacer()
                VStack(alignment: .leading, spacing: 8) {
                    HStack{
                       Image(systemName:"bubble.left")
                        Text("Share your feedback (optional)")
                            .foregroundColor(AppColor.grey)
                            .font(KlavikaFont.regular.font(size: 14))
                    }
                    TextField("Tell us about your ride experience...", text: $feedback, axis: .vertical)
                        .foregroundColor(AppColor.grey)
                        .font(KlavikaFont.regular.font(size: 16))
                        .padding()
                        .padding(.bottom,50)
                        .frame(height: 100)
                        .background(Color.backgroundLight)
                        .cornerRadius(10)
                }
              
                RideButtonsView
            }
            .padding()
            
            .frame(width: 345, height: 642)
            .padding(.vertical, 10)
            .background(AppColor.white)
            .cornerRadius(16)
            .shadow(radius: 10)
            .navigationDestination(isPresented: $showHome, destination: {
                UpcomingRidesView()
                    .environmentObject(home)
                    .environmentObject(viewModel)
            })
        }
    }
    @ViewBuilder var RateThisRide: some View {
        VStack(alignment:.leading) {
            RatingView(rating: $rating, iconRate: AppIcon.ConnectedRide.rate, iconNotRate: AppIcon.ConnectedRide.notRate)
        }
        .padding(.horizontal, 20)
    }
    
    @ViewBuilder var RideButtonsView: some View {
        HStack(spacing: 20) {
            Button(action: {
                isPresented = false
            }) {
                HStack {
                    Text(AppStrings.ConnectedRide.skip.uppercased())
                        .font(KlavikaFont.bold.font(size: 16))
                        .foregroundStyle(AppColor.celticBlue)
                }
                .frame(maxWidth: .infinity, minHeight: 50)
                .background(
                    RoundedRectangle(cornerRadius: 10)
                        .fill(AppColor.white)
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(AppColor.celticBlue, lineWidth: 1)
                )
                
            }
            .buttonStyle(.plain)
            ButtonView(title: AppStrings.ConnectedRide.submitRating.uppercased(),onTap: {
                isPresented = false
            })
            .disabled(rating == 0)
            .opacity(rating == 0 ? 0.9 : 1.0)
            .frame(maxWidth: .infinity)
        }
    }
}



#Preview {
    RatingSheetView(isPresented: .constant(true))
}
