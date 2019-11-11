package saecki.homesweethomeandroidclient.ui.rule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import saecki.homesweethomeandroidclient.R
import saecki.homesweethomeandroidclient.datatypes.rules.Rule
import saecki.homesweethomeandroidclient.datatypes.rules.WeeklyRule
import saecki.homesweethomeandroidclient.ui.dummy.DummyViewHolder
import saecki.homesweethomeandroidclient.ui.rule.weeklyrule.WeeklyRuleViewHolder

class RuleListAdapter(var rules: List<Rule>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            WeeklyRule.type -> {
                val weeklyRuleView = inflater.inflate(R.layout.rule_weekly_rule, parent, false)
                return WeeklyRuleViewHolder(
                    weeklyRuleView
                )
            }
            else -> {
                val dummyView = inflater.inflate(R.layout.dummy, parent, false)
                return DummyViewHolder(dummyView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return rules[position].type
    }

    override fun getItemCount(): Int {
        return rules.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val rule = rules[position]

        when (rule.type) {
            WeeklyRule.type -> {
                val weeklyRuleViewHolder = holder as WeeklyRuleViewHolder
                val weeklyRule = rule as WeeklyRule
                weeklyRuleViewHolder.bindView(weeklyRule)
            }
        }
    }

}