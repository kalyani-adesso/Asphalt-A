//
//  ProfileScreen.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 07/10/25.
//

import SwiftUI

struct ProfileScreen: View {
    @StateObject private var viewModel = ProfileViewModel()
    @State var showEditProfile: Bool = false
    @State var showEditRide: Bool = false
    @Environment(\.dismiss) var dismiss
    var onBackToHome: (() -> Void)? = nil
    var body: some View {
        ZStack {
            List {
                Section {
                    ProfileHeaderView(name: viewModel.profileName, email: viewModel.email, role: viewModel.role, image: viewModel.profileImage, phoneNumber: viewModel.phoneNumber)
                        .frame(height: 135)
                        .background(
                            RoundedRectangle(cornerRadius: 10)
                                .fill(AppColor.listGray)
                        )
                        .listRowInsets(EdgeInsets())
                        .listRowSeparator(.hidden)
                        .listRowBackground(Color.clear)
                        .padding([.leading, .trailing])
                }
                ForEach(viewModel.sections) { section in
                    ProfileSectionView(section: section, itemSelected: $showEditRide, selectedBikeType: viewModel.selectedBikeType, viewModel: viewModel)
                        .listRowSeparator(.hidden)
                        .listRowInsets(EdgeInsets(top: 20, leading: 0, bottom: 0, trailing: 0))
                        .listRowBackground(Color.clear)
                }
            }
            .listStyle(.plain)
            .padding(.top, 30)
            .background(AppColor.white)
            .sheet(isPresented: $showEditRide, onDismiss: {
                showEditRide = false
            }) {
                SelectYourRideView(isPresented: $showEditRide, viewModel: viewModel)
            }
            if showEditProfile {
                EditProfileView(isPresented: $showEditProfile)
            }
        }
        .navigationTitle(AppStrings.SignInLabel.clubName.localized)
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden()
        .toolbar {
            ToolbarItem(placement: .topBarLeading, content: {
                Button(action: {
                    onBackToHome?() 
                }, label:{
                    AppIcon.CreateRide.backButton
                })
            })
            ToolbarItem(placement: .navigationBarTrailing) {
                Button(action: {
                    showEditProfile = true
                }) {
                    AppIcon.Profile.editProfile
                        .resizable()
                        .frame(width: 30, height: 30)
                }
            }
        }
    }
}

struct ProfileSectionView: View {
    let section:ProfileSection
    @Binding var itemSelected: Bool
    let selectedBikeType:[SelectedBikeType]
    @ObservedObject var viewModel: ProfileViewModel
    var body: some View {
        VStack(spacing: 0) {
            ProfileTitleView(title: section.title, subtitle: section.subtitle ?? "", icon: section.icon!)
                .padding([.top,.bottom],21)
                .padding(.horizontal,16)
            if section.section == 0 {
                YourVehicleRow(item: section.items[0], itemIsSelected: $itemSelected, viewModel: viewModel)
            } else if section.section == 1 {
                ProfileGridView(items: section.items)
            } else {
                AchivementsRow(item: section.items[0])
            }
        }
        .frame(maxWidth: .infinity)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.whiteGray, lineWidth: 1)
        )
        .padding(.horizontal, 16)
    }
}

struct YourVehicleRow: View {
    let item: ProfileItemModel
    @Binding var itemIsSelected: Bool
    @ObservedObject var viewModel: ProfileViewModel
    var body: some View {
        if viewModel.selectedBikeType.count > 0 {
            HStack {
                Text("\(AppStrings.Profile.yourgarage) (\(viewModel.selectedBikeType.count))")
                    .font(KlavikaFont.bold.font(size: 16))
                    .foregroundColor(AppColor.black)
                Spacer()
            }
            .padding()
            VStack {
                VStack(alignment: .leading, spacing: 15) {
                    ForEach(viewModel.selectedBikeType, id: \.id) { bikeType in
                        AddBikeView(
                            bikeId: bikeType.id,
                            viewModel: viewModel,
                            title: bikeType.make,
                            subtitle: bikeType.model
                        )
                    }
                    .padding()
                    .background(
                        RoundedRectangle(cornerRadius: 8)
                            .fill(AppColor.white)
                    )
                    .overlay(
                        RoundedRectangle(cornerRadius: 8)
                            .stroke(AppColor.listGray, lineWidth: 1)
                    )
                }
                ButtonView(title: AppStrings.Profile.addBike, onTap: {
                    itemIsSelected = true
                })
                .frame(width: 135)
                .padding(.top,15)
            }
            .frame(maxWidth: .infinity)
            .padding([.leading, .trailing],16)
        } else {
            VStack(alignment: .center,spacing: 15) {
                AppIcon.Profile.bike
                    .resizable()
                    .frame(width: 30, height: 17.5)
                    .padding(.bottom,15)
                    .padding(.top,21)
                ButtonView(title: AppStrings.Profile.addBike, onTap: {
                    itemIsSelected = true
                })
                .frame(width: 135)
                Text(item.title)
                    .font(KlavikaFont.bold.font(size: 16))
                    .foregroundColor(AppColor.black)
                Text(item.subtitle)
                    .lineLimit(0)
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(AppColor.stoneGray)
                    .padding(.bottom,25)
            }
            .frame(maxWidth: .infinity)
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .fill(AppColor.white)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(AppColor.listGray, lineWidth: 1)
            )
            .padding([.leading, .trailing],16)
            .padding(.bottom,21)
        }
    }
}

