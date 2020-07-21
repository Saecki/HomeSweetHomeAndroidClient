package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.RuleWeeklyRuleBinding
import bedbrains.shared.datatypes.rules.WeeklyRule

class WeeklyRuleViewHolder(private val binding: RuleWeeklyRuleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var weeklyRule: WeeklyRule
    private val name = binding.name

    val itemDetails
        get() = object : ItemDetailsLookup.ItemDetails<String>() {
            override fun getSelectionKey(): String? = weeklyRule.uid
            override fun getPosition(): Int = adapterPosition
        }

    fun bind(weeklyRule: WeeklyRule, isSelected: Boolean = false) {
        this.weeklyRule = weeklyRule
        name.text = weeklyRule.name

        binding.root.isActivated = isSelected

        binding.root.setOnClickListener {
            val bundle = Bundle()

            bundle.putString(Res.resources.getString(R.string.uid), weeklyRule.uid)
            binding.root.findNavController().navigate(
                R.id.action_nav_rules_to_nav_weekly_rule,
                bundle
            )
        }
    }
}