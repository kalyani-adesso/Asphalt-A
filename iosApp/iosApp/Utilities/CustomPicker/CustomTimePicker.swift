//
//  TimePicker.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import SwiftUI

struct CustomTimePicker: View {
    @Binding var selectedTime: Date
    var selectedDate: Date
    var referenceTime: Date?
    var onDismiss: (() -> Void)?
    
    @State private var hours = 12
    @State private var minutes = 0
    @State private var isAM = true
    
    private let hourRange = Array(1...12)
    private let minuteRange = Array(0...59)
    
    /// Checks if the selected date + time is in the past or invalid compared to referenceTime
    var isInvalidTime: Bool {
        var components = Calendar.current.dateComponents([.year, .month, .day], from: selectedDate)
        
        var hour = hours % 12
        if !isAM { hour += 12 }
        
        components.hour = hour
        components.minute = minutes
        
        guard let selectedDateTime = Calendar.current.date(from: components) else { return false }
        
        // past check
        if selectedDateTime < Date() { return true }
        
        // compare FULL datetime properly
        if let reference = referenceTime {
            if Calendar.current.isDate(selectedDate, inSameDayAs: reference) {
                if selectedDateTime < reference {
                    return true
                }
            }
        }
        
        return false
    }
    
    var body: some View {
        VStack(spacing: 16) {
            Text("Custom Time")
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundColor(AppColor.black)
                .padding(.horizontal)
                .frame(maxWidth: .infinity, alignment: .leading)
            
            HStack(spacing: 16) {
                // Hour Picker
                Picker("", selection: $hours) {
                    ForEach(hourRange, id: \.self) { hour in
                        Text(String(format: "%02d", hour))
                            .font(KlavikaFont.light.font(size: 30))
                            .foregroundColor(AppColor.black)
                            .tag(hour)
                    }
                }
                .pickerStyle(WheelPickerStyle())
                .frame(width: 80, height: 138)
                
                Text(":")
                    .font(.title)
                    .foregroundColor(AppColor.black)
                
                // Minute Picker
                Picker("", selection: $minutes) {
                    ForEach(minuteRange, id: \.self) { minute in
                        Text(String(format: "%02d", minute))
                            .font(KlavikaFont.light.font(size: 30))
                            .foregroundColor(AppColor.black)
                            .tag(minute)
                    }
                }
                .pickerStyle(WheelPickerStyle())
                .frame(width: 80, height: 138)
                
                // AM/PM Picker
                Picker("", selection: $isAM) {
                    Text("AM")
                        .font(KlavikaFont.medium.font(size: 30))
                        .tag(true)
                    Text("PM")
                        .font(KlavikaFont.medium.font(size: 30))
                        .tag(false)
                }
                .pickerStyle(WheelPickerStyle())
                .frame(width: 80, height: 138)
            }
            .frame(width: 302, height: 138)
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(AppColor.backgroundLight, lineWidth: 2)
            )
            
            if isInvalidTime {
                Text("Selected time is invalid")
                    .font(.caption)
                    .foregroundColor(.red)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal)
            }
            
            // Buttons
            HStack(spacing: 30) {
                Button("Cancel") { onDismiss?() }
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(AppColor.celticBlue)
                
                Button("OK") {
                    var components = Calendar.current.dateComponents([.year, .month, .day], from: selectedDate)
                    var hour = hours % 12
                    if !isAM { hour += 12 }
                    components.hour = hour
                    components.minute = minutes
                    
                    if let newDate = Calendar.current.date(from: components) {
                        selectedTime = newDate
                    }
                    onDismiss?()
                }
                .font(KlavikaFont.regular.font(size: 12))
                .foregroundColor(AppColor.celticBlue)
                .foregroundColor(isInvalidTime ? AppColor.grey : AppColor.celticBlue)
                .opacity(isInvalidTime ? 0.5 : 1.0) 
                .disabled(isInvalidTime) //Disable OK if invalid
            }
            .frame(maxWidth: .infinity, alignment: .trailing)
            .padding(.horizontal)
        }
        .frame(width: 342, height: 233)
        .padding()
        .background(AppColor.white)
        .cornerRadius(15)
        .shadow(radius: 2)
        .onAppear {
            let comps = Calendar.current.dateComponents([.hour, .minute], from: selectedTime)
            let hour24 = comps.hour ?? 0
            
            if hour24 == 0 { hours = 12; isAM = true }
            else if hour24 < 12 { hours = hour24; isAM = true }
            else if hour24 == 12 { hours = 12; isAM = false }
            else { hours = hour24 - 12; isAM = false }
            
            minutes = comps.minute ?? 0
        }
    }
}
#Preview {
    CustomTimePicker(selectedTime: .constant(Date()),  selectedDate: Date())
}

#Preview {
    CustomTimePicker(selectedTime: .constant(Date()), selectedDate: Date())
}
