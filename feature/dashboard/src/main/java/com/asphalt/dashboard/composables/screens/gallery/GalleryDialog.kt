package com.asphalt.dashboard.composables.screens.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.asphalt.commonui.R
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.LightGray40
import com.asphalt.commonui.theme.NeutralBlackGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.REDLIGHT
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.BorderedButton

@Composable
fun GalleryDialog(onDismiss: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {

        Card(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(
                containerColor = NeutralWhite  // Change background here
            )
        ) {
            Box(modifier = Modifier.fillMaxWidth().padding(top = Dimensions.padding16), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.select_photos).uppercase(),
                    style = TypographyBold.bodyMedium,
                    color = NeutralBlackGrey,
                    fontSize = Dimensions.textSize18,

                    )
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 16.dp)
                    .border(
                        width = 2.dp,
                        color = LightGray40,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
            ) {

                Box(
                    modifier = Modifier
                        .padding(
                            vertical = Dimensions.padding16,
                            horizontal = Dimensions.padding16
                        )
                        .heightIn(max = screenHeight * 0.6f)
                    //.weight(1f)

                ) {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .wrapContentHeight()
                    ) {
                        items(10) { index ->

                            Image(
                                painter = painterResource(R.drawable.community_features),
                                modifier = Modifier
                                    .height(130.dp)
                                    .width(131.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                contentDescription = "", contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = Dimensions.padding16,
                            end = Dimensions.padding16,
                            bottom = Dimensions.padding16
                        ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    BorderedButton(
                        onClick = {
                            // showGalleryDialog = true
                        },
                        modifier = Modifier
                            .height(Dimensions.size50)
                            .background(NeutralWhite)
                            .weight(1f),
                        buttonRadius = Dimensions.size10,
                        contentPaddingValues = PaddingValues(0.dp),
                        borderColor = REDLIGHT
                    ) {
                        Text(
                            text = stringResource(R.string.cancel).uppercase(),
                            style = TypographyMedium.bodySmall,
                            color = REDLIGHT
                        )
                    }
                    //Spacer(Modifier.width(Dimensions.padding15))

                    Box(
                        modifier = Modifier
                            .height(Dimensions.size50)
                            .fillMaxWidth()
                            .weight(1f)
                            .background(
                                color = PrimaryDarkerLightB75,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.select_photos).uppercase(),
                            style = TypographyMedium.bodySmall,
                            color = NeutralWhite,

                            )
                    }

                    /*GradientButton(
                        onClick = {

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimensions.size50)
                            .weight(1f),
                        buttonRadius = Dimensions.size10,
                        startColor = PrimaryDarkerLightB75,
                        endColor = PrimaryDarkerLightB75,
                    ) {
                        Text(
                            text = stringResource(R.string.select_photos).uppercase(),
                            style = TypographyMedium.bodySmall,
                            color = NeutralWhite
                        )
                    }*/
                }

            }

        }
    }
}


@Preview
@Composable
fun GalleryDialogPreview() {
    GalleryDialog({})
}