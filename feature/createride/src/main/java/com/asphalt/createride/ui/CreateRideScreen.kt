package com.asphalt.createride.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.commonui.theme.BlueLight
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.GreenLIGHT
import com.asphalt.commonui.theme.GreenLIGHT10
import com.asphalt.commonui.theme.GreenLIGHT25
import com.asphalt.commonui.theme.MagentaDeep
import com.asphalt.commonui.theme.NeutralBlack
import com.asphalt.commonui.theme.NeutralDarkGrey
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.OrangeLight
import com.asphalt.commonui.theme.OrangeLight10
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.theme.PrimaryDeepBlue
import com.asphalt.commonui.theme.REDLIGHT
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.theme.TypographyMedium
import com.asphalt.commonui.ui.ActionBarWithBack
import com.asphalt.commonui.ui.BorderedButton
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.commonui.utils.ComposeUtils.ColorIconRounded
import com.asphalt.createride.viewmodel.CreateRideScreenViewModel

@Composable
fun CreateRideEntry(viewModel: CreateRideScreenViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    AsphaltTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = NeutralWhite)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = Dimensions.spacing250)
            ) {
                item { ActionBarWithBack(R.drawable.ic_arrow_back, "Create a Ride") { } }
                item { TabSelection(viewModel) }
                item { Spacer(Modifier.height(Dimensions.padding20)) }
                if (viewModel.tabSelectState.value == Constants.TAB_DETAILS)
                    item { DetailsSection(viewModel) }
                if (viewModel.tabSelectState.value == Constants.TAB_ROUTE)
                    item { RouteSection(viewModel) }
                if(viewModel.tabSelectState.value== Constants.TAB_REVIEW)
                item { ReviewSection() }

//                ActionBarWithBack(R.drawable.ic_arrow_back, "Create a Ride") { }
//                TabSelection(viewModel)
//                Spacer(Modifier.height(Dimensions.padding20))
//                DetailsSection(viewModel)
//                RouteSection(viewModel)

            }
            BottomButtons(viewModel)
            // Fixed bottom button

        }
    }
}

@Composable
fun ReviewSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = Dimensions.padding16,
                end = Dimensions.padding16,
            )
            .background(
                color = NeutralLightPaper, shape = RoundedCornerShape(Dimensions.size10)
            )
    ) {
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Text(
            text = "Review Your Ride",
            style = TypographyMedium.bodyMedium,
            color = NeutralBlack,
            modifier = Modifier.padding(start = Dimensions.padding16)
        )
        Spacer(modifier = Modifier.height(Dimensions.size8))
        Box(modifier = Modifier.padding(start = Dimensions.padding16,
            end = Dimensions.padding16),) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.size73)
                    .background(
                        color = NeutralWhite,
                        shape = RoundedCornerShape(Dimensions.size10)
                    ).padding(start = Dimensions.padding16, end = Dimensions.padding16),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    ColorIconRounded(backColor = BlueLight, resId = R.drawable.ic_navigate)
                    Spacer(modifier = Modifier.width(Dimensions.size8))
                    Column {
                        Text(
                            text = "Weekend Ride",
                            style = TypographyMedium.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Refreshment to  Munnar",
                            style = Typography.bodySmall,
                            color = NeutralDarkGrey
                        )
                    }

                }
                Box(
                    modifier = Modifier
                        .background(
                            color = GreenLIGHT10,
                            shape = RoundedCornerShape(Dimensions.size5)
                        )
                        .padding(
                            start = Dimensions.size5,
                            end = Dimensions.size5,
                            top = Dimensions.size8,
                            bottom = Dimensions.size8
                        ), contentAlignment = Alignment.Center


                ) {
                    Text(
                        text = "Group ride".uppercase(),
                        style = Typography.bodySmall.copy(fontSize = Dimensions.textSize12),
                        color = GreenLIGHT25,
                        modifier = Modifier
                    )
                }

            }
        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Box(modifier = Modifier.padding(start = Dimensions.padding16,
            end = Dimensions.padding16),) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.size73)
                    .background(
                        color = NeutralWhite,
                        shape = RoundedCornerShape(Dimensions.size10)
                    ).padding(start = Dimensions.padding16, end = Dimensions.padding16),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ColorIconRounded(backColor = OrangeLight, resId = R.drawable.ic_calender_white)
                    Spacer(modifier = Modifier.width(Dimensions.size8))
                    Column {
                        Text(
                            text = "Date and Time",
                            style = TypographyMedium.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Sun, Oct 21 - 09:00 AM",
                            style = Typography.bodySmall,
                            color = NeutralDarkGrey
                        )
                    }

                }


            }
        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Box(modifier = Modifier.padding(start = Dimensions.padding16,
            end = Dimensions.padding16),) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.size73)
                    .background(
                        color = NeutralWhite,
                        shape = RoundedCornerShape(Dimensions.size10)
                    ).padding(start = Dimensions.padding16, end = Dimensions.padding16),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ColorIconRounded(backColor = MagentaDeep, resId = R.drawable.ic_route_white)
                    Spacer(modifier = Modifier.width(Dimensions.size8))
                    Column {
                        Text(
                            text = "Route",
                            style = TypographyMedium.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Kochi - Munnarr",
                            style = Typography.bodySmall,
                            color = NeutralDarkGrey
                        )
                    }

                }

            }
        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Box(modifier = Modifier.padding(start = Dimensions.padding16,
            end = Dimensions.padding16),) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.size73)
                    .background(
                        color = NeutralWhite,
                        shape = RoundedCornerShape(Dimensions.size10)
                    ).padding(start = Dimensions.padding16, end = Dimensions.padding16),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ColorIconRounded(backColor = OrangeLight10, resId = R.drawable.ic_group_white)
                    Spacer(modifier = Modifier.width(Dimensions.size8))
                    Column {
                        Text(
                            text = "Participants",
                            style = TypographyMedium.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "3 riders  selected",
                            style = Typography.bodySmall,
                            color = NeutralDarkGrey
                        )
                    }

                }

            }
        }
