package com.asphalt.queries.repositories

import com.asphalt.commonui.utils.Utils
import com.asphalt.queries.data.Answer
import com.asphalt.queries.data.Query
import com.asphalt.queries.data.bikeQueries
import kotlinx.coroutines.delay

class QueryRepo {
    suspend fun loadQueryList(): List<Query> {
        delay(200)
        return bikeQueries.sortedByDescending { query ->
            Utils.parseISODateToMillis(query.postedOn)
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


    /**
     * Finds the parent Query by answerId, updates the specific Answer using the
     * provided lambda, updates the Query list, and returns the modified Query.
     * @param answerId The ID of the answer to modify.
     * @param updateLogic A lambda that takes the old Answer and returns the new, modified Answer.
     */
    private suspend fun updateAnswerInQuery(
        answerId: Int,
        updateLogic: (Answer) -> Answer
    ): Query? {
        delay(100) // Simulate network delay

        // 1. Find the index of the Query that contains the target Answer
        val queryIndex = bikeQueries.indexOfFirst { query ->
            query.answers.any { it.id == answerId }
        }

        if (queryIndex == -1) {
            return null // Query not found
        }

        val originalQuery = bikeQueries[queryIndex]

        // 2. Map over answers and apply the specific updateLogic to the target Answer
        val updatedAnswers = originalQuery.answers.map { answer ->
            if (answer.id == answerId) {
                updateLogic(answer) // Apply the specific logic here
            } else {
                answer // Return other answers unchanged
            }
        }

        // 3. Create a new Query object and replace it in the list
        val updatedQuery = originalQuery.copy(answers = updatedAnswers)
        bikeQueries[queryIndex] = updatedQuery

        return updatedQuery
    }


    suspend fun likeOrRemoveLikeOfAnswer(answerId: Int): Query? {
        return updateAnswerInQuery(answerId) { answer ->
            val newLikeCount =
                if (answer.isUserLiked) answer.likeCount - 1 else answer.likeCount + 1

            // When LIKING, ensure the user DISLIKE is OFF
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

            // When DISLIKING, ensure the user LIKE is OFF
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