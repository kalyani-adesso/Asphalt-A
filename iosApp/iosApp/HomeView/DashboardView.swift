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
            VStack(spacing: 15) {
                HStack {
                               VStack(alignment: .leading, spacing: 2) {
                                   Text("Your Ride Stats")
                                       .font(KlavikaFont.bold.font(size: 18))

                                   let yearString = Calendar.current.component(.year, from: currentDate)
                                   Text("\((CalendarFormat().dateFormatter.string(from: currentDate))) - \(String(yearString))")
                                       .font(KlavikaFont.bold.font(size: 12))
                                       .foregroundColor(AppColor.mediumDarkGray)
                               }

                               Spacer()

                               HStack(spacing: 20) {
                                   Button {
                                       currentDate = Calendar.current.date(byAdding: .month, value: -1, to: currentDate) ?? currentDate
                                   } label: {
                                       Image(systemName: "chevron.left")
                                           .font(.system(size: 16, weight: .semibold))
                                           .foregroundColor(AppColor.celticBlue)
                                   }
                                   .buttonStyle(.plain)
                                   let isToday = Calendar.current.isDateInToday(currentDate)
                                   Button {
                                       let nextDate = Calendar.current.date(byAdding: .month, value: 1, to: currentDate) ?? currentDate
                                       if nextDate <= Date()
                                       {
                                           currentDate = nextDate
                                       }
                                   } label: {
                                       Image(systemName: "chevron.right")
                                           .font(.system(size: 16, weight: .semibold))
                                           .foregroundColor(isToday ? AppColor.grey : AppColor.celticBlue)
                                   }
                                   .buttonStyle(.plain)
                                   .disabled(isToday)
                               }
                           }
                .padding(.horizontal, 10)
                .cornerRadius(12)
                HStack(spacing: 15) {
                    ForEach(home.stats) { stat in
                        StatCardView(stat: stat)
                    }
                }
            }
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
            VStack(alignment: .leading, spacing: 8) {
                stat.icon
                    .renderingMode(.template)
                    .foregroundColor(AppColor.celticBlue)
                    .frame(width: 28, height: 24)
                
                VStack(alignment: .leading, spacing: 2) {
                    Text("\(stat.value)")
                        .font(KlavikaFont.medium.font(size: 24))
                    Text(stat.title)
                        .font(KlavikaFont.light.font(size: 12))
                }
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 16)
            .background(AppColor.mediumGray)
            .cornerRadius(14)
        }
}

#Preview {
    DashboardView()
        .environmentObject(HomeViewModel())
}
