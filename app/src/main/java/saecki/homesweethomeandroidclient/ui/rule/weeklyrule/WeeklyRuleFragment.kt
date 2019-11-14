package saecki.homesweethomeandroidclient.ui.rule.weeklyrule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import saecki.homesweethomeandroidclient.R
import java.text.DateFormatSymbols

class WeeklyRuleFragment : Fragment() {

    lateinit var weeklyRuleViewModel: WeeklyRuleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        weeklyRuleViewModel = ViewModelProviders.of(this).get(WeeklyRuleViewModel::class.java)
        val root = inflater.inflate(R.layout.rule_fragment_weekly_rule, container, false)
        val name: TextView = root.findViewById(R.id.name)
        val timeSpans: GridLayout = root.findViewById(R.id.time_spans)
        val weekdays = DateFormatSymbols().shortWeekdays

        name.text = weeklyRuleViewModel.weeklyRule.name

        for (i in weekdays.indices) run {
            val dayHeader = TextView(context)
            val dayHeaderParams = GridLayout.LayoutParams()

            dayHeaderParams.columnSpec = GridLayout.spec(i + 1)
            dayHeaderParams.rowSpec = GridLayout.spec(0)
            dayHeader.layoutParams = dayHeaderParams
            dayHeader.text = weekdays[i]

            timeSpans.addView(dayHeader)
        }

        return root
    }

}