//
//  QueryViewModel.swift
//  iosApp
//
//  Created by Lavanya Selvan on 22/10/25.
//

import Foundation

enum QueryAction: String {
    case all = "All"
    case routes = "Routes"
    case gear = "Gear"
    case safety = "Safety"
}


enum QueryStatus: String {
    case all = "All"
    case routes = "Routes"
    case gear = "Gear"
    case safety = "Safety"
}

class QueryViewModel : ObservableObject {
    @Published var queryStatus: [QueryAction]  = [.all, .routes, .gear, .safety]
    @Published var selectedFilter = "All"
    @Published var askQuery = AskQuery()

        let queries: [Query] = [
            Query(
                title: "Best oil for Kawasaki Ninja 650?",
                tags: ["Maintenance", "Answered"],
                author: "Vyshnav",
                daysAgo: "7 days ago",
                content: "I have a 2022 Kawasaki Ninja 650 and I'm due for an oil change. What oil do you recommend for optimal performance?",
                answers: [
                    Answer(
                        author: "Sooraj",
                        role: "Mechanic",
                        daysAgo: "6 days ago",
                        content: "For your Ninja 650, I recommend using Kawasaki 4-Stroke Oil 10W-40 or Motul 7100 10W-40. Both are excellent choices that meet the JASO MA2 specification.",
                        likes: 10,
                        dislikes: 1
                    ),
                ],
                likes: 15,
                comments: 01
            ),
            Query(
                title: "Best oil for Kawasaki Ninja 650?",
                tags: ["Maintenance", "Answered"],
                author: "Vyshnav",
                daysAgo: "7 days ago",
                content: "I have a 2022 Kawasaki Ninja 650 and I'm due for an oil change. What oil do you recommend for optimal performance?",
                answers: [
                    Answer(
                        author: "Sooraj",
                        role: "Mechanic",
                        daysAgo: "6 days ago",
                        content: "For your Ninja 650, I recommend using Kawasaki 4-Stroke Oil 10W-40 or Motul 7100 10W-40. Both are excellent choices that meet the JASO MA2 specification.",
                        likes: 10,
                        dislikes: 1
                    ),
                    Answer(
                        author: "Abhishek",
                        role: nil,
                        daysAgo: "5 days ago",
                        content: "For your Ninja 650, I recommend using Kawasaki 4-Stroke Oil 10W-40 or Motul 7100 10W-40. Both are excellent choices that meet the JASO MA2 specification.",
                        likes: 10,
                        dislikes: 1
                    ),
                ],
                likes: 15,
                comments: 02
            )
        ]
}
