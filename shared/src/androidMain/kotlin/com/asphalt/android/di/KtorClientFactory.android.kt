package com.asphalt.android.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual class KtorClientFactory actual constructor() {
    actual fun create(): HttpClient = HttpClient(OkHttp) {

       install(ContentNegotiation) {
           json(Json { ignoreUnknownKeys = true })
       }
    }
}