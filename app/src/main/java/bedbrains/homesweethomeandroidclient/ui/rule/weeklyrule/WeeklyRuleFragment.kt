package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.service.autofill.DateValueSanitizer
import android.text.format.DateFormat
import android.view.*
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.ui.animation.CollapseAnimation
import bedbrains.homesweethomeandroidclient.ui.animation.ExpandAnimation
import bedbrains.shared.datatypes.rules.WeeklyTime
import bedbrains.shared.datatypes.rules.WeeklyTimeSpan
import com.google.android.material.appbar.AppBarLayout
import java.text.DateFormatSymbols
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*

class WeeklyRuleFragment : Fragment() {

    lateinit var weeklyRuleViewModel: WeeklyRuleViewModel
    lateinit var dayToolBar: LinearLayout
    lateinit var daySpace: View
    lateinit var days: List<TextView>
    lateinit var locale: Locale
    var weekDayStrings = Array<String>(7) { "" }

    lateinit var constraintSet: ConstraintSet
    lateinit var timeLayout: ConstraintLayout
    lateinit var timeLine: LinearLayout
    lateinit var times: List<TextView>
    lateinit var verticalGuideLines: List<View>
    lateinit var topTimeSpanAnchor: Space
    lateinit var bottomTimeSpanAnchor: Space
    lateinit var timeIndicatorHead: View
    lateinit var timeIndicatorLine: View

    var timeSpans = mutableListOf<View>()

    var hourHeight = 0
    var indicatorLineWidth = 0
    var indicatorHeadDiameter = 0
    var timeSpanMargin = 0
    var timeSpanBigMargin = 0
    var cardViewElevation = 0f
    var cardViewHighElevation = 0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        weeklyRuleViewModel = ViewModelProviders.of(this).get(WeeklyRuleViewModel::class.java)

        setHasOptionsMenu(true)
        MainActivity.appBarLayout.findViewById<Toolbar>(R.id.toolbar).title = weeklyRuleViewModel.rule.name

        dayToolBar = inflater.inflate(R.layout.weekly_rule_toolbar, MainActivity.appBarLayout, false) as LinearLayout
        days = listOf(
            dayToolBar.findViewById(R.id.day_0),
            dayToolBar.findViewById(R.id.day_1),
            dayToolBar.findViewById(R.id.day_2),
            dayToolBar.findViewById(R.id.day_3),
            dayToolBar.findViewById(R.id.day_4),
            dayToolBar.findViewById(R.id.day_5),
            dayToolBar.findViewById(R.id.day_6)
        )
        daySpace = dayToolBar.findViewById(R.id.day_space)
        locale = Locale.getDefault()

        val sdkVersion = Build.VERSION.SDK_INT
        if (sdkVersion < Build.VERSION_CODES.O) {
            val dfs = DateFormatSymbols.getInstance(locale)
            weekDayStrings = dfs.shortWeekdays
        } else @TargetApi(Build.VERSION_CODES.O) {
            val wf = WeekFields.of(locale)
            val firstDayOfWeek = wf.firstDayOfWeek

            for (i in weekDayStrings.indices) {
                weekDayStrings[i] = firstDayOfWeek.plus(i.toLong()).getDisplayName(TextStyle.NARROW, locale)
            }
        }

        val root = inflater.inflate(R.layout.fragment_weekly_rule, container, false) as ConstraintLayout
        constraintSet = ConstraintSet()
        timeLayout = root.findViewById(R.id.time_layout) as ConstraintLayout
        timeLine = timeLayout.findViewById(R.id.time_line)
        times = listOf(
            timeLine.findViewById(R.id.time_1),
            timeLine.findViewById(R.id.time_2),
            timeLine.findViewById(R.id.time_3),
            timeLine.findViewById(R.id.time_4),
            timeLine.findViewById(R.id.time_5),
            timeLine.findViewById(R.id.time_6),
            timeLine.findViewById(R.id.time_7),
            timeLine.findViewById(R.id.time_8),
            timeLine.findViewById(R.id.time_9),
            timeLine.findViewById(R.id.time_10),
            timeLine.findViewById(R.id.time_11),
            timeLine.findViewById(R.id.time_12),
            timeLine.findViewById(R.id.time_13),
            timeLine.findViewById(R.id.time_14),
            timeLine.findViewById(R.id.time_15),
            timeLine.findViewById(R.id.time_16),
            timeLine.findViewById(R.id.time_17),
            timeLine.findViewById(R.id.time_18),
            timeLine.findViewById(R.id.time_19),
            timeLine.findViewById(R.id.time_20),
            timeLine.findViewById(R.id.time_21),
            timeLine.findViewById(R.id.time_22),
            timeLine.findViewById(R.id.time_23)
        )
        verticalGuideLines = listOf(
            timeLayout.findViewById(R.id.vertical_line_0),
            timeLayout.findViewById(R.id.vertical_line_1),
            timeLayout.findViewById(R.id.vertical_line_2),
            timeLayout.findViewById(R.id.vertical_line_3),
            timeLayout.findViewById(R.id.vertical_line_4),
            timeLayout.findViewById(R.id.vertical_line_5),
            timeLayout.findViewById(R.id.vertical_line_6),
            timeLayout.findViewById(R.id.vertical_line_7)
        )
        topTimeSpanAnchor = timeLayout.findViewById(R.id.top_time_span_anchor)
        bottomTimeSpanAnchor = timeLayout.findViewById(R.id.bottom_time_span_anchor)
        timeIndicatorHead = timeLayout.findViewById(R.id.time_indicator_head)
        timeIndicatorLine = timeLayout.findViewById(R.id.time_indicator_line)

