//
//  MessagePopupView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 21/11/25.
//

//
//  MessagePopupView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 21/11/25.
//

import SwiftUI

struct MessagePopupView: View {
    @ObservedObject var viewModel: ConnectedRideViewModel
    @Binding var isPresented: Bool
    let previousMessages: [String] = []
    @State private var message: String = ""
    @Environment(\.dismiss) var dismiss
    @FocusState private var isCustomFocused: Bool
    
    private let quickMessages = [
        "All good!",
        "Taking a break",
        "Fuel stop",
        "Road issue"
    ]
    private let quickReplyMessages = [
        "All good!",
        "Take your time",
        "Okay",
        "We will wait"
    ]
    private var activeQuickMessages: [String] {
        previousMessages.isEmpty ? quickMessages : quickReplyMessages
    }
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
                        Text("Message \(viewModel.rider.name)")
                            .foregroundColor(AppColor.black)
                            .font(KlavikaFont.bold.font(size: 18))
                        
                        HStack(spacing: 6) {
                            Circle()
                                .fill(Color.orange)
                                .frame(width: 8, height: 8)
                            
                            Text(rideStatus)
                                .foregroundColor(AppColor.grey)
                                .font(KlavikaFont.regular.font(size: 14))
                        }
                    }
                    
                    Spacer()
                }
                // PREVIOUS MESSAGE CARD
                if let last = previousMessages.last {
                    VStack(alignment: .leading, spacing: 12) {
                        
                        // Header row with the rider avatar + name + time
                        HStack(spacing: 12) {
                            AppImage.Profile.profile
                                .resizable()
                                .frame(width: 36, height: 36)
                                .clipShape(Circle())
                                .overlay(Circle().stroke(AppColor.green, lineWidth: 2))
                            
                            HStack(spacing: 2) {
                                Text("Sooraj ")
                                    .font(KlavikaFont.bold.font(size: 16))
                                    .foregroundColor(AppColor.black)
                                Spacer()
                                HStack(spacing: 4) {
                                    Image(systemName: "clock")
                                        .font(.system(size: 12))
                                        .foregroundColor(.gray)
                                    
                                    Text("Just now")
                                        .font(KlavikaFont.regular.font(size: 12))
                                        .foregroundColor(.gray)
                                }
                            }
                            
                            Spacer()
                        }
                        
                        // Chat bubble
                        Text(last)
                            .font(KlavikaFont.regular.font(size: 15))
                            .foregroundColor(AppColor.black)
                            .padding(12)
                            .frame(maxWidth: .infinity, alignment: .leading)
                    }
                    .padding()
                    .background(AppColor.lightGreen)
                    .cornerRadius(10)
                    .overlay(
                        RoundedRectangle(cornerRadius: 10)
                            .stroke(AppColor.darkGray, lineWidth: 1)
                    )
                    Spacer()
                }
                
                VStack(alignment: .leading, spacing: 12) {
                    Text("Quick Messages")
                        .foregroundColor(AppColor.darkCharcol)
                        .font(KlavikaFont.regular.font(size: 14))
                    
                    // Grid buttons
                    LazyVGrid(columns: Array(repeating: GridItem(.flexible(), spacing: 12), count: 2), spacing: 12) {
                        ForEach(activeQuickMessages, id: \.self) { text in
                            Button {
                                isCustomFocused = true
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
                        .overlay(
                            RoundedRectangle(cornerRadius: 10)
                                .stroke(isCustomFocused ? Color.black : AppColor.bluishGray, lineWidth: 2)
                        )
                        .cornerRadius(10)
                        .focused($isCustomFocused)
                }
                ButtonsView
            }
            .padding()
            .frame(width: 342)
            .fixedSize(horizontal: false, vertical: true)
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
                viewModel.sendMessage(senderName: MBUserDefaults.userNameStatic ?? "", receiverName: viewModel.rider.name, senderId: MBUserDefaults.userIdStatic ?? "", receiverId: viewModel.rider.receiverId, message: message, rideId: viewModel.rider.rideId)
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
    // create a computed property that is going to tell about ride status
    var rideStatus: String {
        let status = viewModel.rider.status
        let time = viewModel.rider.timeSinceUpdate
        
        switch status {
        case .connected, .stopped:
            return "\(status.rawValue) from \(time)"
            
        case .delayed:
            return "\(status.rawValue) by \(time)"
            
        default:
            return "Available"
        }
    }
}





