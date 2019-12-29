package bedbrains.homesweethomeandroidclient.ui.value

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.shared.datatypes.rules.RuleValue

class RuleValueListAdapter(private var values: List<RuleValue>) : RecyclerView.Adapter<RuleValueViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuleValueViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.value_rule_value, parent, false)
        return RuleValueViewHolder(view)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: RuleValueViewHolder, position: Int) {
        holder.bindView(values[position])
    }
}