//added adding
        Spacer(modifier = Modifier.height(Dimensions.padding16))
    }
}

@Composable
fun RouteSection(viewModel: CreateRideScreenViewModel) {
    var text by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = Dimensions.padding16,
                end = Dimensions.padding16,
            )
            .background(
                color = NeutralLightPaper, shape = RoundedCornerShape(Dimensions.size10)
            )
    ) {
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Text(
            text = "Starting Point",
            style = TypographyMedium.bodyMedium,
            color = NeutralBlack,
            modifier = Modifier.padding(start = Dimensions.padding16)
        )
        Spacer(modifier = Modifier.height(Dimensions.size8))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.padding50)
                .padding(start = Dimensions.padding16, end = Dimensions.padding16)
                .background(
                    NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = if (!text.isEmpty()) {
                    text
                } else {
                    ""
                },
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        text = "Enter starting location",
                        style = Typography.bodyMedium,
                        color = NeutralDarkGrey,

                        )
                },
                textStyle = Typography.bodyMedium.copy(NeutralDarkGrey),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,

                    ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_route),
                        contentDescription = "",
                        tint = GreenLIGHT

                    )
                }

            )

        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Text(
            text = "Destination",
            style = TypographyMedium.bodyMedium,
            color = NeutralBlack,
            modifier = Modifier.padding(start = Dimensions.padding16)
        )
        Spacer(modifier = Modifier.height(Dimensions.size8))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.padding50)
                .padding(start = Dimensions.padding16, end = Dimensions.padding16)
                .background(
                    NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = if (!text.isEmpty()) {
                    text
                } else {
                    ""
                },
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        text = "Enter destination",
                        style = Typography.bodyMedium,
                        color = NeutralDarkGrey,

                        )
                },
                textStyle = Typography.bodyMedium.copy(NeutralDarkGrey),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,

                    ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_route),
                        contentDescription = "",
                        tint = REDLIGHT

                    )
                }

            )

        }
//added padding
        Spacer(modifier = Modifier.height(Dimensions.padding16))
    }
}

