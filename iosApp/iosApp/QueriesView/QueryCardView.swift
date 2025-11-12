//
//  QueryCardView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 22/10/25.
//

import SwiftUI

struct QueryCardView: View {
    var query: Query
    @State private var showAnswer: Bool = false
    @ObservedObject var viewModel: QueryViewModel
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
            
            if !query.answers.isEmpty{
                Divider()
            }
            
            // Answers
            ForEach(query.answers) { answer in
                AnswerCardView(viewModel: viewModel, answer: answer, query: query)
            }
            
            Divider()
            
            // Bottom actions
            HStack(spacing: 20) {
                HStack(spacing: 6) {
                    Button {
                        Task {
                            if viewModel.likedQueries.contains(query.apiId) {
                                await viewModel.RemoveQuery(for: query)
                            } else {
                                await viewModel.likeQuery(for: query)
                            }
                            
                            
                        }
                    } label: {
                        Image(systemName: viewModel.likedQueries.contains(query.apiId) ? "hand.thumbsup.fill" : "hand.thumbsup")
                            .frame(width: 18, height: 18)
                            .foregroundColor(AppColor.celticBlue)
                        Text("\(query.likes)")
                            .foregroundColor(AppColor.black)
                            .font(KlavikaFont.medium.font(size: 14))
                    }
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
                Button("Answer") {
                    viewModel.selectedQuery = query
                    showAnswer = true
                }
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
        .sheet(isPresented: $showAnswer) {
            AnswerSheetView(query:  viewModel.selectedQuery!, viewModel: viewModel)
                .presentationDetents([.fraction(0.8)])
                .presentationBackground(.ultraThinMaterial)
        }
        
    }
}

