package com.asphalt.joinaride

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asphalt.android.viewmodel.joinridevm.RidesDifficultyViewModel
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GreenLIGHT
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryBrighterLightW25
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.CommonStatType
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.ui.RoundedBox
import com.asphalt.commonui.utils.ComposeUtils
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ConnectedRideEnd(
    modifier: Modifier = Modifier,
    setTopAppBarState: (AppBarState) -> Unit,
    viewModel: RidesDifficultyViewModel = koinViewModel(),
    onNavigateToDashboard: () -> Unit
    ) {

    val stats = viewModel.stats.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(value = false) }
    val scope = rememberCoroutineScope()

    setTopAppBarState(AppBarState(title = stringResource(R.string.connected_ride)))

    LaunchedEffect(Unit) {
        delay(2000)
        showDialog = true
    }

    Column(
        modifier = modifier.fillMaxSize()
            .padding(top = Dimensions.size30),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.size200)
                .padding(start = Dimensions.padding16, end = Dimensions.padding16),
            colors = CardDefaults.cardColors(
                containerColor = Color.White // or use NeutralWhite
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_tick_green_10),
                    contentDescription = "",
                    modifier = Modifier.size(Dimensions.padding100),

                    )
                Spacer(Modifier.height(Dimensions.padding20))
                Text(
                    text = stringResource(id = R.string.ride_created),
                    style = TypographyBold.bodyMedium
                )
                Spacer(Modifier.height(Dimensions.padding16))
                Text(
                    text = stringResource(R.string.share_with_friends),
                    style = Typography.bodySmall,
                    color = NeutralDarkGrey
                )
            }
        }

        ComposeUtils.CommonContentBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.padding16)
                .background(color = NeutralLightPaper)

        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = Dimensions.padding16,
                    vertical = Dimensions.padding20
                ),
                verticalArrangement = Arrangement.spacedBy(Dimensions.padding16)
            ) {
                ComposeUtils.SectionTitle(stringResource(R.string.ride_difficulty))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.size20),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatView(statType = CommonStatType.TimeStatType, stats.value.rideCount)
                    StatView(statType = CommonStatType.DistanceStatType, stats.value.rideCount)
                    StatView(statType = CommonStatType.RidesStatType, stats.value.rideCount)
                }
            }
        }

        if (showDialog) {
            RatingThisRide(onDismiss = {
                scope.launch {
                    showDialog = false
                }
            }, onSubmit = {

            })
        }

        Spacer(modifier=Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                Dimensions.padding20, Alignment.CenterHorizontally
            ),
            modifier = Modifier.fillMaxWidth()
                .padding(Dimensions.padding20)
                .align(Alignment.CenterHorizontally),
        ) {

            Button(
                onClick = {
                    onNavigateToDashboard.invoke()
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeutralWhite),
                modifier = Modifier
                    .weight(1f)
                    .height(Dimensions.size60)
                    .border(1.dp, color = PrimaryBrighterLightW75,
                        shape = RoundedCornerShape(size = Dimensions.size10)),
            ) {
                Text(
                    stringResource(R.string.share).uppercase(),
                    color = PrimaryBrighterLightW75,
                    style = TypographyBold.titleMedium,
                    fontSize = Dimensions.textSize16,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
            GradientButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    onNavigateToDashboard.invoke()

                },
                buttonHeight = Dimensions.size60,
                contentPadding = PaddingValues(Dimensions.size2)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.home).uppercase(),
                        color = NeutralWhite,
                        style = TypographyBold.titleMedium,
                        fontSize = Dimensions.textSize16,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        }
    }
}
@Composable
fun RowScope.StatView(statType: CommonStatType,count: Int) {
    RoundedBox(
        modifier = Modifier.weight(1f),
        borderColor = NeutralLightGrey,
        borderStroke = Dimensions.spacing1,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimensions.padding15)
                .wrapContentWidth(),
            verticalArrangement = Arrangement.spacedBy(
                Dimensions.spacing10, Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(painter = painterResource(id = statType.iconRes),
                contentDescription = null, tint = statType.tint)

            ComposeUtils.SectionTitle(text = "$count")

            ComposeUtils.SectionSubtitle(
                stringResource(statType.statDescriptionResId),
                color = NeutralBlack, modifier = Modifier.offset(y = Dimensions.spacingNeg4)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectedRideEndPreview(modifier: Modifier = Modifier) {


}