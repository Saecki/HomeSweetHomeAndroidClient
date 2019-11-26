package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import java.text.DateFormatSymbols

class WeeklyRuleFragment : Fragment() {

    lateinit var weeklyRuleViewModel: WeeklyRuleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        weeklyRuleViewModel = ViewModelProviders.of(this).get(WeeklyRuleViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_weekly_rule, container, false)
        val name: TextView = root.findViewById(R.id.name)
        val times: LinearLayout = root.findViewById(R.id.times)
        val daysLayout: ConstraintLayout = root.findViewById(R.id.days)
        val weekdayStrings = DateFormatSymbols().shortWeekdays
        val smallTextMargin = MainActivity.res.getDimensionPixelSize(R.dimen.small_text_margin)
        val days = arrayOfNulls<LinearLayout>(weekdayStrings.size)

        name.text = weeklyRuleViewModel.weeklyRule.name

        //times
        val fillView = TextView(context)
        val fillViewParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        fillView.layoutParams = fillViewParams
        times.addView(fillView)

        val topSpace = Space(context)
        val spaceParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
        topSpace.layoutParams = spaceParams
        times.addView(topSpace)

        for (i in 1..23) {
            val time = TextView(context)
            val timeParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            time.layoutParams = timeParams
            //TODO replace with proper time format
            time.text = (String.format("%02d", i) + ":00")
            times.addView(time)
        }

        val bottomSpace = Space(context)
        bottomSpace.layoutParams = spaceParams
        times.addView(bottomSpace)

        val timesParams = times.layoutParams as ConstraintLayout.LayoutParams
        timesParams.horizontalWeight = 1f

        //days
        for (i in weekdayStrings.indices) {
            val day = inflater.inflate(R.layout.weekly_rule_day, daysLayout, false) as LinearLayout
            val dayName: TextView = day.findViewById(R.id.day_name)
            val timeSpans: ConstraintLayout = day.findViewById(R.id.time_spans)

            days[i] = day
            dayName.text = weekdayStrings[i]
        }

        //setting relative positioning constraints
        timesParams.rightToLeft = days[0]!!.id
        for (i in days.indices) {
            val dayParams = ConstraintLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            if (i == 0) {
                dayParams.leftToLeft = root.id
            } else {
                dayParams.leftToRight = days[i - 1]!!.id
            }
            if (i == weekdayStrings.size - 1) {
                dayParams.rightToRight = root.id
            } else {
                dayParams.rightToLeft = days[i + 1]!!.id
            }
            days[i]!!.layoutParams = dayParams
            daysLayout.addView(days[i]!!)
        }

        return root
    }

    private fun getAnimationDuration(): Long {
        val key: String = MainActivity.res.getString(R.string.pref_animation_duration_key)
        return MainActivity.getPrefInt(key, 250).toLong()
    }
}