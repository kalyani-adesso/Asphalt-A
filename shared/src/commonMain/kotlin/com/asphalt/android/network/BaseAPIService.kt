package com.asphalt.android.network

import com.asphalt.android.model.APIResult
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

abstract class BaseAPIService(private val client: KtorClient)  {

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> T): APIResult<T> {
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


    protected suspend fun get(url: String): HttpResponse {
        return client.getClient().get(buildUrl(url))
    }

    protected suspend fun post(body: Any, url: String): HttpResponse {
        return client.getClient().post(buildUrl(url)) {
            setBody(body)
        }
    }

    protected suspend fun put(body: Any, url: String): HttpResponse {
        return client.getClient().put(buildUrl(url)) {
            setBody(body)
        }
    }

    protected suspend fun delete(url: String): HttpResponse {
        return client.getClient().delete(buildUrl(url))
    }

    protected fun buildUrl(url: String): String {
        return "$url.json"
    }
}