package bedbrains.homesweethomeandroidclient.ui.value.rulevalue

import android.content.Context
import android.text.InputType
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.RuleValueViewBinding
import bedbrains.homesweethomeandroidclient.ui.dialog.*
import bedbrains.shared.datatypes.rules.RuleValue

class RuleValueView(private val binding: RuleValueViewBinding, private val context: Context) {

    private lateinit var value: RuleValue
    private lateinit var onChange: (RuleValue) -> Unit

    fun bind(value: RuleValue, onChange: (RuleValue) -> Unit) {
        this.value = value
        this.onChange = onChange

        binding.heating.text = value.heating.formatGlobal(true)
        binding.light.isChecked = value.light

        binding.heating.setOnClickListener {
            showHeatingDialog(value.heating.formatGlobal(false))
        }

        binding.light.setOnCheckedChangeListener { _, isChecked ->
            onChange(value.apply { light = isChecked })
        }
    }

    private fun showHeatingDialog(text: String) {
        InputDialog(context)
            .title(R.string.temperature)
            .text(text)
            .inputType(InputType.TYPE_CLASS_PHONE)
            .validator { it.replace(',', '.').toDoubleOrNull() != null }
            .onFinished {
                onChange(value.apply {
                    heating.global = it.replace(',', '.').toDouble()
                })
                binding.heating.text = value.heating.formatGlobal(true)
            }
            .show()
    }
}