struct ProfileGridView: View {
    let items: [ProfileItemModel]
    let columns = [
        GridItem(.flexible(), spacing: 20),
        GridItem(.flexible(), spacing: 20)
    ]
    
    var body: some View {
        ScrollView {
            LazyVGrid(columns: columns,spacing: 20) {
                ForEach(items) { item in
                    TotalStatisticsRow(item: item)
                        .background(
                            RoundedRectangle(cornerRadius: 8)
                                .fill(AppColor.white)
                        )
                        .overlay(
                            RoundedRectangle(cornerRadius: 8)
                                .stroke(AppColor.listGray, lineWidth: 1)
                        )
                        .contentShape(Rectangle())
                }
            }
            .background(.clear)
            .padding([.leading, .trailing,.bottom])
        }
    }
}

struct AchivementsRow: View {
    let item: ProfileItemModel
    @State var itemIsSelected: Bool = false
    var body: some View {
        VStack(alignment: .center,spacing: 15) {
            item.icon
                .resizable()
                .frame(width: 18, height: 18)
                .padding(.top,21)
            Text(item.title)
                .font(KlavikaFont.bold.font(size: 16))
                .foregroundColor(AppColor.black)
            Text(item.subtitle)
                .lineSpacing(15)
                .lineLimit(2)
                .font(KlavikaFont.regular.font(size: 12))
                .foregroundColor(AppColor.stoneGray)
                .multilineTextAlignment(.center)
                .padding([.leading, .trailing])
            Button(action: {
                
            }) {
                Text(AppStrings.Profile.startEarnBadge)
                    .frame(maxWidth: .infinity)
                    .frame(height: 50)
                    .background(AppColor.white)
                    .foregroundStyle(AppColor.celticBlue)
                    .font(KlavikaFont.bold.font(size: 14))
                    .overlay(
                        RoundedRectangle(cornerRadius: 10)
                            .stroke(AppColor.celticBlue, lineWidth: 1)
                    )
            }
            .padding(.bottom,25)
            .frame(width: 171)
        }
        .frame(maxWidth: .infinity)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(AppColor.white)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 8)
                .stroke(AppColor.listGray, lineWidth: 1)
        )
        .contentShape(Rectangle())
        .navigationDestination(isPresented: $itemIsSelected, destination: {
            item.destination
        })
        .padding([.leading, .trailing],16)
        .padding(.bottom,21)
    }
}

struct TotalStatisticsRow: View {
    let item: ProfileItemModel
    
    var body: some View {
        VStack(alignment:.center, spacing: 10) {
            item.icon
                .resizable()
                .frame(width: 32, height: 32)
                .padding(.top,17)
            Text(item.title)
                .font(KlavikaFont.bold.font(size: 16))
                .foregroundColor(AppColor.black)
            Text(item.subtitle)
                .font(KlavikaFont.regular.font(size: 12))
                .foregroundColor(AppColor.stoneGray)
                .padding(.bottom,10)
        }
        .frame(maxWidth: .infinity)
        .cornerRadius(12)
        .shadow(color: Color.black.opacity(0.05), radius: 5, x: 0, y: 2)
    }
}

struct ProfileTitleView:View {
    let title:String
    let subtitle:String
    let icon:Image
    var body: some View {
        if subtitle.isEmpty {
            HStack() {
                Text(title)
                    .font(KlavikaFont.bold.font(size: 16))
                    .foregroundColor(AppColor.black)
                Spacer()
            }
        } else {
            HStack(spacing: 20) {
                icon
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
            }
        }
    }
}

struct ProfileHeaderView: View {
    let name: String
    let email: String
    let role: String
    let image: Image
    let phoneNumber: String
    
    var body: some View {
        HStack(spacing: 20) {
            image
                .resizable()
                .scaledToFill()
                .frame(width: 73, height: 73)
                .clipShape(Circle())
                .overlay(Circle().stroke(Color.blue.opacity(0.2), lineWidth: 5))
            VStack(alignment: .leading, spacing: 8) {
                Text(name)
                    .font(KlavikaFont.bold.font(size: 19))
                    .foregroundColor(AppColor.black)
                Text(email)
                    .font(KlavikaFont.regular.font(size: 16))
                    .foregroundColor(AppColor.black)
                HStack {
                    if !role.isEmpty {
                        HStack {
                            AppIcon.NavigationSlider.repair
                                .frame(width: 16, height: 16)
                            Text(role)
                                .font(KlavikaFont.regular.font(size: 12))
                        }
                        .frame(height: 24)
                        .padding([.leading,.trailing],8)
                        .padding([.top, .bottom], 5)
                        .background(AppColor.white)
                        .cornerRadius(5)
                    }
                 
                    HStack {
                        AppIcon.NavigationSlider.call
                            .frame(width: 16, height: 16)
                        Text(phoneNumber)
                            .font(KlavikaFont.regular.font(size: 12))
                    }
                    .frame(height: 24)
                    .padding([.leading,.trailing],8)
                    .padding([.top, .bottom], 5)
                    .background(AppColor.white)
                    .cornerRadius(5)
                }
            }
            Spacer()
        }
        .background(.clear)
        .padding()
    }
}

#Preview {
    ProfileScreen()
}
