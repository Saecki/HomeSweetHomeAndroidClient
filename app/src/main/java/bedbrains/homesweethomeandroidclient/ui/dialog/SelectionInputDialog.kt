package bedbrains.homesweethomeandroidclient.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import bedbrains.homesweethomeandroidclient.databinding.SelectionInputBinding
import bedbrains.homesweethomeandroidclient.ui.adapter.ListDiffUtilCallback

open class SelectionInputDialog<T>(context: Context) : BaseInputDialog(context) {
    var options: List<Pair<String, T>> = listOf()
    var onFinishedSelection: (String, T) -> Unit = { _, _ -> Unit }
    var selectedValue: T? = null
    var selectedIndex: Int = -1

    protected var optionsBinding = SelectionInputBinding.inflate(LayoutInflater.from(context))
    protected var displayedOptions: List<Pair<String, T>> = listOf()

    init {
        binding.container.addView(optionsBinding.root)
    }

    override fun onCreate() {
        if (selectedIndex != -1) {
            selectedOption = options[selectedIndex].first
            selectedValue = options[selectedIndex].second
        }

        displayOptions(options)
    }

    override fun onInput(input: String): Boolean {
        displayOptions(options)

        return selectedValue != null
    }

    protected open fun displayOptions(options: List<Pair<String, T>>) {
        val old = displayedOptions
        val new = options
            .filter { it.first.startsWith(binding.input.text, true) }

        val diff = object : ListDiffUtilCallback<Pair<String, T>>(old, new) {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return old[oldItemPosition] == new[newItemPosition]
            }
        }

        TODO()

        displayedOptions = new
    }
}

fun <T : SelectionInputDialog<V>, V> T.options(options: List<Pair<String, V>>): T {
    this.options = options
    return this
}

fun <T : SelectionInputDialog<V>, V> T.selection(index: Int): T {
    this.selectedIndex = index
    return this
}

fun <T : SelectionInputDialog<V>, V> T.selection(selectedOption: String, selectedValue: V): T {
    this.selectedOption = selectedOption
    this.selectedValue = selectedValue
    return this
}

fun <T : SelectionInputDialog<V>, V> T.onFinishedSelection(onFinishedSelection: (String, V) -> Unit): T {
    this.onFinishedSelection = onFinishedSelection
    return this
}
