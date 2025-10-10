//
//  PlacesVisitedView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 08/10/25.
//

import SwiftUI
import Charts

struct PlacesVisitedView: View {
    @StateObject private var home = HomeViewModel()
    var body: some View {
        VStack(alignment: .leading, spacing: 15) {
            HStack {
                VStack(alignment: .leading, spacing: 4) {
                    Text(AppStrings.Placesvisited.title.rawValue)
                        .font(KlavikaFont.bold.font(size: 16))
                    
                    Text("24 MAR - 24 SEP")
                        .font(KlavikaFont.bold.font(size: 12))
                        .foregroundColor(AppColor.stoneGray)
                }
                Spacer()
                HStack(spacing: 10) {
                    Button(action: {}) {
                        Image(systemName: "chevron.left")
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(.black.opacity(0.8))
                            .frame(width: 32, height: 32)
                            .background(Color.gray.opacity(0.1))
                            .clipShape(RoundedRectangle(cornerRadius: 8))
                    }
                    Button(action: {}) {
                        Image(systemName: "chevron.right")
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(.black.opacity(0.8))
                            .frame(width: 32, height: 32)
                            .background(Color.gray.opacity(0.1))
                            .clipShape(RoundedRectangle(cornerRadius: 8))
                    }
                }
            }
            Chart {
                ForEach(home.placesByMonth) { item in
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
                                    AppColor.celticBlue.opacity(0.6),
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
                AxisMarks(position: .leading, values: .automatic(desiredCount: 6)) { value in
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
        .padding()
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
