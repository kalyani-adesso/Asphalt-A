//
//  TimePicker.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import SwiftUI

struct CustomTimePicker: View {
    @Binding var selectedTime: Date
    var onDismiss: (() -> Void)?
    
    @State private var hours = 12
    @State private var minutes = 0
    @State private var isAM = true
    
    var body: some View {
        VStack(spacing: 16) {
            Text("Custom Time")
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundColor(AppColor.black)
                .padding(.horizontal)
                .frame(maxWidth: .infinity, alignment: .leading)
            
            HStack(spacing: 50){
                HStack(spacing: 8) {
                    StepperText(value: $hours, range: 1...12, label: "Hours")
                    Text(":").font(.title)
                    StepperText(value: $minutes, range: 0...59, label: "Min")
                }
                VStack {
                    Button("PM") {
                        isAM = false
                       
                    }
                    .font(KlavikaFont.bold.font(size: 20))
                    .foregroundColor(!isAM ? AppColor.black : AppColor.black)
                    .buttonStyle(ModeButtonStyle(selected: !isAM))
                    Button("AM") {
                        isAM = true
                      
                    }
                    .font(KlavikaFont.bold.font(size: 20))
                    .foregroundColor(isAM ? AppColor.black : AppColor.black)
                    .buttonStyle(ModeButtonStyle(selected: isAM))
                }
            }
            .frame(width: 302, height: 138)
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(AppColor.backgroundLight, lineWidth: 2)
            )

            HStack(spacing: 30) {
                Button("Cancel") { onDismiss?() }
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(AppColor.celticBlue)
                Button("OK") {
                    var components = Calendar.current.dateComponents([.year, .month, .day], from: Date())
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

                if hour24 == 0 {
                    hours = 12
                    isAM = true
                } else if hour24 < 12 {
                    hours = hour24
                    isAM = true
                } else if hour24 == 12 {
                    hours = 12
                    isAM = false
                } else {
                    hours = hour24 - 12
                    isAM = false
                }

                minutes = comps.minute ?? 0
            }
    }
}

struct StepperText: View {
    @Binding var value: Int
    let range: ClosedRange<Int>
    let label: String
    
    var body: some View {
        VStack (spacing: 15){
            Button(action: { if value < range.upperBound { value += 1 } }) {
                Image(systemName: "chevron.up")
                    .foregroundColor(AppColor.black)
            }
            Text(String(format: "%02d", value))
                .font(KlavikaFont.bold.font(size: 30))
                .foregroundColor(AppColor.black)
            Button(action: { if value > range.lowerBound { value -= 1 } }) {
                Image(systemName: "chevron.down")
                    .foregroundColor(AppColor.black)
            }
            Text(label)
                .font(KlavikaFont.regular.font(size: 14))
                .foregroundColor(AppColor.black)
        }
        .padding(6)
    }
}

struct ModeButtonStyle: ButtonStyle {
    var selected: Bool
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .padding(.vertical, 6)
            .padding(.horizontal, 14)
            .background(selected ? Color.blue : Color.gray.opacity(0.1))
            .foregroundColor(selected ? .white : .black)
            .cornerRadius(8)
    }
}


#Preview {
    CustomTimePicker(selectedTime: .constant(Date()))
}
