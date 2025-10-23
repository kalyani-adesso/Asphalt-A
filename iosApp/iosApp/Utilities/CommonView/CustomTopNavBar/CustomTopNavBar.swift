//
//  CustomTopNavBar.swift
//  iosApp
//
//  Created by Lavanya Selvan on 22/10/25.
//


import SwiftUI

struct CustomTopNavBar: View {
    var title: String
    var onBack: (() -> Void)?
    var onAskTapped: (() -> Void)?
    
    var body: some View {
        ZStack {
            HStack {
                Button(action: { onBack?() }) {
                    AppIcon.CreateRide.backButton
                        .resizable()
                        .frame(width: 24, height: 24)
                        .padding(.leading, 4)
                }
                Text(title)
                    .font(KlavikaFont.bold.font(size: 19))
                    .foregroundColor(AppColor.black)
                Spacer()
                Button {
                    onAskTapped?()  
                } label: {
                    HStack {
                        AppIcon.Queries.add
                            .resizable()
                            .frame(width:12,height: 12)
                        Text("ASK QUESTION")
                            .font(KlavikaFont.bold.font(size: 12))
                            .foregroundStyle(AppColor.celticBlue)
                    }
                    .frame(width: 112, height: 30)
                    .background(
                        RoundedRectangle(cornerRadius: 10)
                            .fill(AppColor.white)
                    )
                    .overlay(
                        RoundedRectangle(cornerRadius: 10)
                            .stroke(AppColor.celticBlue, lineWidth: 1)
                    )
                }
                
            }
           
            
            
        }
        .padding(.horizontal)
        .frame(height: 56)
        .background(AppColor.white)
        .shadow(color: Color.black.opacity(0.08), radius: 4, x: 0, y: 2)
    }
}



#Preview {
    CustomTopNavBar(title: "Queries")
}


