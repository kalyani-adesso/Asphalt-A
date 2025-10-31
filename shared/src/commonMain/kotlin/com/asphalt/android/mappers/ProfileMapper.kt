package com.asphalt.android.mappers

import com.asphalt.android.model.profile.BikeDTO
import com.asphalt.android.model.profile.BikeDomain
import com.asphalt.android.model.profile.ProfileDTO
import com.asphalt.android.model.profile.ProfileDomain
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.orEmpty

fun Map<String, BikeDTO>?.toBikeListDomain(): List<BikeDomain>? {
    return this?.map { (id, bikeDto) ->
        bikeDto.toBikeDomain(id)
    }
}
fun BikeDTO.toBikeDomain(id: String): BikeDomain {
    return BikeDomain(
        bikeId = id,
        type = bikeType,
        make = make,
        model = model
    )
}
 fun ProfileDTO.toProfileDomain(id:String): ProfileDomain {
    return ProfileDomain(
        id,
        userName = userName,
        email = email,
        phoneNumber = phoneNumber,
        emergencyContact = emergencyContact,
        drivingLicense = drivingLicense,
        isMechanic = isMechanic,
        bikes = bikes?.map { (key, value) -> value.toBikeDomain(key) }.orEmpty(),
        profilePicUrl = profilePicUrl
    )
}
