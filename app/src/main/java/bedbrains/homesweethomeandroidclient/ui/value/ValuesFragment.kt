package bedbrains.homesweethomeandroidclient.ui.value

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
import bedbrains.homesweethomeandroidclient.databinding.FragmentValuesBinding
import bedbrains.homesweethomeandroidclient.ui.Sorting
import bedbrains.homesweethomeandroidclient.ui.component.refresh
import bedbrains.homesweethomeandroidclient.ui.dialog.*
import bedbrains.shared.datatypes.rules.RuleValue

class ValuesFragment : Fragment() {

    private lateinit var binding: FragmentValuesBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var ruleValueListAdapter: RuleValueListAdapter
    private lateinit var tracker: SelectionTracker<String>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = FragmentValuesBinding.inflate(inflater)
        swipeRefreshLayout = binding.swipeRefreshLayout

        val values = binding.values
        val addButton = MainActivity.fab
        val linearLayoutManager = LinearLayoutManager(context)
        ruleValueListAdapter = RuleValueListAdapter(DataRepository.values.value!!)
        values.layoutManager = linearLayoutManager
        values.adapter = ruleValueListAdapter

        tracker = SelectionTracker.Builder(
            "rulesSelection",
            values,
            ruleValueListAdapter.KeyProvider(),
            RuleValueDetailsLookup(values),
            StorageStrategy.createStringStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()
        ruleValueListAdapter.tracker = tracker
        tracker.addObserver(object : SelectionTracker.SelectionObserver<String>() {
            override fun onSelectionChanged() {
                selectionChanged()
            }
        })

        DataRepository.values.observe(viewLifecycleOwner, Observer {
            ruleValueListAdapter.updateList(it)
        })

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.refresh(viewLifecycleOwner, context)
        }

        MainActivity.showFabDelayed()
        addButton.text = resources.getString(R.string.action_new)
        addButton.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_add_black_24dp)
        addButton.setOnClickListener {
            val bundle = Bundle()
            val newValue = RuleValue.UNSPECIFIED.apply {
                name = resources.getString(R.string.item_unnamed)
            }

            DataRepository.upsertValue(newValue)
            bundle.putString(Res.resources.getString(R.string.uid), newValue.uid)
            binding.root.findNavController().navigate(
                R.id.action_nav_values_to_nav_rule_value,
                bundle
            )
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.values, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> swipeRefreshLayout.refresh(viewLifecycleOwner, context)
            R.id.action_edit -> Unit//TODO
            R.id.action_sort_by -> showSortDialog()
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
            MainActivity.selectionToolbar.inflateMenu(R.menu.values_selection)

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
            getString(R.string.confirmation_1_value_delete)
        else
            getString(R.string.confirmation_n_values_delete).replace("$1", count.toString())

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val selectedUids = tracker.selection.toList()
                val selectedValues = DataRepository.values.value!!.filter { it.uid in selectedUids }

                DataRepository.removeValues(selectedValues)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun showRenameDialog() {
        val selectedUids = tracker.selection.toList()
        val selectedValues = DataRepository.values.value!!.filter { it.uid in selectedUids }

        val uniqueNames = selectedValues.map { it.name }.distinct()

        val name = if (uniqueNames.size > 1) "" else uniqueNames[0]
        val hint = if (uniqueNames.size > 1) getString(R.string.multiple_values) else ""
        val suggestions = DataRepository.values.value!!.map { it.name }.distinct()

        SuggestionInputDialog(requireContext())
            .title(R.string.action_rename)
            .text(name)
            .hint(hint)
            .suggestions(suggestions)
            .onFinished { newName ->
                DataRepository.updateValues(
                    selectedValues.map { it.also { it.name = newName } }
                )
                //ruleValueListAdapter.notifyDataSetChanged()
            }
            .show()
    }

    private fun showSortDialog() {
        val sortingCriterion = Res.getPrefString(R.string.pref_values_sorting_criterion_key, Sorting.DEFAULT_VALUE_CRITERION.name)
        val currentCriterion = Sorting.ValueCriterion.valueOf(sortingCriterion).ordinal
        val currentOrder = Res.getPrefBool(R.string.pref_values_sorting_order_key, Sorting.DEFAULT_ORDER)

        SortingDialog(requireContext())
            .sortingCriteriaRes(Sorting.ValueCriterion.values().map { it.resId }, currentCriterion)
            .ascending(currentOrder)
            .onFinished { criterion, ascending ->
                Res.putPrefString(R.string.pref_values_sorting_criterion_key, Sorting.ValueCriterion.values()[criterion].name)
                Res.putPrefBool(R.string.pref_values_sorting_order_key, ascending)

                ruleValueListAdapter.updateList(DataRepository.values.value!!)
            }
            .show()
    }
}