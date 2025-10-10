//
//  Calendar.swift
//  iosApp
//
//  Created by Lavanya Selvan on 09/10/25.
//

import Foundation

class CalendarFormat: ObservableObject{
 
     var dateFormatter: DateFormatter {
            let formatter = DateFormatter()
            formatter.dateFormat = "MMM"
            return formatter
        }

         var dayFormatter: DateFormatter {
            let formatter = DateFormatter()
            formatter.dateFormat = "dd" 
            return formatter
        }
}
