package bedbrains.homesweethomeandroidclient.ui.value

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.ValueRuleValueBinding
import bedbrains.homesweethomeandroidclient.ui.Sorting
import bedbrains.homesweethomeandroidclient.ui.adapter.ListItemViewHolder
import bedbrains.homesweethomeandroidclient.ui.adapter.UniqueListAdapter
import bedbrains.homesweethomeandroidclient.ui.value.rulevalue.RuleValueViewHolder
import bedbrains.shared.datatypes.rules.RuleValue

class RuleValueListAdapter(values: List<RuleValue>) : UniqueListAdapter<RuleValue>(values) {

    var tracker: SelectionTracker<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder<RuleValue> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ValueRuleValueBinding.inflate(inflater, parent, false)

        return RuleValueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder<RuleValue>, position: Int) {
        tracker?.let {
            val value = list[position]

            (holder as RuleValueViewHolder).bind(value, it.isSelected(value.uid))
        }
    }

    override fun sortList(list: List<RuleValue>): List<RuleValue> {
        val sortingType = Res.getPrefString(R.string.pref_values_sorting_criterion_key, Sorting.DEFAULT_VALUE_CRITERION.name)

        val selector: (RuleValue) -> String = when (Sorting.ValueCriterion.valueOf(sortingType)) {
            Sorting.ValueCriterion.Manually -> return list
            Sorting.ValueCriterion.Name -> { value -> value.name }
        }

        val sortingOrder = Res.getPrefBool(R.string.pref_values_sorting_order_key, Sorting.DEFAULT_ORDER)

        return when (sortingOrder) {
            Sorting.ASCENDING -> list.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, selector))
            else -> list.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER, selector))
        }
    }
}