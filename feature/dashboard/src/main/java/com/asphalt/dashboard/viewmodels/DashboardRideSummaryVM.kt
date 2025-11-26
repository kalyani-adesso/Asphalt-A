package com.asphalt.dashboard.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.model.dashboard.DashboardDomain
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardRideSummaryVM(
    val ridesRepository: RidesRepository,
    val androidUserVM: AndroidUserVM
) : ViewModel() {
    private val userUID: String
        get() = androidUserVM.getCurrentUserUID()
    private val _dashboardSummary = MutableStateFlow<List<DashboardDomain>>(emptyList())
    val dashboardSummary = _dashboardSummary.asStateFlow()

    fun getRidesData() {
        viewModelScope.launch {
            APIHelperUI.handleApiResult(
                APIHelperUI.runWithLoader {
                    ridesRepository.getRideSummary(userUID)
                    ridesRepository.getRideSummary("NUIAgBiffTUBtoxtgRGcn8wR9bR2")

                }, viewModelScope
            ) {
                _dashboardSummary.value = it
            }

        }
    }


}