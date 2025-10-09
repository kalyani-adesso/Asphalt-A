package com.asphalt.commonui.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity

@Composable
fun BarChart(xLabels: List<String>, xValues: List<String>) {
    require(xValues.size == xLabels.size)
    val selectedIndex by remember { mutableStateOf<Int?>(null) }
    val density = LocalDensity.current
}