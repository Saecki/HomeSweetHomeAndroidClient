package bedbrains.homesweethomeandroidclient.ui.value

import android.view.View
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.shared.datatypes.rules.RuleValue

class RuleValueViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    val name = view.findViewById<TextView>(R.id.name)

    fun bindView(value: RuleValue) {
        name.text = value.name
        view.setOnClickListener {
            view.findNavController().navigate(R.id.action_nav_values_to_nav_rule_value)
        }
    }

}