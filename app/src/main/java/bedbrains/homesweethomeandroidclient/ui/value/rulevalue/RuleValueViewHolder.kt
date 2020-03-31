package bedbrains.homesweethomeandroidclient.ui.value.rulevalue

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.ValueRuleValueBinding
import bedbrains.shared.datatypes.rules.RuleValue

class RuleValueViewHolder(private val viewBinding: ValueRuleValueBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    lateinit var value: RuleValue
    val name = viewBinding.name

    fun bindView(value: RuleValue) {
        this.value = value
        name.text = value.name

        viewBinding.root.setOnClickListener {
            val bundle = Bundle()

            bundle.putString(MainActivity.res.getString(R.string.uid), value.uid)
            viewBinding.root.findNavController().navigate(
                R.id.action_nav_values_to_nav_rule_value,
                bundle
            )
        }
    }

}