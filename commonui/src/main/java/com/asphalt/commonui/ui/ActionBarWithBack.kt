package com.asphalt.commonui.ui

import androidx.annotation.DrawableRes
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.TypographyBold


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionBarWithBack(@DrawableRes icon:Int, title: String, onBackPress: () -> Unit) {
    AsphaltTheme {
        Surface(
            shadowElevation = 8.dp, // Manual shadow elevation
            tonalElevation = 0.dp   // Optional, for tonal color behavior
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        color = NeutralBlack,
                        style = TypographyBold.bodyLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPress.invoke() }) {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = "Back",
                            tint = Color.Unspecified
                        )
                    }
                },
//            actions = {
//            IconButton(onClick = { /* Handle action */ }) {
//                Icon(
//                    painter = painterResource(R.drawable.ic_arrow_back),
//                    contentDescription = "Menu"
//                )
//            }
//            },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = NeutralWhite,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        }
    }

}

@Preview
@Composable
fun ActionPreview() {
    ActionBarWithBack(R.drawable.ic_arrow_back,"Your Riders"){

    }
}