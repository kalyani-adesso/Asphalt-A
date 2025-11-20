package com.asphalt.dashboard.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import kotlinx.coroutines.launch

class DashboardRideSummaryVM(val ridesRepository: RidesRepository,val androidUserVM: AndroidUserVM): ViewModel() {
    private val userUID: String
        get() = androidUserVM.getCurrentUserUID()
    fun getRidesData(){
        viewModelScope.launch {
            APIHelperUI.handleApiResult(
                APIHelperUI.runWithLoader {
                    ridesRepository.getRideSummary("NUIAgBiffTUBtoxtgRGcn8wR9bR2","")

                },viewModelScope
            ){
                it
            }

        }
    }
}