package com.asphalt.commonui.ui

import android.content.Context
import android.widget.TextView
import com.asphalt.commonui.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CustomBubbleMarkerView(context: Context) :
    MarkerView(context, R.layout.custom_marker_view) {

    private val tvContent: TextView = findViewById(R.id.tvContent)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        tvContent.text = e?.y?.toInt()?.toString() ?: ""
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        // center horizontally; place above bar and slightly above the tail
        return MPPointF(-(width / 2).toFloat(), -height.toFloat() - 6f)
    }
}
