package com.asphalt.android.viewmodel.joinridevm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.joinride.JoinRideModel
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.repository.joinride.JoinRideRepository
import com.asphalt.android.repository.rides.RidesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.collections.emptyList
import kotlin.getValue

class JoinRideViewModel(private val repository: RidesRepository) : ViewModel(), KoinComponent {

    private val _rides = MutableStateFlow<List<RidesData>>(emptyList())
    val rides : StateFlow<List<RidesData>> = _rides.asStateFlow()
    fun getAllRiders() {
        viewModelScope.launch {

           // _rides.value = repository.getAllJoinRide()
        }
    }


//    fun onSearchQueryChanged(query: String) {
//        _searchQuery.value = query
//        viewModelScope.launch {
//            _filteredList.value = if (query.isEmpty()) {
//                // get all list
//                repository.getAllRiders()
//            }
//            else {
//                    repository.getAllRiders().filter {
//                        it.byWhom.contains(query, ignoreCase = true)
//                                || it.destination.contains(query,ignoreCase = true)
//                                || it.rideType.contains(query,ignoreCase = true)
//                    }
//            }
//        }
//    }
}

sealed class UIResult<out T> {
    data class Success<out T>(val data: T) : UIResult<T>()
    data class Error(val message: String) : UIResult<Nothing>()
    object Loading : UIResult<Nothing>()
}