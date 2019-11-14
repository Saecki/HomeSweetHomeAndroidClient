package saecki.homesweethomeandroidclient.ui.rule.weeklyrule

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import saecki.homesweethomeandroidclient.R
import saecki.homesweethomeandroidclient.datatypes.rules.WeeklyRule

class WeeklyRuleViewHolder(val view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    lateinit var weeklyRule: WeeklyRule
    val name: TextView = view.findViewById(R.id.name)

    fun update(weeklyRule: WeeklyRule) {
        name.text = weeklyRule.name
    }

    fun bindView(weeklyRule: WeeklyRule) {
        this.weeklyRule = weeklyRule
        update(weeklyRule)
        view.setOnClickListener {
            showWeeklyRuleFragment()
        }
    }

    fun showWeeklyRuleFragment() {
        TODO("show weeklyrule fragment")
    }
}