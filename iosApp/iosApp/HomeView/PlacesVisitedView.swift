//
//  PlacesVisitedView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 08/10/25.
//

import SwiftUI
import Charts

struct PlacesVisitedView: View {
    @EnvironmentObject var home: HomeViewModel
    @State private var monthOffset = 0
    @State private var visiblePlaces: [HomeViewModel.PlacesMonth] = []
    var body: some View {
        VStack(alignment: .leading, spacing: 15) {
            HStack {
                VStack(alignment: .leading, spacing: 4) {
                    Text(AppStrings.Placesvisited.title.rawValue)
                        .font(KlavikaFont.bold.font(size: 16))
                    
                    Text(CalendarFormat().dateRangeText(
                        monthOffset: monthOffset,
                        createdDate: home.userCreatedDate
                    ))
                        .font(KlavikaFont.bold.font(size: 12))
                        .foregroundColor(AppColor.stoneGray)
                }
                Spacer()
                HStack(spacing: 10) {
                    Button(action: {
                        withAnimation {
                            let calendar = Calendar.current
                            let newDate = calendar.date(byAdding: .month, value: monthOffset - 1, to: Date())!
                            
                            if let first = home.firstAvailableMonth, newDate < first {
                                return
                            }
                            monthOffset -= 1
                            updateVisiblePlaces()
                        }
                    }) {
                        Image(systemName: "chevron.left")
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(AppColor.stoneGray)
                            .frame(width: 32, height: 32)
                            .background(Color.gray.opacity(0.1))
                            .clipShape(RoundedRectangle(cornerRadius: 8))
                    }
                    .disabled(isAtFirstMonth)
                    .opacity(isAtFirstMonth ? 0.3 : 1)
                    Button(action: {
                        withAnimation {
                            if monthOffset < 0 {
                                monthOffset += 1
                                updateVisiblePlaces()
                            }
                        }
                    }) {
                        Image(systemName: "chevron.right")
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(AppColor.stoneGray)
                            .frame(width: 32, height: 32)
                            .background(Color.gray.opacity(0.1))
                            .clipShape(RoundedRectangle(cornerRadius: 8))
                    }
                }
            }
            Chart {
                ForEach(visiblePlaces) { item in
                    let isSelected = home.selectedMonth?.month == item.month
                    BarMark(
                        x: .value("Month", item.month),
                        y: .value("Count", item.placesCount),
                        width: .fixed(8)
                    )
                    .cornerRadius(6)
                    .foregroundStyle(AppColor.celticBlue)
                    
                    if isSelected {
                        BarMark(
                            x: .value("Month", item.month),
                            yStart: .value("Base", 0),
                            yEnd: .value("Highlight", item.placesCount + 1)
                        )
                        .cornerRadius(6)
                        .foregroundStyle(
                            LinearGradient(
                                colors: [
                                    AppColor.celticBlue.opacity(0.9),
                                    AppColor.white.opacity(0.2),
                                ],
                                startPoint: .top,
                                endPoint: .bottom
                            )
                        )
                        .annotation(position: .top) {
                            TooltipView(number: item.placesCount)
                        }
                    }
                }
            }
            .chartYAxis {
                AxisMarks(
                    position: .leading,
                    values: Array(stride(from: 0, through: 10, by: 2))
                ) { value in
                    AxisGridLine()
                    AxisValueLabel()
                }
            }
            
            .chartXAxis {
                AxisMarks(values: .automatic) { value in
                    AxisValueLabel()
                }
            }
            .chartOverlay { proxy in
                GeometryReader { geometry in
                    ZStack(alignment: .top) {
                        Rectangle().fill(.clear).contentShape(Rectangle())
                            .onTapGesture { location in
                                if let xValue: String = proxy.value(atX: location.x, as: String.self) {
                                    if let selected = home.placesByMonth.first(where: { $0.month == xValue }) {
                                        withAnimation {
                                            if home.selectedMonth?.month == selected.month {
                                                home.selectedMonth = nil
                                            } else {
                                                home.selectedMonth = selected
                                            }
                                        }
                                    }
                                }
                            }
                    }
                }
            }
            
            .frame(height: 180)
            .padding(.top, 4)
        }
        .onReceive(NotificationCenter.default.publisher(for: .placesDataUpdated)) { _ in
            updateVisiblePlaces()
        }
        .onAppear {
            home.loadUserName()
            updateVisiblePlaces()
        }
        .padding()
    }
    // MARK: - Dynamic chart update Logic
    private func updateVisiblePlaces() {
        let calendar = Calendar.current
        let formatter = DateFormatter()
        formatter.dateFormat = "MMM"
        
        let baseDate = calendar.date(byAdding: .month, value: monthOffset, to: Date()) ?? Date()
        
        visiblePlaces = (0..<6).compactMap { i in
            guard let monthDate = calendar.date(byAdding: .month, value: -i, to: baseDate) else { return nil }
            
            if let first = home.firstAvailableMonth {
                let components = calendar.dateComponents([.year, .month], from: monthDate)
                let normalizedMonth = calendar.date(from: components)!
                
                if normalizedMonth < first {
                    return nil
                }
            }
            
            let m = calendar.component(.month, from: monthDate)
            let y = calendar.component(.year, from: monthDate)
            let short = formatter.string(from: monthDate)
            
            if let dataPoint = home.placesByMonth.first(where: {
                $0.monthIndex == m && $0.year == y
            }) {
                return dataPoint
            } else {
                return HomeViewModel.PlacesMonth(
                    month: short,
                    year: y,
                    placesCount: 0,
                    monthIndex: m
                )
            }
        }
        .reversed()
    }
    private var isAtFirstMonth: Bool {
        guard let first = home.firstAvailableMonth else { return false }
        
        let calendar = Calendar.current
        
        guard let previousMonth = calendar.date(byAdding: .month, value: monthOffset - 1, to: Date()) else {
            return false
        }
        
        let prevComponents = calendar.dateComponents([.year, .month], from: previousMonth)
        let prevNormalized = calendar.date(from: prevComponents)!
        
        return prevNormalized < first
    }
    
}


struct TooltipView: View {
    var number: Int
    var body: some View {
        ZStack {
            AppIcon.Home.pointer
                .resizable()
                .frame(width: 44, height: 24)
            Text("\(number)")
                .font(.system(size: 12, weight: .bold))
                .foregroundColor(.white)
        }
    }
}



#Preview {
    PlacesVisitedView()
}
