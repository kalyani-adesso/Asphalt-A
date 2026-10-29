package com.asphalt.marketplace.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asphalt.commonui.R
import com.asphalt.marketplace.constant.MarketPlaceConstants
import com.asphalt.marketplace.model.SubTabModel

class ProductListViewModel : ViewModel() {
    private val _mainTabController = mutableStateOf(MarketPlaceConstants.BROWSE)
    val mainTabController: State<Int> = _mainTabController

    private val _subTabController = mutableStateOf(getSubTabs())
    val subTabController: State<ArrayList<SubTabModel>> = _subTabController

    fun setMainTabController(value: Int) {
        _mainTabController.value = value
    }

    fun updateSubMenu(ids: Int) {
        _subTabController.value = ArrayList(_subTabController.value.map { item ->
            item.copy(isSelected = item.id == ids)
        })
    }

    fun getSubTabs(): ArrayList<SubTabModel> {
        return arrayListOf(
            SubTabModel(R.string.all, MarketPlaceConstants.ALL, true),
            SubTabModel(R.string.motor_cycle, MarketPlaceConstants.MOTOR_CYCLE),
            SubTabModel(R.string.gear, MarketPlaceConstants.GEAR),
            SubTabModel(R.string.parts, MarketPlaceConstants.PARTS)
        )
    }
}