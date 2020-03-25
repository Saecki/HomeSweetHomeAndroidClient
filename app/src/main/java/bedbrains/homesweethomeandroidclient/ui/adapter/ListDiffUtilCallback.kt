package bedbrains.homesweethomeandroidclient.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import bedbrains.shared.datatypes.Unique

class ListDiffUtilCallback(private val old: List<Unique>, private val new: List<Unique>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].uid.equals(new[newItemPosition].uid)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == new[newItemPosition]
    }
}