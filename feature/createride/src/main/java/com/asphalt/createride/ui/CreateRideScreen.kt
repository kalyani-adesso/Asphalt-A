package com.asphalt.createride.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.commonui.AppBarState
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.PrimaryDeepBlue
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.BorderedButton
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.ui.LoaderPopup
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.createride.ui.composables.DetailsSection
import com.asphalt.createride.ui.composables.ParticipantSection
import com.asphalt.createride.ui.composables.ReviewSection
import com.asphalt.createride.ui.composables.RouteSection
import com.asphalt.createride.ui.composables.ShareSection
import com.asphalt.createride.ui.composables.TabSelection
import com.asphalt.createride.viewmodel.CreateRideScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun CreateRideScreen(
    viewModel: CreateRideScreenViewModel = viewModel(),
    setTopAppBarState: (AppBarState) -> Unit,
    clickDone: () -> Unit
) {

    val scrollState = rememberScrollState()
    setTopAppBarState(
        AppBarState(
            title = stringResource(R.string.create_a_ride),
            actions = {
                Text(
                    text = "${viewModel.tabSelectState.value}/5",
                    style = TypographyMedium.bodyMedium
                )
                Spacer(Modifier.width(Dimensions.size4))
            }
        ))
    AsphaltTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = NeutralWhite)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                //contentPadding = PaddingValues(bottom = Dimensions.spacing250)
            ) {

                // ActionBarWithBack(R.drawable.ic_arrow_back, "Create a Ride") { }
                TabSelection(viewModel)
                Spacer(Modifier.height(Dimensions.padding20))
                if (viewModel.tabSelectState.value == Constants.TAB_DETAILS)
                    DetailsSection(viewModel)
                if (viewModel.tabSelectState.value == Constants.TAB_ROUTE)
                    RouteSection(viewModel)
                if (viewModel.tabSelectState.value == Constants.TAB_REVIEW)
                    ReviewSection(viewModel)
                if (viewModel.tabSelectState.value == Constants.TAB_PARTICIPANT)
                    ParticipantSection(mod = Modifier.weight(1f), viewModel)
                if (viewModel.tabSelectState.value == Constants.TAB_SHARE)
                    ShareSection()
            }
            BottomButtons(viewModel, clickDone)
            // Fixed bottom button

        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun BoxScope.BottomButtons(viewModel: CreateRideScreenViewModel, clickDone: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    if (viewModel.showLoader.value)
        LoaderPopup()
    /* if (viewModel.showToast.value) {
         LaunchedEffect(viewModel.showToast.value) {
             if (viewModel.showToast.value) {
                 Toast.makeText(context, "Ride Created", Toast.LENGTH_SHORT).show()
                 viewModel.updateTab(1)
                 viewModel.showToast.value = false
             }
         }
     }*/
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(color = NeutralWhite)
            .padding(
                /*bottom = WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding() + Dimensions.spacing20,*/
                bottom = Dimensions.spacing20,
                start = Dimensions.padding16,
                end = Dimensions.padding16
            )
    ) {
        if (viewModel.tabSelectState.value == Constants.TAB_DETAILS ||
            viewModel.tabSelectState.value == Constants.TAB_SHARE
        ) {//viewModel.tabSelectState.value == Constants.TAB_DETAILS

            GradientButton(
                onClick = {
                    if (viewModel.tabSelectState.value == Constants.TAB_SHARE) {
                        clickDone.invoke()
                    } else {
                        if (viewModel.detailsFieldValidation())
                            viewModel.updateTab(1)
                    }

                },
            ) {
                ComposeUtils.DefaultButtonContent(
                    if (viewModel.tabSelectState.value == Constants.TAB_SHARE) {
                        stringResource(R.string.done).uppercase()

                    } else {
                        stringResource(R.string.next_step).uppercase()
                    }
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(
                    Dimensions.spacing20
                )
            ) {
                BorderedButton(
                    modifier = Modifier.weight(1f), onClick = {
                        if (viewModel.tabSelectState.value > 1) {
                            viewModel.updateTab(-1)
                        }
                    }, contentPaddingValues = PaddingValues(
                        Dimensions.size0
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(R.string.previous).uppercase(),
                            color = PrimaryDarkerLightB75,
                            style = TypographyBold.bodyMedium
                        )
                    }
                }

                GradientButton(
                    modifier = Modifier.weight(1f), endColor = PrimaryDeepBlue,
                    onClick = {
                        if (viewModel.tabSelectState.value < 5) {
                            if (viewModel.tabSelectState.value == Constants.TAB_ROUTE) {
                                if (viewModel.routeFieldValidation())
                                    viewModel.updateTab(1)
                            } else if (viewModel.tabSelectState.value == Constants.TAB_PARTICIPANT) {
                                viewModel.updateTab(1)
                            } else if (viewModel.tabSelectState.value == Constants.TAB_REVIEW) {
                                scope.launch {
                                    viewModel.createRide()
                                    if (viewModel.showToast.value) {
                                        Toast.makeText(context, "Ride Created", Toast.LENGTH_SHORT)
                                            .show()
                                        viewModel.updateTab(1)
                                        viewModel.showToast.value = false
                                    }
                                }


                            }
                        }

                    }, contentPadding = PaddingValues(
                        Dimensions.size0
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            if (viewModel.tabSelectState.value == Constants.TAB_REVIEW) {
                                stringResource(R.string.create_rides).uppercase()

                            } else {
                                stringResource(R.string.next).uppercase()
                            },
                            color = NeutralWhite,
                            style = TypographyBold.bodyMedium
                        )
                    }
                }
            }
        }

    }
}


@Composable
@Preview
fun CreateRidePreview() {
    CreateRideScreen(setTopAppBarState = {}, clickDone = {

    })

}

