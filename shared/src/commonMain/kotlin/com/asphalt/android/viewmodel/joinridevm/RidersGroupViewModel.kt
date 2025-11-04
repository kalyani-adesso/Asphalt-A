package com.asphalt.android.viewmodel.joinridevm

import androidx.lifecycle.ViewModel
import com.asphalt.android.model.joinride.RidersGroupModel
import com.asphalt.android.repository.joinride.RidersGroupRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RidersGroupViewModel(repo: RidersGroupRepo) : ViewModel() {

    private val _groupRiders = MutableStateFlow<List<RidersGroupModel>>(emptyList())
    val groupRiders : StateFlow<List<RidersGroupModel>> = _groupRiders.asStateFlow()

    init {
       _groupRiders.value =  repo.getAllRiders()
    }


}