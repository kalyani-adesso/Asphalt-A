//
//  PlaceSearchView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 31/10/25.
//

import SwiftUI
import MapKit

struct PlaceSearchView: View {
    @EnvironmentObject var viewModel: CreateRideViewModel
    var searchText: String
    var isSelectingStart: Bool
    var dismiss: () -> Void
    
    var body: some View {
        NavigationStack {
            List(viewModel.results, id: \.self) { result in
                Text(result.title)
                    .onTapGesture {
                        viewModel.selectPlace(result, isStart: isSelectingStart)
                        dismiss()
                    }
            }
            .searchable(text: $viewModel.query, prompt: "Search for a place")
            .navigationTitle("Search Places")
            .navigationBarTitleDisplayMode(.inline)
            .onAppear {
                viewModel.query = searchText
            }
            .onDisappear {
                viewModel.results.removeAll()
                viewModel.query = ""
            }
        }
    }
}
