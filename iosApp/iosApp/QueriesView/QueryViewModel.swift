//
//  QueryViewModel.swift
//  iosApp
//
//  Created by Lavanya Selvan on 22/10/25.
//

import Foundation
import shared

@MainActor
class QueryViewModel: ObservableObject {
    private let queryRepo: QueryRepository
    private let userRepo: UserRepository
    enum QueryCategory: String, CaseIterable {
        case All, Maintenance, Routes, Gear, Safety, Other
    }
    var queryStatus: [QueryCategory] = QueryCategory.allCases
    var filteredQueries: [Query] {
        guard let category = selectedCategory else { return queriesResult }
        if category == "All" { return queriesResult }
        return queriesResult.filter { $0.tags.contains(category) }
    }
    
    @Published var askQuery = AskQuery()
    @Published var queriesResult: [Query] = []
    @Published private var usersById: [String: UserData] = [:]
    @Published var selectedCategory: String? = QueryCategory.All.rawValue
    @Published var isLoading = false
    @Published var answerText: String = ""
    @Published var selectedQuery: Query? = nil
    @Published var isLikedQuery: Bool = false
    @Published var isLikedAnswer: Bool = false
    @Published var isLiking = false
    
    init() {
        let queryApiService = QueryAPIServiceImpl(client: KtorClient())
        self.queryRepo = QueryRepository(apiService: queryApiService)
        let userApiService = UserAPIServiceImpl(client: KtorClient())
        self.userRepo = UserRepository(apiService: userApiService)
    }
    
    // MARK: - Load Queries
    @MainActor
    func fetchAllQueries(showLoader: Bool = true) {
        Task {
            if showLoader { isLoading = true }
            do {
                //Fetch all users first
                try await fetchAllUsers()
                
                // Fetch all queries
                let result = try await queryRepo.getQueries()
                
                if let success = result as? APIResultSuccess<AnyObject>,
                   let domains = success.data as? [QueryDomain] {
                    
                    self.queriesResult = mapQueries(domains)
                    
                    if showLoader { isLoading = false }
                    await MainActor.run {
                        isLiking = false
                    }
                    
                } else {
                    print("Unexpected data type: \(type(of: result))")
                }
            } catch {
                print("Failed to load queries: \(error)")
            }
        }
    }
    
    // MARK: - Fetch Users
    @MainActor
    func fetchAllUsers() async throws {
        let result = try await userRepo.getAllUsers()
        
        if let success = result as? APIResultSuccess<AnyObject>,
           let users = success.data as? [UserData] {
            for user in users {
                print("User: \(user.uid) - \(user.name)")
            }
            // dictionary for easy findings
            self.usersById = Dictionary(uniqueKeysWithValues: users.map { ($0.uid, $0) })
            
        } else {
            print("Unexpected user data type")
        }
    }
    
    // MARK: - Map Queries
    private func mapQueries(_ domains: [QueryDomain]) -> [Query] {
        
        let sortedDomains = domains.sorted {
            parseDate($0.postedOn) > parseDate($1.postedOn)
        }
        return sortedDomains.map { domain in
            
            let postedByName = usersById[domain.postedBy]?.name ?? "Unknown User"
            
            let answers: [Answer] = domain.answers.map { ans in
                let answeredByName = usersById[ans.answeredBy]?.name ?? "Unknown User"
                
                return Answer(
                    apiId: ans.id,
                    author: answeredByName,
                    role: nil,
                    daysAgo: timeAgoString(from: parseDate(ans.answeredOn)),
                    content: ans.answer,
                    likes: ans.likes.count,
                    dislikes: ans.dislikes.count
                )
            }
            
            //answer tag
            let answerTag = answers.isEmpty ? "Unanswered" : "Answered"
            
            //category tag
            let categoryTag: String
            switch domain.categoryId {
            case 1:
                categoryTag = "Maintenance"
            case 2:
                categoryTag = "Routes"
            case 3:
                categoryTag = "Gear"
            case 4:
                categoryTag = "Safety"
            default:
                categoryTag = "Other"
            }
            
            let combinedTags = [categoryTag, answerTag]
            
            return Query(
                apiId: domain.id,
                title: domain.title,
                tags: combinedTags,
                author: postedByName,
                daysAgo: timeAgoString(from: parseDate(domain.postedOn)),
                content: domain.description_,
                answers: answers,
                likes: domain.likes.count,
                comments: answers.count
            )
        }
    }
    
    // MARK: - Helpers
    private func parseDate(_ dateString: String) -> Date {
        let formatter = ISO8601DateFormatter()
        formatter.formatOptions = [.withInternetDateTime]
        return formatter.date(from: dateString) ?? Date()
    }
    
