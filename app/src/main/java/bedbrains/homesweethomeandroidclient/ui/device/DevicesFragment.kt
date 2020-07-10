package bedbrains.homesweethomeandroidclient.ui.device

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.FragmentDevicesBinding
import bedbrains.homesweethomeandroidclient.ui.Sorting
import bedbrains.homesweethomeandroidclient.ui.component.refresh
import bedbrains.homesweethomeandroidclient.ui.dialog.SortingDialog
import bedbrains.homesweethomeandroidclient.ui.dialog.ascending
import bedbrains.homesweethomeandroidclient.ui.dialog.onFinished
import bedbrains.homesweethomeandroidclient.ui.dialog.sortingCriteriaRes

class DevicesFragment : Fragment() {

    private lateinit var binding: FragmentDevicesBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var deviceListAdapter: DeviceListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        binding = FragmentDevicesBinding.inflate(inflater)
        swipeRefreshLayout = binding.swipeRefreshLayout

        val linearLayoutManager = LinearLayoutManager(context)
        val devices = binding.devices
        deviceListAdapter = DeviceListAdapter(DataRepository.devices.value!!)
        devices.layoutManager = linearLayoutManager
        devices.adapter = deviceListAdapter

        val tracker = SelectionTracker.Builder(
            "deviceSelection",
            devices,
            deviceListAdapter.KeyProvider(),
            DeviceDetailsLookup(devices),
            StorageStrategy.createStringStorage()
        )
            .withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
            )
            .build()
        deviceListAdapter.tracker = tracker

        DataRepository.devices.observe(viewLifecycleOwner, Observer {
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
            R.id.action_sort_by -> showSortMenu()
            R.id.action_group_by -> Unit//TODO
            R.id.action_filter_by -> Unit//TODO
            else -> return super.onOptionsItemSelected(item)
        }

        return false
    }

    fun showSortMenu() {
        val sortingCriterion = Res.getPrefString(R.string.pref_devices_sorting_criterion_key, Sorting.DEFAULT_DEVICE_CRITERION.name)
        val currentCriterion = Sorting.DeviceCriterion.valueOf(sortingCriterion).ordinal
        val currentOrder = Res.getPrefBool(R.string.pref_devices_sorting_order_key, Sorting.DEFAULT_ORDER)
        Log.d("TESTING", "shown - criterion: $currentCriterion ascending: $currentOrder")

        SortingDialog(requireContext())
            .sortingCriteriaRes(Sorting.DeviceCriterion.values().map { it.resId }, currentCriterion)
            .ascending(currentOrder)
            .onFinished { criterion, ascending ->
                Log.d("TESTING", "finished - criterion: $criterion ascending: $ascending")
                Res.putPrefString(R.string.pref_devices_sorting_criterion_key, Sorting.DeviceCriterion.values()[criterion].name)
                Res.putPrefBool(R.string.pref_devices_sorting_order_key, ascending)

                deviceListAdapter.updateDevices(DataRepository.devices.value!!)
            }
            .show()
    }
}