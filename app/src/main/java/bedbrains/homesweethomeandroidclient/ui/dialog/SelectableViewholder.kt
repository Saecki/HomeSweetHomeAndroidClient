package bedbrains.homesweethomeandroidclient.ui.dialog

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.SelectableBinding

class SelectableViewHolder<T>(
    private var context: Context,
    private var binding: SelectableBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var selectable: Selectable<T>

    fun bindView(selectable: Selectable<T>) {
        this.selectable = selectable

        binding.text.text = selectable.text
        if (selectable.selected) {
            binding.root.background = ContextCompat.getDrawable(context, R.drawable.item_background_selected)
        } else {
            binding.root.background = null
        }

        binding.root.setOnClickListener {
            selectable.callback(selectable.text, selectable.value)
        }
    }
}