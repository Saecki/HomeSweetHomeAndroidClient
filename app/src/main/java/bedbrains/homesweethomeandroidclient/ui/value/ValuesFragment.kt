package bedbrains.homesweethomeandroidclient.ui.value

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.FragmentValuesBinding
import bedbrains.homesweethomeandroidclient.rest.Resp

class ValuesFragment : Fragment() {

    private val valuesViewModel: ValuesViewModel by viewModels()
    private lateinit var binding: FragmentValuesBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
            DataRepository.updateValues().observe(viewLifecycleOwner, Observer {
                if (it == Resp.FAILURE) {
                    Toast.makeText(context, R.string.resp_update_error, Toast.LENGTH_SHORT).show()
                }
                swipeRefreshLayout.isRefreshing = false
            })
        }

        addButton.setOnClickListener {
            addButton.findNavController().navigate(R.id.action_nav_values_to_nav_rule_value)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.values, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                swipeRefreshLayout.isRefreshing = true
                DataRepository.updateValues().observe(viewLifecycleOwner, Observer {
                    if (it == Resp.FAILURE) {
                        Toast.makeText(context, R.string.resp_update_error, Toast.LENGTH_SHORT).show()
                    }
                    swipeRefreshLayout.isRefreshing = false
                })
            }
            R.id.action_edit -> {
                //TODO
            }
            R.id.action_sort_by -> {
                //TODO
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return false
    }
}