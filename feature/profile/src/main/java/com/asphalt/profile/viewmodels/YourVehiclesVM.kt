package com.asphalt.profile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.repository.profile.ProfileRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.profile.data.VehicleData
import com.asphalt.profile.mapper.toBikeListUIModel
import com.asphalt.profile.mapper.toBikeUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class YourVehiclesVM(
    val profileRepository: ProfileRepository,
    val androidUserVM: AndroidUserVM
) : ViewModel() {
    private val _vehiclesList = MutableStateFlow<List<VehicleData>>(emptyList())
    val vehiclesList: StateFlow<List<VehicleData>> = _vehiclesList
    private val userUID: String
        get() = androidUserVM.getCurrentUserUID()



    fun addToVehicleList(newVehicle: VehicleData) {
        _vehiclesList.update { currentList ->
            currentList + listOf(newVehicle)
        }
    }

    fun deleteVehicleFromList(vehicleId: String) {
        _vehiclesList.update { currentList ->
            currentList.filter { vehicle -> vehicle.id != vehicleId }
        }
    }

    fun addBike(bikeType: Int, model: String, make: String) {
        viewModelScope.launch {
            APIHelperUI.handleApiResult(
                APIHelperUI.runWithLoader {
                    profileRepository.addBike(
                        userUID,
                        bikeType,
                        make,
                        model
                    )
                }, this
            ) {
                getBikeById(it.name)
            }
        }
    }

    fun deleteBike(bikeId: String) {
        viewModelScope.launch {
            APIHelperUI.handleApiResult(APIHelperUI.runWithLoader {
                profileRepository.deleteBike(
                    userUID,
                    bikeId
                )
            }, this) {
                deleteVehicleFromList(bikeId)
            }

        }
    }


    suspend fun getBikeById(bikeId: String) {
        APIHelperUI.handleApiResult(
            profileRepository.getBikeById(bikeId, userUID),
            viewModelScope
        ) {
            addToVehicleList(it.toBikeUIModel())
        }
    }

    fun getVehicles() {
        viewModelScope.launch {
            val bikes = APIHelperUI.runWithLoader {
                profileRepository.getBikes(userUID)
            }
            APIHelperUI.handleApiResult(bikes, viewModelScope) {
                _vehiclesList.value = it.toBikeListUIModel()
            }
        }
    }
}