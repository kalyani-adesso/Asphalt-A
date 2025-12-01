//
//  DatePicker.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import SwiftUI

struct CustomDatePicker: View {

    @Binding var selectedDate: Date
    var onDismiss: (() -> Void)?
    let minimumDate: Date

    @State private var currentMonthOffset: Int = 0

    var body: some View {
        VStack(spacing: 16) {
            Text("Select date")
                .font(KlavikaFont.medium.font(size: 12))
                    .foregroundColor(AppColor.stoneGray)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)

            Text(formattedDate(selectedDate))
                .font(KlavikaFont.regular.font(size: 32))
                    .foregroundColor(AppColor.black)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal)
            Divider()
                .background(AppColor.celticBlue)
            
            CalendarGrid(selectedDate: $selectedDate, monthOffset: currentMonthOffset,  minimumDate: minimumDate)

            HStack(spacing: 30) {
                Button("Cancel") { onDismiss?() }
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(AppColor.celticBlue)
                Button("OK") { onDismiss?() }
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(AppColor.celticBlue)
            }
            .frame(maxWidth: .infinity, alignment: .trailing)
            .padding(.horizontal)
        }
        .padding()
        .background(Color.white)
        .cornerRadius(20)
        .shadow(radius: 5)
    }

    private func formattedDate(_ date: Date) -> String {
        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        return formatter.string(from: date)
    }
}

struct CalendarGrid: View {
    @Binding var selectedDate: Date
    var monthOffset: Int
    let minimumDate: Date 

    @State private var calendar = Calendar.current
    @State private var currentMonth: Date = Date()

    private let months = Calendar.current.monthSymbols

    var body: some View {
        VStack(spacing: 12) {

            // MARK: - Header (Dropdown + Arrows)
            HStack {
                Menu {
                    ForEach(0..<months.count, id: \.self) { index in
                        Button("\(months[index]) \(yearString(from: currentMonth))") {
                            if let newMonth = calendar.date(from: DateComponents(
                                year: yearInt(from: currentMonth),
                                month: index + 1
                            )) {
                                currentMonth = newMonth
                            }
                        }
                    }
                } label: {
                    HStack(spacing: 4) {
                        Text(monthTitle)
                            .font(KlavikaFont.regular.font(size: 16))
                            .foregroundColor(AppColor.black)
                        Image(systemName: "chevron.down")
                            .font(.system(size: 10))
                            .foregroundColor(AppColor.celticBlue)
                    }
                }

                Spacer()

                HStack(spacing: 16) {
                    Button(action: previousMonth) {
                        Image(systemName: "chevron.left")
                            .foregroundColor(AppColor.stoneGray)
                    }
                    Button(action: nextMonth) {
                        Image(systemName: "chevron.right")
                            .foregroundColor(AppColor.stoneGray)
                    }
                }
            }
            .padding(.horizontal)
            .padding(.bottom , 10)

            // MARK: - Days Grid
            let days = generateDays(for: currentMonth)

            LazyVGrid(columns: Array(repeating: GridItem(.flexible()), count: 7), spacing: 8) {
                let weekdays = ["S", "M", "T", "W", "T", "F", "S"]
                ForEach(weekdays.indices, id: \.self) { index in
                    Text(weekdays[index])
                        .font(KlavikaFont.medium.font(size: 12))
                        .foregroundColor(AppColor.stoneGray)
                }

                ForEach(days, id: \.self) { day in
                    let isToday = calendar.isDateInToday(day)
                    let isSelected = calendar.isDate(day, inSameDayAs: selectedDate)
                    let isDisabled = day < minimumDate 

                    Text(dayString(day))
                        .font(KlavikaFont.regular.font(size: 14))
                        .frame(width: 34, height: 34)
                        .background(
                            Circle()
                                .fill(isSelected ? AppColor.celticBlue : .clear)
                        )
                        .overlay(
                            Circle()
                                .stroke(isToday ? AppColor.celticBlue : .clear, lineWidth: 1)
                        )
                        .foregroundColor(isSelected ? .white : .black)
                        .opacity(isDisabled ? 0.3 : 1) 
                        .onTapGesture {
                            if !isDisabled {
                                           selectedDate = day
                                       }
                        }
                }
            }
            .padding(.horizontal)
        }
        .onAppear {
            currentMonth = calendar.date(byAdding: .month, value: monthOffset, to: Date()) ?? Date()
        }
    }

    // MARK: - Helpers
    private var monthTitle: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "MMM yyyy"
        return formatter.string(from: currentMonth)
    }

    private func generateDays(for date: Date) -> [Date] {
        guard
            let range = calendar.range(of: .day, in: .month, for: date),
            let monthStart = calendar.date(from: calendar.dateComponents([.year, .month], from: date))
        else { return [] }

        let firstWeekday = calendar.component(.weekday, from: monthStart)
        let paddingDays = Array(repeating: Date.distantPast, count: firstWeekday - 1)
        let monthDays = range.compactMap { day -> Date? in
            calendar.date(byAdding: .day, value: day - 1, to: monthStart)
        }

        return paddingDays + monthDays
    }

    private func dayString(_ date: Date) -> String {
        if date == Date.distantPast { return "" }
        return String(calendar.component(.day, from: date))
    }

    private func previousMonth() {
        if let newMonth = calendar.date(byAdding: .month, value: -1, to: currentMonth) {
            currentMonth = newMonth
        }
    }

    private func nextMonth() {
        if let newMonth = calendar.date(byAdding: .month, value: 1, to: currentMonth) {
            currentMonth = newMonth
        }
    }

    private func yearInt(from date: Date) -> Int {
        calendar.component(.year, from: date)
    }

    private func yearString(from date: Date) -> String {
        String(calendar.component(.year, from: date))
    }
}


