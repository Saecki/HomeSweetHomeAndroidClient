package bedbrains.homesweethomeandroidclient.ui.device

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.ui.device.heating.HeatingViewHolder
import bedbrains.homesweethomeandroidclient.ui.device.light.LightViewHolder
import java.io.InvalidClassException

class DeviceDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<String>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<String>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)

        if (view != null) {
            val viewHolder = recyclerView.getChildViewHolder(view)

            return when (viewHolder) {
                is LightViewHolder -> viewHolder.itemDetails
                is HeatingViewHolder -> viewHolder.itemDetails
                else -> throw InvalidClassException("Invalid class")
            }
        }

        return null
    }
}