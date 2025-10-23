package com.asphalt.createride.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.createride.viewmodel.CreateRideScreenViewModel

@Composable
fun TabSelection(viewModel: CreateRideScreenViewModel) {
    Spacer(Modifier.height(Dimensions.size30))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimensions.padding16, end = Dimensions.padding16),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing10)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .height(Dimensions.padding50)
                    .width(Dimensions.padding50)
                    .then(
                        if (viewModel.tabSelectState.value >= Constants.TAB_DETAILS) {
                            Modifier.background(
                                color = PrimaryDarkerLightB75,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            )
                        } else {
                            Modifier.background(
                                color = NeutralWhite,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            )
                        }
                    ), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_path),
                    contentDescription = "",
                    colorFilter = if (viewModel.tabSelectState.value >= Constants.TAB_DETAILS) {
                        ColorFilter.tint(NeutralWhite)
                    } else {
                        ColorFilter.tint(NeutralDarkGrey)
                    }
                )

            }
            Spacer(modifier = Modifier.height(Dimensions.size10))
            Text(
                text = stringResource(R.string.details),
                style = if (viewModel.tabSelectState.value == Constants.TAB_DETAILS) {
                    TypographyBold.bodySmall
                } else {
                    Typography.bodySmall
                },
                color = if (viewModel.tabSelectState.value == Constants.TAB_DETAILS) {
                    NeutralBlack
                } else {
                    NeutralDarkGrey
                },
                fontSize = Dimensions.textSize12,
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .height(Dimensions.padding50)
                    .width(Dimensions.padding50)
                    .then(
                        if (viewModel.tabSelectState.value >= Constants.TAB_ROUTE) {
                            Modifier.background(
                                color = PrimaryDarkerLightB75,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            )
                        } else {
                            Modifier.background(
                                color = NeutralWhite,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            )
                        }
                    ), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_route),
                    contentDescription = "",
                    colorFilter = if (viewModel.tabSelectState.value >= Constants.TAB_ROUTE) {
                        ColorFilter.tint(NeutralWhite)
                    } else {
                        ColorFilter.tint(NeutralDarkGrey)
                    },
                )

            }
            Spacer(modifier = Modifier.height(Dimensions.size10))
            Text(
                text = stringResource(R.string.route), style = if (viewModel.tabSelectState.value == Constants.TAB_ROUTE) {
                    TypographyBold.bodySmall
                } else {
                    Typography.bodySmall
                }, color = if (viewModel.tabSelectState.value == Constants.TAB_ROUTE) {
                    NeutralBlack
                } else {
                    NeutralDarkGrey
                }, fontSize = Dimensions.textSize12
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .height(Dimensions.padding50)
                    .width(Dimensions.padding50)
                    .then(
                        if (viewModel.tabSelectState.value >= Constants.TAB_PARTICIPANT) {
                            Modifier.background(
                                color = PrimaryDarkerLightB75,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            )
                        } else {
                            Modifier.background(
                                color = NeutralWhite,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            )
                        }
                    ), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_participants),
                    contentDescription = "",
                    colorFilter = if (viewModel.tabSelectState.value >= Constants.TAB_PARTICIPANT) {
                        ColorFilter.tint(NeutralWhite)
                    } else {
                        ColorFilter.tint(NeutralDarkGrey)
                    }
                )

            }
            Spacer(modifier = Modifier.height(Dimensions.size10))
            Text(
                text = stringResource(R.string.participants),
                style = if (viewModel.tabSelectState.value == Constants.TAB_PARTICIPANT) {
                    TypographyBold.bodySmall
                } else {
                    Typography.bodySmall
                },
                color = if (viewModel.tabSelectState.value == Constants.TAB_PARTICIPANT) {
                    NeutralBlack
                } else {
                    NeutralDarkGrey
                },
                maxLines = 1,
                fontSize = Dimensions.textSize12
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .height(Dimensions.padding50)
                    .width(Dimensions.padding50)
                    .then(
                        if (viewModel.tabSelectState.value >= Constants.TAB_REVIEW) {
                            Modifier.background(
                                color = PrimaryDarkerLightB75,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            )
                        } else {
                            Modifier.background(
                                color = NeutralWhite,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            )
                        }
                    ), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_review),
                    contentDescription = "",
                    colorFilter = if (viewModel.tabSelectState.value >= Constants.TAB_REVIEW) {
                        ColorFilter.tint(NeutralWhite)
                    } else {
                        ColorFilter.tint(NeutralDarkGrey)
                    }
                )

            }
            Spacer(modifier = Modifier.height(Dimensions.size10))
            Text(
                text = stringResource(R.string.review),
                style = if (viewModel.tabSelectState.value == Constants.TAB_REVIEW) {
                    TypographyBold.bodySmall
                } else {
                    Typography.bodySmall
                },
                color = if (viewModel.tabSelectState.value == Constants.TAB_REVIEW) {
                    NeutralBlack
                } else {
                    NeutralDarkGrey
                },
                fontSize = Dimensions.textSize12
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .height(Dimensions.padding50)
                    .width(Dimensions.padding50)
                    .then(
                        if (viewModel.tabSelectState.value >= Constants.TAB_SHARE) {
                            Modifier.background(
                                color = PrimaryDarkerLightB75,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            )
                        } else {
                            Modifier.background(
                                color = NeutralWhite,
                                shape = RoundedCornerShape(Dimensions.padding10)
                            )
                        }
                    ), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = "",
                    colorFilter = if (viewModel.tabSelectState.value >= Constants.TAB_SHARE) {
                        ColorFilter.tint(NeutralWhite)
                    } else {
                        ColorFilter.tint(NeutralDarkGrey)
                    }
                )

            }
            Spacer(modifier = Modifier.height(Dimensions.size10))
            Text(
                text = stringResource(R.string.share), style = if (viewModel.tabSelectState.value == Constants.TAB_SHARE) {
                    TypographyBold.bodySmall
                } else {
                    Typography.bodySmall
                }, color = if (viewModel.tabSelectState.value == Constants.TAB_SHARE) {
                    NeutralBlack
                } else {
                    NeutralDarkGrey
                }, fontSize = Dimensions.textSize12
            )
        }
    }

}

@Preview
@Composable
fun ButtonPreview(){
    var viewModel : CreateRideScreenViewModel= viewModel()
    Box(){
        TabSelection(viewModel)
    }

}
