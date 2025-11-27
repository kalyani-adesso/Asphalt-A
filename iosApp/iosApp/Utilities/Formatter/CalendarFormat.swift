//
//  Calendar.swift
//  iosApp
//
//  Created by Lavanya Selvan on 09/10/25.
//

import Foundation

class CalendarFormat : ObservableObject{
    var dateFormatter: DateFormatter {
        let formatter = DateFormatter()
        formatter.dateFormat = "MMMM"
        return formatter
    }
    
    var dayFormatter: DateFormatter {
        let formatter = DateFormatter()
        formatter.dateFormat = "dd"
        return formatter
    }
    
    func dateRangeText(monthOffset: Int) -> String {
        let calendar = Calendar.current
        let today = Date()
        guard let endDate = calendar.date(byAdding: .month, value: monthOffset * 7, to: today),
              let startDate = calendar.date(byAdding: .month, value: -7, to: endDate)
        else { return "" }
        let formatter = DateFormatter()
        formatter.dateFormat = "dd MMM YYYY"
        return "\(formatter.string(from: startDate).uppercased()) - \(formatter.string(from: endDate).uppercased())"
    }
    
}
