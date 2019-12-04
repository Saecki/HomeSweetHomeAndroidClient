package bedbrains.homesweethomeandroidclient.ui.rules

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.ui.rule.RuleListAdapter

class RulesFragment : Fragment() {

    lateinit var rulesViewModel: RulesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        rulesViewModel = ViewModelProviders.of(this).get(RulesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_rules, container, false)
        val rules: RecyclerView = root.findViewById(R.id.rules)
        val linearLayoutManager = LinearLayoutManager(context)
        val ruleListAdapter = RuleListAdapter(rulesViewModel.rules)

        rules.layoutManager = linearLayoutManager
        rules.adapter = ruleListAdapter

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.rules, menu)
    }

}