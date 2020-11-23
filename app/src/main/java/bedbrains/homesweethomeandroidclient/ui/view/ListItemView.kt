package bedbrains.homesweethomeandroidclient.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.setPadding
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res

class ListItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    val handle: ImageView

    var isHandleVisible: Boolean = false
        set(value) {
            if (value) handle.visibility = View.VISIBLE
            else handle.visibility = View.GONE

            field = value
        }

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setBackgroundResource(R.drawable.item_background)

        handle = ImageView(context).apply {
            setImageResource(R.drawable.ic_drag_handle_black_24dp)
            setColorFilter(Res.getAttrColor(R.attr.colorOnBackground))
            setPadding(resources.getDimensionPixelSize(R.dimen.icon_padding))
            visibility = View.GONE
        }
        addView(handle)
    }
}
