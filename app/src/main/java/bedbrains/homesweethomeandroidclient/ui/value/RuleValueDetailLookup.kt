package bedbrains.homesweethomeandroidclient.ui.value;

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.ui.value.rulevalue.RuleValueViewHolder
import java.io.InvalidClassException

class RuleValueDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<String>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<String>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)

        if (view != null) {
            val viewHolder = recyclerView.getChildViewHolder(view)

            return when (viewHolder) {
                is RuleValueViewHolder -> viewHolder.itemDetails
                else -> throw InvalidClassException("Invalid class")
            }
        }

        return null
    }
}
