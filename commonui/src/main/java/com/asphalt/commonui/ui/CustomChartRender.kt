package com.asphalt.commonui.ui

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import androidx.core.graphics.withClip
import com.asphalt.commonui.constants.BarChartConstants
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

open class RoundedBarChartRenderer(
    chart: BarChart,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler,
    private val cornerRadius: Float = BarChartConstants.BAR_CHART_CORNER_RADIUS // change as needed
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val barRect = RectF()
    private val clipPath = Path()

    override fun drawDataSet(
        c: Canvas,
        dataSet: IBarDataSet,
        index: Int
    ) {
        val trans = mChart.getTransformer(dataSet.axisDependency)

        mBarBorderPaint.color = dataSet.barBorderColor
        mBarBorderPaint.strokeWidth = dataSet.barBorderWidth

        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        if (mBarBuffers.isEmpty()) return
        val buffer = mBarBuffers[index]
        buffer.setPhases(phaseX, phaseY)
        buffer.feed(dataSet)

        trans.pointValuesToPixel(buffer.buffer)

        val isSingleColor = dataSet.colors.size == 1
        if (isSingleColor) mRenderPaint.color = dataSet.color

        for (j in 0 until buffer.size() step 4) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) continue
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break

            if (!isSingleColor) mRenderPaint.color = dataSet.getColor(j / 4)

            // Bar rectangle
            val left =
                (buffer.buffer[j] + buffer.buffer[j + 2]) / 2 - BarChartConstants.BAR_CHART_OFFSET
            val right =
                (buffer.buffer[j] + buffer.buffer[j + 2]) / 2 + BarChartConstants.BAR_CHART_OFFSET
            barRect.set(
                left, buffer.buffer[j + 1],
                right, buffer.buffer[j + 3]
            )

            // Create rounded top path
            clipPath.reset()
            clipPath.addRoundRect(
                barRect,
                floatArrayOf(
                    cornerRadius, cornerRadius, // top-left, top-right
                    cornerRadius, cornerRadius, // top-right, top-left (mirrored)
                    0f, 0f, 0f, 0f              // bottom corners flat
                ),
                Path.Direction.CW
            )

            c.withClip(clipPath) {
                drawRect(barRect, mRenderPaint)
            }
        }
    }
}
