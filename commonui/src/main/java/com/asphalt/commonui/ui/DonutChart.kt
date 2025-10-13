package com.asphalt.commonui.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.theme.Dimensions

@Composable
fun DonutChart(values: List<Float>, colors: List<Color>) {

    require(values.size == colors.size)
    val total = values.sum()
    var startAngle = Constants.DONUT_START_ANGLE

    Canvas(modifier = Modifier.size(Dimensions.size185)) {
        val strokeWidth = Constants.DONUT_STROKE_WIDTH
        val outerRadius = size.minDimension / 2 - strokeWidth / 2 - Constants.DONUT_RADIUS_OFFSET
        val innerRadius = outerRadius - strokeWidth
        val gapOffset = Constants.DONUT_SEGMENT_GAP_OFFSET
        val gap = (strokeWidth / (2 * Math.PI * outerRadius) * 360f).toFloat() + gapOffset

        val sweepOffset = Constants.DONUT_SWEEP_OFFSET

        values.forEachIndexed { index, value ->
            val sweep = Constants.DONUT_TOTAL_ANGLE * (value / total) - gap
            if (sweep > 0) {
                val path = Path().apply {
                    arcTo(
                        rect = Rect(
                            center.x - outerRadius,
                            center.y - outerRadius,
                            center.x + outerRadius,
                            center.y + outerRadius
                        ),
                        startAngle,
                        sweep + sweepOffset,
                        false
                    )
                    arcTo(
                        rect = Rect(
                            center.x - innerRadius,
                            center.y - innerRadius,
                            center.x + innerRadius,
                            center.y + innerRadius
                        ),
                        startAngle + sweep,
                        -sweep,
                        false
                    )

                    close()
                }
                drawPath(
                    path,
                    colors[index],
                    style = Stroke(
                        strokeWidth + sweepOffset,
                        join = StrokeJoin.Round,
                        cap = StrokeCap.Round
                    )
                )
            }
            startAngle += Constants.DONUT_TOTAL_ANGLE * (value / total)
        }
    }
}
