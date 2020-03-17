package bedbrains.homesweethomeandroidclient.ui.value

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.ValueRuleValueBinding
import bedbrains.shared.datatypes.rules.RuleValue

class RuleValueViewHolder(private val viewBinding: ValueRuleValueBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    val name = viewBinding.name

    fun bindView(value: RuleValue) {
        name.text = value.name
        viewBinding.root.setOnClickListener {
            viewBinding.root.findNavController().navigate(R.id.action_nav_values_to_nav_rule_value)
        }
    }

}