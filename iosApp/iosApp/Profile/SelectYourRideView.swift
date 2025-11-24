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
    @Binding var isPresented: Bool
    
    private let totalPages = 7
    @ObservedObject var viewModel:ProfileViewModel
    var body: some View {
        VStack {
            ScrollView {
                VStack {
                    Group {
                        ProfileTitleView(title:AppStrings.SelectRide.selectingRide, subtitle: AppStrings.SelectRide.chooseType, icon: AppIcon.Profile.ride)
                            .padding(.horizontal,15)
                            .padding(.top,20)
                        SelectBikeTabView(image:viewModel.vehicleImageArray ,vehicleArray: viewModel.vehicleArray,  currentPage:$currentPage , totalPages: totalPages)
                        AddBikeFieldView(label: AppStrings.SelectRide.make, placeholder: AppStrings.SelectRide.selectMake, inputText: $make)
                        AddBikeFieldView(label: AppStrings.SelectRide.model, placeholder: AppStrings.SelectRide.model, inputText: $model)
                        ButtonView(title: AppStrings.SelectRide.addVehicle, onTap: {
                            Task {
                                await viewModel.addNewBike(userId: MBUserDefaults.userIdStatic ?? "", model: model, make: make, type:viewModel.vehicleArray[currentPage].constantValue)
                            }
                           
                            isPresented = false
                        })
                        .disabled(viewModel.validateMake(make: make, moodel: model))
                    }
                    .padding()
                }
                .frame(maxWidth: .infinity,maxHeight: .infinity)
                .background(AppColor.listGray)
                .cornerRadius(10)
                .padding(EdgeInsets(top: 15, leading: 15, bottom: 50, trailing: 15))
            }
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
    let image: [Image]
    let vehicleArray: [AppStrings.VehicleType]
    @Binding var currentPage: Int
    let totalPages: Int
    
    private var title: String {
        guard currentPage < vehicleArray.count else { return "" }
        return vehicleArray[currentPage].rawValue
    }
    
    private var imageName: Image {
        guard currentPage < image.count else { return Image("") }
        return image[currentPage]
    }
    
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
            imageName
                .resizable()
                .frame(width: 311, height: 164)
                .cornerRadius(10)
                .animation(.easeInOut, value: currentPage)
            
            Text(title)
                .font(KlavikaFont.bold.font(size: 16))
                .foregroundColor(AppColor.black)
                .multilineTextAlignment(.center)
        }
    }
}

struct AddBikeView: View {
    let bikeId: UUID
    let viewModel:ProfileViewModel
    let title:String
    let subtitle:String
    var body: some View {
        HStack(spacing: 20) {
            AppIcon.Profile.addBike
                .resizable()
                .frame(width: 30, height: 30)
            VStack(alignment: .leading,spacing: 5) {
                Text(title)
                    .font(KlavikaFont.bold.font(size: 16))
                    .foregroundColor(AppColor.black)
                Text(subtitle)
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(.stoneGray)
            }
            Spacer()
            Button(action: {
                Task {
                    await viewModel.deleteSelectedBikeType(id: bikeId)
                }
            }) {
                AppIcon.Profile.deleteBike
                    .resizable()
                    .frame(width: 30,height: 30)
            }
            .buttonStyle(.plain)
            .contentShape(Rectangle())
        }
    }
}
