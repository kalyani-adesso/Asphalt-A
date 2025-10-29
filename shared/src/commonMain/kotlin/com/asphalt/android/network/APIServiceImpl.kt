package com.asphalt.android.network

import com.asphalt.android.constants.APIConstants.ANSWERS_URL
import com.asphalt.android.constants.APIConstants.QUERIES_URL
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.queries.AnswerRequestDTO
import com.asphalt.android.model.queries.QueryRequestDTO
import com.asphalt.android.model.queries.QueryResponseDTO
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
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
    override suspend fun getQueries(): Map<String, QueryResponseDTO>? {
        val httpResponse = client.getClient().get(urlString = buildUrl(QUERIES_URL))
        return httpResponse.body()
    }

    override suspend fun postQuery(queryRequestDTO: QueryRequestDTO) {
        client.getClient().post(buildUrl(QUERIES_URL)) {
            setBody(queryRequestDTO)
        }
    }

    override suspend fun postAnswer(
        queryId: String,
        answerRequestDTO: AnswerRequestDTO
    ) {
        client.getClient().post(buildUrl("$QUERIES_URL/$queryId$ANSWERS_URL")) {
            setBody(answerRequestDTO)
        }
    }

    companion object {
        private fun buildUrl(url: String): String {
            return "$url.json"
        }

    }
}