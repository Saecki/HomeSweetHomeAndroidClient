package bedbrains.homesweethomeandroidclient.ui.device

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.FragmentDevicesBinding
import bedbrains.homesweethomeandroidclient.ui.component.refresh

class DevicesFragment : Fragment() {

    private val homeViewModel: DevicesViewModel by viewModels()
    private lateinit var binding: FragmentDevicesBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = FragmentDevicesBinding.inflate(inflater)
        swipeRefreshLayout = binding.swipeRefreshLayout

        val linearLayoutManager = LinearLayoutManager(context)
        val devices = binding.devices
        val deviceListAdapter = DeviceListAdapter(homeViewModel.devices.value!!)

        devices.layoutManager = linearLayoutManager
        devices.adapter = deviceListAdapter

        homeViewModel.devices.observe(viewLifecycleOwner, Observer {
            deviceListAdapter.updateDevices(it)
        })

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.refresh(viewLifecycleOwner, context)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.devices, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> swipeRefreshLayout.refresh(viewLifecycleOwner, context)
            R.id.action_edit -> Unit//TODO
            R.id.action_sort_by -> Unit//TODO
            R.id.action_group_by -> Unit//TODO
            R.id.action_filter_by -> Unit//TODO
            else -> return super.onOptionsItemSelected(item)
        }

        return false
    }
}