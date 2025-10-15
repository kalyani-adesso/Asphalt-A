package com.asphalt.profile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.profile.data.StatsData
import com.asphalt.profile.repositories.TotalStatsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TotalStatsVM(val statsRepo: TotalStatsRepo) : ViewModel() {
    private val _stats = MutableStateFlow<StatsData>(defaultStats())
    val stats: StateFlow<StatsData> = _stats

    init {
        viewModelScope.launch {
            getStats()
        }
    }

    private fun defaultStats(): StatsData {
        return StatsData(0, 0)
    }

    suspend fun getStats() {
        _stats.value = statsRepo.getStats()
    }
}