package com.asphalt.dashboard.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.rides.RidesData
import com.asphalt.android.repository.UserRepoImpl
import com.asphalt.android.repository.rides.RidesRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RidesDetailsViewModel() : ViewModel(), KoinComponent {
    val ridesRepo: RidesRepository by inject()
    val userRepoImpl: UserRepoImpl by inject()
    private val ridesDetails = mutableStateOf<RidesData?>(null)
    val ridesData: State<RidesData?> = ridesDetails

    fun getSingleRide(ridesId: String) {
        viewModelScope.launch {
            val apiResult = APIHelperUI.runWithLoader {
                ridesRepo.getSingeRide(ridesId)
            }
            APIHelperUI.handleApiResult(apiResult, viewModelScope) { response ->
                ridesDetails.value = response
            }
        }

    }

    fun getUserList(){
        viewModelScope.launch {
            var user = userRepoImpl.getUserDetails()
            val userList=ridesDetails.value?.participants?:emptyList()

        }
    }
}