@Composable
fun DetailsSection(viewModel: CreateRideScreenViewModel) {
    var text by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = Dimensions.padding16,
                end = Dimensions.padding16,
            )
            .background(
                color = NeutralLightPaper, shape = RoundedCornerShape(Dimensions.size10)
            )
    ) {
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Text(
            text = "Ride Type",
            style = TypographyMedium.bodyMedium,
            color = NeutralBlack,
            modifier = Modifier.padding(start = Dimensions.padding16)
        )
        Spacer(modifier = Modifier.height(Dimensions.size8))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.padding50)
                .padding(start = Dimensions.padding16, end = Dimensions.padding16)
                .background(
                    NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Select ride type",
                style = Typography.bodyMedium,
                color = NeutralDarkGrey,
                modifier = Modifier.padding(start = Dimensions.padding16)
            )
            Image(
                painter = painterResource(R.drawable.ic_dropdown_arrow),
                contentDescription = "",
                modifier = Modifier.padding(end = Dimensions.padding16)
            )
        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Text(
            text = "Ride Title",
            style = TypographyMedium.bodyMedium,
            color = NeutralBlack,
            modifier = Modifier.padding(start = Dimensions.padding16)
        )
        Spacer(modifier = Modifier.height(Dimensions.size8))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.padding50)
                .padding(start = Dimensions.padding16, end = Dimensions.padding16)
                .background(
                    NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = if (!text.isEmpty()) {
                    text
                } else {
                    ""
                },
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        text = "Enter ride name...",
                        style = Typography.bodyMedium,
                        color = NeutralDarkGrey,

                        )
                },
                textStyle = Typography.bodyMedium.copy(NeutralDarkGrey),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,

                    ),

                )

        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Text(
            text = "Description",
            style = TypographyMedium.bodyMedium,
            color = NeutralBlack,
            modifier = Modifier.padding(start = Dimensions.padding16)
        )
        Spacer(modifier = Modifier.height(Dimensions.size8))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimensions.padding16, end = Dimensions.padding16)
                .background(
                    NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = if (!text2.isEmpty()) {
                    text2
                } else {
                    ""
                },
                onValueChange = { text2 = it },
                placeholder = {
                    Text(
                        text = "Describe the vibe...",
                        style = Typography.bodyMedium,
                        color = NeutralDarkGrey,

                        )
                },
                textStyle = Typography.bodyMedium.copy(NeutralDarkGrey),
                maxLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = Dimensions.padding80),

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,

                    ),

                )

        }
        Spacer(modifier = Modifier.height(Dimensions.padding16))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimensions.padding16, end = Dimensions.padding16),
            horizontalArrangement = Arrangement.spacedBy(Dimensions.size10)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Date",
                    style = TypographyMedium.bodyMedium,
                    color = NeutralBlack,
                    modifier = Modifier.padding(start = Dimensions.padding16)
                )
                Spacer(modifier = Modifier.height(Dimensions.size8))
                Row(
                    modifier = Modifier
                        .height(Dimensions.padding50)
                        .fillMaxWidth()
                        .background(
                            NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10),
                        ), verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(Dimensions.size10))
                    Image(
                        painter = painterResource(R.drawable.ic_calendar_blue),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(NeutralDarkGrey),
                        modifier = Modifier
                            .height(Dimensions.spacing20)
                            .width(Dimensions.spacing20)
                    )
                    Spacer(Modifier.width(Dimensions.size10))
                    Text(
                        "Pick Date",
                        style = Typography.bodyMedium,
                        color = NeutralDarkGrey,
                        modifier = Modifier
                    )
                }

            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Time",
                    style = TypographyMedium.bodyMedium,
                    color = NeutralBlack,
                    modifier = Modifier.padding(start = Dimensions.padding16)
                )
                Spacer(modifier = Modifier.height(Dimensions.size8))
                Row(
                    modifier = Modifier
                        .height(Dimensions.padding50)
                        .fillMaxWidth()
                        .background(
                            NeutralWhite, shape = RoundedCornerShape(Dimensions.padding10),
                        ), verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(Dimensions.size10))
                    Image(
                        painter = painterResource(R.drawable.ic_clock),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(NeutralDarkGrey),
                        modifier = Modifier
                            .height(Dimensions.spacing20)
                            .width(Dimensions.spacing20)
                    )
                    Spacer(Modifier.width(Dimensions.size10))
                    Text(
                        "Pick Time",
                        style = Typography.bodyMedium,
                        color = NeutralDarkGrey,
                        modifier = Modifier
                    )
                }

            }

        }

        //padding
        Spacer(modifier = Modifier.height(Dimensions.padding16))
    }
}

@Composable
fun BoxScope.BottomButtons(viewModel: CreateRideScreenViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(color = NeutralWhite)
            .padding(
                bottom = WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding() + Dimensions.spacing20,
                start = Dimensions.padding16,
                end = Dimensions.padding16
            )
    ) {
        if (viewModel.tabSelectState.value == Constants.TAB_DETAILS) {//viewModel.tabSelectState.value == Constants.TAB_DETAILS

            GradientButton(
                onClick = {
                    viewModel.updateTab(1)
                },
            ) {
                ComposeUtils.DefaultButtonContent("Next Step".uppercase())
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
                            "Previous".uppercase(),
                            color = PrimaryDarkerLightB75,
                            style = TypographyBold.bodyMedium
                        )
                    }
                }

                GradientButton(
                    modifier = Modifier.weight(1f), endColor = PrimaryDeepBlue, onClick = {
                        if (viewModel.tabSelectState.value < 5) {
                            viewModel.updateTab(1)
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
                            "Next".uppercase(),
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
                text = "Details",
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
                text = "Route", style = if (viewModel.tabSelectState.value == Constants.TAB_ROUTE) {
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
                text = "Participants",
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
                text = "Review",
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
                text = "Share", style = if (viewModel.tabSelectState.value == Constants.TAB_SHARE) {
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

@Composable
@Preview
fun CreateRidePreview() {
    CreateRideEntry()

}

