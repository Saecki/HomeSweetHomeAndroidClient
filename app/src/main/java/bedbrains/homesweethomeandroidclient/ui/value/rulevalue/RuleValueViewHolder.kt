package bedbrains.homesweethomeandroidclient.ui.value.rulevalue

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.ValueRuleValueBinding
import bedbrains.shared.datatypes.rules.RuleValue

class RuleValueViewHolder(private val binding: ValueRuleValueBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var value: RuleValue
    val name = binding.name

    val itemDetails
        get() = object : ItemDetailsLookup.ItemDetails<String>() {
            override fun getSelectionKey(): String? = value.uid
            override fun getPosition(): Int = adapterPosition
        }

    fun bind(value: RuleValue, isSelected: Boolean = false) {
        this.value = value
        name.text = value.name

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