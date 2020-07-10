package bedbrains.homesweethomeandroidclient.ui.device

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.DeviceHeatingBinding
import bedbrains.homesweethomeandroidclient.databinding.DeviceLightBinding
import bedbrains.homesweethomeandroidclient.ui.Sorting
import bedbrains.homesweethomeandroidclient.ui.adapter.UniqueListDiffUtilCallback
import bedbrains.homesweethomeandroidclient.ui.device.heating.HeatingViewHolder
import bedbrains.homesweethomeandroidclient.ui.device.light.LightViewHolder
import bedbrains.shared.datatypes.devices.Device
import bedbrains.shared.datatypes.devices.Heating
import bedbrains.shared.datatypes.devices.Light

class DeviceListAdapter(devices: List<Device>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var devicesValid = false
    var devices = devices
        get() {
            if (devicesValid) return cachedDevices

            cachedDevices = sortDevices(field)
            devicesValid = true

            return cachedDevices
        }
        set(value) {
            devicesValid = false
            field = value
        }

    private var cachedDevices = devices

    inner class KeyProvider : ItemKeyProvider<String>(SCOPE_MAPPED) {
        override fun getKey(position: Int): String? {
            return devices[position].uid
        }

        override fun getPosition(key: String): Int {
            val position = devices.indexOfFirst { it.uid == key }

            return if (position == -1)
                RecyclerView.NO_POSITION
            else
                position
        }
    }

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
        val device = devices[position]

        tracker?.let {
            when (device) {
                is Heating -> {
                    val heatingViewHolder = holder as HeatingViewHolder
                    heatingViewHolder.bind(device, it.isSelected(device.uid))
                }
                is Light -> {
                    val lightViewHolder = holder as LightViewHolder
                    lightViewHolder.bind(device, it.isSelected(device.uid))
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return devices[position].type
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun updateDevices(new: List<Device>) {
        val newSorted = sortDevices(new)
        val diff = DiffUtil.calculateDiff(UniqueListDiffUtilCallback(cachedDevices, newSorted))

        devices = new
        diff.dispatchUpdatesTo(this)
    }

    private fun sortDevices(devices: List<Device>): List<Device> {
        val sortingType = Res.getPrefString(R.string.pref_devices_sorting_criterion_key, Sorting.DEFAULT_DEVICE_CRITERION.name)

        val selector: (Device) -> String = when (Sorting.DeviceCriterion.valueOf(sortingType)) {
            Sorting.DeviceCriterion.Manually -> return devices
            Sorting.DeviceCriterion.Name -> { device -> device.name }
            Sorting.DeviceCriterion.Room -> { device -> device.room }
        }

        val sortingOrder = Res.getPrefBool(R.string.pref_devices_sorting_order_key, Sorting.DEFAULT_ORDER)

        return when (sortingOrder) {
            Sorting.ASCENDING -> devices.sortedBy(selector)
            else -> devices.sortedByDescending(selector)
        }
    }
}