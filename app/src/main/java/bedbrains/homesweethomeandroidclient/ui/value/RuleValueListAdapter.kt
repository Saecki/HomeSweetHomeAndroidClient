package bedbrains.homesweethomeandroidclient.ui.value

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.databinding.ValueRuleValueBinding
import bedbrains.shared.datatypes.rules.RuleValue

class RuleValueListAdapter(private var values: List<RuleValue>) : RecyclerView.Adapter<RuleValueViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuleValueViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ValueRuleValueBinding.inflate(inflater, parent, false)
        return RuleValueViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: RuleValueViewHolder, position: Int) {
        holder.bindView(values[position])
    }
}