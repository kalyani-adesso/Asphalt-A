package com.asphalt.createride.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.places.PhotonFeature
import com.asphalt.android.model.places.PlaceData
import com.asphalt.android.repository.places.PlacesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlacesViewModel(val placesRepo: PlacesRepository) : ViewModel() {
    private val _placesMutableState = MutableStateFlow<List<PlaceData>>(emptyList())
    val placeData: StateFlow<List<PlaceData>> = _placesMutableState

    private val _autoCompletePlaces = MutableStateFlow<List<PhotonFeature>>(emptyList())
    val autoCompletePlaces: StateFlow<List<PhotonFeature>> = _autoCompletePlaces

    private val _showLoader = MutableStateFlow(false)
    val showLoader: StateFlow<Boolean> = _showLoader
    private var searchJob: Job? = null

    fun clearList() {
        _placesMutableState.value = emptyList()
        _autoCompletePlaces.value = emptyList()
    }

    fun getPlaces(location: String) {
        viewModelScope.launch {
            _showLoader.value = true
            val result = placesRepo.getPlaces(location)
            when (result) {
                is APIResult.Error -> _showLoader.value = false
                is APIResult.Success -> {
                    _showLoader.value = false
                    _placesMutableState.value = result.data
                }
            }
        }
    }

    fun getAutoCompletePlaces(location: String, needDelay: Boolean = true) {
        searchJob?.cancel()

        if (location.trim().length < 3) {
            clearList()
            _showLoader.value = false
            return
        }

        searchJob = viewModelScope.launch {
            if (needDelay)
                delay(500)

            _showLoader.value = true

            val result = placesRepo.getPlacesForAutoComplete(location)

            when (result) {
                is APIResult.Error -> {
                    _showLoader.value = false
                }

                is APIResult.Success -> {
                    _showLoader.value = false
                    _autoCompletePlaces.value = result.data.features
                    Log.d("autocomplete", _autoCompletePlaces.value.toString())
                }
            }
        }
    }
}

