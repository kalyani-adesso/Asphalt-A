package com.asphalt.android.network

import com.asphalt.android.constants.APIConstants.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorClient {

    fun getClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 3000
                connectTimeoutMillis = 3000
                requestTimeoutMillis = 3000
            }

            install(DefaultRequest) {
                url {
                    // api call
                    host = BASE_URL
                    protocol = URLProtocol.HTTPS
                    headers {
                        append(name = HttpHeaders.Authorization, value = "")
                    }
                    // json format
                    contentType(type = ContentType.Application.Json)
                }
            }
        }
    }


    suspend fun Registration() {
        getClient()
            .get(urlString = "") // mention end point
        //.body<>()
    }


}