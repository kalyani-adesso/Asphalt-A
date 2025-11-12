package com.asphalt.android.repository.queries

import com.asphalt.android.mapApiResult
import com.asphalt.android.mappers.toAnswerDomain
import com.asphalt.android.mappers.toDomain
import com.asphalt.android.mappers.toQueryDomain
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.queries.AnswerDomain
import com.asphalt.android.model.queries.AnswerRequestDTO
import com.asphalt.android.model.queries.QueryDomain
import com.asphalt.android.model.queries.QueryRequestDTO
import com.asphalt.android.network.queries.QueryAPIService

class QueryRepository(private val apiService: QueryAPIService) {
    suspend fun getQueries(): APIResult<List<QueryDomain>> {
        return apiService.getQueries().mapApiResult { response ->
            response?.toDomain().orEmpty()
        }
    }


    suspend fun addQuery(
        queryTitle: String,
        queryDescription: String,
        categoryId: Int,
        postedOn: String,
        postedBy: String
    ): APIResult<GenericResponse> {
        val request = QueryRequestDTO(
            queryTitle = queryTitle,
            queryDescription = queryDescription,
            categoryId = categoryId,
            postedOn = postedOn,
            postedBy = postedBy,
        )
        return apiService.postQuery(request).mapApiResult {
            it
        }
    }

    suspend fun addAnswer(
        queryId: String,
        answer: String,
        answeredBy: String,
        answeredOn: String
    ): APIResult<GenericResponse> {
        val request = AnswerRequestDTO(answer, answeredBy, answeredOn)
        return apiService.postAnswer(queryId, request).mapApiResult {
            it
        }
    }

    suspend fun likeQuery(userId: String, queryId: String): APIResult<Unit> {
        return apiService.likeQuery(queryId, userId).mapApiResult {
        }
    }

    suspend fun removeLikeQuery(userId: String, queryId: String): APIResult<Unit> {
        return apiService.deleteLikeQuery(queryId, userId).mapApiResult {
        }
    }

    suspend fun likeOrDislikeAnswer(
        userId: String,
        queryId: String,
        answerId: String,
        isLike: Boolean
    ): APIResult<Unit> {
        return apiService.likeOrDislikeAnswer(queryId, answerId, userId, isLike).mapApiResult {
        }
    }

    suspend fun removeLikeOrDislikeAnswer(
        userId: String,
        queryId: String,
        answerId: String
    ): APIResult<Unit> {
        return apiService.deleteLikeOrDislikeAnswer(queryId, answerId, userId).mapApiResult {
        }
    }

    suspend fun getQueryByID(id: String): APIResult<QueryDomain>? {
        return apiService.getQuery(id)?.mapApiResult { it ->
            it.toQueryDomain(id)
        }
    }

    suspend fun getAnswerByID(queryId: String, answerId: String): APIResult<AnswerDomain>? {
        return apiService.getAnswer(queryId, answerId)?.mapApiResult {
            it.toAnswerDomain(answerId)
        }
    }

}


