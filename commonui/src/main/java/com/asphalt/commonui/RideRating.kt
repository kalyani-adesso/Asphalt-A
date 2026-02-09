package com.asphalt.commonui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ReadOnlyRatingBar(
    rating: Int = 0,
    maxRating: Int = 5,
    starSize: Dp = 24.dp,
    spaceBetween: Dp = 5.dp,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        repeat(maxRating) { index ->
            val icon = if (index < rating)
                R.drawable.ic_filled_start
            else
                R.drawable.ic_outlined_star

            Icon(
                painter = painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier
                    .size(starSize)
                    .testTag("star_$index")
                    .semantics {
                        stateDescription = if (index < rating) "filled" else "empty"
                    },
                tint = Color.Unspecified  // keep original color from vector
            )
            Spacer(Modifier.width(spaceBetween))
        }
    }
}


@Preview
@Composable
fun RatePreview() {
    ReadOnlyRatingBar()
}
