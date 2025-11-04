package com.asphalt.joinaride

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralLightPaper
import com.asphalt.commonui.theme.NeutralPink
import com.asphalt.commonui.theme.NeutralWhite
import com.asphalt.commonui.theme.PrimaryBrighterLightW75
import com.asphalt.commonui.theme.StarBackGround
import com.asphalt.commonui.theme.Typography
import com.asphalt.commonui.theme.TypographyBold
import com.asphalt.commonui.ui.GradientButton
import com.asphalt.commonui.utils.ComposeUtils
import com.asphalt.commonui.utils.ComposeUtils.ColorIconRounded
import com.asphalt.joinaride.viewmodel.RatingViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RatingThisRide(
    modifier: Modifier = Modifier,
    viewModel: RatingViewModel = koinViewModel(),
    onDismiss : () -> Unit,
    onSubmit : () -> Unit
) {
    val rating by viewModel.rating.collectAsState()
    val isSubmitted by viewModel.isSumitted.collectAsState()
    var feedbackText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = {onDismiss},
        properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {

        ComposeUtils.CommonContentBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING)
                .verticalScroll(
                    rememberScrollState())
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = Constants.DEFAULT_SCREEN_HORIZONTAL_PADDING,
                    vertical = Dimensions.size25)
            ) {
                Spacer(modifier = Modifier.height(Dimensions.padding))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_rating_star),
                        contentDescription = "rating",
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.height(Dimensions.padding20))

                    ComposeUtils.SectionTitle(stringResource(R.string.rate_ride))
                    Spacer(modifier = Modifier.height(Dimensions.padding20))
                    Text(
                        text = "How was your experience on this ride?",
                        style = Typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(Dimensions.padding50))

                    RatingRow(
                        rating = rating,
                        onRatingChanged = { viewModel.setRating(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(Dimensions.padding20))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_message),
                        contentDescription = ""
                    )

                    Spacer(modifier = modifier.width(Dimensions.padding8))
                    Text(text = "Share your feedback (optional)")
                }
                Spacer(modifier = Modifier.height(Dimensions.padding2))

                OutlinedTextField(
                    value = feedbackText,
                    onValueChange = {
                        feedbackText = it
                    },
                    modifier = Modifier.fillMaxWidth()
                        .height(120.dp),
                    placeholder = { Text("Tell us about your ride experience...") },
                    singleLine = false,
                    maxLines = 5,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(Dimensions.padding20))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        Dimensions.padding20, Alignment.CenterHorizontally
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .padding(Dimensions.padding20)
                ) {

                    Button(
                        onClick = {
                            onDismiss.invoke()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeutralWhite),
                        modifier = Modifier
                            .weight(1f)
                            .height(Dimensions.size60)
                            .border(1.dp, color = PrimaryBrighterLightW75,
                                shape = RoundedCornerShape(size = Dimensions.size10)),
                    ) {
                        Text(
                            stringResource(R.string.skip).uppercase(),
                            color = PrimaryBrighterLightW75,
                            style = TypographyBold.titleMedium,
                            fontSize = Dimensions.textSize16,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }

                    GradientButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onSubmit.invoke()

                        },
                        buttonHeight = Dimensions.size60,
                        contentPadding = PaddingValues(Dimensions.size2)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                stringResource(R.string.submit_rating).uppercase(),
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
    }
}

@Composable
fun RatingRow(
    rating:Int,
    onRatingChanged : (Int) -> Unit,
    modifier: Modifier = Modifier,
    startCount : Int = 5,
    circleBackground : Color = StarBackGround,
    selectedStarColor : Color = PrimaryBrighterLightW75,
    unSelectedStarColor : Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = modifier,
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {

            for (index in 1..startCount) {
                val selected = index <= rating
                RatingStar(
                    selected = selected,
                    index = index,
                    onClick = { onRatingChanged(index)},
                    circleBackground = circleBackground,
                    selectedStarColor = selectedStarColor,
                    unSelectedStarColor = unSelectedStarColor
                )
            }
        }
        Spacer(modifier=modifier.height(Dimensions.padding20))
        if (rating > 0) {
            Text(
                color = PrimaryBrighterLightW75,
                style = TypographyBold.titleMedium,
                text = when(rating) {
                    1 -> "Needs improvement"
                    2 -> "Fair"
                    3 -> "Good!"
                    4 -> "Great"
                    5 -> "Excellent"
                    else -> ""
                }
            )
        }
    }

}

@Composable
fun RatingStar(
    modifier: Modifier = Modifier,
    selected : Boolean,
    index: Int,
    onClick:() -> Unit,
    circleBackground : Color,
    selectedStarColor : Color,
    unSelectedStarColor : Color) {

    // scale animation for press/selection
    val targetScale = if (selected) 1.08f else 1f
    val scale by animateFloatAsState(targetValue = targetScale)

    //color animation for inner star
    val iconTint by animateColorAsState(targetValue = if (selected) selectedStarColor else unSelectedStarColor)

    // touch target
    Surface(
        shape = CircleShape,
        color = circleBackground,
        modifier = modifier
            .size(52.dp)
            .scale(scale)
            .semantics { contentDescription = "Rate $index star" }
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (selected) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filled_start),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(size = 32.dp)
                )
            }
            else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outlined_star),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Preview()
@Composable
fun RatingThisRidePreview() {

    RatingThisRide(onDismiss = {}, onSubmit = {})
    
}