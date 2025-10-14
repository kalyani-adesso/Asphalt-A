package com.asphalt.commonui.constants

import com.asphalt.commonui.theme.Dimensions

object Constants {
    const val DONUT_START_ANGLE = -180f
    const val DONUT_STROKE_WIDTH = 40f
    const val DONUT_SEGMENT_GAP_OFFSET = 5f
    const val DONUT_RADIUS_OFFSET = 5f
    const val DONUT_SWEEP_OFFSET = 2.5f
    const val DONUT_TOTAL_ANGLE = 360f
    val DEFAULT_CORNER_RADIUS = Dimensions.radius15
    val DEFAULT_BUTTON_HEIGHT = Dimensions.size60
    const val SPLASH_TIMER: Long = 2000
    val DEFAULT_BORDER_STROKE = Dimensions.spacing1
    val DEFAULT_SCREEN_HORIZONTAL_PADDING = Dimensions.spacing15
    val AVATAR_DEFAULT_SIZE = Dimensions.size73
    val AVATAR_DEFAULT_BORDER_WIDTH = Dimensions.size4
    const val DATA_STORE_NAME = "asphalt_prefs"

    const val DATE_FORMAT_PLACES_VISITED = "dd MMM yyyy"
    const val NO_OF_MONTHS_PLACE_VISITED = 7

    //Firebase Data base
    const val FIREBASE_DB = "users_android"
    const val Firebase_user_name = "name"
    const val Firebase_user_email = "email"


    //Create Ride Tab Constants
    const val TAB_DETAILS = 1
    const val TAB_ROUTE = 2
    const val TAB_PARTICIPANT = 3
    const val TAB_REVIEW = 4
    const val TAB_SHARE = 5


}