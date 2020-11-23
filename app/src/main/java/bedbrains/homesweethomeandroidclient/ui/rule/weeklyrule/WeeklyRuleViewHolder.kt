package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.os.Bundle
import androidx.navigation.findNavController
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.RuleWeeklyRuleBinding
import bedbrains.homesweethomeandroidclient.ui.adapter.ListItemViewHolder
import bedbrains.shared.datatypes.rules.WeeklyRule

class WeeklyRuleViewHolder(private val binding: RuleWeeklyRuleBinding) : ListItemViewHolder<WeeklyRule>(binding.root) {

    private val name = binding.name


    override fun onBind(value: WeeklyRule, isSelected: Boolean, isEditing: Boolean) {
        name.text = value.name

        binding.root.setOnClickListener {
            val bundle = Bundle()

            bundle.putString(Res.resources.getString(R.string.uid), value.uid)
            binding.root.findNavController().navigate(
                R.id.action_nav_rules_to_nav_weekly_rule,
                bundle
            )
        }
    }
}