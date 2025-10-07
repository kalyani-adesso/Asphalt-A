//
//  CustomNavBar.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct CustomNavBar: View {
    var body: some View {
        HStack {
            NavigationLink(destination: NavigationSlideBar()) {
               AppIcon.Home.navigation
            }
           
            
            Spacer()
            
            VStack(spacing: 2) {
                Text("Hello Aromal")
                    .font(.system(size: 17, weight: .semibold))
                HStack(spacing: 4) {
                    Circle().fill(Color.green).frame(width: 6, height: 6)
                    Text("Kochi, Infopark")
                        .font(.system(size: 13))
                        .foregroundColor(.gray)
                }
            }
            
            Spacer()
            
            Button {
                print("Notifications tapped")
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
        .padding(.top, 54)
        .frame(height: 115)
        .background(Color.white)
        .shadow(color: Color.black.opacity(0.25), radius: 4, x: 0, y: 3)
    }
}

#Preview {
    CustomNavBar()
}
