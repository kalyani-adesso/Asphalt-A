package com.asphalt.marketplace.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asphalt.marketplace.constant.MarketPlaceConstants

class ProductListViewModel : ViewModel() {
    private val _mainTabController = mutableStateOf(MarketPlaceConstants.BROWSE)
    val mainTabController: State<Int> = _mainTabController

    fun setMainTabController(value: Int) {
        _mainTabController.value = value
    }
}