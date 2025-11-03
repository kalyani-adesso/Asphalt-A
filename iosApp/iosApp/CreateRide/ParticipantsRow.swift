//
//  ParticipantsRow.swift
//  iosApp
//
//  Created by Lavanya Selvan on 14/10/25.
//

import SwiftUI

struct ParticipantsRow: View {
    let participant: Participant
    @Binding var isSelected: Bool
    
    var body: some View {
        HStack(spacing: 12) {
            ZStack(alignment: .bottomTrailing) {
                AppImage.Profile.profile.resizable()
                    .frame(width: 37, height: 37)
                    .clipShape(Circle())
                    .overlay(
                        RoundedRectangle(cornerRadius: 32.5)
                            .stroke(AppColor.green, lineWidth: 2.5)
                    )
                if participant.isOnline {
                    Circle()
                        .fill(Color.green)
                        .frame(width: 13, height: 13)
                        .offset(x: 2, y: 2)
                        .overlay(
                            Circle()
                                .offset(x: 2, y: 2)
                                .stroke(Color.white, lineWidth: 1.5)
                        )
                }
            }
            
            VStack(alignment: .leading, spacing: 4) {
                HStack {
                    Text(participant.name)
                        .font(KlavikaFont.bold.font(size: 16))
                        .foregroundColor(AppColor.black)
                    
                    if let role = participant.role {
                        HStack(spacing: 4) {
                            AppIcon.CreateRide.tools
                                .frame(width: 16, height: 16)
                            Text(role)
                                .font(KlavikaFont.regular.font(size: 12))
                        }
                        .padding(.horizontal, 6)
                        .padding(.vertical, 3)
                        .background(AppColor.backgroundLight)
                        .cornerRadius(6)
                    }
                }
                
                HStack(spacing: 4) {
                    AppIcon.Home.chart
                        .resizable()
                        .renderingMode(.template)
                        .foregroundColor(AppColor.stoneGray)
                        .frame(width: 12, height: 10)
                    Text(participant.bike)
                        .font(KlavikaFont.regular.font(size: 13))
                        .foregroundColor(.gray)
                }
            }
            
            Spacer()
            
            Button {
                isSelected.toggle()
            } label: {
                ZStack {
                    Circle()
                        .stroke(AppColor.stoneGray, lineWidth: 1)
                        .frame(width: 24, height: 24)
                    
                    if isSelected {
                        Circle()
                            .stroke(AppColor.celticBlue, lineWidth: 8)
                            .frame(width: 20, height: 20)
                    }
                }
            }
        }
        .padding()
        .background(Color.white)
        .cornerRadius(10)
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.darkGray, lineWidth: 1)
        )
    }
}

#Preview {
    ParticipantsRow(participant: Participant(name: "Sooraj Rajan", role: "Mechanic", bike: "Harley Davidson 750", image: "avatar1", isOnline: true), isSelected: .constant(false))
}
