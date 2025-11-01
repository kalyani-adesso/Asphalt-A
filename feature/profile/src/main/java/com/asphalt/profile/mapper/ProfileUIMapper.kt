package com.asphalt.profile.mapper

import com.asphalt.android.model.CurrentUser
import com.asphalt.android.model.profile.BikeDomain
import com.asphalt.android.model.profile.ProfileDomain
import com.asphalt.profile.data.ProfileUIModel
import com.asphalt.profile.data.VehicleData

fun BikeDomain.toBikeUIModel(): VehicleData {
    return VehicleData(
        id = bikeId,
        type = type, make = make, model = model
    )
}

fun List<BikeDomain>.toBikeListUIModel(): List<VehicleData> {
    return map {
        it.toBikeUIModel()
    }
}

fun ProfileDomain.toProfileUIModel() : ProfileUIModel{
    return with(this) {
        ProfileUIModel(
            id,
            profilePicUrl,
            userName,
            email,
            phoneNumber,
            isMechanic,
            drivingLicense,
            emergencyContact
        )
    }
}
fun ProfileUIModel.toCurrentUserModel(): CurrentUser{
    return with(this){
        CurrentUser(isSuccess = true, name = username, uid = uid, email = userEmail)
    }
}