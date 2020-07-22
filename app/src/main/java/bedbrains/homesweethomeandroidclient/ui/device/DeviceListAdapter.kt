package bedbrains.homesweethomeandroidclient.ui.device

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.DeviceHeatingBinding
import bedbrains.homesweethomeandroidclient.databinding.DeviceLightBinding
import bedbrains.homesweethomeandroidclient.ui.Sorting
import bedbrains.homesweethomeandroidclient.ui.adapter.UniqueListAdapter
import bedbrains.homesweethomeandroidclient.ui.device.heating.HeatingViewHolder
import bedbrains.homesweethomeandroidclient.ui.device.light.LightViewHolder
import bedbrains.shared.datatypes.devices.Device
import bedbrains.shared.datatypes.devices.Heating
import bedbrains.shared.datatypes.devices.Light

class DeviceListAdapter(devices: List<Device>) : UniqueListAdapter<Device>(devices) {

    var tracker: SelectionTracker<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            Heating.TYPE -> {
                val heatingBinding = DeviceHeatingBinding.inflate(inflater, parent, false)
                HeatingViewHolder(heatingBinding)
            }
            Light.TYPE -> {
                val lightBinding = DeviceLightBinding.inflate(inflater, parent, false)
                LightViewHolder(lightBinding)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val device = list[position]

        tracker?.let {
            when (device) {
                is Heating -> (holder as HeatingViewHolder).bind(device, it.isSelected(device.uid))
                is Light -> (holder as LightViewHolder).bind(device, it.isSelected(device.uid))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    override fun sortList(list: List<Device>): List<Device> {
        val sortingType = Res.getPrefString(R.string.pref_devices_sorting_criterion_key, Sorting.DEFAULT_DEVICE_CRITERION.name)

        val selector: (Device) -> String = when (Sorting.DeviceCriterion.valueOf(sortingType)) {
            Sorting.DeviceCriterion.Manually -> return list
            Sorting.DeviceCriterion.Name -> { device -> device.name }
            Sorting.DeviceCriterion.Room -> { device -> device.room }
        }

        val sortingOrder = Res.getPrefBool(R.string.pref_devices_sorting_order_key, Sorting.DEFAULT_ORDER)

        return when (sortingOrder) {
            Sorting.ASCENDING -> list.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, selector))
            else -> list.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER, selector))
        }
    }
}