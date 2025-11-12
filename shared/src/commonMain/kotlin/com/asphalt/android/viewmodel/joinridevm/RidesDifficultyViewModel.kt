package com.asphalt.android.viewmodel.joinridevm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.model.joinride.RidesDifficultyModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RidesDifficultyViewModel : ViewModel() {

    private val _stats = MutableStateFlow<RidesDifficultyModel>(defaultStats())
    val stats: StateFlow<RidesDifficultyModel> = _stats

    init {
        viewModelScope.launch {
           // getStats()
        }
    }

    private fun defaultStats(): RidesDifficultyModel {
        return RidesDifficultyModel(0)
    }

//    suspend fun getStats() {
//        _stats.value = statsRepo.getStats()
//    }

}