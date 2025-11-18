//
//  JoruneyCardView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 08/10/25.
//

import SwiftUI
import Charts

struct JourneyCardView: View {
    @EnvironmentObject var home: HomeViewModel 
    @State private var selectedOption = "This month"
    @State private var currentSlices: [JourneySlice] = []
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack {
                Text(AppStrings.JourneyChart.title.rawValue)
                    .font(KlavikaFont.bold.font(size: 18))
                    .multilineTextAlignment(.leading)
                Spacer()
                ZStack{
                    RoundedRectangle(cornerRadius: 5)
                        .fill(Color.white)
                        .frame(width:111, height: 31)
                        .overlay(
                            RoundedRectangle(cornerRadius: 5)
                                .stroke(Color.black.opacity(0.10), lineWidth: 1)
                        )
                    Menu {
                        Button("This month"){selectedOption = "This month"}
                        Button("Last month") {selectedOption = "Last month"}
                        Button("Last 4 months"){selectedOption = "Last 4 months"}
                        Button("Last 6 months") {selectedOption = "Last 6 months"}
                        Button("Last year") {selectedOption = "Last year"}
                    } label: {
                        HStack(spacing: 10) {
                            Text(selectedOption)
                            Image(systemName: "chevron.down")
                                .frame(width: 5, height: 11)
                        }
                        .font(KlavikaFont.regular.font(size: 12))
                        .foregroundColor(AppColor.stoneGray)
                    }
                }
            }
            VStack {
                DonutChartView(slices: currentSlices)
                    .frame(width: 190, height: 190)
                
                VStack(spacing: 8) {
                    let chunks = home.journeySlices.chunked(into: 3)
                    ForEach(chunks.indices, id: \.self) { rowIndex in
                        HStack(spacing: 12) {
                            ForEach(chunks[rowIndex]) { slice in
                                CategoryList(color: slice.color, label: slice.category)
                            }
                        }
                    }
                }
                .padding()
                .frame(maxWidth: .infinity, alignment: .center)
            }
        }
        .padding()
        .background(AppColor.backgroundLight)
        .cornerRadius(15)
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.darkGray, lineWidth: 2)
        )
        .padding(.top,20)
        .onAppear {
            home.getRideSummary(userID: MBUserDefaults.userIdStatic ?? "", range: "This month")
            currentSlices = home.getJourneySlices(for: selectedOption)
        }
        .onChange(of: selectedOption) { newValue in
            withAnimation {
                currentSlices = home.getJourneySlices(for: newValue)
            }
        }
    }
}

// MARK: - Category List
struct CategoryList: View {
    let color: Color
    let label: String
    
    var body: some View {
        HStack(spacing: 6) {
            RoundedRectangle(cornerRadius: 2)
                .fill(color)
                .frame(width: 10, height: 10)
            Text(label)
                .font(KlavikaFont.regular.font(size: 12))
                .foregroundColor(AppColor.stoneGray)
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 5)
        .background(
            RoundedRectangle(cornerRadius: 5)
                .fill(Color.white)
                .overlay(
                    RoundedRectangle(cornerRadius: 5)
                        .stroke(Color.black.opacity(0.1), lineWidth: 1)
                )
        )
    }
}

// MARK: - Array Helper
extension Array {
    func chunked(into size: Int) -> [[Element]] {
        stride(from: 0, to: count, by: size).map {
            Array(self[$0 ..< Swift.min($0 + size, count)])
        }
    }
}


// MARK: - Donut Chart
struct DonutChartView: View {
    let slices: [JourneySlice]
    @EnvironmentObject var home: HomeViewModel 
    var body: some View {
        Chart {
            ForEach(slices) { s in
                if #available(iOS 17.0, *) {
                    SectorMark(
                        angle: .value("Value", s.value),
                        innerRadius: .ratio(0.60),
                        angularInset: 2.0
                    )
                    .cornerRadius(5)
                    .foregroundStyle(s.color)
                } else {
                    // Fallback on earlier versions
                }
            }
        }
        .overlay(
            VStack {
                AppIcon.Home.chart
                    .frame(width: 38, height: 38)
                Text("Total \(Int(home.journeySlices.first(where: { $0.category == "Total Rides" })?.value ?? 0)) Rides")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }
        )
    }
}

#Preview {
    JourneyCardView()
}
