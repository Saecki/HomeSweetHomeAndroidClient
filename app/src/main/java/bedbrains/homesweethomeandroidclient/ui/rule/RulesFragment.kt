package bedbrains.homesweethomeandroidclient.ui.rule

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.FragmentRulesBinding

class RulesFragment : Fragment() {

    private val rulesViewModel: RulesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        val binding = FragmentRulesBinding.inflate(inflater)
        val rules = binding.rules
        val addButton = binding.addButton
        val linearLayoutManager = LinearLayoutManager(context)
        val ruleListAdapter = RuleListAdapter(rulesViewModel.rules)

        rules.layoutManager = linearLayoutManager
        rules.adapter = ruleListAdapter

        addButton.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_nav_rules_to_nav_weekly_rule)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.rules, menu)
    }

}