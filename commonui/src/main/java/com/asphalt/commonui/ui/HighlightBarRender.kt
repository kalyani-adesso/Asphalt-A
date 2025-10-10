package com.asphalt.commonui.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import androidx.compose.ui.graphics.toArgb
import com.asphalt.commonui.constants.BarChartConstants
import com.asphalt.commonui.theme.PrimaryDarkerLightB75
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.ViewPortHandler

class HighlightRoundedBarRenderer(
    val chart: BarChart,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler,
    private val cornerRadius: Float = BarChartConstants.BAR_CHART_HIGHLIGHT_CORNER_RADIUS,
    private val selectedIndexProvider: () -> Int?
) : RoundedBarChartRenderer(chart, animator, viewPortHandler, cornerRadius) {

    private val highlightPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }


    override fun drawHighlighted(
        c: Canvas?,
        indices: Array<out Highlight?>?
    ) {

    }

    override fun drawExtras(c: Canvas) {
        super.drawExtras(c)
        val selectedIndex = selectedIndexProvider() ?: return
        val buffer = mBarBuffers[0]

        val entryIndex = selectedIndex
        val j = entryIndex * 4
        if (j + 3 >= buffer.buffer.size) return
        val left =
            (buffer.buffer[j] + buffer.buffer[j + 2]) / 2 - BarChartConstants.BAR_CHART_HIGHLIGHT_OFFSET
        val right =
            (buffer.buffer[j] + buffer.buffer[j + 2]) / 2 + BarChartConstants.BAR_CHART_HIGHLIGHT_OFFSET
        val top = buffer.buffer[j + 1] - 18f
        val bottom = buffer.buffer[j + 3]

        val barRect = RectF(left, top, right, bottom)
        // Create a vertical gradient: top color -> bottom color
        highlightPaint.shader = LinearGradient(
            barRect.left, barRect.top,
            barRect.left, barRect.bottom,
            intArrayOf(
                PrimaryDarkerLightB75.toArgb(),
                Color.TRANSPARENT
            ), // top->bottom colors
            null,
            Shader.TileMode.CLAMP
        )
        // Draw rounded highlight
        val path = Path()
        path.addRoundRect(
            barRect,
            floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0f, 0f, 0f, 0f),
            Path.Direction.CW
        )
        c.drawPath(path, highlightPaint)
        chart.highlightValue(selectedIndex.toFloat(), 0) // highlight the bar
        chart.marker?.let { marker ->
            val highlight = chart.highlighted[0] // current highlight
            marker.refreshContent(
                chart.data.getDataSetByIndex(0).getEntryForIndex(selectedIndex),
                highlight
            )
            marker.draw(
                c,
                (left + right) / 2,
                top + BarChartConstants.BAR_CHART_TOOLTIP_OFFSET_Y
            ) // Chart will handle positioning automatically
        }
    }
}
