package com.asphalt.android.constants

object APIConstants {
    const val BASE_URL = "asphalt-a-d59cb-default-rtdb.firebaseio.com"
    const val BASE_URL_PlACES = "nominatim.openstreetmap.org"
    const val  USERS_URL = "/users"
    const val BIKES_URL = "/bikes"
    const val QUERIES_URL = "/queries"
    const val ANSWERS_URL = "/answers"
    const val LIKES_URL = "/likes"
    const val LIKES_DISLIKES_URL = "/likes_dislikes"
    const val RIDES_URL = "/rides"
    const val PARTICIPANTS_URL = "/participants"
    const val GENERIC_ERROR_MSG = "Something went wrong!"
    const val PLACE_SEARCH = "/search"

    const val RATINGS = "/ratings"

    const val ONGOING_RIDE_URL = "/ongoing_ride"

    const val END_RIDE_SUMMARY_URL = "/endRide_summary"

    //Ride Invite Status
    const val RIDE_INVITED = 0
    const val RIDE_ACCEPTED = 1
    const val RIDE_DECLINED = 2
    const val RIDE_JOINED = 3
    const val END_RIDE = 4
}