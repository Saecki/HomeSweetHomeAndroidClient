package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.FragmentWeeklyRuleBinding
import bedbrains.homesweethomeandroidclient.databinding.WeeklyRuleToolbarBinding
import bedbrains.homesweethomeandroidclient.ui.animation.CollapseAnimation
import bedbrains.homesweethomeandroidclient.ui.animation.ExpandAnimation
import bedbrains.homesweethomeandroidclient.ui.component.refresh
import bedbrains.platform.Time
import bedbrains.shared.datatypes.time.WeeklyTime
import bedbrains.shared.datatypes.time.WeeklyTimeSpan
import bedbrains.shared.datatypes.time.hours
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class WeeklyRuleFragment : Fragment() {

    private val weeklyRuleViewModel: WeeklyRuleViewModel by viewModels()

    private lateinit var root: ConstraintLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var dayToolbar: LinearLayout
    private lateinit var daySpace: View
    private lateinit var days: List<TextView>
    private lateinit var locale: Locale
    private var weekDayStrings = List(7) { "" }

    private lateinit var constraintSet: ConstraintSet
    private lateinit var timeLayout: ConstraintLayout
    private lateinit var timeLine: LinearLayout
    private lateinit var times: List<TextView>
    private lateinit var verticalGuideLines: List<View>
    private lateinit var topTimeSpanAnchor: Space
    private lateinit var bottomTimeSpanAnchor: Space
    private lateinit var timeIndicatorHead: View
    private lateinit var timeIndicatorLine: View
    private var timeSpans = mutableListOf<View>()

    private lateinit var addButton: FloatingActionButton

    private var hourHeight = 0
    private var indicatorLineWidth = 0
    private var indicatorHeadDiameter = 0
    private var timeSpanMargin = 0
    private var timeSpanBigMargin = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        if (weeklyRuleViewModel.initialCreation) {
            val uid = arguments?.getString(resources.getString(R.string.uid))

            if (uid == null) {
                findNavController().popBackStack()
                Toast.makeText(context, R.string.resp_item_no_longer_exists, Toast.LENGTH_LONG).show()
            } else {
                weeklyRuleViewModel.observe(viewLifecycleOwner, uid)
            }
        }

        val dayToolbarBinding = WeeklyRuleToolbarBinding.inflate(inflater)
        dayToolbar = dayToolbarBinding.root as LinearLayout
        days = listOf(
            dayToolbarBinding.day0.root as TextView,
            dayToolbarBinding.day1.root as TextView,
            dayToolbarBinding.day2.root as TextView,
            dayToolbarBinding.day3.root as TextView,
            dayToolbarBinding.day4.root as TextView,
            dayToolbarBinding.day5.root as TextView,
            dayToolbarBinding.day6.root as TextView
        )
        daySpace = dayToolbarBinding.daySpace
        locale = Locale.getDefault()

        weekDayStrings = weekDayStrings.mapIndexed { index, _ -> Time.formatWeekdayNarrow(index, locale) }

        val binding = FragmentWeeklyRuleBinding.inflate(inflater)
        root = binding.root as ConstraintLayout
        swipeRefreshLayout = binding.swipeRefreshLayout
        constraintSet = ConstraintSet()
        timeLayout = binding.timeLayout
        timeLine = binding.timeLine
        times = listOf(
            binding.time1.root as TextView,
            binding.time2.root as TextView,
            binding.time3.root as TextView,
            binding.time4.root as TextView,
            binding.time5.root as TextView,
            binding.time6.root as TextView,
            binding.time7.root as TextView,
            binding.time8.root as TextView,
            binding.time9.root as TextView,
            binding.time10.root as TextView,
            binding.time11.root as TextView,
            binding.time12.root as TextView,
            binding.time13.root as TextView,
            binding.time14.root as TextView,
            binding.time15.root as TextView,
            binding.time16.root as TextView,
            binding.time17.root as TextView,
            binding.time18.root as TextView,
            binding.time19.root as TextView,
            binding.time20.root as TextView,
            binding.time21.root as TextView,
            binding.time22.root as TextView,
            binding.time23.root as TextView
        )
        verticalGuideLines = listOf(
            binding.verticalLine0,
            binding.verticalLine1,
            binding.verticalLine2,
            binding.verticalLine3,
            binding.verticalLine4,
            binding.verticalLine5,
            binding.verticalLine6,
            binding.verticalLine7
        )
        topTimeSpanAnchor = binding.topTimeSpanAnchor
        bottomTimeSpanAnchor = binding.bottomTimeSpanAnchor
        timeIndicatorHead = binding.timeIndicatorHead
        timeIndicatorLine = binding.timeIndicatorLine

        addButton = binding.addButton

        hourHeight = resources.getDimensionPixelSize(R.dimen.weekly_rule_hour_height)
        indicatorLineWidth = resources.getDimensionPixelSize(R.dimen.time_indicator_line_width)
        indicatorHeadDiameter = resources.getDimensionPixelSize(R.dimen.time_indicator_head_diameter)
        timeSpanMargin = resources.getDimensionPixelSize(R.dimen.weekly_rule_time_span_margin)
        timeSpanBigMargin = resources.getDimensionPixelSize(R.dimen.weekly_rule_time_span_big_margin)

        //toolbar
        days.forEachIndexed { index, textView ->
            textView.text = weekDayStrings[index]
        }

        val dayParams: AppBarLayout.LayoutParams = if (weeklyRuleViewModel.initialCreation) {
            AppBarLayout.LayoutParams(0, 0)
        } else {
            AppBarLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT)
        }
        dayParams.weight = 1f
        days.forEach { it.layoutParams = dayParams }
        dayToolbar.visibility = View.VISIBLE
        MainActivity.appBarLayout.addView(dayToolbar)

        //times
        constraintSet.clone(timeLayout)

        times.forEachIndexed { index, textView ->
            textView.text = Time.formatTime(index + 1, 0, locale)
        }

        //add button
        addButton.setOnClickListener {
            val newTimeSpan = WeeklyTimeSpan.UNSPECIFIED.apply {
                value.name = resources.getString(R.string.item_untitled)
                end += 1.hours
            }

            DataRepository.upsertRule(weeklyRuleViewModel.rule.value!!.also {
                it.timeSpans.add(newTimeSpan)
            })
            showTimeSpanDialog(newTimeSpan.uid)
        }

        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        timeLine.measure(wrapContentMeasureSpec, wrapContentMeasureSpec)
        daySpace.layoutParams.width = timeLine.measuredWidth

        startUpdatingTimeIndicator()

        weeklyRuleViewModel.rule.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                findNavController().popBackStack()
                Toast.makeText(context, R.string.resp_item_no_longer_exists, Toast.LENGTH_LONG).show()
            } else {
                MainActivity.toolbar.title = it.name
                displayTimeSpans(it.timeSpans.toList())
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.refresh(viewLifecycleOwner, context)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        updateTimeIndicator(WeeklyTime.now)

        if (weeklyRuleViewModel.initialCreation) {
            val duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            val expandAnimation = ExpandAnimation(*days.toTypedArray())

            dayToolbar.visibility = View.VISIBLE
            expandAnimation.duration = duration
            expandAnimation.startOffset = duration / 4
            dayToolbar.startAnimation(expandAnimation)
            weeklyRuleViewModel.initialCreation = false
        }
    }

    override fun onPause() {
        super.onPause()

        val duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        val collapseAnimation = CollapseAnimation(dayToolbar)

        collapseAnimation.duration = duration
        collapseAnimation.startOffset = duration / 4
        collapseAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                MainActivity.appBarLayout.removeView(dayToolbar)
            }

            override fun onAnimationStart(animation: Animation?) {}
        })

        dayToolbar.startAnimation(collapseAnimation)
        weeklyRuleViewModel.initialCreation = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.weekly_rule, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> swipeRefreshLayout.refresh(viewLifecycleOwner, context)
            R.id.action_clear -> showClearAllDialog()
            R.id.action_rename -> showRenameDialog(weeklyRuleViewModel.rule.value!!.name)
            else -> return super.onOptionsItemSelected(item)
        }

        return false
    }

    private fun displayTimeSpans(timeSpans: List<WeeklyTimeSpan>) {
        this.timeSpans.forEach { view -> timeLayout.removeView(view) }
        timeSpans.forEach { this.timeSpans.addAll(displayTimeSpan(it)) }
        constraintSet.applyTo(timeLayout)
    }

    private fun displayTimeSpan(t: WeeklyTimeSpan): List<View> {
        val timeSpans = mutableListOf<View>()
        var endDay = t.end.localizedDay

        if (t.start.localizedAfter(t.end))
            endDay += 7

        if (t.end.inDailyMilliseconds == 0)
            endDay--

        for (i in t.start.localizedDay until endDay + 1) {
            val day = i % 7
            val card = createTimeSpan(day, t)

            timeSpans.add(card)

            if (i > t.start.localizedDay) {
                constraintSet.connect(card.id, ConstraintSet.TOP, topTimeSpanAnchor.id, ConstraintSet.TOP)
            } else {
                constraintSet.connect(card.id, ConstraintSet.TOP, timeLine.id, ConstraintSet.TOP, (hourHeight * t.start.inDailyHours).toInt())
            }

            if (i == endDay && t.end.inDailyMilliseconds == 0) {
                constraintSet.connect(card.id, ConstraintSet.BOTTOM, timeLine.id, ConstraintSet.BOTTOM)
            } else if (i < endDay) {
                constraintSet.connect(card.id, ConstraintSet.BOTTOM, bottomTimeSpanAnchor.id, ConstraintSet.BOTTOM)
            } else {
                constraintSet.connect(card.id, ConstraintSet.BOTTOM, timeLine.id, ConstraintSet.BOTTOM, (hourHeight * (24 - t.end.inDailyHours)).toInt())
            }
        }

        return timeSpans
    }

    private fun createTimeSpan(day: Int, timeSpan: WeeklyTimeSpan): View {
        val card = CardView(context!!)
        card.background = ContextCompat.getDrawable(context!!, R.drawable.time_span_background)
        card.id = View.generateViewId()
        timeLayout.addView(card)

        card.setOnClickListener {
            showTimeSpanDialog(timeSpan.uid)
        }

        constraintSet.connect(card.id, ConstraintSet.LEFT, verticalGuideLines[day].id, ConstraintSet.RIGHT, timeSpanMargin)
        constraintSet.connect(card.id, ConstraintSet.RIGHT, verticalGuideLines[day + 1].id, ConstraintSet.LEFT, timeSpanBigMargin)

        return card
    }

    private fun startUpdatingTimeIndicator() {
        val mainHandler = Handler(Looper.myLooper()!!)

        mainHandler.post(object : Runnable {
            override fun run() {
                updateTimeIndicator(WeeklyTime.now)
                mainHandler.postDelayed(this, 60000)
            }
        })
    }

    private fun updateTimeIndicator(time: WeeklyTime) {
        constraintSet.clear(timeIndicatorHead.id)
        constraintSet.clear(timeIndicatorLine.id)

        constraintSet.connect(timeIndicatorHead.id, ConstraintSet.RIGHT, verticalGuideLines[time.localizedDay].id, ConstraintSet.RIGHT)
        constraintSet.connect(timeIndicatorHead.id, ConstraintSet.TOP, timeIndicatorLine.id, ConstraintSet.TOP)
        constraintSet.connect(timeIndicatorHead.id, ConstraintSet.BOTTOM, timeIndicatorLine.id, ConstraintSet.BOTTOM)
        constraintSet.constrainWidth(timeIndicatorHead.id, indicatorHeadDiameter)
        constraintSet.constrainHeight(timeIndicatorHead.id, indicatorHeadDiameter)

        constraintSet.connect(timeIndicatorLine.id, ConstraintSet.RIGHT, verticalGuideLines[time.localizedDay + 1].id, ConstraintSet.LEFT)
        constraintSet.connect(timeIndicatorLine.id, ConstraintSet.LEFT, timeIndicatorHead.id, ConstraintSet.RIGHT)
        constraintSet.connect(timeIndicatorLine.id, ConstraintSet.TOP, timeLine.id, ConstraintSet.TOP, (hourHeight * time.inDailyHours).toInt())
        constraintSet.constrainHeight(timeIndicatorLine.id, indicatorLineWidth)

        constraintSet.applyTo(timeLayout)
    }

    private fun showTimeSpanDialog(timeSpanUid: String) {
        val bundle = Bundle()

        bundle.putString(resources.getString(R.string.rule_uid), weeklyRuleViewModel.rule.value!!.uid)
        bundle.putString(resources.getString(R.string.time_span_uid), timeSpanUid)

        MainActivity.bottomSheetBehavior.peekHeight = 240
        MainActivity.bottomSheetBehavior.halfExpandedRatio = 0.5f
        MainActivity.bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        MainActivity.activity.supportFragmentManager.beginTransaction()
            .add(R.id.bottom_sheet_content, WeeklyTimeSpanFragment::class.java, bundle)
            .commit()

        // root.findNavController().navigate(
        //     R.id.action_nav_weekly_rule_to_nav_weekly_time_span,
        //     bundle
        // )
    }

    private fun showRenameDialog(text: String) {
        val input = EditText(context)
        input.setText(text)

        AlertDialog.Builder(context!!)
            .setTitle(MainActivity.res.getString(R.string.pref_temperature_category_title))
            .setView(input)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                DataRepository.upsertRule(weeklyRuleViewModel.rule.value!!.also {
                    it.name = input.text.toString()
                })
            }
            .show()

        input.requestFocusFromTouch()
        input.postDelayed({
            val keyboard =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)
        }, 0)
    }

    private fun showClearAllDialog() {
        AlertDialog.Builder(context!!)
            .setTitle(R.string.weekly_time_span_clear_all_confirmation)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                DataRepository.upsertRule(weeklyRuleViewModel.rule.value!!.also {
                    it.timeSpans.clear()
                })
            }
            .show()
    }
}
