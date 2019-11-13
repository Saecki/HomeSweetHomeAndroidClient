package saecki.homesweethomeandroidclient.ui.rule.weeklyrule

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.w3c.dom.Text
import saecki.homesweethomeandroidclient.R
import saecki.homesweethomeandroidclient.datatypes.rules.WeeklyTimeSpan
import java.text.DateFormatSymbols

class WeeklyRuleFragment : Fragment() {

    lateinit var weeklyRuleViewModel: WeeklyRuleViewModel
    val weekdays = DateFormatSymbols().weekdays

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        weeklyRuleViewModel = ViewModelProviders.of(this).get(WeeklyRuleViewModel::class.java)
        val root = inflater.inflate(R.layout.rule_fragment_weekly_rule, container, false)
        val header = root.findViewById<LinearLayout>(R.id.header)
        val name: TextView = root.findViewById(R.id.name)
        val timeSpans: GridLayout = root.findViewById(R.id.time_spans)
        val displayMetrics = DisplayMetrics()

        name.text = weeklyRuleViewModel.weeklyRule.name

        timeSpans.layoutParams.width = displayMetrics.widthPixels
        timeSpans.layoutParams.height = displayMetrics.heightPixels - header.height

        for (i in weekdays) run {
            val dayHeader = TextView(context)
            val dayHeaderParams = GridLayout.LayoutParams()

            dayHeader.layoutParams = dayHeaderParams
            dayHeader.text = i
        }

        return root
    }

    fun addViews(timespans: List<WeeklyTimeSpan>) {

    }

}