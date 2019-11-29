package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import bedbrains.homesweethomeandroidclient.R
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList

class WeeklyRuleFragment : Fragment() {

    lateinit var weeklyRuleViewModel: WeeklyRuleViewModel
    lateinit var rootLayout: ConstraintLayout
    lateinit var timeLine: LinearLayout
    lateinit var times: MutableList<TextView>
    lateinit var dayHeader: LinearLayout
    lateinit var daySpace: View
    lateinit var days: MutableList<TextView>
    lateinit var horizontalGuideLines: MutableList<View>
    lateinit var verticalGuideLines: MutableList<View>
    lateinit var weekdayStrings: ArrayList<String>
    lateinit var locale: Locale
    lateinit var wf: WeekFields
    lateinit var firstDayOfWeek: DayOfWeek
    var hourHeight = 0
    var textMargin = 0
    var smallTextMargin = 0
    var gridMargin = 0
    var timeSpanMargin = 0
    var headerPadding = 0
    var elevation = 0f
    var timeSpans = mutableListOf<View>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        weeklyRuleViewModel = ViewModelProviders.of(this).get(WeeklyRuleViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_weekly_rule, container, false)
        dayHeader = root.findViewById(R.id.day_header) as LinearLayout
        rootLayout = root.findViewById(R.id.root_layout) as ConstraintLayout
        timeLine = LinearLayout(context)
        times = mutableListOf<TextView>()
        days = mutableListOf<TextView>()
        horizontalGuideLines = mutableListOf<View>()
        verticalGuideLines = mutableListOf<View>()
        weekdayStrings = arrayListOf<String>()
        locale = Locale.getDefault()
        wf = WeekFields.of(locale)
        firstDayOfWeek = wf.firstDayOfWeek
        hourHeight = resources.getDimensionPixelSize(R.dimen.weekly_rule_hour_height)
        textMargin = resources.getDimensionPixelSize(R.dimen.text_margin)
        smallTextMargin = resources.getDimensionPixelSize(R.dimen.small_text_margin)
        gridMargin = resources.getDimensionPixelSize(R.dimen.grid_margin)
        timeSpanMargin = resources.getDimensionPixelSize(R.dimen.weekly_rule_time_span_margin)
        headerPadding = resources.getDimensionPixelSize(R.dimen.header_padding)
        elevation = resources.getDimension(R.dimen.card_view_elevation)

        //adding weekday strings
        for (i in DayOfWeek.values().indices) {
            weekdayStrings.add(firstDayOfWeek.plus(i.toLong()).getDisplayName(TextStyle.NARROW, locale))
        }

        //days
        dayHeader.setBackgroundColor(Color.WHITE)
        dayHeader.elevation = elevation

