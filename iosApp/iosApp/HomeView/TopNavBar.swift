//
//  CustomNavBar.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct TopNavBar: View {
    @EnvironmentObject var home: HomeViewModel 
    @State var showNotification: Bool = false
    var body: some View {
        ZStack(alignment: .bottom) {
            Rectangle()
                .fill(Color.white)
                .frame(height: 115)
                .shadow(color: Color.black.opacity(0.15), radius: 4, x: 0, y: 3)
            HStack {
                NavigationLink(destination: NavigationSlideBar()) {
                    AppIcon.Home.navigation
                }
                .navigationBarBackButtonHidden(true)
                Spacer()
                VStack(spacing: 2) {
                    Text("Hello \(home.userName)")
                        .font(.system(size: 17, weight: .semibold))
                    HStack(spacing: 4) {
                        Circle().fill(Color.green).frame(width: 6, height: 6)
                        Text(home.location == "Location access denied"
                             ? "Location access denied"
                             : home.location)
                            .font(.system(size: 13))
                            .foregroundColor(.gray)
                    }
                }
                
                Spacer()
                Button {
                    self.showNotification = true
                } label: {
                    ZStack(alignment: .topTrailing) {
                        Image(systemName: "bell")
                            .font(.system(size: 22))
                            .foregroundColor(.black)
                        Circle()
                            .fill(Color.red)
                            .frame(width: 8, height: 8)
                            .offset(x: 4, y: -4)
                    }
                }
            }
            .padding(.horizontal, 20)
            .padding(.bottom,10)
        }
        .navigationDestination(isPresented: $showNotification, destination: {
            NotificationView()
        })
    }
}

#Preview {
    TopNavBar()
}
