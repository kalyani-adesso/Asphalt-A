package com.asphalt.joinaride.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.constants.APIConstants
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.UserDomain
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.joinride.JoinRideRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.commonui.utils.Utils
import com.asphalt.dashboard.data.YourRideDataModel
import com.asphalt.dashboard.utils.RidesFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.collections.emptyList
import kotlin.getValue

class JoinRideViewModel(private val repository: JoinRideRepository) : ViewModel(), KoinComponent {

    private val _rides = MutableStateFlow<List<RidesData>>(emptyList())
    val rides : StateFlow<List<RidesData>> = _rides.asStateFlow()

    val userRepoImpl: UserRepoImpl by inject()

    fun getAllRiders() {
        viewModelScope.launch {
            var user = userRepoImpl.getUserDetails()
            val apiResult = APIHelperUI.runWithLoader {
                repository.getAllRidesList()
            }
            APIHelperUI.handleApiResult(apiResult, viewModelScope) { rides ->
                _rides.value = rides
            }

          //  var acceptedRides = RidesFilter.getInvites(user?.uid ?: "",)
        }
    }
    var searchQuery by mutableStateOf("")
        private set

    fun onQueryChange(newQuery: String) {
        searchQuery = newQuery
    }
}
