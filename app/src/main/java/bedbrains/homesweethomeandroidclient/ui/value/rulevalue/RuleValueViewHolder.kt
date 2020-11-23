package bedbrains.homesweethomeandroidclient.ui.value.rulevalue

import android.os.Bundle
import androidx.navigation.findNavController
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.ValueRuleValueBinding
import bedbrains.homesweethomeandroidclient.ui.adapter.ListItemViewHolder
import bedbrains.shared.datatypes.rules.RuleValue

class RuleValueViewHolder(private val binding: ValueRuleValueBinding) : ListItemViewHolder<RuleValue>(binding.root) {

    override fun onBind(value: RuleValue, isSelected: Boolean, isEditing: Boolean) {
        this.value = value
        binding.name.text = value.name

        binding.root.isSelected = isSelected

        binding.root.setOnClickListener {
            val bundle = Bundle()

            bundle.putString(Res.resources.getString(R.string.uid), value.uid)
            binding.root.findNavController().navigate(
                R.id.action_nav_values_to_nav_rule_value,
                bundle
            )
        }
        binding.root.setOnLongClickListener {
            true
        }
    }
}