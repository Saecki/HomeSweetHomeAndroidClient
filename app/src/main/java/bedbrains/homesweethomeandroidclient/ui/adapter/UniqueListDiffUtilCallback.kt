package bedbrains.homesweethomeandroidclient.ui.adapter

import bedbrains.shared.datatypes.Unique

class UniqueListDiffUtilCallback(private val old: List<Unique>, private val new: List<Unique>) : ListDiffUtilCallback<Unique>(old, new) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].uid == new[newItemPosition].uid
    }
}