package bedbrains.homesweethomeandroidclient.ui.value

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.shared.datatypes.rules.RuleValue

class RuleValueViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bindView(value: RuleValue) {
        val name = view.findViewById<TextView>(R.id.name)

        name.text = value.name
    }

}