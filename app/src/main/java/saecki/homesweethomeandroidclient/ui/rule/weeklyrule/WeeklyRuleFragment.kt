package saecki.homesweethomeandroidclient.ui.rule.weeklyrule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Space
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import saecki.homesweethomeandroidclient.MainActivity
import saecki.homesweethomeandroidclient.R
import java.text.DateFormatSymbols

class WeeklyRuleFragment : Fragment() {

    lateinit var weeklyRuleViewModel: WeeklyRuleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        weeklyRuleViewModel = ViewModelProviders.of(this).get(WeeklyRuleViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_weekly_rule, container, false)
        val name: TextView = root.findViewById(R.id.name)
        val timeSpans: GridLayout = root.findViewById(R.id.time_spans)
        val weekdays = DateFormatSymbols().shortWeekdays
        val smallTextMargin = MainActivity.res.getDimensionPixelSize(R.dimen.small_text_margin)

        name.text = weeklyRuleViewModel.weeklyRule.name

        /*
        //grid
        for (i in 1 until weekdays.size) {
            val vertLine = View(context)
            val vertLineParams = GridLayout.LayoutParams()
            vertLineParams.columnSpec = GridLayout.spec(i * 2 + 1, 0f)
            vertLineParams.rowSpec = GridLayout.spec(0, 24, 0f)
            vertLineParams.width = 1
            vertLine.layoutParams = vertLineParams
            vertLine.setBackgroundResource(R.drawable.background)

            timeSpans.addView(vertLine)
        }

        //days
        for (i in weekdays.indices) run {
            val dayHeader = TextView(context)
            val dayHeaderParams = GridLayout.LayoutParams()

            dayHeaderParams.columnSpec = GridLayout.spec((i + 1) * 2, 1f)
            dayHeaderParams.rowSpec = GridLayout.spec(0)
            dayHeaderParams.setMargins(smallTextMargin)
            dayHeaderParams.width = 0
            dayHeader.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            dayHeader.layoutParams = dayHeaderParams
            dayHeader.text = weekdays[i]

            timeSpans.addView(dayHeader)
        }

        //times
        val space = Space(context)
        val spaceParams = GridLayout.LayoutParams()

        spaceParams.columnSpec = GridLayout.spec(0, 1f)
        spaceParams.rowSpec = GridLayout.spec(1, 1f)
        space.layoutParams = spaceParams

        timeSpans.addView(space)

        for (i in 1..23) {
            val time = TextView(context)
            val timeParams = GridLayout.LayoutParams()

            timeParams.columnSpec = GridLayout.spec(0)
            timeParams.rowSpec = GridLayout.spec((i + 1) * 2, 2, 1f)
            timeParams.setMargins(smallTextMargin)
            time.layoutParams = timeParams
            time.text = (String.format("%02d", i) + ":00")

            timeSpans.addView(time)
        }*/

        return root
    }

    private fun getAnimationDuration(): Long {
        val key: String = MainActivity.res.getString(R.string.pref_animation_duration_key)
        return MainActivity.getPrefInt(key, 250).toLong()
    }
}