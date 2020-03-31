package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.RuleWeeklyRuleBinding
import bedbrains.shared.datatypes.rules.WeeklyRule

class WeeklyRuleViewHolder(private val viewBinding: RuleWeeklyRuleBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    lateinit var weeklyRule: WeeklyRule
    val name = viewBinding.name

    fun bindView(weeklyRule: WeeklyRule) {
        this.weeklyRule = weeklyRule
        name.text = weeklyRule.name

        viewBinding.root.setOnClickListener {
            val bundle = Bundle()

            bundle.putString(MainActivity.res.getString(R.string.uid), weeklyRule.uid)
            viewBinding.root.findNavController().navigate(
                R.id.action_nav_rules_to_nav_weekly_rule,
                bundle
            )
        }
    }
}