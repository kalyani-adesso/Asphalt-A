package com.asphalt.marketplace.model

import android.service.quicksettings.Tile

class CreateAdUIModel(
    var isShowTitleError: Boolean = false,
    var isShowColorError: Boolean = false,
    var isShowFuelError: Boolean = false,
    var isShowPriceError: Boolean = false,
    var isShowDescError: Boolean = false,
    var tile: String = "",
    var color: String = "",
    var fuel: String = "",
    var price: Int = 0,
    var desc: String = ""
)