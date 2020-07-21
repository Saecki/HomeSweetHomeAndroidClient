package bedbrains.homesweethomeandroidclient.ui.rule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.RuleWeeklyRuleBinding
import bedbrains.homesweethomeandroidclient.ui.Sorting
import bedbrains.homesweethomeandroidclient.ui.adapter.UniqueListAdapter
import bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule.WeeklyRuleViewHolder
import bedbrains.shared.datatypes.rules.Rule
import bedbrains.shared.datatypes.rules.WeeklyRule

class RuleListAdapter(rules: List<Rule>) : UniqueListAdapter<Rule>(rules) {

    var tracker: SelectionTracker<String>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            WeeklyRule.TYPE -> {
                val weeklyRuleBinding = RuleWeeklyRuleBinding.inflate(inflater, parent, false)
                WeeklyRuleViewHolder(weeklyRuleBinding)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val rule = list[position]

        tracker?.let {
            when (rule) {
                is WeeklyRule -> {
                    val weeklyRuleViewHolder = holder as WeeklyRuleViewHolder
                    weeklyRuleViewHolder.bind(rule, it.isSelected(rule.uid))
                }
            }
        }
    }

    override fun sortList(list: List<Rule>): List<Rule> {
        val sortingType = Res.getPrefString(R.string.pref_rules_sorting_criterion_key, Sorting.DEFAULT_RULE_CRITERION.name)

        val selector: (Rule) -> String = when (Sorting.RuleCriterion.valueOf(sortingType)) {
            Sorting.RuleCriterion.Manually -> return list
            Sorting.RuleCriterion.Name -> { rule -> rule.name }
        }

        val sortingOrder = Res.getPrefBool(R.string.pref_rules_sorting_order_key, Sorting.DEFAULT_ORDER)

        return when (sortingOrder) {
            Sorting.ASCENDING -> list.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, selector))
            else -> list.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER, selector))
        }
    }
}