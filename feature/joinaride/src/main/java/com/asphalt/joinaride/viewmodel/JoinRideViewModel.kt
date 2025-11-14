package com.asphalt.joinaride.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.constants.APIConstants.RIDE_ACCEPTED
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.joinride.JoinRideRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.collections.emptyList
import kotlin.getValue
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class JoinRideViewModel(private val repository: JoinRideRepository) : ViewModel(), KoinComponent {
    private val _rides = MutableStateFlow<List<RidesData>>(emptyList())
   val rides = _rides.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    val userRepoImpl: UserRepoImpl by inject()
    val acceptedRides: StateFlow<List<RidesData>> =
        combine(rides, _searchQuery) { ridesData, query ->

            val q = query.trim().lowercase()
            // STEP 1 → Filter ACCEPTED rides
            val accepted = ridesData.filter { ride ->
                ride.participants.any { p ->
                    p.inviteStatus == RIDE_ACCEPTED
                }
            }
            // STEP 2 → Apply SEARCH on those accepted rides
            val finalList =
                if (q.isEmpty()) {
                    accepted
                } else {
                    accepted.filter { ride ->
                        val titleMatch =
                            ride.rideTitle?.lowercase()?.contains(q) == true
                        titleMatch
                    }
                }
            println("Accepted Count before search = ${accepted.size}")
            println("Accepted Count after search = ${finalList.size}")

            finalList

        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )
    init {
        getAllRiders()
    }
    fun getAllRiders() {
        viewModelScope.launch {
            var user = userRepoImpl.getUserDetails()

            val apiResult = APIHelperUI.runWithLoader {
                repository.getAllRidesList()
            }
            APIHelperUI.handleApiResult(apiResult, viewModelScope) { ride ->
                _rides.value = ride
            }
        }
    }
    // Called from UI
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}