package bedbrains.homesweethomeandroidclient.ui.dialog

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.view.postDelayed
import androidx.core.widget.addTextChangedListener
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.InputBinding

open class InputDialog(context: Context) : BaseDialog(context) {

    var inputType = InputType.TYPE_CLASS_TEXT
    var initialText: CharSequence = ""
    var hint: CharSequence = ""
    var validator: (String) -> Boolean = { true }
    var onFinished: (String) -> Unit = {}
    var selectedOption: String? = null
    var focusAutomatically = true

    protected val binding: InputBinding = InputBinding.inflate(LayoutInflater.from(context))

    init {
        dialogBuilder
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                if (selectedOption != null) onFinished()
            }
            .setNegativeButton(android.R.string.cancel, null)
    }

    override fun onCreate() {
        binding.input.setText(initialText)
        binding.input.hint = hint
        binding.input.inputType = inputType
        binding.input.setSelection(initialText.length)
        binding.input.addTextChangedListener {
            val input = it.toString()
            val valid = onInput(input)

            positveButtonEnabled = valid
            selectedOption = when (valid) {
                true -> input
                false -> null
            }
        }
    }

    override fun onShow() {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

        if (focusAutomatically) {
            binding.input.requestFocus()
            binding.input.postDelayed(Res.resources.getInteger(R.integer.keyboard_show_delay).toLong()) {
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .showSoftInput(binding.input, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    protected open fun onInput(input: String): Boolean {
        return input.isNotBlank() && validator(input) && input != initialText
    }

    protected open fun onFinished() {
        onFinished(selectedOption!!)
    }
}

fun <T : InputDialog> T.text(text: CharSequence): T {
    this.initialText = text
    return this
}

fun <T : InputDialog> T.hint(hint: CharSequence): T {
    this.hint = hint
    return this
}

fun <T : InputDialog> T.inputType(inputType: Int): T {
    this.inputType = inputType
    return this
}

fun <T : InputDialog> T.validator(validator: (String) -> Boolean): T {
    this.validator = validator
    return this
}

fun <T : InputDialog> T.onFinished(onFinished: (String) -> Unit): T {
    this.onFinished = onFinished
    return this
}

fun <T : InputDialog> T.focusAutomatically(focus: Boolean): T {
    this.focusAutomatically = focus
    return this
}
