package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import bedbrains.homesweethomeandroidclient.ui.cardview.EditTimeSpan
import bedbrains.shared.datatypes.rules.WeeklyTime
import bedbrains.shared.datatypes.rules.WeeklyTimeSpan
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*

class WeeklyRuleFragment : Fragment() {

    lateinit var weeklyRuleViewModel: WeeklyRuleViewModel
    lateinit var constraintSet: ConstraintSet
    lateinit var dayHeader: LinearLayout
    lateinit var daySpace: View
    lateinit var days: MutableList<TextView>
    lateinit var timeLayout: ConstraintLayout
    lateinit var timeLine: LinearLayout
    lateinit var timeSpanAnchor: Space
    lateinit var times: MutableList<TextView>
    lateinit var horizontalGuideLines: MutableList<View>
    lateinit var verticalGuideLines: MutableList<View>
    lateinit var timeIndicatorHead: View
    lateinit var timeIndicatorLine: View
    lateinit var weekdayStrings: ArrayList<String>
    lateinit var locale: Locale
    lateinit var wf: WeekFields
    lateinit var firstDayOfWeek: DayOfWeek
    var timeSpans = mutableListOf<View>()
    var hourHeight = 0
    var cardViewHandleDiameter = 0
    var guidelineWidth = 0
    var indicatorLineWidth = 0
    var indicatorHeadDiameter = 0
    var textMargin = 0
    var gridMargin = 0
    var timeSpanMargin = 0
    var timeSpanBigMargin = 0
    var headerPadding = 0
    var cardViewElevation = 0f
    var cardViewHighElevation = 0f
    var timeIndicatorElevation = 0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_weekly_rule, container, false)
        weeklyRuleViewModel = ViewModelProviders.of(this).get(WeeklyRuleViewModel::class.java)
        constraintSet = ConstraintSet()
        dayHeader = root.findViewById(R.id.day_header) as LinearLayout
        daySpace = View(context)
        days = mutableListOf()
        timeLayout = root.findViewById(R.id.time_layout) as ConstraintLayout
        timeSpanAnchor = Space(context)
        timeLine = LinearLayout(context)
        times = mutableListOf()
        horizontalGuideLines = mutableListOf()
        verticalGuideLines = mutableListOf()
        timeIndicatorHead = View(context)
        timeIndicatorLine = View(context)
        weekdayStrings = arrayListOf()
        locale = Locale.getDefault()
        wf = WeekFields.of(locale)
        firstDayOfWeek = wf.firstDayOfWeek
        hourHeight = resources.getDimensionPixelSize(R.dimen.weekly_rule_hour_height)
        cardViewHandleDiameter = resources.getDimensionPixelSize(R.dimen.card_view_handle_diameter)
        guidelineWidth = resources.getDimensionPixelSize(R.dimen.guideline_width)
        indicatorLineWidth = resources.getDimensionPixelSize(R.dimen.time_indicaotr_line_width)
        indicatorHeadDiameter = resources.getDimensionPixelSize(R.dimen.time_indicator_head_diameter)
        textMargin = resources.getDimensionPixelSize(R.dimen.text_margin)
        gridMargin = resources.getDimensionPixelSize(R.dimen.grid_margin)
        timeSpanMargin = resources.getDimensionPixelSize(R.dimen.weekly_rule_time_span_margin)
        timeSpanBigMargin = resources.getDimensionPixelSize(R.dimen.weekly_rule_time_span_big_margin)
        headerPadding = resources.getDimensionPixelSize(R.dimen.header_padding)
        cardViewElevation = resources.getDimension(R.dimen.card_view_elevation)
        cardViewHighElevation = resources.getDimension(R.dimen.card_view_high_elevation)
        timeIndicatorElevation = resources.getDimension(R.dimen.time_indicator_elevation)

        //adding weekday strings
        for (i in DayOfWeek.values().indices) {
            weekdayStrings.add(firstDayOfWeek.plus(i.toLong()).getDisplayName(TextStyle.NARROW, locale))
        }

        //days
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
        timeLayout.maxHeight = hourHeight * 24

        timeSpanAnchor.id = View.generateViewId()
        timeLayout.addView(timeSpanAnchor)

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

        //time indicator
        timeIndicatorHead.id = View.generateViewId()
        timeIndicatorHead.background = ContextCompat.getDrawable(context!!, R.drawable.time_indicator_head)
        timeIndicatorHead.elevation = timeIndicatorElevation
        timeLayout.addView(timeIndicatorHead)

        timeIndicatorLine.id = View.generateViewId()
        timeIndicatorLine.background = ContextCompat.getDrawable(context!!, R.drawable.time_indicator_line)
        timeIndicatorLine.elevation = timeIndicatorElevation
        timeLayout.addView(timeIndicatorLine)

        //constraining timeline
        constraintSet.connect(timeSpanAnchor.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.constrainHeight(timeSpanAnchor.id, cardViewHandleDiameter / 2)

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
            constraintSet.constrainHeight(horizontalGuideLines[i].id, guidelineWidth)
        }

        val verticalGuidelineChainIds = IntArray(verticalGuideLines.size)
        val verticalGuidelineWeights = FloatArray(verticalGuideLines.size) { 1f }

        //constraining vertical guidelines
        for (i in verticalGuideLines.indices) {
            verticalGuidelineChainIds[i] = verticalGuideLines[i].id
            constraintSet.constrainWidth(verticalGuideLines[i].id, guidelineWidth)
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

        displayTimeSpans()

        constraintSet.applyTo(timeLayout)

        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        timeLine.measure(wrapContentMeasureSpec, wrapContentMeasureSpec)
        daySpace.layoutParams.width = timeLine.measuredWidth

        startUpdatingTimeIndicator()

        return root
    }

    fun displayTimeSpans() {
        timeSpans.forEach { view -> timeLayout.removeView(view) }

        for (t in weeklyRuleViewModel.rule.timeSpans) {
            displayTimeSpan(t, constraintSet)
        }

        constraintSet.applyTo(timeLayout)
    }

    fun displayTimeSpan(t: WeeklyTimeSpan, constraintSet: ConstraintSet) {
        if (t.start.before(t.end)) {

            for (i in t.start.day until t.end.day + 1) {
                val card = createTimeSpan(i)

                if (i > t.start.day) {
                    constraintSet.connect(card.id, ConstraintSet.TOP, timeSpanAnchor.id, ConstraintSet.TOP)
                } else {
                    constraintSet.connect(
                        card.id,
                        ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.TOP,
                        (hourHeight * t.start.inDailyHours()).toInt()
                    )
                }

                if (i < t.end.day) {
                    constraintSet.constrainHeight(card.id, hourHeight * 25)
                } else if (i > t.start.day) {
                    constraintSet.constrainHeight(card.id, (hourHeight * t.end.inDailyHours()).toInt() + cardViewHandleDiameter / 2)
                } else {
                    constraintSet.constrainHeight(card.id, (hourHeight * (t.end.inHours() - t.start.inHours())).toInt())
                }
            }
        } else {
            for (i in t.start.day until 7) {
                val card = createTimeSpan(i)

                if (i > t.start.day) {
                    constraintSet.connect(card.id, ConstraintSet.TOP, timeSpanAnchor.id, ConstraintSet.TOP)
                } else {
                    constraintSet.connect(
                        card.id,
                        ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.TOP,
                        (hourHeight * t.start.inDailyHours()).toInt()
                    )
                }

                constraintSet.constrainHeight(card.id, hourHeight * 25)
            }

            for (i in 0 until t.end.day + 1) {
                val card = createTimeSpan(i)

                constraintSet.connect(card.id, ConstraintSet.TOP, timeSpanAnchor.id, ConstraintSet.TOP)

                if (i < t.end.day) {
                    constraintSet.constrainHeight(card.id, hourHeight * 25)
                } else {
                    constraintSet.constrainHeight(card.id, (hourHeight * t.end.inDailyHours()).toInt() + cardViewHandleDiameter / 2)
                }
            }
        }
    }

    fun createTimeSpan(day: Int): View {
        val card = View(context)
        card.id = View.generateViewId()
        card.background = ContextCompat.getDrawable(context!!, R.drawable.background_card_view)
        card.elevation = cardViewElevation
        timeLayout.addView(card)
        timeSpans.add(card)

        constraintSet.connect(card.id, ConstraintSet.LEFT, verticalGuideLines[day].id, ConstraintSet.RIGHT, timeSpanMargin)
        constraintSet.connect(card.id, ConstraintSet.RIGHT, verticalGuideLines[day + 1].id, ConstraintSet.LEFT, timeSpanBigMargin)

        return card
    }

    fun createEditTimeSpan(start: WeeklyTime): EditTimeSpan {
        val card = EditTimeSpan(context!!)
        card.elevation = cardViewHighElevation
        timeLayout.addView(card)

        constraintSet.connect(card.id, ConstraintSet.LEFT, verticalGuideLines[start.day].id, ConstraintSet.RIGHT, timeSpanMargin)
        constraintSet.connect(card.id, ConstraintSet.RIGHT, verticalGuideLines[start.day + 1].id, ConstraintSet.LEFT, timeSpanMargin)
        constraintSet.connect(card.id, ConstraintSet.TOP, timeSpanAnchor.id, ConstraintSet.TOP, (hourHeight * start.inDailyHours()).toInt())
        constraintSet.constrainHeight(card.id, hourHeight)

        return card
    }

    fun startUpdatingTimeIndicator() {
        val mainHandler = Handler(Looper.myLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                updateTimeIndicator(WeeklyTime.now())
                mainHandler.postDelayed(this, 60000)
            }
        })
    }

    fun updateTimeIndicator(time: WeeklyTime) {
        constraintSet.clear(timeIndicatorHead.id)
        constraintSet.clear(timeIndicatorLine.id)

        constraintSet.connect(timeIndicatorHead.id, ConstraintSet.RIGHT, verticalGuideLines[time.day].id, ConstraintSet.RIGHT)
        constraintSet.connect(timeIndicatorHead.id, ConstraintSet.TOP, timeIndicatorLine.id, ConstraintSet.TOP)
        constraintSet.connect(timeIndicatorHead.id, ConstraintSet.BOTTOM, timeIndicatorLine.id, ConstraintSet.BOTTOM)
        constraintSet.constrainWidth(timeIndicatorHead.id, indicatorHeadDiameter)
        constraintSet.constrainHeight(timeIndicatorHead.id, indicatorHeadDiameter)

        constraintSet.connect(timeIndicatorLine.id, ConstraintSet.RIGHT, verticalGuideLines[time.day + 1].id, ConstraintSet.LEFT)
        constraintSet.connect(timeIndicatorLine.id, ConstraintSet.LEFT, timeIndicatorHead.id, ConstraintSet.RIGHT)
        constraintSet.connect(timeIndicatorLine.id, ConstraintSet.TOP, timeLayout.id, ConstraintSet.TOP, (hourHeight * time.inDailyHours()).toInt())
        constraintSet.constrainHeight(timeIndicatorLine.id, indicatorLineWidth)

        constraintSet.applyTo(timeLayout)
    }
}