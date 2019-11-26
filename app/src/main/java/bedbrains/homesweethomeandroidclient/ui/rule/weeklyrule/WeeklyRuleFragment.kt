package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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
        val root = inflater.inflate(R.layout.fragment_weekly_rule, container, false) as LinearLayout
        val times: LinearLayout = LinearLayout(context)
        val locale = Locale.getDefault()
        val wf = WeekFields.of(locale)
        val firstDayOfWeek = wf.firstDayOfWeek
        val weekdayStrings = arrayListOf<String>()
        val smallTextMargin = MainActivity.res.getDimension(R.dimen.small_text_margin).toInt()

        //adding weekdays
        for (i in DayOfWeek.values().indices) {
            weekdayStrings.add(firstDayOfWeek.plus(i.toLong()).getDisplayName(TextStyle.SHORT, locale))
        }

        //times
        val timesParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
        times.layoutParams = timesParams
        times.orientation = LinearLayout.VERTICAL

        val filler = TextView(context)
        val fillerParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)


        for (i in 1..23) {
            val time = TextView(context)
            val timeParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
            val localTime = LocalTime.of(i, 0)
            timeParams.setMargins(smallTextMargin, smallTextMargin, smallTextMargin, smallTextMargin)
            time.layoutParams = timeParams
            time.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            time.text = localTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            times.addView(time)
        }
        root.addView(times)

        //days
        for (i in weekdayStrings.indices) {
            val day = LinearLayout(context)
            val dayParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            val dayName = TextView(context)
            val dayNameParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            dayNameParams.setMargins(smallTextMargin, smallTextMargin, smallTextMargin, smallTextMargin)
            day.layoutParams = dayParams
            dayName.layoutParams = dayNameParams
            dayName.text = weekdayStrings[i]
            day.addView(dayName)
            root.addView(day)
        }

        return root
    }
}