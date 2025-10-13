//
//  SelectYourRideView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 09/10/25.
//

import SwiftUI

struct SelectYourRideView: View {
    @State var make: String = ""
    @State var model: String = ""
    @State private var currentPage = 0
    private let totalPages = 8
    var body: some View {
        VStack {
            VStack {
                Group {
                    ProfileTitleView(title:AppStrings.SelectRide.selectingRide, subtitle: AppStrings.SelectRide.chooseType, icon: AppIcon.Profile.ride)
                        .padding(.horizontal,15)
                        .padding(.top,20)
                    SelectBikeTabView(image:AppIcon.Profile.sportsBike , title: AppStrings.VehicleType.sportBike.rawValue, subtitle: AppStrings.SelectRide.sportBikeDesc, currentPage:$currentPage , totalPages: totalPages)
                    AddBikeFieldView(label: AppStrings.SelectRide.make, placeholder: AppStrings.SelectRide.selectMake, inputText: $make)
                    AddBikeFieldView(label: AppStrings.SelectRide.model, placeholder: AppStrings.SelectRide.model, inputText: $make)
                    ButtonView(title: AppStrings.SelectRide.addVehicle, onTap: {
                        
                    })
                }
                .padding()
            }
            .frame(maxWidth: .infinity,maxHeight: .infinity)
            .background(AppColor.listGray)
            .cornerRadius(10)
            .padding(EdgeInsets(top: 50, leading: 15, bottom: 0, trailing: 15))
        }
        .frame(maxWidth: .infinity,maxHeight: .infinity)
        .background(AppColor.darkgray)
    }
}

struct AddBikeFieldView: View {
    let label: String
    let placeholder: String
    @Binding var inputText: String
    
    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text(label)
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundStyle(AppColor.black)
            HStack {
                TextField(placeholder, text: $inputText)
                    .onChange(of: inputText) { value in
                        
                    }
                    .font(KlavikaFont.regular.font(size: 16))
                    .autocapitalization(.none)
                    .foregroundStyle(AppColor.richBlack)
            }
            .padding()
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.white)
            )
            .cornerRadius(10)
        }
    }
}

private struct SelectBikeTabView: View {
    let image: Image
    let title: String
    let subtitle: String
    @Binding var currentPage: Int
    let totalPages: Int
    
    public var body: some View {
        VStack(spacing: 16) {
            HStack {
                Button(action: {
                    if currentPage > 0 {
                        withAnimation(.easeInOut) {
                            currentPage -= 1
                        }
                    }
                }) {
                    AppIcon.Profile.leftIcon
                        .opacity(currentPage == 0 ? 0.3 : 1.0)
                }
                Spacer()
                VStack(spacing: 6) {
                    Text("\(currentPage + 1) of \(totalPages)")
                        .font(KlavikaFont.regular.font(size: 12))
                        .foregroundColor(AppColor.stoneGray)
                    
                    HStack(spacing: 6) {
                        ForEach(0..<totalPages, id: \.self) { index in
                            Circle()
                                .fill(index == currentPage ? AppColor.celticBlue : AppColor.celticBlue.opacity(0.2))
                                .frame(width: 8, height: 8)
                        }
                    }
                }
                Spacer()
                Button(action: {
                    if currentPage < totalPages - 1 {
                        withAnimation(.easeInOut) {
                            currentPage += 1
                        }
                    }
                }) {
                    AppIcon.Profile.rightIcon
                        .opacity(currentPage == totalPages - 1 ? 0.3 : 1.0)
                }
            }
            image
                .resizable()
                .scaledToFill()
                .frame(width: 311, height: 164)
                .clipShape(RoundedRectangle(cornerRadius: 40))
                .clipped()
                .animation(.easeInOut, value: currentPage)
            
            Text(title)
                .font(KlavikaFont.bold.font(size: 16))
                .foregroundColor(AppColor.black)
                .multilineTextAlignment(.center)
            
            Text(subtitle)
                .font(KlavikaFont.regular.font(size: 12))
                .foregroundColor(AppColor.stoneGray)
                .multilineTextAlignment(.center)
        }
    }
}

#Preview {
    SelectYourRideView()
}
