package com.asphalt.dashboard.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.PrimaryDeepBlue
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.BorderedButton
import com.asphalt.commonui.ui.GradientButton

@Composable
fun CreateOrJoinRide(onCreateRideClick: () -> Unit, onJoinRideClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        GradientButton(endColor = PrimaryDeepBlue, onClick = { onCreateRideClick() }) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(R.drawable.ic_path), null)
                Spacer(Modifier.width(Dimensions.spacing10))
                Text(
                    stringResource(R.string.create_ride).uppercase(),
                    color = NeutralWhite,
                    style = TypographyBold.bodyMedium
                )
            }
        }
        Spacer(Modifier.width(Dimensions.spacing15pt25))
        BorderedButton(onClick = { onJoinRideClick() }) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_group_blue),
                    contentDescription = null
                )
                Spacer(Modifier.width(Dimensions.spacing10))
                Text(
                    stringResource(R.string.join_ride).uppercase(),
                    color = PrimaryDarkerLightB75,
                    style = TypographyBold.bodyMedium
                )
            }
        }


    }
}

@Preview
@Composable
fun RidePreview() {
    CreateOrJoinRide({
    }, {

    })
}
