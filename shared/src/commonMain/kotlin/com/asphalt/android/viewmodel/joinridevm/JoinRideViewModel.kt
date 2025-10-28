package com.asphalt.android.viewmodel.joinridevm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.joinride.JoinRideModel
import com.asphalt.android.repository.joinride.JoinRideRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JoinRideViewModel(private val repository: JoinRideRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery : StateFlow<String> = _searchQuery.asStateFlow()

    private val _filteredList = MutableStateFlow<List<JoinRideModel>>(emptyList())
    val filteredList : StateFlow<List<JoinRideModel>> = _filteredList.asStateFlow()

    init {
        // load all items initially
        _filteredList.value = repository.getAllRiders()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _filteredList.value = if (query.isEmpty()) {
                // get all list
                repository.getAllRiders()
            }
            else {
                    repository.getAllRiders().filter {
                        it.byWhom.contains(query, ignoreCase = true)
                                || it.destination.contains(query,ignoreCase = true)
                                || it.rideType.contains(query,ignoreCase = true)
                    }
            }
        }
    }
}