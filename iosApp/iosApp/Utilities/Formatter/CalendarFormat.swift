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
    
    func dateRangeText(monthOffset: Int, createdDate: Date?) -> String {
            let calendar = Calendar.current
            let today = Date()
            
            guard let endDate = calendar.date(byAdding: .month, value: monthOffset, to: today),
                  let startDate = calendar.date(byAdding: .month, value: -5, to: endDate)
            else { return "" }
            
            var adjustedStart = startDate
            
            // Ensure we don't go before account creation date
            if let created = createdDate, startDate < created {
                adjustedStart = created
            }
            
            let formatter = DateFormatter()
            formatter.dateFormat = "dd MMM YYYY"
            
            return "\(formatter.string(from: adjustedStart).uppercased()) - \(formatter.string(from: endDate).uppercased())"
        }
    
}
