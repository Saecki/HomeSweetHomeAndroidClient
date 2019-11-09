package saecki.homesweethomeandroidclient.ui.rule

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import saecki.homesweethomeandroidclient.R
import saecki.homesweethomeandroidclient.datatypes.rules.WeeklyRule

class WeeklyRuleViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val name: TextView = view.findViewById(R.id.name)

    fun update(weeklyRule: WeeklyRule) {
        name.text = weeklyRule.name
    }

    fun bindView(weeklyRule: WeeklyRule) {
        update(weeklyRule)
    }
}