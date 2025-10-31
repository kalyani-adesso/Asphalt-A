package com.asphalt.android.model.rides

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
 class UserInvites(

    @SerialName("acceptInvite")
    var acceptInvite: Int = 0 // 0 - invite sent,1- invite accept, 2- invite declined
)