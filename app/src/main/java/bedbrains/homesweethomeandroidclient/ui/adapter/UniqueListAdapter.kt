package bedbrains.homesweethomeandroidclient.ui.adapter

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import bedbrains.shared.datatypes.Unique

abstract class UniqueListAdapter<T : Unique>(list: List<T>) :
    RecyclerView.Adapter<ListItemViewHolder<T>>(),
    ItemMoveCallback.Listener {

    private var listValid = false
    private var cachedList: List<T> = list
    protected var list = list
        get() {
            if (listValid) return cachedList

            cachedList = sortList(field)
            listValid = true

            return cachedList
        }
        set(value) {
            listValid = false
            field = value
        }

    inner class KeyProvider : ItemKeyProvider<String>(SCOPE_MAPPED) {
        override fun getKey(position: Int): String? {
            return list[position].uid
        }

        override fun getPosition(key: String): Int {
            val position = list.indexOfFirst { it.uid == key }

            return if (position == -1)
                RecyclerView.NO_POSITION
            else
                position
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onMove(from: Int, to: Int) {
        val mList = cachedList.toMutableList()
        val movedItem = mList.removeAt(from)

        if (from < to) mList.add(to - 1, movedItem)
        else mList.add(to, movedItem)

        cachedList = mList
    }

    fun updateList(new: List<T>) {
        val newSorted = sortList(new)
        val diff = DiffUtil.calculateDiff(UniqueListDiffUtilCallback(cachedList, newSorted))

        list = new
        diff.dispatchUpdatesTo(this)
    }

    protected abstract fun sortList(list: List<T>): List<T>
}