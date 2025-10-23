//
//  QueriesView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 22/10/25.
//

import SwiftUI

struct QueriesView: View {
    @StateObject private var viewModel = QueryViewModel()
    @State private var selectedStatus: String? = nil
    @State private var searchText = ""
    @State private var showAskPopup = false

    var onBackToHome: (() -> Void)? = nil
    var body: some View {
        NavigationStack {
            CustomTopNavBar(
                title: "Queries",
                onBack: onBackToHome,
                onAskTapped: {
                          showAskPopup = true
                  }
            )
            ZStack {
                VStack(spacing: 20){
                    FormFieldView(
                        label: " ",
                        icon:  AppIcon.CreateRide.searchLens,
                        placeholder:"Search questions ...",
                        iconColor: AppColor.celticBlue,
                        value: $searchText,
                        isValidEmail: .constant(false),
                        backgroundColor: AppColor.listGray
                    )
                    
                    HStack(spacing: 12) {
                        ForEach(viewModel.queryStatus, id: \.self) { status in
                            let firstRideStatus = viewModel.queryStatus.first?.rawValue ?? ""
                            let isSelected = (selectedStatus ?? firstRideStatus) == status.rawValue
                            QSegmentButtonView(
                                rideStatus: status.rawValue,
                                isSelected: isSelected
                            ) {
                                selectedStatus = status.rawValue
                            }
                        }
                    }
                    .frame(maxWidth: .infinity)
                    .padding(16)
                    .background(
                        RoundedRectangle(cornerRadius: 10)
                            .fill(AppColor.listGray)
                    )
                    .contentShape(Rectangle())
                    
                    ScrollView {
                        LazyVStack(spacing: 20) {
                            ForEach(viewModel.queries) { query in
                                QueryCardView(query: query)
                            }
                        }
                        
                        
                    }
                    
                }
                .padding(.horizontal,20)
                .padding(.top,-10)
                if showAskPopup {
                    QueryPopupView(isPresented: $showAskPopup, viewModel: viewModel)
                            .transition(.scale)
                            .zIndex(1)
                    }
            }
        }
        
        
    }
}
// MARK: - Custom Navigation Bar Component
struct CustomNavBar: View {
    var title: String
    var onBack: (() -> Void)?
    
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
                    print("Menu tapped")
                } label: {
                    HStack {
                        AppIcon.Queries.add
                            .resizable()
                            .frame(width:12,height: 12)
                        Text("ASK QUESTION")
                            .font(KlavikaFont.regular.font(size: 12))
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


// MARK: - SegmentButtonView
struct QSegmentButtonView: View {
    var rideStatus: String
    var isSelected: Bool = false
    var onTap: (() -> Void)? = nil
    var body: some View {
        Button(action: {
            onTap?()
        }) {
            Text(rideStatus)
                .frame(maxWidth: .infinity)
                .frame(height: 50)
                .background(
                    Group {
                        if isSelected {
                            LinearGradient(
                                gradient: Gradient(colors: [AppColor.royalBlue, AppColor.pursianBlue]),
                                startPoint: .leading,
                                endPoint: .trailing
                            )
                            .clipShape(RoundedRectangle(cornerRadius: 10))
                        } else {
                            RoundedRectangle(cornerRadius: 10)
                                .fill(AppColor.white)
                        }
                    }
                )
                .cornerRadius(10)
                .overlay(
                    RoundedRectangle(cornerRadius: 15)
                        .stroke(Color.gray.opacity(0.2), lineWidth: 1)
                )
                .foregroundColor(isSelected ? AppColor.white : .black)
                .font(KlavikaFont.bold.font(size: 16))
        }
        .buttonStyle(.plain)
    }
}

#Preview {
    QueriesView()
}
