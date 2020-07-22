package bedbrains.homesweethomeandroidclient.ui.device

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.FragmentDevicesBinding
import bedbrains.homesweethomeandroidclient.ui.Sorting
import bedbrains.homesweethomeandroidclient.ui.component.refresh
import bedbrains.homesweethomeandroidclient.ui.dialog.*

class DevicesFragment : Fragment() {
    private lateinit var binding: FragmentDevicesBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var deviceListAdapter: DeviceListAdapter
    private lateinit var tracker: SelectionTracker<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        binding = FragmentDevicesBinding.inflate(inflater)
        swipeRefreshLayout = binding.swipeRefreshLayout

        val linearLayoutManager = LinearLayoutManager(context)
        val devices = binding.devices
        deviceListAdapter = DeviceListAdapter(DataRepository.devices.value!!)
        devices.layoutManager = linearLayoutManager
        devices.adapter = deviceListAdapter

        tracker = SelectionTracker.Builder(
            "devicesSelection",
            devices,
            deviceListAdapter.KeyProvider(),
            DeviceDetailsLookup(devices),
            StorageStrategy.createStringStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()
        deviceListAdapter.tracker = tracker
        tracker.addObserver(object : SelectionTracker.SelectionObserver<String>() {
            override fun onSelectionChanged() {
                selectionChanged()
            }
        })

        DataRepository.devices.observe(viewLifecycleOwner, Observer {
            deviceListAdapter.updateList(it)
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
            R.id.action_sort_by -> showSortDialog()
            R.id.action_group_by -> Unit//TODO
            R.id.action_filter_by -> Unit//TODO
            else -> return super.onOptionsItemSelected(item)
        }

        return false
    }

    private fun selectionChanged() {
        val count = tracker.selection.size()

        if (count == 0) {
            MainActivity.hideSelectionToolbar()
            return
        }

        if (!MainActivity.selecting) {
            MainActivity.showSelectionToolbar()
            MainActivity.selectionToolbar.inflateMenu(R.menu.devices_selection)

            MainActivity.selectionToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_rename -> showRenameDialog()
                    R.id.action_change_room -> showChangeRoomDialog()
                }

                false
            }

            MainActivity.selectionToolbar.setNavigationOnClickListener {
                MainActivity.hideSelectionToolbar()
                tracker.clearSelection()
            }
        }

        MainActivity.setSelectedCount(count)
    }

    private fun showRenameDialog() {
        val selectedUids = tracker.selection.toList()
        val selectedDevices = DataRepository.devices.value!!.filter { it.uid in selectedUids }
        val uniqueNames = selectedDevices.map { it.name }.distinct()

        val name = if (uniqueNames.size > 1) "" else uniqueNames[0]
        val hint = if (uniqueNames.size > 1) getString(R.string.multiple_values) else ""
        val suggestions = DataRepository.devices.value!!.map { it.name }.distinct()

        SuggestionInputDialog(requireContext())
            .title(R.string.action_rename)
            .text(name)
            .hint(hint)
            .suggestions(suggestions)
            .onFinished { newName ->
                DataRepository.updateDevices(
                    selectedDevices.map { it.also { it.name = newName } }
                )
                //deviceListAdapter.notifyDataSetChanged()
            }
            .show()
    }

    private fun showChangeRoomDialog() {
        val selectedUids = tracker.selection.toList()
        val selectedDevices = DataRepository.devices.value!!.filter { it.uid in selectedUids }
        val uniqueRooms = selectedDevices.map { it.room }.distinct()

        val room = if (uniqueRooms.size > 1) "" else uniqueRooms[0]
        val hint = if (uniqueRooms.size > 1) getString(R.string.multiple_values) else ""
        val suggestions = DataRepository.devices.value!!.map { it.room }.distinct()

        SuggestionInputDialog(requireContext())
            .title(R.string.action_device_change_room)
            .text(room)
            .hint(hint)
            .suggestions(suggestions)
            .onFinished { newRoom ->
                DataRepository.updateDevices(
                    selectedDevices.map { it.also { it.room = newRoom } }
                )
                //deviceListAdapter.notifyDataSetChanged()
            }
            .show()
    }

    private fun showSortDialog() {
        val sortingCriterion = Res.getPrefString(R.string.pref_devices_sorting_criterion_key, Sorting.DEFAULT_DEVICE_CRITERION.name)
        val currentCriterion = Sorting.DeviceCriterion.valueOf(sortingCriterion).ordinal
        val currentOrder = Res.getPrefBool(R.string.pref_devices_sorting_order_key, Sorting.DEFAULT_ORDER)

        SortingDialog(requireContext())
            .sortingCriteriaRes(Sorting.DeviceCriterion.values().map { it.resId }, currentCriterion)
            .ascending(currentOrder)
            .onFinished { criterion, ascending ->
                Res.putPrefString(R.string.pref_devices_sorting_criterion_key, Sorting.DeviceCriterion.values()[criterion].name)
                Res.putPrefBool(R.string.pref_devices_sorting_order_key, ascending)

                deviceListAdapter.updateList(DataRepository.devices.value!!)
            }
            .show()
    }
}