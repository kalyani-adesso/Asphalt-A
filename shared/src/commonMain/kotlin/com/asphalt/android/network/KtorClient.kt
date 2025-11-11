package com.asphalt.android.network

import com.asphalt.android.constants.APIConstants.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorClient {

    fun getClient(baseUrl: String = BASE_URL, passHeaders: Boolean = false): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true // ignore extra fields in JSON
                    prettyPrint = true
                    isLenient = true // accept non-strict JSON
                    encodeDefaults = true
                    coerceInputValues= true // coerce missing primitive values to defaults
                })
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 3000
                connectTimeoutMillis = 3000
                requestTimeoutMillis = 3000
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }

            install(DefaultRequest) {
                url {
                    // api call
                    host = baseUrl
                    protocol = URLProtocol.HTTPS
                    if (passHeaders)
                        headers {
                            append(
                                name = HttpHeaders.UserAgent,
                                value = "adessomotorbiker@gmail.com"
                            )
                        }
                    // json format
                    contentType(type = ContentType.Application.Json)
                }
            }
        }
    }
}