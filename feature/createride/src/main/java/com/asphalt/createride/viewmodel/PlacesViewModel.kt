package com.asphalt.createride.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.places.PhotonFeature
import com.asphalt.android.model.places.PlaceData
import com.asphalt.android.repository.places.PlacesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlacesViewModel(val placesRepo: PlacesRepository) : ViewModel() {
    private val placesMutableState: MutableState<List<PlaceData>> = mutableStateOf(emptyList())
    val placeData: State<List<PlaceData>> = placesMutableState

    private val _autoCompletePlaces: MutableState<List<PhotonFeature>> = mutableStateOf(emptyList())
    val autoCompletePlaces: State<List<PhotonFeature>> = _autoCompletePlaces

    private val showLoader = mutableStateOf(false)
    val _showLoader: State<Boolean> = showLoader
    private var searchJob: Job? = null

    fun clearList() {
        placesMutableState.value = emptyList()
        _autoCompletePlaces.value = emptyList()
    }

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

    fun getAutoCompletePlaces(location: String, needDelay: Boolean = true) {
        searchJob?.cancel()

        // 2. Prevent searching for tiny strings (saves API calls)
        if (location.trim().length < 3) {
            clearList()
            showLoader.value = false
            return
        }

        // 3. Start a new Coroutine Job
        searchJob = viewModelScope.launch {
            if (needDelay)
                delay(500)

            showLoader.value = true

            val result = placesRepo.getPlacesForAutoComplete(location)

            when (result) {
                is APIResult.Error -> {
                    showLoader.value = false
                }

                is APIResult.Success -> {
                    showLoader.value = false
                    _autoCompletePlaces.value = result.data.features
                    Log.d("autocomplete",_autoCompletePlaces.value.toString() )
                }
            }
        }
    }
}

