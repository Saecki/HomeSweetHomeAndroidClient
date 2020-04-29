package bedbrains.homesweethomeandroidclient.ui.value.rulevalue

import android.content.Context
import android.text.InputType
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.postDelayed
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.RuleValueViewBinding
import bedbrains.shared.datatypes.rules.RuleValue
import com.google.android.material.snackbar.Snackbar

class RuleValueView(private val binding: RuleValueViewBinding, private val context: Context?) {

    private lateinit var value: RuleValue
    private lateinit var onChange: (RuleValue) -> Unit

    fun bind(value: RuleValue, onChange: (RuleValue) -> Unit) {
        this.value = value
        this.onChange = onChange

        binding.heating.text = value.heating.formatGlobal(true)
        binding.light.isChecked = value.light

        binding.heating.setOnClickListener {
            showInputDialog(value.heating.formatGlobal(false))
        }

        binding.light.setOnCheckedChangeListener { _, isChecked ->
            onChange(value.also { it.light = isChecked })
        }
    }

    private fun showInputDialog(text: String) {
        val input = EditText(context)
        input.setText(text)
        input.inputType = InputType.TYPE_CLASS_PHONE

        AlertDialog.Builder(context!!)
            .setTitle(Res.resources.getString(R.string.pref_temperature_category_title))
            .setView(input)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                try {
                    onChange(value.apply {
                        heating.global = input.text.toString().replace(',', '.').toDouble()
                    })

                    binding.heating.text = value.heating.formatGlobal(true)
                } catch (e: Exception) {
                    Snackbar.make(
                        binding.root,
                        R.string.notification_no_number_recognized,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.action_edit) {
                            showInputDialog(input.text.toString())
                        }
                        .show()
                }
            }
            .show()

        input.requestFocusFromTouch()
        input.postDelayed(Res.resources.getInteger(R.integer.keyboard_show_delay).toLong()) {
            (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}