    private func timeAgoString(from date: Date) -> String {
        let secondsAgo = Int(Date().timeIntervalSince(date))
        
        if secondsAgo < 60 {
            return "Just now"
        } else if secondsAgo < 3600 {
            let minutes = secondsAgo / 60
            return minutes == 1 ? "1 min ago" : "\(minutes) mins ago"
        } else if secondsAgo < 86400 {
            let hours = secondsAgo / 3600
            return hours == 1 ? "1 hour ago" : "\(hours) hours ago"
        } else if secondsAgo < 604800 {
            let days = secondsAgo / 86400
            return days == 1 ? "1 day ago" : "\(days) days ago"
        } else {
            let weeks = secondsAgo / 604800
            return weeks == 1 ? "1 week ago" : "\(weeks) weeks ago"
        }
    }
    
    private func timeToPostValue() -> String {
        let isoFormatter = ISO8601DateFormatter()
        isoFormatter.timeZone = TimeZone(secondsFromGMT: 0)
        isoFormatter.formatOptions = [.withInternetDateTime]
        let postedOnString = isoFormatter.string(from: Date())
        return postedOnString
    }
    
    // MARK: - Post Query
    @MainActor
    func addQuery() async {
        guard let category = askQuery.category else { return }
        
        do {
            let result = try await queryRepo.addQuery(
                queryTitle: askQuery.question,
                queryDescription: askQuery.description,
                categoryId: Int32(category.backendId),
                postedOn: timeToPostValue(),
                postedBy: MBUserDefaults.userIdStatic ?? ""
            )
            
            if result is APIResultSuccess<GenericResponse> {
                print(" Query added successfully!")
                self.fetchAllQueries()
            }
        } catch {
            print(" Exception: \(error.localizedDescription)")
        }
    }
    
    // MARK: - Post Answer
    @MainActor
    func addAnswer() async {
        guard !answerText.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else { return }
        guard let query = selectedQuery else {
            print("No query selected!")
            return
        }
        Task{
                isLiking = true
            do {
                let result = try await queryRepo.addAnswer(
                    queryId: query.apiId,
                    answer: answerText,
                    answeredBy: "OoYGII16wtRbooBdYYDCAwHyFf62",
                    answeredOn: timeToPostValue()
                )
                print(result)
                if result is APIResultSuccess<GenericResponse> {
                    print(" Answer added successfully!")
                    self.fetchAllQueries(showLoader: false)
                }
            } catch {
                print(" Exception: \(error.localizedDescription)")
            }
        }
    }
    
    
    // MARK: - Post Like for Query
    @MainActor
    func likeQuery(for query: Query) async {
        Task {
            isLiking = true
            do {
                let result = try await queryRepo.likeQuery(
                    userId: "OoYGII16wtRbooBdYYDCAwHyFf62",
                    queryId: query.apiId
                    
                )
                print(result)
                if result is APIResultSuccess<GenericResponse> {
                    print(" Liked Query successfully!")
                    self.fetchAllQueries(showLoader: false)
                    if let index = queriesResult.firstIndex(where: { $0.apiId == query.apiId }) {
                        queriesResult[index].likes = 1
                        isLikedQuery = true
                    }
                }
            } catch {
                print(" Exception: \(error.localizedDescription)")
            }
        }
    }
    
    // MARK: - Remove Like for Query
    @MainActor
    func RemoveQuery(for query: Query) async {
        isLiking = true
        do {
            let result = try await queryRepo.removeLikeQuery(
                userId: "OoYGII16wtRbooBdYYDCAwHyFf62",
                queryId: query.apiId
                
            )
            print(result)
            if result is APIResultSuccess<GenericResponse> {
                isLikedQuery = false //for icon fill
                print(" Removed Like from Query successfully!")
                self.fetchAllQueries(showLoader: false)
            }
        } catch {
            print(" Exception: \(error.localizedDescription)")
        }
    }
    
    // MARK: - Post Like/Dislike for Anwer
    @MainActor
    func likeDislikeAnswer(for query: Query, for answer : Answer, isLikeorDisLike: Bool) async {
        isLiking = true
        do {
            let result = try await queryRepo.likeOrDislikeAnswer(
                userId: "OoYGII16wtRbooBdYYDCAwHyFf62",
                queryId: query.apiId,
                answerId: answer.apiId,
                isLike: isLikeorDisLike
            )
            print(result)
            if result is APIResultSuccess<GenericResponse> {
                print(" Liked Answer successfully! ")
                self.fetchAllQueries(showLoader: false)
                isLikedAnswer = true
            }
        } catch {
            print(" Exception: \(error.localizedDescription)")
        }
    }
    
    // MARK: - Remove Like/Dislike for Anwer
    @MainActor
    func RemovelikeDislikeAnswer(for query: Query, for answer : Answer) async {
        isLiking = true
        do {
            let result = try await queryRepo.removeLikeOrDislikeAnswer(
                userId: "OoYGII16wtRbooBdYYDCAwHyFf62",
                queryId: query.apiId,
                answerId: answer.apiId
            )
            print(result)
            if result is APIResultSuccess<GenericResponse> {
                print(" Removed Liked Answer successfully! ")
                self.fetchAllQueries(showLoader: false)
                isLikedAnswer = false
            }
        } catch {
            print(" Exception: \(error.localizedDescription)")
        }
    }
    
}
