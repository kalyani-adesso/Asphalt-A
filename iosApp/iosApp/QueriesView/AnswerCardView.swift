//
//  AnswerCardView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 22/10/25.
//

import SwiftUI

struct AnswerCardView: View {
    let answer: Answer
    
    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            HStack {
                AppImage.Profile.profile.resizable()
                    .frame(width: 36, height: 36)
                    .clipShape(Circle())
                    .overlay(
                        RoundedRectangle(cornerRadius: 32.5)
                            .stroke(AppColor.green, lineWidth: 1.5)
                    )
                VStack(alignment: .leading, spacing: 2) {
                    HStack {
                        Text(answer.author)
                            .font(KlavikaFont.bold.font(size: 16))
                        
                        if let role = answer.role {
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
    
                        Spacer()
                        Text(answer.daysAgo)
                            .font(KlavikaFont.regular.font(size: 12))
                            .foregroundColor(.gray)
                    }
                    
                }
            }
            
            Text(answer.content)
                .font(KlavikaFont.regular.font(size: 14))
                .foregroundColor(AppColor.stoneGray)
                .lineSpacing(1)
            
            HStack(spacing: 16) {
                HStack(spacing: 6) {
                    AppIcon.Queries.like
                            .resizable()
                            .scaledToFit()
                            .frame(width: 18, height: 18)
                    Text("\(answer.likes)")
                        .font(KlavikaFont.medium.font(size: 14))
                    }
                HStack(spacing: 6) {
                    AppIcon.Queries.dislike
                            .resizable()
                            .scaledToFit()
                            .frame(width: 18, height: 18)
                    Text("\(answer.dislikes)")
                        .font(KlavikaFont.medium.font(size: 14))
                    }
                Spacer()
            }
            .padding(.top, 4)
        }
        .padding()
        .background(AppColor.lightGreen)
        .cornerRadius(10)
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.darkGray, lineWidth: 1)
        )
    }
}

#Preview {
    AnswerCardView(answer: Answer(
        author: "Abhishek",
        role: "Mechanic",
        daysAgo: "5 days ago",
        content: "For your Ninja 650, I recommend using Kawasaki 4-Stroke Oil 10W-40 or Motul 7100 10W-40. Both are excellent choices that meet the JASO MA2 specification.",
        likes: 10,
        dislikes: 1
    ))
}
