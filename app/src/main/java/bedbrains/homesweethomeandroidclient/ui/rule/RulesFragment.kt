package bedbrains.homesweethomeandroidclient.ui.rule

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RulesFragment : Fragment() {

    lateinit var rulesViewModel: RulesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        rulesViewModel = ViewModelProviders.of(this).get(RulesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_rules, container, false)
        val rules: RecyclerView = root.findViewById(R.id.rules)
        val linearLayoutManager = LinearLayoutManager(context)
        val ruleListAdapter = RuleListAdapter(rulesViewModel.rules)
        val addButton = root.findViewById<FloatingActionButton>(R.id.add_button)

        rules.layoutManager = linearLayoutManager
        rules.adapter = ruleListAdapter

        addButton.setOnClickListener {
            root.findNavController().navigate(R.id.action_nav_rules_to_nav_weekly_rule)
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.rules, menu)
    }

}