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

open class BaseInputDialog(protected val context: Context) {

    var title = ""
    var inputType = InputType.TYPE_CLASS_TEXT
    var initialText = ""
    var validator: (String) -> Boolean = { true }
    var onFinished: (String) -> Unit = {}
    var selectedOption: String? = null

    protected val binding: InputBinding = InputBinding.inflate(LayoutInflater.from(context))
    protected val dialogBuilder = AlertDialog.Builder(context)
        .setView(binding.root)
        .setPositiveButton(android.R.string.ok) { _, _ ->
            if (selectedOption != null) onFinished(selectedOption!!)
        }

    protected lateinit var dialog: AlertDialog

    fun create(): AlertDialog {
        binding.input.setText(initialText)
        binding.input.inputType = inputType
        binding.input.setSelection(initialText.length)
        binding.input.addTextChangedListener {
            val input = it.toString()
            val valid = onInput(input)

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = valid
            selectedOption = when (valid) {
                true -> input
                false -> null
            }
        }

        dialog = dialogBuilder.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

            binding.input.requestFocus()
            binding.input.postDelayed(Res.resources.getInteger(R.integer.keyboard_show_delay).toLong()) {
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .showSoftInput(binding.input, InputMethodManager.SHOW_IMPLICIT)
            }
        }
        onCreate()

        return dialog
    }

    fun show() {
        create().show()
    }

    protected open fun onCreate() {}

    protected open fun onInput(input: String): Boolean {
        return input.isNotBlank() && validator(input) && input != initialText
    }
}

fun <T : BaseInputDialog> T.title(title: Int): T {
    this.title = Res.resources.getString(title)
    return this
}

fun <T : BaseInputDialog> T.title(title: String): T {
    this.title = title
    return this
}

fun <T : BaseInputDialog> T.text(text: CharSequence): T {
    this.initialText = text.toString()
    return this
}

fun <T : BaseInputDialog> T.inputType(inputType: Int): T {
    this.inputType = inputType
    return this
}

fun <T : BaseInputDialog> T.validator(validator: (String) -> Boolean): T {
    this.validator = validator
    return this
}

fun <T : BaseInputDialog> T.onFinished(onFinished: (String) -> Unit): T {
    this.onFinished = onFinished
    return this
}
