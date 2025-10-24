package com.asphalt.queries.repositories

import com.asphalt.commonui.utils.Utils
import com.asphalt.queries.data.Answer
import com.asphalt.queries.data.Query
import com.asphalt.queries.data.bikeQueries
import kotlinx.coroutines.delay
import java.time.Instant

class QueryRepo {
    suspend fun loadQueryList(): List<Query> {
        delay(200)

        val sortedQueries = bikeQueries.sortedByDescending { query ->
            Utils.parseISODateToMillis(query.postedOn)
        }

        return sortedQueries.map { query ->
            val sortedAnswers = query.answers.sortedByDescending { answer ->
                Utils.parseISODateToMillis(answer.answeredOn)
            }
            query.copy(answers = sortedAnswers)
        }
    }

    suspend fun addQuery(query: Query) {
        delay(200)
        bikeQueries.add(query)
    }

    suspend fun likeOrRemoveLikeOfQuestion(queryId: Int): Query? {
        val index = bikeQueries.indexOfFirst { question -> question.id == queryId }

        if (index != -1) {
            val originalQuery = bikeQueries[index]

            val updatedQuery = originalQuery.copy(
                isUserLiked = !originalQuery.isUserLiked,
                likeCount = if (originalQuery.isUserLiked) originalQuery.likeCount - 1 else originalQuery.likeCount + 1
            )
            delay(200)

            bikeQueries[index] = updatedQuery

            return updatedQuery
        }
        return null

    }



    private suspend fun updateAnswerInQuery(
        answerId: Int,
        updateLogic: (Answer) -> Answer
    ): Query? {
        delay(100) // Simulate network delay

        val queryIndex = bikeQueries.indexOfFirst { query ->
            query.answers.any { it.id == answerId }
        }

        if (queryIndex == -1) {
            return null
        }

        val originalQuery = bikeQueries[queryIndex]

        val updatedAnswersUnsorted = originalQuery.answers.map { answer ->
            if (answer.id == answerId) {
                updateLogic(answer)
            } else {
                answer
            }
        }

        val updatedAnswers = updatedAnswersUnsorted.sortedByDescending { answer ->
            Utils.parseISODateToMillis(answer.answeredOn)
        }

        val updatedQuery = originalQuery.copy(answers = updatedAnswers)
        bikeQueries[queryIndex] = updatedQuery

        return updatedQuery
    }
    suspend fun postAnswer(answer: String, queryId: Int?, name: String?){
        if (queryId == null || name.isNullOrBlank()) {
            return  // Cannot post without a query ID and a name
        }

        delay(300)

        val queryIndex = bikeQueries.indexOfFirst { it.id == queryId }

        if (queryIndex == -1) {
            return
        }

        val originalQuery = bikeQueries[queryIndex]

        val maxAnswerId = bikeQueries.flatMap { it.answers }.maxOfOrNull { it.id } ?: 0
        val newAnswerId = maxAnswerId + 1

        val newAnswer = Answer(
            id = newAnswerId,
            answer = answer,
            answeredOn = Utils.formatClientMillisToISO(System.currentTimeMillis()), // Current timestamp
            answeredByName = name,
            answeredByUrl = "https://example.com/placeholder_profile.jpg",
            likeCount = 0,
            dislikeCount = 0,
            isUserLiked = false,
            isUserDisliked = false
        )

        val updatedAnswers = originalQuery.answers + newAnswer

        val updatedQuery = originalQuery.copy(
            answers = updatedAnswers,
            answerCount = updatedAnswers.size,
            isAnswered = true
        )

        bikeQueries[queryIndex] = updatedQuery



    }


    suspend fun likeOrRemoveLikeOfAnswer(answerId: Int): Query? {
        return updateAnswerInQuery(answerId) { answer ->
            val newLikeCount =
                if (answer.isUserLiked) answer.likeCount - 1 else answer.likeCount + 1

            val newDislikeCount =
                if (answer.isUserDisliked) answer.dislikeCount - 1 else answer.dislikeCount

            answer.copy(
                likeCount = newLikeCount,
                dislikeCount = newDislikeCount,
                isUserLiked = !answer.isUserLiked,
                isUserDisliked = false
            )
        }
    }

    suspend fun likeOrRemoveDislikeOfAnswer(answerId: Int): Query? {
        return updateAnswerInQuery(answerId) { answer ->
            val newDislikeCount =
                if (answer.isUserDisliked) answer.dislikeCount - 1 else answer.dislikeCount + 1

            val newLikeCount = if (answer.isUserLiked) answer.likeCount - 1 else answer.likeCount

            answer.copy(
                dislikeCount = newDislikeCount,
                likeCount = newLikeCount,
                isUserDisliked = !answer.isUserDisliked,
                isUserLiked = false
            )
        }
    }
}