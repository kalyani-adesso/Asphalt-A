package com.asphalt.commonui.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BouncingCirclesLoader(
    modifier: Modifier = Modifier,
    circleColor: Color = PrimaryDarkerLightB75,
    animationDuration: Int = 300,
    animationDelay: Int = 150
) {
    val baseSizes = remember { listOf(16.dp, 18.dp, 22.dp) }
    val bounceHeight = 6.dp
    val yOffsets = remember { mutableStateOf(baseSizes.map { 0f }) }
    val density = LocalDensity.current

    LaunchedEffect(key1 = true) {
        baseSizes.forEachIndexed { index, baseSize ->
            launch {
                delay(index * animationDelay.toLong())
                val maxOffset = with(density) { bounceHeight.toPx() * -1f }

                while (true) {
                    val newOffsets = yOffsets.value.toMutableList()
                    newOffsets[index] = maxOffset
                    yOffsets.value = newOffsets
                    delay(animationDuration.toLong())

                    val newOffsets2 = yOffsets.value.toMutableList()
                    newOffsets2[index] = 0f
                    yOffsets.value = newOffsets2
                    delay(animationDuration.toLong())
                }
            }
        }
    }

    Popup(
        alignment = Alignment.Center,
        onDismissRequest = {}
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            baseSizes.forEachIndexed { index, baseSize ->
                val currentOffset by animateFloatAsState(
                    targetValue = yOffsets.value[index],
                    animationSpec = tween(animationDuration, easing = FastOutSlowInEasing),
                    label = "yOffset_${index}"
                )

                val currentSizeDp = with(density) {
                    val heightDifference = baseSize + bounceHeight
                    val animationProgress = 1f - (currentOffset.toDp().value / bounceHeight.value)
                    baseSize + (bounceHeight * animationProgress)
                }

                Box(
                    modifier = Modifier
                        .size(currentSizeDp)
                        .offset(y = with(density) { currentOffset.toDp() })
                        .background(color = circleColor, shape = CircleShape)
                )
                if (index < baseSizes.size - 1) {
                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    }
}