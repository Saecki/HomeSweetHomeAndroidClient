package bedbrains.homesweethomeandroidclient.ui.adapter

import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.State
import bedbrains.homesweethomeandroidclient.ui.view.ListItemView
import bedbrains.shared.datatypes.Unique

abstract class ListItemViewHolder<T : Unique>(val view: ListItemView) : RecyclerView.ViewHolder(view) {

    lateinit var value: T
    val itemDetails
        get() = object : ItemDetailsLookup.ItemDetails<String>() {
            override fun getSelectionKey(): String? = value.uid
            override fun getPosition(): Int = adapterPosition
        }

    fun bind(value: T, isSelected: Boolean = false, isEditing: Boolean = false) {
        this.value = value
        view.isSelected = isSelected
        view.isHandleVisible = isEditing

        onBind(value, isSelected, isEditing)
    }

    fun navigate(whatever: () -> Unit) {
        if (MainActivity.state.value == State.Default)
            whatever()
    }

    abstract fun onBind(value: T, isSelected: Boolean, isEditing: Boolean)
}
