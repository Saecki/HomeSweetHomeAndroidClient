package bedbrains.homesweethomeandroidclient.ui.value.rulevalue

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import bedbrains.homesweethomeandroidclient.databinding.RuleValueViewBinding
import bedbrains.shared.datatypes.rules.RuleValue

class RuleValueView(val binding: RuleValueViewBinding) {

    fun bind(value: RuleValue, onChange: (RuleValue) -> Unit) {
        binding.heating.text = value.heating.formatGlobal(true)
        binding.light.isChecked = value.light

        binding.heating.setOnClickListener {
        }

        binding.light.setOnClickListener {
            onChange(value.also {
                it.light = binding.light.isChecked
            })
        }
    }
}