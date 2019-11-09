package saecki.homesweethomeandroidclient.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import saecki.homesweethomeandroidclient.R
import saecki.homesweethomeandroidclient.ui.device.DeviceListAdapter

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val linearLayoutManager = LinearLayoutManager(context)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val devices: RecyclerView = root.findViewById(R.id.devices)
        val deviceListAdapter = DeviceListAdapter(homeViewModel.devices)

        devices.layoutManager = linearLayoutManager
        devices.adapter = deviceListAdapter

        return root
    }
}