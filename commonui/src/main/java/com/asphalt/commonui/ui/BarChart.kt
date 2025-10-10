package com.asphalt.commonui.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.asphalt.commonui.R
import com.asphalt.commonui.constants.BarChartConstants
import com.asphalt.commonui.theme.Dimensions
import com.asphalt.commonui.theme.NeutralDeepBlack
import com.asphalt.commonui.theme.NeutralPaperGrey
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.asphalt.commonui.utils.Utils.nextMultipleOfFive
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

@Composable
fun CustomBarChart(xLabels: List<String>, values: List<Int>) {
    require(values.size == xLabels.size) { "xLabels and values must have the same size" }

    val barColor = PrimaryDarkerLightB75.toArgb()
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.size220),
        factory = { context ->
            BarChart(context).apply {

                setNoDataText(context.getString(R.string.no_data))
                setTouchEnabled(true)
                setScaleEnabled(false)
                setPinchZoom(false)
                setDrawGridBackground(false)
                axisRight.isEnabled = false
                description.isEnabled = false
                legend.isEnabled = false
                isHighlightPerTapEnabled = true
                data?.isHighlightEnabled = false
                renderer = HighlightRoundedBarRenderer(
                    this,
                    animator,
                    viewPortHandler,
                    selectedIndexProvider = { selectedIndex })
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                    granularity = 1f
                    textColor = NeutralDeepBlack.toArgb()
                    textSize = BarChartConstants.AXIS_LABEL_TEXT_SIZE
                    typeface = ResourcesCompat.getFont(context, R.font.klavika_regular)
                }

                axisLeft.apply {
                    gridColor = NeutralPaperGrey.toArgb()
                    gridLineWidth = BarChartConstants.GRID_LINE_WIDTH
                    axisMinimum = 0f
                    setDrawGridLines(true)
                    setDrawAxisLine(false)
                    textColor = NeutralDeepBlack.toArgb()
                    textSize = BarChartConstants.AXIS_LABEL_TEXT_SIZE
                    typeface = ResourcesCompat.getFont(context, R.font.klavika_regular)

                }
                val marker = CustomBubbleMarkerView(context)
                marker.chartView = this
                this.marker = marker

                setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(
                        e: Entry?,
                        h: Highlight?
                    ) {
                        selectedIndex = e?.x?.toInt()
                    }

                    override fun onNothingSelected() {
                        selectedIndex = null
                    }

                })
            }
        },
        update = { chart ->
            // Map values to entries
            val entries = values.mapIndexed { index, value ->
                BarEntry(index.toFloat(), value.toFloat())
            }
            selectedIndex = null

            val dataSet = BarDataSet(entries, null).apply {
                color = barColor
                setDrawValues(false)
                isHighlightEnabled = true
                highLightColor = Color.Transparent.toArgb()
            }
            chart.marker = CustomBubbleMarkerView(chart.context)

            chart.data = BarData(dataSet)
            // --- X Axis (Months) ---
            chart.xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(xLabels)
                labelCount = xLabels.size
                granularity = 1f
                setCenterAxisLabels(false)
                yOffset = BarChartConstants.X_AXIS_LABEL_OFFSET
            }

            // --- Y Axis ---
            val rawMax = (values.maxOrNull() ?: 0).coerceAtLeast(10)
            val topValue =
                if (rawMax % BarChartConstants.Y_AXIS_NO_OF_DIVISIONS == 0) rawMax
                else rawMax + nextMultipleOfFive(
                    rawMax
                )
            chart.axisLeft.apply {
                axisMaximum = topValue.toFloat()
                labelCount = BarChartConstants.LABEL_COUNT
                granularity = (topValue / BarChartConstants.Y_AXIS_NO_OF_DIVISIONS.toFloat())
                isGranularityEnabled = true
                setLabelCount(BarChartConstants.LABEL_COUNT, true)

                xOffset = BarChartConstants.Y_AXIS_LABEL_OFFSET
                valueFormatter = object : ValueFormatter() {
                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        return value.toInt().toString()
                    }
                }
            }

            chart.invalidate()
            chart.animateY(BarChartConstants.CHART_ANIM_DURATION)
        }
    )
}


@Preview(showBackground = true)
@Composable
fun BarPreview() {
    var data by remember {
        mutableStateOf(listOf(3, 5, 7, 2, 0, 1, 4))
    }
    var months by remember {
        mutableStateOf(listOf("Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep"))
    }

    CustomBarChart(months, data)
}

