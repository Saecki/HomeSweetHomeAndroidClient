package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.RuleWeeklyRuleBinding
import bedbrains.shared.datatypes.rules.WeeklyRule

class WeeklyRuleViewHolder(private val viewBinding: RuleWeeklyRuleBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    lateinit var weeklyRule: WeeklyRule
    val name = viewBinding.name

    fun update(weeklyRule: WeeklyRule) {
        name.text = weeklyRule.name
    }

    fun bindView(weeklyRule: WeeklyRule) {
        this.weeklyRule = weeklyRule
        update(weeklyRule)
        viewBinding.root.setOnClickListener {
            viewBinding.root.findNavController().navigate(R.id.action_nav_rules_to_nav_weekly_rule)
        }
    }

}