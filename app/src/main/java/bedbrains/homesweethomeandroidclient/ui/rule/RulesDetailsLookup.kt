package bedbrains.homesweethomeandroidclient.ui.rule

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule.WeeklyRuleViewHolder
import java.io.InvalidClassException

class RuleDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<String>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<String>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)

        if (view != null) {
            val viewHolder = recyclerView.getChildViewHolder(view)

                return when (viewHolder) {
                    is WeeklyRuleViewHolder -> viewHolder.itemDetails
                else -> throw InvalidClassException("Invalid class")
            }
        }

        return null
    }
}