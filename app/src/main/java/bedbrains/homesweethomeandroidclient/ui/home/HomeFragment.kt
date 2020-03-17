package bedbrains.homesweethomeandroidclient.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.FragmentHomeBinding
import bedbrains.homesweethomeandroidclient.ui.device.DeviceListAdapter

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        val linearLayoutManager = LinearLayoutManager(context)
        val binding = FragmentHomeBinding.inflate(inflater)
        val devices = binding.devices
        val deviceListAdapter = DeviceListAdapter(homeViewModel.devices.value!!)

        homeViewModel.devices.observe(viewLifecycleOwner, Observer {
            deviceListAdapter.updateDevices(it)
        })

        devices.layoutManager = linearLayoutManager
        devices.adapter = deviceListAdapter

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
    }
}