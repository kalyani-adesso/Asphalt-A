package com.asphalt.android.viewmodel.createridevm

import com.asphalt.android.model.createride.CreateRideRoot
import com.asphalt.android.repository.createride.CreateRideRepository
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.MutableStateFlow

class CreateRideVm {

    suspend fun createRid(uid: String, createRide: CreateRideRoot): Boolean {
        //var createMap: MutableMap<String, CreateRideRoot> = mutableMapOf()
        // createMap.put(uid, createRide)
        var repo = CreateRideRepository()
        var response: HttpResponse = repo.createRide(uid, createRide)
        if (response.status.value == 200)
            return true
        else
            return false

    }
}