package com.yusufbek.runtrackingapp.other

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.yusufbek.runtrackingapp.R
import com.yusufbek.runtrackingapp.db.RunEntity
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView(
    val runs: List<RunEntity>,
    private val ctx: Context,
    private val layoutId: Int
) : MarkerView(ctx, layoutId) {

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if (e == null) {
            return
        }
        val curRunId = e.x.toInt()
        val run = runs[curRunId]

        val layout = LayoutInflater.from(ctx).inflate(layoutId, chartView, false)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timeStamp
        }
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        layout.findViewById<TextView>(R.id.tvDate).text = dateFormat.format(calendar.time)

        "${run.avgSpeed}km/h".also { layout.findViewById<TextView>(R.id.tvAvgSpeed).text = it }

        "${run.distanceInMeters / 1000f}km".also {
            layout.findViewById<TextView>(R.id.tvDistance).text = it
        }

        layout.findViewById<TextView>(R.id.tvDuration).text =
            TrackingUtilities.getFormattedStopWatchTime(run.timeInMillis)

        "${run.caloriesBurned}ccal".also {
            layout.findViewById<TextView>(R.id.tvCaloriesBurned).text = it
        }
    }

}