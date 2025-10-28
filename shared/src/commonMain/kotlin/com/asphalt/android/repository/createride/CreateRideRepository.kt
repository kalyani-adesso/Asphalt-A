package com.asphalt.android.repository.createride

import com.asphalt.android.model.createride.CreateRideRoot
import com.asphalt.android.network.ApiService
import io.ktor.client.statement.HttpResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateRideRepository() : KoinComponent{
    val apiService : ApiService by inject()
    suspend fun createRide(uid: String, createMap: CreateRideRoot): HttpResponse {
        val response: HttpResponse = apiService.createRide(uid,createMap)
        return response
    }
}