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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.ui.animation.RiseDownAnimation
import bedbrains.homesweethomeandroidclient.ui.cardview.EditTimeSpan
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
    lateinit var rule: LinearLayout
    lateinit var ruleName: TextView
    lateinit var dayHeader: LinearLayout
    lateinit var daySpace: View
    lateinit var days: MutableList<TextView>
    lateinit var timeLayout: ConstraintLayout
    lateinit var timeLine: LinearLayout
    lateinit var times: MutableList<TextView>
    lateinit var horizontalGuideLines: MutableList<View>
    lateinit var verticalGuideLines: MutableList<View>
    lateinit var weekdayStrings: ArrayList<String>
    lateinit var locale: Locale
    lateinit var wf: WeekFields
    lateinit var firstDayOfWeek: DayOfWeek
    var timeSpans = mutableListOf<View>()
    var hourHeight = 0
    var lineWidth = 0
    var textMargin = 0
    var smallTextMargin = 0
    var gridMargin = 0
    var timeSpanMargin = 0
    var headerPadding = 0
    var elevation = 0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        weeklyRuleViewModel = ViewModelProviders.of(this).get(WeeklyRuleViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_weekly_rule, container, false)
        rule = root.findViewById(R.id.rule)
        ruleName = rule.findViewById(R.id.name)
        dayHeader = root.findViewById(R.id.day_header) as LinearLayout
        timeLayout = root.findViewById(R.id.time_layout) as ConstraintLayout
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
        lineWidth = resources.getDimensionPixelSize(R.dimen.line_width)
        textMargin = resources.getDimensionPixelSize(R.dimen.text_margin)
        smallTextMargin = resources.getDimensionPixelSize(R.dimen.small_text_margin)
        gridMargin = resources.getDimensionPixelSize(R.dimen.grid_margin)
        timeSpanMargin = resources.getDimensionPixelSize(R.dimen.weekly_rule_time_span_margin)
        headerPadding = resources.getDimensionPixelSize(R.dimen.header_padding)
        elevation = resources.getDimension(R.dimen.card_view_elevation)

        ruleName.text = weeklyRuleViewModel.rule.name

        //adding weekday strings
        for (i in DayOfWeek.values().indices) {
            weekdayStrings.add(firstDayOfWeek.plus(i.toLong()).getDisplayName(TextStyle.NARROW, locale))
        }

        //days
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
        timeLayout.addView(timeLine)

        val timeLineSpace = Space(context)
        timeLineSpace.id = View.generateViewId()
        timeLayout.addView(timeLineSpace)

        //guidelines
        for (i in times.indices) {
            val guideLine = View(context)
            guideLine.id = View.generateViewId()
            guideLine.setBackgroundColor(Color.LTGRAY)
            timeLayout.addView(guideLine)
            horizontalGuideLines.add(guideLine)
        }

        for (i in 0 until days.size + 1) {
            val guideline = View(context)
            guideline.id = View.generateViewId()
            guideline.setBackgroundColor(Color.LTGRAY)
            timeLayout.addView(guideline)
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
            constraintSet.connect(
                horizontalGuideLines[i].id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                hourHeight * (i + 1)
            )
            constraintSet.connect(horizontalGuideLines[i].id, ConstraintSet.LEFT, timeLine.id, ConstraintSet.RIGHT)
            constraintSet.connect(horizontalGuideLines[i].id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
            constraintSet.constrainHeight(horizontalGuideLines[i].id, lineWidth)
        }

        val verticalGuidelineChainIds = IntArray(verticalGuideLines.size)
        val verticalGuidelineWeights = FloatArray(verticalGuideLines.size) { 1f }

        //constraining vertical guidelines
        for (i in verticalGuideLines.indices) {
            verticalGuidelineChainIds[i] = verticalGuideLines[i].id
            constraintSet.constrainWidth(verticalGuideLines[i].id, lineWidth)
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

        constraintSet.applyTo(timeLayout)

        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        timeLine.measure(wrapContentMeasureSpec, wrapContentMeasureSpec)
        daySpace.layoutParams.width = timeLine.measuredWidth
        Log.d("UI", "timeLine.measuredWidth: ${timeLine.measuredWidth}")

        displayTimespans()

        if (weeklyRuleViewModel.initialCreation) {
            weeklyRuleRiseDown()
            weeklyRuleViewModel.initialCreation = false
        } else {
            rule.elevation = 0f
        }

        return root
    }

    fun displayTimespans() {
        timeSpans.forEach { view -> timeLayout.removeView(view) }
        val constraintSet = ConstraintSet()
        constraintSet.clone(timeLayout)

        for (t in weeklyRuleViewModel.rule.timeSpans) {
            Log.d("UI", "t.end.before(t.start): ${t.end.before(t.start)}")
            if (t.start.before(t.end)) {
                val card = EditTimeSpan(context!!)
                card.elevation = elevation
                timeLayout.addView(card)
                timeSpans.add(card)

                constraintSet.connect(card.id, ConstraintSet.LEFT, verticalGuideLines[t.start.day].id, ConstraintSet.RIGHT, timeSpanMargin)
                constraintSet.connect(card.id, ConstraintSet.RIGHT, verticalGuideLines[t.start.day + 1].id, ConstraintSet.LEFT, timeSpanMargin)
                constraintSet.connect(card.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, hourHeight * t.start.hour)
                constraintSet.constrainHeight(card.id, (hourHeight * (t.end.inHours() - t.start.inHours())).toInt())
            }
        }

        constraintSet.applyTo(timeLayout)
    }

    fun weeklyRuleRiseDown() {
        val riseDownAnimation = RiseDownAnimation(rule)
        riseDownAnimation.duration = getAnimationDuration()
        rule.startAnimation(riseDownAnimation)
    }

    private fun getAnimationDuration(): Long {
        val key: String = MainActivity.res.getString(R.string.pref_animation_duration_key)
        return MainActivity.getPrefInt(key, 250).toLong()
    }
}