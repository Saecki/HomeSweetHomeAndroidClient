package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.os.Bundle
import android.util.Log
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
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*

class WeeklyRuleFragment : Fragment() {

    lateinit var weeklyRuleViewModel: WeeklyRuleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        weeklyRuleViewModel = ViewModelProviders.of(this).get(WeeklyRuleViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_weekly_rule, container, false) as ConstraintLayout
        val times: LinearLayout = LinearLayout(context)
        val locale = Locale.getDefault()
        val wf = WeekFields.of(locale)
        val firstDayOfWeek = wf.firstDayOfWeek
        val weekdayStrings = arrayListOf<String>()
        val smallTextMargin = MainActivity.res.getDimension(R.dimen.small_text_margin).toInt()
        val days = mutableListOf<LinearLayout>()

        //adding weekdays
        for (i in DayOfWeek.values().indices) {
            weekdayStrings.add(firstDayOfWeek.plus(i.toLong()).getDisplayName(TextStyle.SHORT, locale))
        }

        //times
        times.id = View.generateViewId()
        times.orientation = LinearLayout.VERTICAL

        val filler = TextView(context)
        val fillerParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        fillerParams.setMargins(smallTextMargin, smallTextMargin, smallTextMargin, smallTextMargin)
        filler.layoutParams = fillerParams
        filler.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        times.addView(filler)

        val topSpace = Space(context)
        val bottomSpace = Space(context)
        val spaceParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
        spaceParams.setMargins(smallTextMargin, smallTextMargin, smallTextMargin, smallTextMargin)
        topSpace.layoutParams = spaceParams
        bottomSpace.layoutParams = spaceParams

        times.addView(topSpace)

        for (i in 1..23) {
            val time = TextView(context)
            val timeParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 2f)
            val localTime = LocalTime.of(i, 0)
            timeParams.setMargins(smallTextMargin, smallTextMargin, smallTextMargin, smallTextMargin)
            time.layoutParams = timeParams
            time.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            time.text = localTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            times.addView(time)
        }

        times.addView(bottomSpace)

        root.addView(times)

        //days
        for (i in weekdayStrings.indices) {
            val day = LinearLayout(context)
            val dayName = TextView(context)
            val dayNameParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            dayNameParams.setMargins(smallTextMargin, smallTextMargin, smallTextMargin, smallTextMargin)
            day.id = View.generateViewId()
            day.orientation = LinearLayout.VERTICAL
            dayName.layoutParams = dayNameParams
            dayName.text = weekdayStrings[i]
            day.addView(dayName)
            root.addView(day)
            days.add(day)
        }

        //constraining views
        val constraintSet = ConstraintSet()

        constraintSet.constrainWidth(times.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.connect(times.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(times.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

        for (d in days) {
            constraintSet.constrainWidth(d.id, ConstraintSet.WRAP_CONTENT)
            constraintSet.connect(d.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            constraintSet.connect(d.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        }

        val chainIds = IntArray(days.size + 1)
        val weights = FloatArray(days.size + 1) { 1f }
        chainIds[0] = times.id
        for (i in days.indices) {
            chainIds[i + 1] = days[i].id
        }
        weights[0] = 0f

        constraintSet.createHorizontalChain(
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT,
            chainIds,
            weights,
            ConstraintSet.CHAIN_SPREAD_INSIDE
        )

        constraintSet.applyTo(root)

        return root

    }
}