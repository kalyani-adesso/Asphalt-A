//
//  NavigationSlideBar.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 04/10/25.
//

import SwiftUI

struct NavigationSlideBar: View {
    @StateObject private var viewModel = NavigationSliderViewModel()
    
    var body: some View {
        List {
            Section {
                ProfileHeaderView(
                    name: viewModel.profileName,
                    bike: viewModel.bikeType,
                    role: viewModel.role,
                    image: viewModel.profileImage
                )
                .frame(height: 135)
                .background(
                    RoundedRectangle(cornerRadius: 10)
                        .fill(AppColor.listGray)
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(AppColor.celticBlue.opacity(0.2), lineWidth: 1)
                )
                .listRowInsets(EdgeInsets())
                .listRowSeparator(.hidden)
                .listRowBackground(Color.clear)
                .padding()
            }
            
            ForEach(viewModel.sections) { section in
                MenuSectionView(section: section)
                    .listRowSeparator(.hidden)
                    .listRowInsets(EdgeInsets(top: 8, leading: 0, bottom: 8, trailing: 0)) // Add spacing between sections if needed
                    .listRowBackground(Color.clear)
            }
        }
        .listStyle(.plain)
        .padding(.top, 30)
        .background(AppColor.white)
        .navigationTitle(AppStrings.SignInLabel.clubName.localized)
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct MenuSectionView: View {
    let section:MenuSection
    
    var body: some View {
        VStack(spacing: 0) {
            // Section header
            HeaderTitleView(title: section.title)
                .padding(.horizontal, 16)
                .padding(.top, 16)
                .padding(.bottom, 8)
            
            // Section items
            ForEach(section.items) { item in
                MenuItemRow(item: item)
                    .padding(.horizontal, 16)
                    .padding(.bottom, 8)
            }
        }
        .frame(maxWidth: .infinity) // Ensure it fills the row width
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.listGray)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.seperatorGray, lineWidth: 1)
        )
        .padding(.horizontal, 16)
    }
}

struct MenuItemRow: View {
    let item: MenuItemModel
    @State var itemIsSelected: Bool = false
    
    var body: some View {
        HStack(spacing: 11) {
            item.icon
                .resizable()
                .frame(width: 30, height: 30)
                .padding(.leading, 8)
            
            VStack(alignment: .leading, spacing: 4) {
                Text(item.title)
                    .font(KlavikaFont.bold.font(size: 16))
                    .foregroundColor(item.iconColor)
                Text(item.subtitle)
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(AppColor.stoneGray)
            }
            Spacer()
            Image(systemName: "chevron.right")
                .foregroundColor(item.iconColor)
                .frame(width: 24, height: 24)
                .padding(.trailing, 8)
        }
        .frame(height: 80)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(AppColor.white)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 8)
                .stroke(AppColor.seperatorGray, lineWidth: 1)
        )
        .contentShape(Rectangle())
        .onTapGesture {
            itemIsSelected = true
        }
        .navigationDestination(isPresented: $itemIsSelected, destination: {
            item.destination
        })
    }
}

struct ProfileHeaderView: View {
    let name: String
    let bike: String
    let role: String
    let image: Image
    
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
                Text(bike)
                    .font(KlavikaFont.regular.font(size: 16))
                    .foregroundColor(AppColor.black)
                HStack {
                    AppIcon.NavigationSlider.call
                        .frame(width: 16, height: 16)
                        .padding(.trailing,6)
                    Text(role)
                        .font(KlavikaFont.regular.font(size: 12))
                }
                .frame(height: 24)
                .padding([.leading,.trailing],8)
                .padding([.top, .bottom], 5)
                .background(AppColor.white)
            }
            Spacer()
        }
        .background(.clear)
        .padding()
    }
}

struct HeaderTitleView:View {
    let title:String
    var body: some View {
        HStack {
            Rectangle()
                .background(AppColor.celticBlue)
                .frame(width: 9, height: 9)
                .cornerRadius(1)
                .padding(.trailing,8)
            Text(title)
                .font(KlavikaFont.bold.font(size: 16))
                .foregroundColor(.primary)
            Spacer()
        }
    }
}

#Preview {
    NavigationSlideBar()
}
