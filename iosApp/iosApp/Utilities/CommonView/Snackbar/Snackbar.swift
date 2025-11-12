//
//  Snackbar.swift
//  iosApp
//
//  Created by Lavanya Selvan on 07/10/25.
//

import SwiftUI

struct Snackbar: View {
    
    let message: String
    let subMessage: String
    
    var body: some View {
        
        VStack {
            HStack(alignment: .center, spacing: 10) {
                AppIcon.ConnectedRide.checkmark
                
                VStack(alignment: .leading, spacing: 5) {
                    Text(message)
                        .font(KlavikaFont.bold.font(size: 14))
                        .foregroundColor(.spanishGreen)
                    Text(subMessage)
                        .font(KlavikaFont.regular.font(size: 12))
                        .foregroundColor(.spanishGreen)
                }
                Spacer()
            }
            .padding(.all, 15)
            .background(
                RoundedRectangle(cornerRadius: 12)
                    .fill(AppColor.lightGreen)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(AppColor.aeroGreen, lineWidth: 2)
            )
            .cornerRadius(12)
            .padding(.horizontal, 16)
            
        }
    }
}

#Preview {
    Snackbar(message: "Ride succesful", subMessage: "completed")
}
