//
//  QueryPopupView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 22/10/25.
//

import SwiftUI

struct QueryPopupView: View {
    @Binding var isPresented: Bool
    @State private var questionTitle = ""
    @State private var category = ""
    @State private var description = ""
    @ObservedObject var viewModel: QueryViewModel
    var body: some View {
        ZStack {
            // Dimmed background
            Color.black.opacity(0.4)
                .ignoresSafeArea()
                .onTapGesture {
                    withAnimation { isPresented = false }
                }
            
            VStack(spacing: 25) {
                HStack(spacing: 35){
                    QuerypopupEdit(title: "Ask a Question", subtitle: "Get help from the motorcycle community!", icon: AppIcon.Queries.crisis_alert)
                    Button(action: {
                        isPresented = false
                    }) {
                        Image(systemName: "xmark")
                            .resizable()
                            .frame(width: 14,height: 14)
                            .foregroundStyle(AppColor.richBlack)
                            .padding(.trailing,15)
                    }
                }

                // Text fields
                VStack(alignment: .leading, spacing: 20) {
                    
                    Text("Question Title").font(KlavikaFont.medium.font(size: 16))
                        .foregroundColor(AppColor.black)
                    TextField("Brief Description of the question..", text: $viewModel.askQuery.question)
                        .font(KlavikaFont.regular.font(size: 16))
                        .foregroundColor(AppColor.richBlack)
                        .padding()
                        .background(Color.white)
                        .cornerRadius(10)
                    Text("Category")
                        .font(KlavikaFont.medium.font(size: 16))
                        .foregroundColor(AppColor.black)
                    
                    Menu {
                        ForEach(QueryCategory.allCases) { type in
                            Button(action: {
                                viewModel.askQuery.category = type
                            }) {
                                Text(type.rawValue)
                                    .font(KlavikaFont.regular.font(size: 14))
                                    .foregroundColor(AppColor.richBlack)
                            }
                        }
                    } label: {
                        HStack {
                            Text(viewModel.askQuery.category?.rawValue ?? "Select a category")
                                .foregroundColor( viewModel.askQuery.category == nil ? Color(.placeholderText) : AppColor.richBlack)
                                .font(KlavikaFont.regular.font(size: 16))
                            Spacer()
                            Image(systemName: "chevron.down")
                                .resizable()
                                .frame(width: 10, height: 6)
                                .foregroundColor(AppColor.stoneGray)
                        }
                        .padding()
                        .background(Color.white)
                        .cornerRadius(10)
                    }
                    
                    
                    
                    
                    Text("Description").font(KlavikaFont.medium.font(size: 16))
                        .foregroundColor(AppColor.black)
                    TextField("Provide more details about your question...", text: $viewModel.askQuery.description, axis: .vertical)
                        .font(KlavikaFont.regular.font(size: 16))
                        .foregroundColor(AppColor.richBlack)
                        .padding()
                        .padding(.bottom,50)
                        .frame(height: 100)
                        .background(Color.white)
                        .cornerRadius(10)
                    
                }
                    // Buttons
                    
                    HStack(spacing: 19) {
                        ButtonView( title: "CANCEL",
                                    background: LinearGradient(
                                        gradient: Gradient(colors: [.white, .white]),
                                        startPoint: .leading,
                                        endPoint: .trailing),
                                    foregroundColor: AppColor.darkRed,
                                    showShadow: false ,
                                    borderColor: AppColor.darkRed, onTap: {
                            isPresented = false
                        })
                        ButtonView( title: "ASK QUESTION",
                                    showShadow: false,onTap: {
                            Task {
                                        await viewModel.addQuery()
                                        await MainActor.run {
                                            isPresented = false
                                        }
                                    }
                        }
                        )
                    
                }
            }
            .padding(20)
            .background(AppColor.backgroundLight)
            .cornerRadius(16)
            .shadow(radius: 10)
            .padding(.horizontal, 20)
            
        }
    }
    
}

struct QuerypopupEdit:View {
    let title:String
    let subtitle:String
    let icon:Image
    var body: some View {
        HStack(spacing: 10) {
            icon
                .resizable()
                .frame(width: 30, height: 30)
            VStack(alignment: .leading,spacing: 5) {
                Text(title)
                    .font(KlavikaFont.bold.font(size: 16))
                    .foregroundColor(AppColor.black)
                Text(subtitle)
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(.stoneGray)
            }
        }
    }
}


#Preview {
    QueryPopupView(isPresented: .constant(true), viewModel: QueryViewModel.init())
}