        daySpace = View(context)
        val daySpaceParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0f)
        daySpaceParams.setMargins(0, 0, gridMargin, 0)
        daySpace.layoutParams = daySpaceParams
        dayHeader.addView(daySpace)

        for (i in weekdayStrings.indices) {
            val day = TextView(context)
            val dayParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            day.layoutParams = dayParams
            day.textAlignment = View.TEXT_ALIGNMENT_CENTER
            day.text = weekdayStrings[i]
            day.setPadding(0, headerPadding, 0, headerPadding)
            dayHeader.addView(day)
            days.add(day)
        }

        //times
        timeLine.id = View.generateViewId()
        timeLine.orientation = LinearLayout.VERTICAL

        val topSpace = Space(context)
        val bottomSpace = Space(context)
        val topSpaceParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, hourHeight / 2)
        val bottomSpaceParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, hourHeight / 2)
        topSpace.layoutParams = topSpaceParams
        bottomSpace.layoutParams = bottomSpaceParams

        timeLine.addView(topSpace)

        for (i in 1..23) {
            val time = TextView(context)
            val timeParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, hourHeight)
            val localTime = LocalTime.of(i, 0)

            time.layoutParams = timeParams
            time.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            time.text = localTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            time.setPadding(textMargin, textMargin, textMargin, textMargin)
            time.gravity = Gravity.CENTER_VERTICAL
            timeLine.addView(time)
            times.add(time)
        }
        timeLine.addView(bottomSpace)
        rootLayout.addView(timeLine)

        val timeLineSpace = Space(context)
        timeLineSpace.id = View.generateViewId()
        rootLayout.addView(timeLineSpace)

        //guidelines
        for (i in times.indices) {
            val guideLine = View(context)
            guideLine.id = View.generateViewId()
            guideLine.setBackgroundColor(Color.LTGRAY)
            rootLayout.addView(guideLine)
            horizontalGuideLines.add(guideLine)
        }

        for (i in 0 until days.size + 1) {
            val guideline = View(context)
            guideline.id = View.generateViewId()
            guideline.setBackgroundColor(Color.LTGRAY)
            rootLayout.addView(guideline)
            verticalGuideLines.add(guideline)
        }

        //constraining views
        val constraintSet = ConstraintSet()

        //constraining timeline
        constraintSet.constrainWidth(timeLine.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.connect(timeLine.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)

        constraintSet.connect(timeLineSpace.id, ConstraintSet.LEFT, timeLine.id, ConstraintSet.RIGHT)
        constraintSet.constrainWidth(timeLineSpace.id, gridMargin)

        //constraining horizontal guidelines
        for (i in horizontalGuideLines.indices) {
            constraintSet.connect(horizontalGuideLines[i].id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, hourHeight * (i + 1))
            constraintSet.connect(horizontalGuideLines[i].id, ConstraintSet.LEFT, timeLine.id, ConstraintSet.RIGHT)
            constraintSet.connect(horizontalGuideLines[i].id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
            constraintSet.constrainHeight(horizontalGuideLines[i].id, 1)
        }

        val verticalGuidelineChainIds = IntArray(verticalGuideLines.size)
        val verticalGuidelineWeights = FloatArray(verticalGuideLines.size) { 1f }

        //constraining vertical guidelines
        for (i in verticalGuideLines.indices) {
            verticalGuidelineChainIds[i] = verticalGuideLines[i].id
            constraintSet.constrainWidth(verticalGuideLines[i].id, 1)
            constraintSet.connect(verticalGuideLines[i].id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            constraintSet.connect(verticalGuideLines[i].id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        }

        constraintSet.createHorizontalChain(
            timeLineSpace.id,
            ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT,
            verticalGuidelineChainIds,
            verticalGuidelineWeights,
            ConstraintSet.CHAIN_SPREAD_INSIDE
        )

        constraintSet.applyTo(rootLayout)

        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        timeLine.requestLayout()
        timeLine.measure(wrapContentMeasureSpec, wrapContentMeasureSpec)
        daySpace.layoutParams.width = timeLine.measuredWidth
        Log.d("UI", "timeLine.measuredWidth: ${timeLine.measuredWidth}")

        displayTimespans()

        return root
    }

    fun displayTimespans() {
        timeSpans.forEach { view -> rootLayout.removeView(view) }
        val constraintSet = ConstraintSet()
        constraintSet.clone(rootLayout)

        for (t in weeklyRuleViewModel.weeklyRule.timeSpans) {
            Log.d("UI", "t.end.before(t.start): ${t.end.before(t.start)}")
            if (t.start.before(t.end)) {
                val view = View(context)
                view.id = View.generateViewId()
                view.background = ContextCompat.getDrawable(context!!, R.drawable.card_view)
                view.elevation = elevation
                rootLayout.addView(view)

                constraintSet.connect(view.id, ConstraintSet.LEFT, verticalGuideLines[t.start.day].id, ConstraintSet.RIGHT, timeSpanMargin)
                constraintSet.connect(view.id, ConstraintSet.RIGHT, verticalGuideLines[t.start.day + 1].id, ConstraintSet.LEFT, timeSpanMargin)
                constraintSet.connect(view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, hourHeight * t.start.hour)
                constraintSet.constrainHeight(view.id, (hourHeight * (t.end.inHours() - t.start.inHours())).toInt())
            }
        }

        constraintSet.applyTo(rootLayout)
    }
}