package bedbrains.homesweethomeandroidclient.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.core.view.children
import androidx.core.view.forEachIndexed
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import bedbrains.homesweethomeandroidclient.databinding.SuggestionInputBinding
import bedbrains.homesweethomeandroidclient.ui.adapter.ListDiffUtilCallback
import com.google.android.material.chip.Chip

open class SuggestionInputDialog(context: Context) : InputDialog(context) {
    var suggestions: List<String> = listOf()

    protected val suggestionBinding = SuggestionInputBinding.inflate(LayoutInflater.from(context))

    init {
        binding.container.addView(suggestionBinding.root)
    }

    override fun onCreate() {
        super.onCreate()

        displaySuggestions(suggestions)
    }

    override fun onInput(input: String): Boolean {
        displaySuggestions(suggestions)

        return super.onInput(input)
    }

    protected open fun displaySuggestions(suggestions: List<String>) {
        val old =
            suggestionBinding.suggestions.children.map { (it as Chip).text.toString() }.toList()
        val new = suggestions
            .filter { it.startsWith(binding.input.text, true) }
            .filter(validator)

        val diff = object : ListDiffUtilCallback<String>(old, new) {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return old[oldItemPosition] == new[newItemPosition]
            }
        }

        val update = object : ListUpdateCallback {
            override fun onChanged(position: Int, count: Int, payload: Any?) {}

            override fun onMoved(fromPosition: Int, toPosition: Int) {}

            override fun onInserted(position: Int, count: Int) {
                for (i in 0 until count) {
                    val chip = Chip(context)

                    chip.setOnClickListener {
                        binding.input.setText((it as Chip).text.toString())
                    }
                    suggestionBinding.suggestions.addView(chip)
                }
            }

            override fun onRemoved(position: Int, count: Int) {
                for (i in 0 until count) suggestionBinding.suggestions.removeViewAt(position)
            }
        }

        DiffUtil.calculateDiff(diff).dispatchUpdatesTo(update)

        suggestionBinding.suggestions.forEachIndexed { i, v ->
            (v as Chip).text = new[i]
        }
    }
}

fun <T : SuggestionInputDialog> T.suggestions(suggestions: List<String>): T {
    this.suggestions = suggestions
    return this
}
