package bedbrains.homesweethomeandroidclient.ui.adapter

import androidx.recyclerview.widget.DiffUtil

abstract class ListDiffUtilCallback<T>(private val old: List<T>, private val new: List<T>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == new[newItemPosition]
    }
}