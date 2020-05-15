package bedbrains.homesweethomeandroidclient.ui.rule

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.FragmentRulesBinding
import bedbrains.homesweethomeandroidclient.ui.component.refresh
import bedbrains.platform.UIDProvider
import bedbrains.shared.datatypes.rules.WeeklyRule

class RulesFragment : Fragment() {

    private val rulesViewModel: RulesViewModel by viewModels()
    private lateinit var binding: FragmentRulesBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

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
        val ruleListAdapter = RuleListAdapter(rulesViewModel.rules.value!!)

        rules.layoutManager = linearLayoutManager
        rules.adapter = ruleListAdapter

        rulesViewModel.rules.observe(viewLifecycleOwner, Observer {
            ruleListAdapter.updateRules(it)
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
            R.id.action_sort_by -> Unit//TODO
            R.id.action_group_by -> Unit//TODO
            else -> return super.onOptionsItemSelected(item)
        }

        return false
    }
}