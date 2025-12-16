package com.asphalt.profile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.repository.rides.RidesRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.profile.data.StatsData
import com.asphalt.profile.mapper.calculateGrandTotals
import com.asphalt.profile.repositories.TotalStatsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TotalStatsVM(val statsRepo: TotalStatsRepo,val ridesRepository: RidesRepository,val androidUserVM: AndroidUserVM) : ViewModel() {
    private val _stats = MutableStateFlow<StatsData>(defaultStats())
    val userUID: String?
        get() = androidUserVM.userState.value?.uid
    val stats: StateFlow<StatsData> = _stats


    fun getTotalStats() {
        viewModelScope.launch {
            APIHelperUI.handleApiResult(
                APIHelperUI.runWithLoader {
                    userUID?.let { ridesRepository.getRideSummary(it) }

                }, viewModelScope
            ) {
//                _dashboardSummary.value = it
                _stats.value = it.calculateGrandTotals()

            }

        }
    }

    private fun defaultStats(): StatsData {
        return StatsData(0, 0)
    }


}