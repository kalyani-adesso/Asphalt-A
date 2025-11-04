package com.asphalt.commonui.ui

import androidx.compose.ui.graphics.Color
import com.asphalt.commonui.R

sealed class CommonStatType(val iconRes: Int, val tint: Color, val statTypeResId: Int, val statDescriptionResId: Int) {
    object TimeStatType : CommonStatType(
        iconRes= R.drawable.ic_clock,
        tint = Color(0XFF266EB7),
       R.string.time, R.string.duration
    )

    object DistanceStatType : CommonStatType(
        R.drawable.ic_location,
        tint = Color(0xFF00C950),
        R.string.duration, R.string.kms
    )

    object RidesStatType : CommonStatType(
        R.drawable.ic_group_white,
        tint = Color(0xFFFE8D01),
        R.string.riders, R.string.riders
    )
}