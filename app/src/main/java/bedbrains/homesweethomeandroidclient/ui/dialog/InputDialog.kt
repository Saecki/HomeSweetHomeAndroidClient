package bedbrains.homesweethomeandroidclient.ui.dialog

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.core.view.postDelayed
import androidx.core.widget.addTextChangedListener
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.InputBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

object InputDialog {
    fun show(
        context: Context,
        title: Int,
        text: String = "",
        options: Set<String> = setOf(),
        invalidOptions: Set<String> = setOf(),
        inputType: Int = InputType.TYPE_CLASS_TEXT,
        validator: (String) -> Boolean = { true },
        onFinished: (String) -> Unit = {}
    ) {
        show(context, Res.resources.getString(title), text, options, invalidOptions, inputType, validator, onFinished)
    }

    fun show(
        context: Context,
        title: String,
        text: String = "",
        options: Set<String> = setOf(),
        invalidOptions: Set<String> = setOf(),
        inputType: Int = InputType.TYPE_CLASS_TEXT,
        validator: (String) -> Boolean = { true },
        onFinished: (String) -> Unit = {}
    ) {
        val dialog: AlertDialog
        val binding = InputBinding.inflate(LayoutInflater.from(context))
        val validOptions = options.filter { !invalidOptions.contains(it) }.toSet()
        var tag: String? = null

        binding.input.inputType = inputType
        binding.input.setText(text)
        binding.input.setSelection(text.length)

        displayOptions(context, validOptions, binding.options) {
            binding.input.setText(it)
        }

        dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                if (tag != null) {
                    onFinished(tag!!)
                }
            }
            .create()

        binding.input.addTextChangedListener {
            val text = it.toString()
            displayOptions(
                context,
                validOptions.filter { option -> option.startsWith(text, true) && option != text }.toSet(),
                binding.options
            ) {
                binding.input.setText(it)
            }

            tag = if (
                invalidOptions.contains(text) ||
                text.isBlank() ||
                !validator(text)
            ) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                null
            } else {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
                text
            }
        }

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

            binding.input.requestFocus()
            binding.input.postDelayed(Res.resources.getInteger(R.integer.keyboard_show_delay).toLong()) {
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .showSoftInput(binding.input, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        dialog.show()
    }

    private fun displayOptions(context: Context, options: Set<String>, chipGroup: ChipGroup, onSelected: (String) -> Unit) {
        val chips = chipGroup.children.map { it as Chip }.toList()
        val removed = chips.filter { chip -> options.none { tag -> chip.text == tag } }
        val added = options.filter { tag -> chips.none { chip -> chip.text == tag } }

        removed.forEach {
            chipGroup.removeView(it)
        }

        added.forEach { tag ->
            val chip = Chip(context)

            chip.text = tag
            chip.setOnClickListener {
                onSelected((it as Chip).text.toString())
            }
            chipGroup.addView(chip)
        }
    }
}