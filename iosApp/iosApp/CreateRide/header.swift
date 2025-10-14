//
//  header.swift
//  iosApp
//
//  Created by Lavanya Selvan on 13/10/25.
//

import SwiftUI

struct header: View {
    @ObservedObject var viewModel = CreateRideViewModel()
    var body: some View {
        HStack {
           
            Button(action: { viewModel.previousStep() }) {
                Image(systemName: "chevron.left")
                    .foregroundColor(.black)
            }
            Spacer()
            Text("Create a Ride")
                .font(.headline)
            Spacer()
            Text("1/5")
                .font(.subheadline)
                .foregroundColor(.gray)
        }
        .padding(.horizontal)
    }
}

#Preview {
    header()
}
