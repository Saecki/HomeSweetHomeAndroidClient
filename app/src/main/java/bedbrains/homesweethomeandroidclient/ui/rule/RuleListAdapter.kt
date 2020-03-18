package bedbrains.homesweethomeandroidclient.ui.rule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.RuleWeeklyRuleBinding
import bedbrains.shared.datatypes.rules.Rule
import bedbrains.shared.datatypes.rules.WeeklyRule
import bedbrains.homesweethomeandroidclient.ui.dummy.DummyViewHolder
import bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule.WeeklyRuleViewHolder

class RuleListAdapter(var rules: List<Rule>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            WeeklyRule.TYPE -> {
                val weeklyRuleViewBinding = RuleWeeklyRuleBinding.inflate(inflater, parent, false)
                WeeklyRuleViewHolder(weeklyRuleViewBinding)
            }
            else -> {
                val dummyView = inflater.inflate(R.layout.dummy, parent)
                DummyViewHolder(dummyView)
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
            WeeklyRule.TYPE -> {
                val weeklyRuleViewHolder = holder as WeeklyRuleViewHolder
                val weeklyRule = rule as WeeklyRule
                weeklyRuleViewHolder.bindView(weeklyRule)
            }
        }
    }

}