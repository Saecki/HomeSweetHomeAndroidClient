package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.Animation
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.FragmentWeeklyRuleBinding
import bedbrains.homesweethomeandroidclient.databinding.WeeklyRuleToolbarBinding
import bedbrains.homesweethomeandroidclient.ui.animation.CollapseAnimation
import bedbrains.homesweethomeandroidclient.ui.animation.ExpandAnimation
import bedbrains.homesweethomeandroidclient.ui.component.refresh
import bedbrains.homesweethomeandroidclient.ui.dialog.InputDialog
import bedbrains.homesweethomeandroidclient.ui.dialog.onFinished
import bedbrains.homesweethomeandroidclient.ui.dialog.text
import bedbrains.homesweethomeandroidclient.ui.dialog.title
import bedbrains.platform.Time
import bedbrains.shared.datatypes.deepCopy
import bedbrains.shared.datatypes.time.WeeklyTime
import bedbrains.shared.datatypes.time.WeeklyTimeSpan
import bedbrains.shared.datatypes.time.hours
import bedbrains.shared.datatypes.upsert
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.util.*

class WeeklyRuleFragment : Fragment() {
    private val weeklyRuleViewModel: WeeklyRuleViewModel by viewModels()
        private val navListener = NavController.OnDestinationChangedListener { _, destination, _ ->
            if (this.destinationId != destination.id) {
                hideDayToolbar(true)
                hideTimeSpanDialog()
            }
        }
        private var destinationId: Int? = null

        private lateinit var swipeRefreshLayout: SwipeRefreshLayout
        private lateinit var dayToolbar: LinearLayout
        private lateinit var daySpace: View
        private lateinit var days: List<TextView>
        private lateinit var locale: Locale
        private lateinit var weekDayStrings: List<String>

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
        private var previewTimeSpans = mutableListOf<View>()

        private lateinit var addButton: ExtendedFloatingActionButton
        private lateinit var timeSpanFragment: WeeklyTimeSpanFragment

        private var hourHeight = 0
        private var indicatorLineWidth = 0
        private var indicatorHeadDiameter = 0
        private var timeSpanMargin = 0
        private var timeSpanBigMargin = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            locale = Locale.getDefault()
            weekDayStrings = (0..6).mapIndexed { index, _ -> Time.formatWeekdayNarrow(index, locale) }
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            setHasOptionsMenu(true)

            if (weeklyRuleViewModel.initialCreation) {
                val uid = arguments?.getString(resources.getString(R.string.uid))

                if (uid == null) {
                    findNavController().popBackStack()
                    Toast.makeText(context, R.string.resp_item_no_longer_exists, Toast.LENGTH_LONG)
                        .show()
                } else {
                    weeklyRuleViewModel.observe(viewLifecycleOwner, uid)
                }
            }

            destinationId = findNavController().currentDestination?.id

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

            val binding = FragmentWeeklyRuleBinding.inflate(inflater)
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

            addButton = MainActivity.fab

            hourHeight = resources.getDimensionPixelSize(R.dimen.weekly_rule_hour_height)
            indicatorLineWidth = resources.getDimensionPixelSize(R.dimen.time_indicator_line_width)
            indicatorHeadDiameter =
                resources.getDimensionPixelSize(R.dimen.time_indicator_head_diameter)
            timeSpanMargin = resources.getDimensionPixelSize(R.dimen.weekly_rule_time_span_margin)
            timeSpanBigMargin =
                resources.getDimensionPixelSize(R.dimen.weekly_rule_time_span_big_margin)

            //toolbar
            days.forEachIndexed { index, textView ->
                textView.text = weekDayStrings[index]
            }

            showDayToolbar(weeklyRuleViewModel.initialCreation)

            //times
            constraintSet.clone(timeLayout)

            times.forEachIndexed { index, textView ->
                textView.text = Time.formatTime(index + 1, 0, locale)
            }

