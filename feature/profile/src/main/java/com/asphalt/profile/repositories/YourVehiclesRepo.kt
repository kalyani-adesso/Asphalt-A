package com.asphalt.profile.repositories

import com.asphalt.profile.data.VehicleData
import com.asphalt.profile.data.vehicleList
import kotlinx.coroutines.delay

class YourVehiclesRepo {
    suspend fun addVehicle(data: VehicleData) {
        delay(200)
        vehicleList = vehicleList.plus(data)
    }

    suspend fun getVehicles(): List<VehicleData> {
        delay(200)
        return vehicleList
    }

    suspend fun deleteVehicle(vehicleData: VehicleData) {
        delay(200)
        vehicleList.minus(vehicleData)
    }

}