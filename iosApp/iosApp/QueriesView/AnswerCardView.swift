//
//  AnswerCardView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 22/10/25.
//

import SwiftUI

struct AnswerCardView: View {
    @ObservedObject var viewModel: QueryViewModel
    let answer: Answer
    var query: Query
  
    var isLiked: Bool {
           viewModel.likedAnswers.contains(answer.apiId)
       }

       var isDisliked: Bool {
           viewModel.disLikedAnswers.contains(answer.apiId)
       }
    
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
                    Button {
                        Task {
                            if isLiked {
                                await viewModel.RemovelikeDislikeAnswer(for: query, for: answer)
                            } else {
                                await viewModel.likeDislikeAnswer(for: query, for: answer, isLikeorDisLike: true)
                            }
                            
                            
                        }
                    } label: {
                        Image(systemName: isLiked ? "hand.thumbsup.fill" : "hand.thumbsup")
                            .frame(width: 18, height: 18)
                            .foregroundColor(AppColor.celticBlue)
                        Text("\(answer.likes)")
                            .foregroundColor(AppColor.black)
                            .font(KlavikaFont.medium.font(size: 14))
                    }
                }
                HStack(spacing: 6) {
                    Button {
                        Task {
                            if isDisliked {
                                                await viewModel.RemovelikeDislikeAnswer(for: query, for: answer)
                                            } else {
                                                await viewModel.likeDislikeAnswer(for: query, for: answer, isLikeorDisLike: false)
                                            }                        }
                    } label: {
                        Image(systemName: isDisliked ? "hand.thumbsdown.fill" : "hand.thumbsdown")
                            .frame(width: 18, height: 18)
                            .foregroundColor(AppColor.darkRed)
                        Text("\(answer.dislikes)")
                            .foregroundColor(AppColor.black)
                            .font(KlavikaFont.medium.font(size: 14))
                    }
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

