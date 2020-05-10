package bedbrains.homesweethomeandroidclient.ui.dialog

import android.content.Context
import android.widget.LinearLayout
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

open class SuggestionInputDialog(context: Context) : BaseInputDialog(context) {
    var suggestions: List<String> = listOf()
    val suggestionGroup = ChipGroup(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            ChipGroup.LayoutParams.MATCH_PARENT,
            ChipGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreate(dialog: androidx.appcompat.app.AlertDialog) {
        binding.container.addView(suggestionGroup)
        displaySuggestions(suggestions)
    }

    override fun onInput(input: String): Boolean {
        displaySuggestions(suggestions)

        return super.onInput(input)
    }

    fun displaySuggestions(suggestions: List<String>) {
        val validSuggestions = suggestions
            .filter { it.startsWith(binding.input.text) }
            .filter(validator)

        val chips = suggestionGroup.children.map { it as Chip }.toList()
        val removed = chips.filter { chip -> validSuggestions.none { s -> chip.text == s } }
        val added = validSuggestions.filter { s -> chips.none { chip -> chip.text == s } }

        removed.forEach {
            suggestionGroup.removeView(it)
        }

        added.forEach { tag ->
            val chip = Chip(context)

            chip.text = tag
            chip.setOnClickListener {
                binding.input.setText((it as Chip).text.toString())
            }
            suggestionGroup.addView(chip)
        }
    }
}

fun <T : SuggestionInputDialog> T.suggestions(suggestions: List<String>): T {
    this.suggestions = suggestions
    return this
}
