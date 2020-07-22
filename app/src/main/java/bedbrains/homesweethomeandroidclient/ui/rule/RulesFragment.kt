package bedbrains.homesweethomeandroidclient.ui.rule

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.FragmentRulesBinding
import bedbrains.homesweethomeandroidclient.ui.Sorting
import bedbrains.homesweethomeandroidclient.ui.component.refresh
import bedbrains.homesweethomeandroidclient.ui.dialog.*
import bedbrains.platform.UIDProvider
import bedbrains.shared.datatypes.rules.WeeklyRule

class RulesFragment : Fragment() {
    private lateinit var binding: FragmentRulesBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var ruleListAdapter: RuleListAdapter
    private lateinit var tracker: SelectionTracker<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = FragmentRulesBinding.inflate(inflater)
        swipeRefreshLayout = binding.swipeRefreshLayout

        val rules = binding.rules
        val addButton = MainActivity.fab
        val linearLayoutManager = LinearLayoutManager(context)
        ruleListAdapter = RuleListAdapter(DataRepository.rules.value!!)
        rules.layoutManager = linearLayoutManager
        rules.adapter = ruleListAdapter

        tracker = SelectionTracker.Builder(
            "rulesSelection",
            rules,
            ruleListAdapter.KeyProvider(),
            RuleDetailsLookup(rules),
            StorageStrategy.createStringStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()
        ruleListAdapter.tracker = tracker
        tracker.addObserver(object : SelectionTracker.SelectionObserver<String>() {
            override fun onSelectionChanged() {
                selectionChanged()
            }
        })

        DataRepository.rules.observe(viewLifecycleOwner, Observer {
            ruleListAdapter.updateList(it)
        })

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.refresh(viewLifecycleOwner, context)
        }

        MainActivity.showFabDelayed()
        addButton.text = resources.getString(R.string.action_new)
        addButton.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_add_black_24dp)
        addButton.setOnClickListener {
            val bundle = Bundle()
            val newRule = WeeklyRule(
                UIDProvider.newUID,
                resources.getString(R.string.item_unnamed)
            )

            DataRepository.upsertRule(newRule)
            bundle.putString(Res.resources.getString(R.string.uid), newRule.uid)
            binding.root.findNavController().navigate(
                R.id.action_nav_rules_to_nav_weekly_rule,
                bundle
            )
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.rules, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> swipeRefreshLayout.refresh(viewLifecycleOwner, context)
            R.id.action_edit -> Unit//TODO
            R.id.action_sort_by -> showSortDialog()
            R.id.action_group_by -> Unit//TODO
            else -> return super.onOptionsItemSelected(item)
        }

        return false
    }

    private fun selectionChanged() {
        val count = tracker.selection.size()

        if (count == 0) {
            MainActivity.hideSelectionToolbar()
            return
        }

        if (!MainActivity.selecting) {
            MainActivity.showSelectionToolbar()
            MainActivity.selectionToolbar.inflateMenu(R.menu.rules_selection)

            MainActivity.selectionToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_delete -> showDeleteDialog()
                    R.id.action_rename -> showRenameDialog()
                }

                false
            }

            MainActivity.selectionToolbar.setNavigationOnClickListener {
                MainActivity.hideSelectionToolbar()
                tracker.clearSelection()
            }
        }

        MainActivity.setSelectedCount(count)
    }

    private fun showDeleteDialog() {
        val count = tracker.selection.size()
        val title = if (count == 1)
            getString(R.string.confirmation_1_rule_delete)
        else
            getString(R.string.confirmation_n_rules_delete).replace("$1", count.toString())

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val selectedUids = tracker.selection.toList()
                val deletedRules = DataRepository.rules.value!!.filter { it.uid in selectedUids }

                DataRepository.removeRules(deletedRules)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun showRenameDialog() {
        val selectedUids = tracker.selection.toList()
        val selectedRules = DataRepository.rules.value!!.filter { it.uid in selectedUids }

        val uniqueNames = selectedRules.map { it.name }.distinct()

        val name = if (uniqueNames.size > 1) "" else uniqueNames[0]
        val hint = if (uniqueNames.size > 1) getString(R.string.multiple_values) else ""
        val suggestions = DataRepository.rules.value!!.map { it.name }.distinct()

        SuggestionInputDialog(requireContext())
            .title(R.string.action_rename)
            .text(name)
            .hint(hint)
            .suggestions(suggestions)
            .onFinished { newName ->
                DataRepository.updateRules(
                    selectedRules.map { it.also { it.name = newName } }
                )
                //ruleListAdapter.notifyDataSetChanged()
            }
            .show()
    }

    private fun showSortDialog() {
        val sortingCriterion = Res.getPrefString(R.string.pref_rules_sorting_criterion_key, Sorting.DEFAULT_RULE_CRITERION.name)
        val currentCriterion = Sorting.RuleCriterion.valueOf(sortingCriterion).ordinal
        val currentOrder = Res.getPrefBool(R.string.pref_rules_sorting_order_key, Sorting.DEFAULT_ORDER)

        SortingDialog(requireContext())
            .sortingCriteriaRes(Sorting.RuleCriterion.values().map { it.resId }, currentCriterion)
            .ascending(currentOrder)
            .onFinished { criterion, ascending ->
                Res.putPrefString(R.string.pref_rules_sorting_criterion_key, Sorting.RuleCriterion.values()[criterion].name)
                Res.putPrefBool(R.string.pref_rules_sorting_order_key, ascending)

                ruleListAdapter.updateList(DataRepository.rules.value!!)
            }
            .show()
    }
}