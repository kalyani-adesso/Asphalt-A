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
    
    let showBack: Bool
    let content: Content

    init(showBack: Bool = true, @ViewBuilder content: () -> Content) {
        self.showBack = showBack
        self.content = content()
    }

    var body: some View {
        content
            .toolbar {
                ToolbarItemGroup(placement: .navigationBarLeading) {
                    if showBack {
                        Button {
                            dismiss()
                        } label: {
                            AppIcon.CreateRide.backButton
                        }
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
    }
}

