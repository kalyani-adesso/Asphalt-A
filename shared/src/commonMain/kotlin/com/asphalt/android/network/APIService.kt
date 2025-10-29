package com.asphalt.android.network

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.queries.AnswerDTO
import com.asphalt.android.model.queries.AnswerRequestDTO
import com.asphalt.android.model.queries.QueryRequestDTO
import com.asphalt.android.model.queries.QueryResponseDTO

interface APIService {
    suspend fun getQueries(): APIResult<Map<String, QueryResponseDTO>?>
    suspend fun postQuery(queryRequestDTO: QueryRequestDTO): APIResult<GenericResponse>
    suspend fun postAnswer(
        queryId: String,
        answerRequestDTO: AnswerRequestDTO
    ): APIResult<GenericResponse>

    suspend fun likeQuery(queryId: String, userId: String): APIResult<Unit>
    suspend fun deleteLikeQuery(queryId: String, userId: String): APIResult<Unit>
    suspend fun likeOrDislikeAnswer(
        queryId: String,
        answerId: String,
        userId: String,
        isLike: Boolean
    ): APIResult<Unit>

    suspend fun deleteLikeOrDislikeAnswer(
        queryId: String,
        answerId: String,
        userId: String
    ): APIResult<Unit>
    suspend fun getQuery(queryId: String): APIResult<QueryResponseDTO>?
    suspend fun getAnswer(queryId: String,answerId: String): APIResult<AnswerDTO>?
}