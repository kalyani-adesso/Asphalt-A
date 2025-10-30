package com.asphalt.android.network.queries

import com.asphalt.android.constants.APIConstants.ANSWERS_URL
import com.asphalt.android.constants.APIConstants.LIKES_DISLIKES_URL
import com.asphalt.android.constants.APIConstants.LIKES_URL
import com.asphalt.android.constants.APIConstants.QUERIES_URL
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.queries.AnswerDTO
import com.asphalt.android.model.queries.AnswerRequestDTO
import com.asphalt.android.model.queries.QueryRequestDTO
import com.asphalt.android.model.queries.QueryResponseDTO
import com.asphalt.android.network.BaseAPIService
import com.asphalt.android.network.KtorClient
import io.ktor.client.call.body

class QueryAPIServiceImpl(client: KtorClient) : BaseAPIService(client),
    QueryAPIService {

    override suspend fun getQueries(): APIResult<Map<String, QueryResponseDTO>?> {
        return safeApiCall {
            get(QUERIES_URL).body()
        }
    }


    override suspend fun postQuery(queryRequestDTO: QueryRequestDTO): APIResult<GenericResponse> {
        return safeApiCall {
            post(queryRequestDTO, QUERIES_URL).body()
        }
    }

    override suspend fun postAnswer(
        queryId: String,
        answerRequestDTO: AnswerRequestDTO
    ): APIResult<GenericResponse> {
        return safeApiCall {
            post(answerRequestDTO, "$QUERIES_URL/$queryId$ANSWERS_URL").body()
        }
    }


    override suspend fun likeQuery(
        queryId: String,
        userId: String
    ): APIResult<Unit> {
        return safeApiCall {
            put(true, "$QUERIES_URL/$queryId$LIKES_URL/$userId").body()
        }
    }

    override suspend fun deleteLikeQuery(
        queryId: String,
        userId: String
    ): APIResult<Unit> {
        return safeApiCall {
            delete("$QUERIES_URL/$queryId$LIKES_URL/$userId").body()
        }
    }

    override suspend fun likeOrDislikeAnswer(
        queryId: String,
        answerId: String,
        userId: String,
        isLike: Boolean
    ): APIResult<Unit> {
        return safeApiCall {
            put(
                isLike,
                "$QUERIES_URL/$queryId$ANSWERS_URL/$answerId/$LIKES_DISLIKES_URL/$userId"
            ).body()
        }
    }

    override suspend fun deleteLikeOrDislikeAnswer(
        queryId: String,
        answerId: String,
        userId: String
    ): APIResult<Unit> {
        return safeApiCall {
            delete(
                "$QUERIES_URL/$queryId$ANSWERS_URL/$answerId/$LIKES_DISLIKES_URL/$userId"
            ).body()
        }
    }

    override suspend fun getQuery(queryId: String): APIResult<QueryResponseDTO>? {
        return safeApiCall {
            get("$QUERIES_URL/$queryId").body()
        }
    }

    override suspend fun getAnswer(
        queryId: String,
        answerId: String
    ): APIResult<AnswerDTO>? {
        return safeApiCall {
            get("$QUERIES_URL/$queryId$ANSWERS_URL/$answerId").body()
        }
    }
}