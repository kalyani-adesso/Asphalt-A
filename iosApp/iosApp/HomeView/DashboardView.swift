//
//  DashboardView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI


struct DashboardView: View {
    @StateObject private var home = HomeViewModel()
    
    var body: some View {
        VStack(alignment: .leading) {
            HStack(alignment: .top, spacing: 16) {
                
                // Date box
                VStack(spacing: 10) {
                    AppIcon.Home.arrow
                    Text("Aug")
                        .font(KlavikaFont.medium.font(size: 18))
                    Text("25")
                        .font(KlavikaFont.bold.font(size: 30))
                    AppIcon.Home.arrow
                        .rotationEffect(.degrees(180))
                }
                .padding(.horizontal, 8)
                .padding(.vertical, 10)
                .cornerRadius(12)
                .shadow(color: Color.black.opacity(0.05), radius: 4, x: 0, y: 2)
                
                // Scrollable stats row
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 12) {
                        ForEach(home.stats) { stat in
                            StatCardView(stat: stat)
                        }
                    }
                }
            }
            .padding()
            .background(AppColor.backgroundLight)
            .cornerRadius(15)
            .overlay(
                        RoundedRectangle(cornerRadius: 10)
                            .stroke(AppColor.border, lineWidth: 2)
                    )
            
            Spacer()
        }
    }
}

struct StatCardView: View {
    let stat: RideStat
    
    var body: some View {
        VStack(alignment: .leading, spacing: 15) {
            stat.icon
                .frame(width: 21, height: 19)
            
            LinearGradient(
                gradient: Gradient(colors: [.white.opacity(0), .white, .white.opacity(0)]),
                startPoint: .leading,
                endPoint: .trailing
            )
            .frame(width: 53, height: 1)
            
            VStack(alignment: .leading, spacing: 2) {
                Text("\(stat.value)")
                    .font(KlavikaFont.medium.font(size: 16))
                Text(stat.title)
                    .font(KlavikaFont.regular.font(size: 12))
            }
        }
        .padding(12)
        .frame(width: 80, height: 114)
        .background(stat.color)
        .cornerRadius(10)
        .foregroundColor(.white)
    }
}



#Preview {
    DashboardView()
}
