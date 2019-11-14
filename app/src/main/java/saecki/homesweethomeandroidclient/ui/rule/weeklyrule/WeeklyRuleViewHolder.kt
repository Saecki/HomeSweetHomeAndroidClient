package saecki.homesweethomeandroidclient.ui.rule.weeklyrule

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import saecki.homesweethomeandroidclient.R
import saecki.homesweethomeandroidclient.datatypes.rules.WeeklyRule

class WeeklyRuleViewHolder(val view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    val name: TextView = view.findViewById(R.id.name)

    fun update(weeklyRule: WeeklyRule) {
        name.text = weeklyRule.name
    }

    fun bindView(weeklyRule: WeeklyRule) {
        update(weeklyRule)
        view.setOnClickListener {
            showWeeklyRuleFragment()
        }
    }

    fun showWeeklyRuleFragment() {
        val activity = context as FragmentActivity
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        val weeklyRuleFragment = WeeklyRuleFragment()
        fragmentTransaction.add(R.id.nav_host_fragment, weeklyRuleFragment, "test")
        fragmentTransaction.show(weeklyRuleFragment).commit()
    }
}