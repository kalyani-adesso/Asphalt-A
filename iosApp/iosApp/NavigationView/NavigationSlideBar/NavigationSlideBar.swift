//
//  NavigationSlideBar.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 04/10/25.
//

import SwiftUI

struct NavigationSlideBar: View {
    @StateObject private var viewModel = NavigationSliderViewModel()
    @Environment(\.dismiss) var dismiss
    @State var showHome: Bool = false
    var body: some View {
        AppToolBar(showBack: false){
            VStack {
                List(viewModel.sections, id: \.self) { item in
                    MenuItemRow(viewModel: viewModel, item: item)
                        .padding(.vertical, 5)
                        .listRowSeparator(.hidden)
                        .listRowBackground(AppColor.listGray)
                }
                .scrollContentBackground(.hidden)
                .listStyle(.plain)
                .cornerRadius(10)
                .padding(16)
            }
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarBackButtonHidden(true)
            .navigationDestination(isPresented: $showHome, destination: {
                BottomNavBar()
            })
        }
    }
}

struct MenuItemRow: View {
    let viewModel:NavigationSliderViewModel
    let item: MenuItemModel
    @State var itemIsSelected: Bool = false
    var body: some View {
        HStack(spacing: 8) {
            item.icon
                .resizable()
                .frame(width: 24, height: 24)
            Text(item.title)
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundColor(item.iconColor)
            Spacer()
            Image(systemName: "chevron.right")
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundColor(item.iconColor)
                .frame(width: 24, height: 24)
                .padding(.trailing, 8)
        }
        .modifier(logoutSection(title: item.title))
        .contentShape(Rectangle())
        .onTapGesture {
            //TODO: Show Logout toast
            viewModel.logout {
                itemIsSelected = true
                MBUserDefaults.rememberMeDataStatic = false
                MBUserDefaults.userIdStatic = ""
                MBUserDefaults.userNameStatic = ""
            }
        }
        .navigationDestination(isPresented: $itemIsSelected, destination: {
            item.destination
        })
    }
}

struct logoutSection:ViewModifier {
    var title: String
    func body(content: Content) -> some View {
        if title == AppStrings.NavigationSlider.logout {
            VStack(spacing: 0) {
                Divider()
                    .frame(maxWidth: .infinity)
                    .padding(.top, -9)
                content
            }
        } else {
            content
        }
    }
}

#Preview {
    NavigationSlideBar()
}
