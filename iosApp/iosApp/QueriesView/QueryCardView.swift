//
//  QueryCardView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 22/10/25.
//

import SwiftUI

struct QueryCardView: View {
    let query: Query
    var body: some View {
        VStack(alignment: .leading, spacing: 15) {
            
            // Title
            Text(query.title)
                .font(KlavikaFont.bold.font(size: 16))
            
            // Tags
            HStack {
                ForEach(query.tags, id: \.self) { tag in
                    Text(tag)
                        .font(KlavikaFont.regular.font(size: 14))
                        .padding(.horizontal, 8)
                        .padding(.vertical, 4)
                        .background(tag == "Answered" ? AppColor.lightGreen : AppColor.paleOrange)
                        .foregroundColor(tag == "Answered" ? AppColor.darkGreen : AppColor.lightOrange)
                        .cornerRadius(6)
                }
            }
            
            // Question Content
            Text(query.content)
                .font(KlavikaFont.regular.font(size: 14))
                .foregroundColor(AppColor.stoneGray)
                .lineSpacing(1)
            
            
            // Author and time
            HStack {
                AppImage.Profile.profile.resizable()
                    .frame(width: 30, height: 30)
                    .clipShape(Circle())
                    .overlay(
                        RoundedRectangle(cornerRadius: 30)
                            .stroke(AppColor.blushPink, lineWidth: 1.5)
                    )
                Text(query.author)
                    .font(KlavikaFont.bold.font(size: 16))
                Spacer()
                Text(query.daysAgo)
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(AppColor.stoneGray)
            }
            
            Divider()
            
            // Answers
            ForEach(query.answers) { answer in
                AnswerCardView(answer: answer)
            }
            
            Divider()
            
            // Bottom actions
            HStack(spacing: 20) {
                HStack(spacing: 6) {
                    AppIcon.Queries.like
                            .resizable()
                            .scaledToFit()
                            .frame(width: 18, height: 18)
                    Text("\(query.likes)")
                        .font(KlavikaFont.medium.font(size: 14))
                    }
                HStack(spacing: 6) {
                    AppIcon.Queries.chat
                            .resizable()
                            .scaledToFit()
                            .frame(width: 18, height: 18)
                    Text("\(query.comments)")
                        .font(KlavikaFont.medium.font(size: 14))
                    }
                Spacer()
                Button("Answer") {}
                    .font(KlavikaFont.medium.font(size: 12))
                    .foregroundColor(AppColor.black)
                    .frame(width: 72, height: 24)
                    .background(AppColor.white)
                    .overlay(
                        RoundedRectangle(cornerRadius: 0)
                            .stroke(AppColor.darkGray, lineWidth: 1)
                    )
                   
            }
            .padding(.top, 4)
        }
        .padding()
        .background(AppColor.listGray)
        .cornerRadius(12)
       
    }
}

#Preview {
    QueryCardView(query: Query(
        title: "Best oil for Kawasaki Ninja 650?",
        tags: ["Maintenance", "Answered"],
        author: "Vyshnav",
        daysAgo: "7 days ago",
        content: "I have a 2022 Kawasaki Ninja 650 and I'm due for an oil change. What oil do you recommend for optimal performance?",
        answers: [
            Answer(
                author: "Sooraj",
                role: "Mechanic",
                daysAgo: "6 days ago",
                content: "For your Ninja 650, I recommend using Kawasaki 4-Stroke Oil 10W-40 or Motul 7100 10W-40. Both are excellent choices that meet the JASO MA2 specification.",
                likes: 10,
                dislikes: 1
            ),
        ],
        likes: 15,
        comments: 1
    ))
}
