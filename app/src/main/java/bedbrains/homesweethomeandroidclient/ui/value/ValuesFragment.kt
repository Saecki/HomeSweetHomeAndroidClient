package bedbrains.homesweethomeandroidclient.ui.value

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.FragmentValuesBinding

class ValuesFragment : Fragment() {

    private val valuesViewModel: ValuesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        val binding = FragmentValuesBinding.inflate(inflater)
        val values = binding.values
        val addButton = binding.addButton
        val linearLayoutManager = LinearLayoutManager(context)
        val ruleValueListAdapter = RuleValueListAdapter(valuesViewModel.values)

        values.layoutManager = linearLayoutManager
        values.adapter = ruleValueListAdapter

        addButton.setOnClickListener {
            addButton.findNavController().navigate(R.id.action_nav_values_to_nav_rule_value)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.values, menu)
    }

}