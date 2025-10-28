//
//  QueryModel.swift
//  iosApp
//
//  Created by Lavanya Selvan on 22/10/25.
//

import Foundation

struct Query: Identifiable {
    let id = UUID()
    let title: String
    let tags: [String]
    let author: String
    let daysAgo: String
    let content: String
    var answers: [Answer]
    let likes: Int
    let comments: Int
}

struct Answer: Identifiable {
    let id = UUID()
    let author: String
    let role: String?
    let daysAgo: String
    let content: String
    let likes: Int
    let dislikes: Int
}

struct AskQuery: Identifiable {
    let id = UUID()
    var question : String = ""
    var category : QueryCategory? = nil
    var description: String = ""
}


enum QueryCategory: String, CaseIterable, Identifiable {
    case none = "Select a category"
    case maintenance = "Maintenance"
    case routes = "Routes"
    case gear = "Gear"
    case safety = "Safety"
    case other = "Other"

    var id: String { rawValue }
}
