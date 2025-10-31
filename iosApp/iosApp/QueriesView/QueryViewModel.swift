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
    private let currentUserId = MBUserDefaults.userIdStatic ?? ""
    enum QueryCategory: String, CaseIterable {
        case All, Maintenance, Routes, Gear, Safety, Other
    }
    var queryStatus: [QueryCategory] = QueryCategory.allCases
    var filteredQueries: [Query] {
        
        var filtered = queriesResult
        
        //category filter
        if let category = selectedCategory, category != "All" {
            filtered = filtered.filter { $0.tags.contains(category) }
        }
        
        //search filter
        if !searchText.trimmingCharacters(in: .whitespaces).isEmpty {
            filtered = filtered.filter {
                $0.title.localizedCaseInsensitiveContains(searchText) ||
                $0.content.localizedCaseInsensitiveContains(searchText) ||
                $0.author.localizedCaseInsensitiveContains(searchText)
            }
        }
        
        return filtered
    }
    @Published var askQuery = AskQuery()
    @Published var queriesResult: [Query] = []
    @Published private var usersById: [String: UserDomain] = [:]
    @Published var selectedCategory: String? = QueryCategory.All.rawValue
    @Published var isLoading = false
    @Published var answerText: String = ""
    @Published var selectedQuery: Query? = nil
    @Published var isLikedQuery: Bool = false
    @Published var isLikedAnswer: Bool = false
    @Published var isLiking = false
    @Published  var searchText = ""
    @Published var likedQueries: Set<String> = []
    @Published var likedAnswers: Set<String> = []
    @Published var disLikedAnswers: Set<String> = []
    
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
                    
                    // show likes/dislikes in fetching
                    for query in self.queriesResult {
                        
                        if query.likesDict[currentUserId] == true {
                            likedQueries.insert(query.apiId)
                        }
                        
                        for answer in query.answers {
                            if answer.likesAnsDict[currentUserId] == true {
                                likedAnswers.insert(answer.apiId)
                            }
                            if answer.disLikesAnsDict[currentUserId] == true {
                                disLikedAnswers.insert(answer.apiId)
                            }
                        }
                    }
                    
                    
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
           let users = success.data as? [UserDomain] {
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
                
                let likes: [String: Bool]
                let dislikes: [String: Bool]
                
                if let likeKeys = ans.likes as? [String] {
                    likes = Dictionary(uniqueKeysWithValues: likeKeys.map { ($0, true) })
                } else if let likeMap = ans.likes as? [String: Bool] {
                    likes = likeMap
                } else {
                    likes = [:]
                }

                if let dislikeKeys = ans.dislikes as? [String] {
                    dislikes = Dictionary(uniqueKeysWithValues: dislikeKeys.map { ($0, true) })
                } else if let dislikeMap = ans.dislikes as? [String: Bool] {
                    dislikes = dislikeMap
                } else {
                    dislikes = [:]
                }
                
                
                return Answer(
                    apiId: ans.id,
                    author: answeredByName,
                    role: nil,
                    daysAgo: timeAgoString(from: parseDate(ans.answeredOn)),
                    content: ans.answer,
                    likes: ans.likes.count,
                    dislikes: ans.dislikes.count,
                    likesAnsDict: likes,
                    disLikesAnsDict: dislikes
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
            
            let queryLikesDict: [String: Bool]
            if let likeKeys = domain.likes as? [String] {
                queryLikesDict = Dictionary(uniqueKeysWithValues: likeKeys.map { ($0, true) })
            }
            else{
                queryLikesDict = [:]
            }
            
            
            return Query(
                apiId: domain.id,
                title: domain.title,
                tags: combinedTags,
                author: postedByName,
                daysAgo: timeAgoString(from: parseDate(domain.postedOn)),
                content: domain.description_,
                answers: answers,
                likes: domain.likes.count,
                comments: answers.count,
                likesDict: queryLikesDict
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
                postedBy: currentUserId
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
       
            isLiking = true
            do {
                let result = try await queryRepo.addAnswer(
                    queryId: query.apiId,
                    answer: answerText,
                    answeredBy: currentUserId,
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
    
    
    // MARK: - Post Like for Query
    @MainActor
    func likeQuery(for query: Query) async {
        Task {
            isLiking = true
            do {
                let result = try await queryRepo.likeQuery(
                    userId: currentUserId,
                    queryId: query.apiId
                    
                )
                print(result)
                if result is APIResultSuccess<GenericResponse> {
                    print(" Liked Query successfully!")
                    self.fetchAllQueries(showLoader: false)
                    if let index = queriesResult.firstIndex(where: { $0.apiId == query.apiId }) {
                        queriesResult[index].likes = 1
                        
                    }
                    likedQueries.insert(query.apiId)
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
                userId: currentUserId,
                queryId: query.apiId
                
            )
            print(result)
            if result is APIResultSuccess<GenericResponse> {
                print(" Removed Like from Query successfully!")
                self.fetchAllQueries(showLoader: false)
                if let index = queriesResult.firstIndex(where: { $0.apiId == query.apiId }),
                   queriesResult[index].likes > 0 {
                    queriesResult[index].likes -= 1
                }
                likedQueries.remove(query.apiId)
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
                userId: currentUserId,
                queryId: query.apiId,
                answerId: answer.apiId,
                isLike: isLikeorDisLike
            )
            print(result)
            if result is APIResultSuccess<GenericResponse> ,
               let qIndex = queriesResult.firstIndex(where: { $0.apiId == query.apiId }),
               let aIndex = queriesResult[qIndex].answers.firstIndex(where: { $0.apiId == answer.apiId }) {
                print(" Liked Answer successfully! ")
                self.fetchAllQueries(showLoader: false)
                
                
                if isLikeorDisLike {
                    queriesResult[qIndex].answers[aIndex].likes += 1
                    likedAnswers.insert(answer.apiId)
                    disLikedAnswers.remove(answer.apiId)
                } else {
                    queriesResult[qIndex].answers[aIndex].dislikes += 1
                    disLikedAnswers.insert(answer.apiId)
                    likedAnswers.remove(answer.apiId)
                }
                
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
                userId: currentUserId,
                queryId: query.apiId,
                answerId: answer.apiId
            )
            print(result)
            if result is APIResultSuccess<GenericResponse> ,
               let qIndex = queriesResult.firstIndex(where: { $0.apiId == query.apiId }),
               let aIndex = queriesResult[qIndex].answers.firstIndex(where: { $0.apiId == answer.apiId }) {
                print(" Removed Liked Answer successfully! ")
                self.fetchAllQueries(showLoader: false)
                
                if likedAnswers.contains(answer.apiId),
                   queriesResult[qIndex].answers[aIndex].likes > 0 {
                    queriesResult[qIndex].answers[aIndex].likes -= 1
                    likedAnswers.remove(answer.apiId)
                }
                
                if disLikedAnswers.contains(answer.apiId),
                   queriesResult[qIndex].answers[aIndex].dislikes > 0 {
                    queriesResult[qIndex].answers[aIndex].dislikes -= 1
                    disLikedAnswers.remove(answer.apiId)
                }
                
            }
        } catch {
            print(" Exception: \(error.localizedDescription)")
        }
    }
    
    
}
