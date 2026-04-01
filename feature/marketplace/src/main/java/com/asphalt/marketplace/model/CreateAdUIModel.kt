package com.asphalt.marketplace.model

import android.service.quicksettings.Tile

data class CreateAdUIModel(
    var isShowTitleError: Boolean = false,
    var isShowColorError: Boolean = false,
    var isShowFuelError: Boolean = false,
    var isShowPriceError: Boolean = false,
    var isShowDescError: Boolean = false,
    var isLocationError: Boolean = false,
    var tile: String = "",
    var color: String = "",
    var fuel: String = "",
    var price: Double? = null,
    var desc: String = "",
    var location: String = ""
)