package bedbrains.homesweethomeandroidclient.ui.device

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.FragmentDevicesBinding
import bedbrains.homesweethomeandroidclient.rest.Resp

class DevicesFragment : Fragment() {

    private val homeViewModel: DevicesViewModel by viewModels()
    private lateinit var binding: FragmentDevicesBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
            Log.d("TESTING", "updating devices")
        })

        swipeRefreshLayout.setOnRefreshListener {
            DataRepository.updateDevices().observe(viewLifecycleOwner, Observer {
                if (it == Resp.FAILURE) {
                    Toast.makeText(context, R.string.resp_update_error, Toast.LENGTH_SHORT).show()
                }
                swipeRefreshLayout.isRefreshing = false
            })
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                swipeRefreshLayout.isRefreshing = true
                DataRepository.updateDevices().observe(viewLifecycleOwner, Observer {
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
            R.id.action_group_by -> {
                //TODO
            }
            R.id.action_filter_by -> {
                //TODO
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return false
    }
}