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
       
            HStack(alignment: .top, spacing: 15) {
                
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
                .padding(.vertical, 8)
                .cornerRadius(12)
                HStack(spacing: 15) {
                    ForEach(home.stats) { stat in
                        StatCardView(stat: stat)
                    }
                }
            }
            .frame(width: 345)
            .padding()
            .background(AppColor.backgroundLight)
            .cornerRadius(15)
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(AppColor.border, lineWidth: 2)
            )
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
        .frame(width: 80, height: 114)
        .background(stat.color)
        .cornerRadius(10)
        .foregroundColor(.white)
    }
}



#Preview {
    DashboardView()
}
