//
//  DashboardView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct DashboardView: View {
    @EnvironmentObject var home: HomeViewModel
    @State private var currentDate = Date()
    
    
    var body: some View {
            HStack(alignment: .top, spacing: 15) {
                VStack(spacing: 10) {
                    Button {
                        currentDate = Calendar.current.date(byAdding: .month, value: -1, to: currentDate) ?? currentDate
                    } label: {
                        AppIcon.Home.arrow
                    }
                    .buttonStyle(.plain)
                    let yearString = Calendar.current.component(.year, from: currentDate)  % 100
                    Text(CalendarFormat().dateFormatter.string(from: currentDate))
                        .font(KlavikaFont.medium.font(size: 18))
                    Text(String(yearString))
                        .font(KlavikaFont.bold.font(size: 30))
                    let isToday = Calendar.current.isDateInToday(currentDate)
                    Button {
                        let nextDate = Calendar.current.date(byAdding: .month, value: 1, to: currentDate) ?? currentDate
                        if nextDate <= Date()
                        {
                            currentDate = nextDate
                        }
                    } label: {
                        AppIcon.Home.arrow
                            .rotationEffect(.degrees(180))
                    }
                    .buttonStyle(.plain)
                    .disabled(isToday)
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
                    .stroke(AppColor.darkGray, lineWidth: 2)
            )
            .onChange(of: currentDate) { newValue in
                        let month = Calendar.current.component(.month, from: newValue)
                        let year = Calendar.current.component(.year, from: newValue)
                        home.updateStatsFor(month: month, year: year)
                    }
                    .onAppear {
                       
                            let month = Calendar.current.component(.month, from: currentDate)
                            let year = Calendar.current.component(.year, from: currentDate)
                            home.updateStatsFor(month: month, year: year)
                        
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
        .frame(width: 80, height: 114)
        .background(stat.color)
        .cornerRadius(10)
        .foregroundColor(.white)
    }
}

#Preview {
    DashboardView()
}
