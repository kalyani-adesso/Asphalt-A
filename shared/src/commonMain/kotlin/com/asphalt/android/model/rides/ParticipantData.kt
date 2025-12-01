package com.asphalt.android.model.rides

import kotlinx.serialization.Serializable

@Serializable
class ParticipantData(var userId: String, var inviteStatus: Int)