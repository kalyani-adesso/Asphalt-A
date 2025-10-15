package com.asphalt.profile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.profile.data.VehicleData
import com.asphalt.profile.repositories.YourVehiclesRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class YourVehiclesVM(val yourVehiclesRepo: YourVehiclesRepo) : ViewModel() {
    private val _vehiclesList = MutableStateFlow<List<VehicleData>>(emptyList())
    val vehiclesList: StateFlow<List<VehicleData>> = _vehiclesList

    init {
        getVehicles()
    }

    fun addVehicle(data: VehicleData) {
        data.let {
            viewModelScope.launch {
                yourVehiclesRepo.addVehicle(data)
                updateList()
            }
        }
    }

    fun deleteVehicle(data: VehicleData) {
        viewModelScope.launch { yourVehiclesRepo.deleteVehicle(data) }
        updateList()
    }

    fun updateList() {
        getVehicles()
    }

    fun getVehicles() {
        viewModelScope.launch { _vehiclesList.value = yourVehiclesRepo.getVehicles() }
    }
}