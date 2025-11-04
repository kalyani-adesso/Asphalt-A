package com.asphalt.android.model.joinride

data class RidersGroupModel(
    val riderProfile : String,
    val riderName : String,
    val status : String,  // connected/delayed/stop
    val distance : String,
)