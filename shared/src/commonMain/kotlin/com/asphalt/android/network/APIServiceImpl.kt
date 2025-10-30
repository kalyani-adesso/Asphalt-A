package com.asphalt.android.network

import com.asphalt.android.constants.APIConstants.ANSWERS_URL
import com.asphalt.android.constants.APIConstants.LIKES_DISLIKES_URL
import com.asphalt.android.constants.APIConstants.LIKES_URL
import com.asphalt.android.constants.APIConstants.QUERIES_URL
import com.asphalt.android.constants.APIConstants.USER_URL
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.User
import com.asphalt.android.model.queries.AnswerDTO
import com.asphalt.android.model.queries.AnswerRequestDTO
import com.asphalt.android.model.queries.QueryRequestDTO
import com.asphalt.android.model.queries.QueryResponseDTO
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.JsonConvertException

class APIServiceImpl(private val client: KtorClient) : APIService {

    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): APIResult<T> {
        return try {
            val result = apiCall()
            APIResult.Success(result)
        } catch (e: JsonConvertException) {
            // Handle JSON/Serialization errors specifically
            APIResult.Error(exception = e, code = 400)
        } catch (e: ClientRequestException) {
            // Handle 4xx HTTP errors
            APIResult.Error(exception = e, code = e.response.status.value)
        } catch (e: ServerResponseException) {
            // Handle 5xx HTTP errors
            APIResult.Error(exception = e, code = e.response.status.value)
        } catch (e: HttpRequestTimeoutException) {
            // Handle timeout errors
            APIResult.Error(exception = e, code = 408)
        } catch (e: Exception) {
            // Handle general errors (network, unknown)
            APIResult.Error(exception = e, code = null)
        }
    }

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

    override suspend fun getAllUser(): APIResult<Map<String, User>> {
        return safeApiCall {
            get(USER_URL).body()
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

    private suspend fun get(url: String): HttpResponse {
        return client.getClient().get(buildUrl(url))
    }

    private suspend fun post(body: Any, url: String): HttpResponse {
        return client.getClient().post(buildUrl(url)) {
            setBody(body)
        }
    }

    private suspend fun put(body: Any, url: String): HttpResponse {
        return client.getClient().put(buildUrl(url)) {
            setBody(body)
        }
    }

    private suspend fun delete(url: String): HttpResponse {
        return client.getClient().delete(buildUrl(url))
    }

    companion object {
        private fun buildUrl(url: String): String {
            return "$url.json"
        }
    }
}