//
//  AnswerSheetView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 27/10/25.
//

import SwiftUI

struct AnswerSheetView: View {
    @Environment(\.dismiss) var dismiss
    var query: Query
    @ObservedObject var viewModel: QueryViewModel

    var body: some View {
        VStack(spacing: 20) {
            // MARK: Header
            HStack {
                Text("Answer to Query")
                    .font(KlavikaFont.bold.font(size: 18))
                Spacer()
                Button {
                    dismiss()
                } label: {
                    Image(systemName: "xmark")
                        .foregroundColor(AppColor.stoneGray)
                        .font(.system(size: 14, weight: .semibold))
                        .padding(8)
                }
            }
            .padding()
            
            
            // MARK: Question + Answers
            ScrollView {
                VStack(alignment: .leading, spacing: 16) {
                    
                    // Question Card
                    VStack(alignment: .leading, spacing: 12) {
                        Text(query.title)
                            .font(KlavikaFont.bold.font(size: 16))
                        
                        HStack {
                            ForEach(query.tags, id: \.self) { tag in
                                Text(tag)
                                    .font(KlavikaFont.regular.font(size: 13))
                                    .padding(.horizontal, 8)
                                    .padding(.vertical, 4)
                                    .background(tag == "Answered" ? AppColor.lightGreen : AppColor.paleOrange)
                                    .foregroundColor(tag == "Answered" ? AppColor.darkGreen : AppColor.lightOrange)
                                    .cornerRadius(6)
                            }
                        }
                        
                        Text(query.content)
                            .font(KlavikaFont.regular.font(size: 14))
                            .foregroundColor(AppColor.stoneGray)
                            .lineSpacing(2)
                        
                        HStack {
                            AppImage.Profile.profile
                                .resizable()
                                .frame(width: 28, height: 28)
                                .clipShape(Circle())
                                .overlay(
                                    RoundedRectangle(cornerRadius: 28)
                                        .stroke(AppColor.blushPink, lineWidth: 1.5)
                                )
                            Text(query.author)
                                .font(KlavikaFont.bold.font(size: 14))
                            Spacer()
                            Text(query.daysAgo)
                                .font(KlavikaFont.regular.font(size: 12))
                                .foregroundColor(AppColor.stoneGray)
                        }
                        
                        HStack(spacing: 16) {
                            HStack(spacing: 4) {
                                AppIcon.Queries.like
                                    .resizable()
                                    .frame(width: 16, height: 16)
                                Text("\(query.likes)")
                                    .font(KlavikaFont.medium.font(size: 13))
                            }
                            HStack(spacing: 4) {
                                AppIcon.Queries.chat
                                    .resizable()
                                    .frame(width: 16, height: 16)
                                Text("\(query.comments)")
                                    .font(KlavikaFont.medium.font(size: 13))
                            }
                        }
                    }
                    .padding()
                    .background(AppColor.white)
                    .cornerRadius(10)
                    .overlay(
                        RoundedRectangle(cornerRadius: 10)
                            .stroke(AppColor.darkGray, lineWidth: 1)
                    )
                    // Answers
                    ForEach(query.answers) { answer in
                        AnswerCardView(viewModel: viewModel, answer: answer, query: query)
                    }
                }
                .padding(.horizontal, 35)
            }
            
            // MARK: Bottom Input Bar
            VStack(alignment: .leading, spacing: 12) {
                // TextField Row
                HStack(alignment: .top, spacing: 12) {
                    AppImage.Profile.profile
                        .resizable()
                        .frame(width: 44, height: 44)
                        .clipShape(Circle())
                        .overlay(
                            RoundedRectangle(cornerRadius: 44)
                                .stroke(AppColor.lightBlue.opacity(0.4), lineWidth: 4)
                        )
                    
                    VStack(alignment: .leading, spacing: 10) {
                        TextField("Share your knowledge and help the community...", text: $viewModel.answerText, axis: .vertical)
                            .font(KlavikaFont.regular.font(size: 16))
                            .foregroundColor(AppColor.richBlack)
                            .padding()
                            .padding(.bottom, 50)
                            .frame(height: 100)
                            .background(Color.white)
                            .cornerRadius(10)
                            .overlay(
                                RoundedRectangle(cornerRadius: 10)
                                    .stroke(AppColor.darkGray, lineWidth: 1)
                            )
                        
                        // Buttons under textfield
                        HStack(spacing: 12) {
                            Button("Cancel") {
                                dismiss()
                            }
                            .font(KlavikaFont.medium.font(size: 14))
                            .foregroundColor(AppColor.black)
                            .frame(width: 87, height: 36)
                            .background(AppColor.white)
                            .overlay(RoundedRectangle(cornerRadius: 8).stroke(AppColor.darkGray, lineWidth: 1))
                            Spacer()
                            Button {
                                Task {
                                    await viewModel.addAnswer()
                                    viewModel.answerText = ""
                                    dismiss()
                                }
                            } label: {
                                HStack {
                                    Image(systemName: "paperplane.fill")
                                    Text("Post Answer")
                                }
                                .font(KlavikaFont.medium.font(size: 14))
                                .foregroundColor(.white)
                            }
                            .frame(width: 132, height: 36)
                            .background(AppColor.celticBlue)
                            .cornerRadius(8)
                        }
                        .padding(.top, 10)
                    }
                    
                }
                .padding(.top, 60)
                .padding(.horizontal, 30)
                .padding(.bottom, 50)
            }
            .frame(width: 400, height: 189)
            .background(Color.white)
            
        }
        .background(
            LinearGradient(
                gradient: Gradient(stops: [
                    .init(color:AppColor.white.opacity(0.8), location: 0.2),
                    .init(color: Color.clear, location: 1.0)
                ]),
                startPoint: .top,
                endPoint: .bottom
            )
        )
        .onAppear {
            viewModel.selectedQuery = query
        }
        
    }
    
}

