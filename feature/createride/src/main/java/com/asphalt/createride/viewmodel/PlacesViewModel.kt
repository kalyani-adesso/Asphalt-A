package com.asphalt.createride.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.places.PlaceData
import com.asphalt.android.repository.places.PlacesRepository
import kotlinx.coroutines.launch

class PlacesViewModel(val placesRepo: PlacesRepository) : ViewModel() {
    private val placesMutableState: MutableState<List<PlaceData>> = mutableStateOf(emptyList())
    val placeData: State<List<PlaceData>> = placesMutableState

    private val showLoader = mutableStateOf(false)
    val _showLoader: State<Boolean> = showLoader

    fun getPlaces(location: String) {
        viewModelScope.launch {
            showLoader.value = true
            val result = placesRepo.getPlaces(location)
            when (result) {
                is APIResult.Error -> showLoader.value = false
                is APIResult.Success -> {
                    showLoader.value = false
                    placesMutableState.value = result.data
                }
            }

        }
    }

}