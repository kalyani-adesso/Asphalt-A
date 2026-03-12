package com.asphalt.android.repository.joinride

import com.asphalt.android.model.joinride.RidersGroupModel

class RidersGroupRepo {

    private val groupRidersList = listOf(
        RidersGroupModel(
            riderName = "Sooraj Rajan",
            status = "Connected",
            distance = "55Kph",
            riderProfile = ""
        ),
        RidersGroupModel(
            riderName = "Aromal Sijulal",
            status = "Delayed",
            distance = "40Kph",
            riderProfile = ""
        ),
        RidersGroupModel(
            riderName = "Abhishek",
            status = "Stopped",
            distance = "65Kph",
            riderProfile = ""
        ),
    )
    fun getAllRiders() : List<RidersGroupModel> = groupRidersList
}