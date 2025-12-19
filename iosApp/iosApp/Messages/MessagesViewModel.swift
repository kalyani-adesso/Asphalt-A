//
//  MessagesViewModel.swift
//  iosApp
//
//  Created by Lavanya Selvan on 15/12/25.
//

import Foundation

class MessagesViewModel: ObservableObject {
    enum MessageCategory: String, CaseIterable {
        case All, Unread, Groups, Favourites
    }
    var messageStatus: [MessageCategory] = MessageCategory.allCases
    @Published  var searchText = ""
    @Published var selectedCategory: String? = MessageCategory.All.rawValue
}
