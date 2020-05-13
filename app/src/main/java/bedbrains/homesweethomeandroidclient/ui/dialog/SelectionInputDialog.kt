package bedbrains.homesweethomeandroidclient.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.SelectableBinding
import bedbrains.homesweethomeandroidclient.databinding.SelectionInputBinding
import bedbrains.homesweethomeandroidclient.ui.adapter.ListDiffUtilCallback

open class SelectionInputDialog<T>(context: Context) : InputDialog(context) {
    var options: List<Pair<String, T>> = listOf()
    var onFinishedSelection: (String, T) -> Unit = { _, _ -> Unit }
    var selectedValue: T? = null
    var selectedIndex: Int = -1

    protected var optionsBinding = SelectionInputBinding.inflate(LayoutInflater.from(context), binding.container, true)
    protected var displayedOptions: List<Pair<String, T>> = listOf()

    private val optionsAdapter = object : RecyclerView.Adapter<SelectableViewHolder<T>>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableViewHolder<T> {
            return SelectableViewHolder(
                context,
                SelectableBinding.inflate(LayoutInflater.from(context), optionsBinding.options, false)
            )
        }

        override fun getItemCount(): Int {
            return displayedOptions.size
        }

        override fun onBindViewHolder(holder: SelectableViewHolder<T>, position: Int) {
            val selected = displayedOptions[position].second == selectedValue
            val selectable = Selectable(
                displayedOptions[position].first,
                displayedOptions[position].second,
                selected
            ) { text, value ->
                selectedOption = text
                selectedValue = value
                positveButtonEnabled = true

                this.notifyDataSetChanged()
            }

            holder.bindView(selectable)
        }
    }

    init {
        binding.input.hint = Res.resources.getString(R.string.action_search)

        optionsBinding.options.layoutManager = LinearLayoutManager(context)
        optionsBinding.options.adapter = optionsAdapter

        focusAutomatically = false
    }

    override fun onCreate() {
        if (selectedIndex >= 0) {
            selectedOption = options[selectedIndex].first
            selectedValue = options[selectedIndex].second
        }

        displayOptions(options)
    }

    override fun onInput(input: String): Boolean {
        displayOptions(options)

        return selectedValue != null
    }

    override fun onFinished() {
        if (selectedValue != null) {
            super.onFinished()
            onFinishedSelection(selectedOption!!, selectedValue!!)
        }
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

        DiffUtil.calculateDiff(diff).dispatchUpdatesTo(optionsAdapter)

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
