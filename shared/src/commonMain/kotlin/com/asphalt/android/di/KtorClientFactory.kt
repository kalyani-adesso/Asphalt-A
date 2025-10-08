package com.asphalt.android.di

import io.ktor.client.HttpClient

expect class KtorClientFactory() {

    fun create() : HttpClient
}