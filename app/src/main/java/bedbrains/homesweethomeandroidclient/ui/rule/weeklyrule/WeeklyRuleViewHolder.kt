package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.shared.datatypes.rules.WeeklyRule

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
            view.findNavController().navigate(R.id.action_nav_rules_to_nav_weekly_rule)
        }
    }

}