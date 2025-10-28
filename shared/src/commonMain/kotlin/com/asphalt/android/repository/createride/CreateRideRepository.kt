package com.asphalt.android.repository.createride

import com.asphalt.android.model.createride.CreateRideRoot
import com.asphalt.android.network.ApiSevice
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateRideRepository() : KoinComponent{
    val apiSevice : ApiSevice by inject()
    suspend fun createRide(uid: String, createMap: CreateRideRoot): HttpResponse {
        val response: HttpResponse = apiSevice.createRide(uid,createMap)
        return response
    }
}