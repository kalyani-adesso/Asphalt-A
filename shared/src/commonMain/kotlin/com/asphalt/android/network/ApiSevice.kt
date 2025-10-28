package com.asphalt.android.network

import com.asphalt.android.model.createride.CreateRideRoot
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import org.koin.core.component.KoinComponent

class ApiSevice (val client : KtorClient) : KoinComponent {
    suspend fun createRide(uid: String, createMap: CreateRideRoot): HttpResponse {
        val response: HttpResponse =
            client.getClient().post("/test_ride/${uid}.json") {
                setBody(createMap)
            }
        return response
    }
}