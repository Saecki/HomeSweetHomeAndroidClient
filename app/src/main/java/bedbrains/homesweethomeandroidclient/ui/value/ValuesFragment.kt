package bedbrains.homesweethomeandroidclient.ui.value

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.FragmentValuesBinding
import bedbrains.homesweethomeandroidclient.ui.component.refresh
import bedbrains.shared.datatypes.rules.RuleValue

class ValuesFragment : Fragment() {

    private val valuesViewModel: ValuesViewModel by viewModels()
    private lateinit var binding: FragmentValuesBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = FragmentValuesBinding.inflate(inflater)
        swipeRefreshLayout = binding.swipeRefreshLayout

        val values = binding.values
        val addButton = binding.addButton
        val linearLayoutManager = LinearLayoutManager(context)
        val ruleValueListAdapter = RuleValueListAdapter(valuesViewModel.values.value!!)

        values.layoutManager = linearLayoutManager
        values.adapter = ruleValueListAdapter

        valuesViewModel.values.observe(viewLifecycleOwner, Observer {
            ruleValueListAdapter.updateValues(it)
        })

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.refresh(viewLifecycleOwner, context)
        }

        addButton.setOnClickListener {
            val bundle = Bundle()
            val newValue = RuleValue.UNSPECIFIED.apply {
                name = resources.getString(R.string.item_untitled)
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
            R.id.action_sort_by -> Unit//TODO
            else -> return super.onOptionsItemSelected(item)
        }

        return false
    }
}