        hourHeight = resources.getDimensionPixelSize(R.dimen.weekly_rule_hour_height)
        indicatorLineWidth = resources.getDimensionPixelSize(R.dimen.time_indicaotr_line_width)
        indicatorHeadDiameter = resources.getDimensionPixelSize(R.dimen.time_indicator_head_diameter)
        timeSpanMargin = resources.getDimensionPixelSize(R.dimen.weekly_rule_time_span_margin)
        timeSpanBigMargin = resources.getDimensionPixelSize(R.dimen.weekly_rule_time_span_big_margin)
        cardViewElevation = resources.getDimension(R.dimen.card_view_elevation)
        cardViewHighElevation = resources.getDimension(R.dimen.card_view_high_elevation)

        //toolbar
        for (i in days.indices) {
            days[i].text = weekDayStrings[i]
        }

        val dayHeaderParams: AppBarLayout.LayoutParams = if (weeklyRuleViewModel.initialCreation) {
            AppBarLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 0)
        } else {
            AppBarLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT)
        }

        dayToolBar.layoutParams = dayHeaderParams
        MainActivity.appBarLayout.addView(dayToolBar)

        if (weeklyRuleViewModel.initialCreation) {
            val duration = getAnimationDuration()
            val expandAnimation = ExpandAnimation(dayToolBar)
            expandAnimation.duration = duration
            expandAnimation.startOffset = duration / 2
            dayToolBar.layoutParams = dayHeaderParams
            dayToolBar.startAnimation(expandAnimation)
            weeklyRuleViewModel.initialCreation = false
        }

        //times
        constraintSet.clone(timeLayout)

        if (sdkVersion < Build.VERSION_CODES.O) {
            for (i in times.indices) {
                val cal = Calendar.getInstance(locale)
                cal.set(Calendar.HOUR_OF_DAY, i + 1)
                cal.set(Calendar.MINUTE, 0)
                val date = Date(cal.timeInMillis)
                val df = DateFormat.getTimeFormat(context)

                times[i].text = df.format(date)
            }
        } else @TargetApi(Build.VERSION_CODES.O) {
            for (i in times.indices) {
                val dtf = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                val localTime = LocalTime.of(i + 1, 0)
                times[i].text = localTime.format(dtf)
            }
        }

        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        timeLine.measure(wrapContentMeasureSpec, wrapContentMeasureSpec)
        daySpace.layoutParams.width = timeLine.measuredWidth

        displayTimeSpans()
        startUpdatingTimeIndicator()

        return root
    }

    override fun onResume() {
        super.onResume()
        updateTimeIndicator(WeeklyTime.now())
    }

    override fun onDestroy() {
        super.onDestroy()

        val collapseAnimation = CollapseAnimation(dayToolBar)
        collapseAnimation.duration = getAnimationDuration()
        collapseAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                MainActivity.appBarLayout.removeView(dayToolBar)
            }

            override fun onAnimationStart(animation: Animation?) {}

        })
        dayToolBar.startAnimation(collapseAnimation)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.weekly_rule, menu)
    }

    fun displayTimeSpans() {
        timeSpans.forEach { view -> timeLayout.removeView(view) }

        for (t in weeklyRuleViewModel.rule.timeSpans) {
            displayTimeSpan(t)
        }

        constraintSet.applyTo(timeLayout)
    }

    fun displayTimeSpan(t: WeeklyTimeSpan) {

        var endDay = t.end.day

        if (t.start.after(t.end))
            endDay += 7

        if (t.end.inDailyMillis() == 0)
            endDay--

        for (i in t.start.day until endDay + 1) {
            val day = i % 7

            val card = createTimeSpan(day)

            if (i > t.start.day) {
                constraintSet.connect(card.id, ConstraintSet.TOP, topTimeSpanAnchor.id, ConstraintSet.TOP)
            } else {
                constraintSet.connect(card.id, ConstraintSet.TOP, timeLine.id, ConstraintSet.TOP, (hourHeight * t.start.inDailyHours()).toInt())
            }

            if (i == endDay && t.end.inDailyMillis() == 0) {
                constraintSet.connect(card.id, ConstraintSet.BOTTOM, timeLine.id, ConstraintSet.BOTTOM)
            } else if (i < endDay) {
                constraintSet.connect(card.id, ConstraintSet.BOTTOM, bottomTimeSpanAnchor.id, ConstraintSet.BOTTOM)
            } else {
                constraintSet.connect(card.id, ConstraintSet.BOTTOM, timeLine.id, ConstraintSet.BOTTOM, (hourHeight * (24 - t.end.inDailyHours())).toInt())
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

    fun startUpdatingTimeIndicator() {
        val mainHandler = Handler(Looper.myLooper()!!)

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

    private fun getAnimationDuration(): Long {
        val key: String = MainActivity.res.getString(R.string.pref_animation_duration_key)
        return MainActivity.getPrefInt(key, 250).toLong()
    }
}