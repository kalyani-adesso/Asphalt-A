//
//  AppToolBar.swift
//  iosApp
//
//  Created by Lavanya Selvan on 26/11/25.
//

import SwiftUI

struct AppToolBar<Content: View>: View {
    @Environment(\.dismiss) private var dismiss
    @State private var showNotification = false
    @State private var showSlideBar = false
    @State var showHome: Bool = false
    @State var showBack: Bool = false
    
    let content: Content
    
    init( showBack: Bool = false, @ViewBuilder content: () -> Content) {
        _showBack = State(initialValue: showBack)
        self.content = content()
    }
    
    var body: some View {
        content
            .toolbar {
                ToolbarItemGroup(placement: .navigationBarLeading) {
                    Button {
                        if showBack{
                            dismiss()
                        }
                        else{
                            showHome = true
                        }
                       
                    } label: {
                        AppIcon.CreateRide.backButton
                    }
                    
                }
                ToolbarItemGroup(placement: .navigationBarTrailing) {
                    Button {
                        showNotification = true
                    } label: {
                        ZStack(alignment: .topTrailing) {
                            Image(systemName: "bell")
                                .font(.system(size: 15))
                                .foregroundColor(AppColor.celticBlue)
                            Circle()
                                .fill(Color.red)
                                .frame(width: 8, height: 8)
                                .offset(x: -2, y: 1)
                        }
                    }
                    
                    Button {
                        showSlideBar = true
                    } label: {
                        AppIcon.Home.navigation
                    }
                }
            }
            .navigationDestination(isPresented: $showSlideBar, destination: {
                NavigationSlideBar()
            })
            .navigationDestination(isPresented: $showNotification, destination: {
                NotificationView()
            })
            .navigationDestination(isPresented: $showHome, destination: {
                BottomNavBar()
            })
    }
}