            //add button
            MainActivity.showFabDelayed()
            addButton.text = resources.getString(R.string.action_new)
            addButton.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_add_black_24dp)
            addButton.setOnClickListener {
                val newTimeSpan = WeeklyTimeSpan.UNSPECIFIED

                newTimeSpan.apply {
                    value.name = resources.getString(R.string.item_unnamed)
                    end += 1.hours
                }

                showTimeSpanDialog(newTimeSpan)
            }

            val wrapContentMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            timeLine.measure(wrapContentMeasureSpec, wrapContentMeasureSpec)
            daySpace.layoutParams.width = timeLine.measuredWidth

            if (weeklyRuleViewModel.previewTimeSpan != null) {
                val timeSpan = weeklyRuleViewModel.previewTimeSpan!!.deepCopy()
                weeklyRuleViewModel.previewTimeSpan = null
                showTimeSpanDialog(timeSpan)
            }

            startUpdatingTimeIndicator()

            weeklyRuleViewModel.rule.observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    findNavController().popBackStack()
                    Toast.makeText(context, R.string.resp_item_no_longer_exists, Toast.LENGTH_LONG)
                        .show()
                } else {
                    MainActivity.toolbar.title = it.name
                    displayTimeSpans(it.timeSpans)
                }
            })

            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.refresh(viewLifecycleOwner, context)
            }

            return binding.root
        }

        override fun onResume() {
            super.onResume()

            findNavController().addOnDestinationChangedListener(navListener)

            updateTimeIndicator(WeeklyTime.now)
        }

        override fun onPause() {
            super.onPause()

            findNavController().removeOnDestinationChangedListener(navListener)
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
            this.timeSpans.forEach { timeLayout.removeView(it) }
            timeSpans
                .filter { weeklyRuleViewModel.previewTimeSpan?.uid != it.uid }
                .forEach { this.timeSpans.addAll(displayTimeSpan(it)) }
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
                    constraintSet.connect(
                        card.id,
                        ConstraintSet.TOP,
                        topTimeSpanAnchor.id,
                        ConstraintSet.TOP
                    )
                } else {
                    constraintSet.connect(
                        card.id,
                        ConstraintSet.TOP,
                        timeLine.id,
                        ConstraintSet.TOP,
                        (hourHeight * t.start.inDailyHours).toInt()
                    )
                }

                if (i == endDay && t.end.inDailyMilliseconds == 0) {
                    constraintSet.connect(
                        card.id,
                        ConstraintSet.BOTTOM,
                        timeLine.id,
                        ConstraintSet.BOTTOM
                    )
                } else if (i < endDay) {
                    constraintSet.connect(
                        card.id,
                        ConstraintSet.BOTTOM,
                        bottomTimeSpanAnchor.id,
                        ConstraintSet.BOTTOM
                    )
                } else {
                    constraintSet.connect(
                        card.id,
                        ConstraintSet.BOTTOM,
                        timeLine.id,
                        ConstraintSet.BOTTOM,
                        (hourHeight * (24 - t.end.inDailyHours)).toInt()
                    )
                }
            }

            constraintSet.applyTo(timeLayout)

            return timeSpans
        }

        private fun createTimeSpan(day: Int, timeSpan: WeeklyTimeSpan): View {
            val card = CardView(requireContext())

            card.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.time_span_background)
            card.id = View.generateViewId()
            card.tag = timeSpan.uid
            timeLayout.addView(card)

            card.setOnClickListener {
                showTimeSpanDialog(timeSpan.deepCopy())
            }

            constraintSet.connect(
                card.id,
                ConstraintSet.LEFT,
                verticalGuideLines[day].id,
                ConstraintSet.RIGHT,
                timeSpanMargin
            )
            constraintSet.connect(
                card.id,
                ConstraintSet.RIGHT,
                verticalGuideLines[day + 1].id,
                ConstraintSet.LEFT,
                timeSpanBigMargin
            )

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

            constraintSet.connect(
                timeIndicatorHead.id,
                ConstraintSet.RIGHT,
                verticalGuideLines[time.localizedDay].id,
                ConstraintSet.RIGHT
            )
            constraintSet.connect(
                timeIndicatorHead.id,
                ConstraintSet.TOP,
                timeIndicatorLine.id,
                ConstraintSet.TOP
            )
            constraintSet.connect(
                timeIndicatorHead.id,
                ConstraintSet.BOTTOM,
                timeIndicatorLine.id,
                ConstraintSet.BOTTOM
            )
            constraintSet.constrainWidth(timeIndicatorHead.id, indicatorHeadDiameter)
            constraintSet.constrainHeight(timeIndicatorHead.id, indicatorHeadDiameter)

            constraintSet.connect(
                timeIndicatorLine.id,
                ConstraintSet.RIGHT,
                verticalGuideLines[time.localizedDay + 1].id,
                ConstraintSet.LEFT
            )
            constraintSet.connect(
                timeIndicatorLine.id,
                ConstraintSet.LEFT,
                timeIndicatorHead.id,
                ConstraintSet.RIGHT
            )
            constraintSet.connect(
                timeIndicatorLine.id,
                ConstraintSet.TOP,
                timeLine.id,
                ConstraintSet.TOP,
                (hourHeight * time.inDailyHours).toInt()
            )
            constraintSet.constrainHeight(timeIndicatorLine.id, indicatorLineWidth)

            constraintSet.applyTo(timeLayout)
        }

        private fun showDayToolbar(animate: Boolean) {
            if (animate) {
                dayToolbar.visibility = View.GONE
                MainActivity.appBarLayout.addView(dayToolbar)
                dayToolbar.layoutParams.height = 0
                dayToolbar.visibility = View.VISIBLE

                val duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
                val showDayToolbar = ExpandAnimation(dayToolbar)

                showDayToolbar.duration = duration
                showDayToolbar.startOffset = duration / 4

                dayToolbar.startAnimation(showDayToolbar)
            } else {
                MainActivity.appBarLayout.addView(dayToolbar)
                dayToolbar.visibility = View.VISIBLE
                dayToolbar.layoutParams.height = Toolbar.LayoutParams.WRAP_CONTENT
            }
        }

        private fun hideDayToolbar(animate: Boolean) {
            if (animate) {
                val duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
                val hideDayToolbar = CollapseAnimation(dayToolbar)

                hideDayToolbar.duration = duration
                hideDayToolbar.startOffset = duration / 4
                hideDayToolbar.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        MainActivity.appBarLayout.removeView(dayToolbar)
                    }

                    override fun onAnimationStart(animation: Animation?) {}
                })

                dayToolbar.startAnimation(hideDayToolbar)
            } else {
                dayToolbar.visibility = View.GONE
                MainActivity.appBarLayout.removeView(dayToolbar)
            }
        }

        private fun showTimeSpanDialog(timeSpan: WeeklyTimeSpan) {
            if (weeklyRuleViewModel.previewTimeSpan != null)
                return

            val fragment = WeeklyTimeSpanFragment()
            val duration = Res.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            timeSpanFragment = fragment

            MainActivity.activity.supportFragmentManager.beginTransaction()
                .replace(R.id.bottom_sheet_fragment, fragment)
                .commit()

            MainActivity.bottomNav.animate()
                .translationY(MainActivity.bottomNav.height.toFloat())
                .setDuration(duration)
                .start()

            MainActivity.hideFab()

            Handler().postDelayed({
                val height =
                    MainActivity.bottomSheet.findViewById<ConstraintLayout>(R.id.rule_value).y.toInt()

                MainActivity.bottomSheetBehavior.isHideable = false
                MainActivity.bottomSheetBehavior.peekHeight = height
                MainActivity.bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

                fragment.bind(timeSpan) { event ->
                    onTimeSpanEdited(event)
                }
            }, (duration * 0.8).toLong())
        }

        private fun hideTimeSpanDialog() {
            if (weeklyRuleViewModel.previewTimeSpan == null) return

            previewTimeSpans.clear()
            weeklyRuleViewModel.previewTimeSpan = null

            val duration = Res.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            MainActivity.bottomSheetBehavior.isHideable = true
            MainActivity.bottomSheetBehavior.peekHeight = 0
            MainActivity.bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            Handler().postDelayed({
                MainActivity.bottomNav.animate()
                    .translationY(0f)
                    .setDuration(duration)
                    .start()

                MainActivity.showFab()

            }, (duration * 0.8).toLong())
        }

        private fun onTimeSpanEdited(event: WeeklyTimeSpanEditEvent) {
            if (context == null)
                return

            when (event.action) {
                WeeklyTimeSpanEditEvent.Action.FINISHED -> {
                    if (weeklyRuleViewModel.rule.value!!.timeSpans.overlays(event.timeSpan)) {
                        Toast.makeText(
                            context,
                            R.string.notice_weekly_time_span_overlaying,
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    } else if (event.timeSpan.length == WeeklyTime.MIN) {
                        Toast.makeText(
                            context,
                            R.string.notice_weekly_time_span_no_length,
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }

                    DataRepository.updateRule(weeklyRuleViewModel.rule.value!!.apply {
                        timeSpans.upsert(event.timeSpan) { event.timeSpan.uid == it.uid }
                    })

                    timeSpans.addAll(previewTimeSpans)

                    hideTimeSpanDialog()
                }
                WeeklyTimeSpanEditEvent.Action.DELETED -> {
                    DataRepository.updateRule(weeklyRuleViewModel.rule.value!!.apply {
                        timeSpans.removeIf { event.timeSpan.uid == it.uid }
                    })

                    previewTimeSpans.forEach { timeLayout.removeView(it) }

                    hideTimeSpanDialog()
                }
                WeeklyTimeSpanEditEvent.Action.CANCELED -> {

                    previewTimeSpans.forEach { timeLayout.removeView(it) }

                    hideTimeSpanDialog()

                    displayTimeSpans(weeklyRuleViewModel.rule.value!!.timeSpans)
                }
                WeeklyTimeSpanEditEvent.Action.APPLIED_TO_ALL -> {
                    DataRepository.updateRule(weeklyRuleViewModel.rule.value!!.apply {
                        timeSpans.applyToAll(event.timeSpan.value)
                    })
                }
                WeeklyTimeSpanEditEvent.Action.PREVIEW -> {
                    timeSpans.filter { event.timeSpan.uid == it.tag.toString() }
                        .forEach {
                            timeLayout.removeView(it)
                            timeSpans.remove(it)
                        }

                    previewTimeSpans.forEach { timeLayout.removeView(it) }
                    previewTimeSpans.clear()
                    previewTimeSpans.addAll(displayTimeSpan(event.timeSpan))
                    weeklyRuleViewModel.previewTimeSpan = event.timeSpan
                }
            }
        }

        private fun showRenameDialog(text: String) {
            InputDialog(requireContext())
                .title(R.string.action_rename)
                .text(text)
                .onFinished {
                    DataRepository.updateRule(weeklyRuleViewModel.rule.value!!.apply {
                        name = it
                    })
                }
                .show()
        }

        private fun showClearAllDialog() {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.confirmation_weekly_time_span_clear_all)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    DataRepository.updateRule(weeklyRuleViewModel.rule.value!!.also {
                        it.timeSpans.clear()
                    })
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }
    }
