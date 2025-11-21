//
//  MessagePopupView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 21/11/25.
//

import SwiftUI

struct MessagePopupView: View {
    @Binding var isPresented: Bool
    let riderName: String
    let delayText: String
        
        @State private var message: String = ""
        @Environment(\.dismiss) var dismiss

        private let quickMessages = [
            "All good!",
            "Taking a break",
            "Fuel stop",
            "Road issue"
        ]

        var body: some View {
            ZStack {
                Color.black.opacity(0.6)
                    .ignoresSafeArea()
                VStack(spacing: 20) {
                    HStack(alignment: .center, spacing: 12) {
                        AppImage.Profile.profile.resizable()
                            .frame(width: 40, height: 40)
                            .clipShape(Circle())
                        
                        VStack(alignment: .leading, spacing: 4) {
                            Text("Message \(riderName)")
                                .foregroundColor(AppColor.black)
                                .font(KlavikaFont.bold.font(size: 18))
                            
                            HStack(spacing: 6) {
                                Circle()
                                    .fill(Color.orange)
                                    .frame(width: 8, height: 8)
                                
                                Text(delayText)
                                    .foregroundColor(AppColor.grey)
                                    .font(KlavikaFont.regular.font(size: 14))
                            }
                        }
                        
                        Spacer()
                    }
                    VStack(alignment: .leading, spacing: 12) {
                        Text("Quick Messages")
                            .foregroundColor(AppColor.darkCharcol)
                            .font(KlavikaFont.regular.font(size: 14))
                        
                        // Grid buttons
                        LazyVGrid(columns: Array(repeating: GridItem(.flexible(), spacing: 12), count: 2), spacing: 12) {
                            ForEach(quickMessages, id: \.self) { text in
                                Button {
                                    message = text
                                } label: {
                                    Text(text)
                                        .foregroundColor(AppColor.black)
                                        .font(KlavikaFont.bold.font(size: 14))
                                        .frame(maxWidth: .infinity)
                                        .padding(.vertical, 14)
                                        .background(
                                            RoundedRectangle(cornerRadius: 10)
                                                .stroke(AppColor.bluishGray, lineWidth: 2)
                                        )
                                }
                            }
                        }
                    }
                    VStack(alignment: .leading, spacing: 12) {
                        Text("Custom Message")
                            .foregroundColor(AppColor.darkCharcol)
                            .font(KlavikaFont.regular.font(size: 14))
                        
                        TextField("Type your message...", text: $message, axis: .vertical)
                            .font(KlavikaFont.regular.font(size: 16))
                            .foregroundColor(AppColor.charcol)
                            .padding(.horizontal, 12)
                            .padding(.vertical, 8)
                            .frame(minHeight: 100, alignment: .topLeading)
                            .background(AppColor.listGray)
                            .cornerRadius(10)
                    }
                    ButtonsView
                }
                .padding()
                .frame(width: 342, height: 500)
                .background(Color.white)
                .cornerRadius(22)
            }
        }
    @ViewBuilder var ButtonsView: some View {
        HStack(spacing: 20) {
            Button(action: {
                isPresented = false
            }) {
                HStack {
                    Text("CANCEL")
                        .font(KlavikaFont.bold.font(size: 16))
                        .foregroundStyle(AppColor.black)
                }
                .frame(maxWidth: .infinity, minHeight: 50)
                .background(
                    RoundedRectangle(cornerRadius: 10)
                        .fill(AppColor.white)
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(AppColor.stoneGray.opacity(0.2), lineWidth: 1)
                )
                
            }
            .buttonStyle(.plain)
            Button(action: {
                isPresented = false
            }) {
                HStack {
                    Text("SEND")
                        .font(KlavikaFont.bold.font(size: 16))
                        .foregroundStyle(AppColor.white)
                }
                .frame(maxWidth: .infinity, minHeight: 50)
                .background(
                    RoundedRectangle(cornerRadius: 10)
                        .fill(AppColor.celticBlue)
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(AppColor.stoneGray.opacity(0.2), lineWidth: 1)
                )
                
            }
            .disabled(message.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty)
            .buttonStyle(.plain)
        }
        .padding()
    }
}

#Preview {
    MessagePopupView(isPresented: .constant(true), riderName: "Sooraj Rajan", delayText: "Delayed by 15min", )
}
