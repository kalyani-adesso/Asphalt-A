//
//  ParticipantsView.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import SwiftUI

struct ParticipantsView: View {
    @State private var searchText = ""
    @ObservedObject var viewModel: CreateRideViewModel
    
    var body: some View {
        VStack(spacing: 20) {
            stepIndicator
            VStack(alignment: .leading, spacing: 20) {
                
                HStack {
                    Text("Invite Contacts")
                        .font(KlavikaFont.medium.font(size: 16))
                    Spacer()
                    Text("\(viewModel.selectedParticipants.count) selected")
                        .font(KlavikaFont.regular.font(size: 14))
                        .foregroundColor(.gray)
                }
                FormFieldView(
                    label: " ",
                    icon:  AppIcon.CreateRide.searchLens,
                    placeholder:"Search by name ,number or bike type...",
                    iconColor: AppColor.celticBlue,
                    value: $searchText,
                    isValidEmail: .constant(false),
                    backgroundColor: AppColor.white)
                
                
                
                ScrollView {
                    VStack(spacing: 10) {
                        ForEach(filteredParticipants) { participant in
                            ParticipantsRow(
                                participant: participant,
                                isSelected: Binding(
                                    get: { viewModel.isSelected(participant.id) },
                                    set: { _ in viewModel.toggle(participant.id)
                                    }
                                )
                            )
                        }
                    }
                }
            }
            .frame(width: 343, height: 472)
            .padding()
            .background(AppColor.backgroundLight)
            .cornerRadius(10)
        }
        Spacer()
        HStack(spacing: 15) {
            ButtonView( title: AppStrings.CreateRide.previous.rawValue,
                        background: LinearGradient(
                            gradient: Gradient(colors: [.white, .white]),
                            startPoint: .leading,
                            endPoint: .trailing),
                        foregroundColor: AppColor.celticBlue,
                        showShadow: false ,
                        borderColor: AppColor.celticBlue) {
                viewModel.previousStep()
            }
            
            ButtonView( title: AppStrings.CreateRide.next.rawValue,
                        showShadow: false , onTap: {
                viewModel.nextStep()
            }
            )
            
        }
        .padding()
        .onAppear {
            viewModel.getAllUsers()
        }
    }
    var filteredParticipants: [Participant] {
        if searchText.isEmpty {
            return viewModel.participants
        } else {
            return viewModel.participants.filter {
                $0.name.localizedCaseInsensitiveContains(searchText) ||
                $0.bike.localizedCaseInsensitiveContains(searchText)
            }
        }
    }
    
    
    var stepIndicator: some View {
        HStack(spacing: 32) {
            StepIndicator(icon: AppIcon.Home.createRide, title: "Details", isActive: true, isCurrentPage: false)
            StepIndicator(icon: AppIcon.CreateRide.route, title: "Route", isActive: true, isCurrentPage: false)
            if viewModel.ride.type?.rawValue != "Solo Ride" {
                       StepIndicator(icon: AppIcon.Home.group, title: "Participants",isActive: true, isCurrentPage: true)
                   }
           
            StepIndicator(icon: AppIcon.CreateRide.review, title: "Review")
            StepIndicator(icon: AppIcon.CreateRide.share, title: "Share")
        }
    }
}

#Preview {
    ParticipantsView(viewModel: CreateRideViewModel.init())
}
