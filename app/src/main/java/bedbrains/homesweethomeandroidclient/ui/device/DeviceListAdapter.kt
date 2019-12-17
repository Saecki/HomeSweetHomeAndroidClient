package bedbrains.homesweethomeandroidclient.ui.device

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.shared.datatypes.devices.Device
import bedbrains.shared.datatypes.devices.Heating
import bedbrains.shared.datatypes.devices.Light
import bedbrains.homesweethomeandroidclient.ui.device.heating.HeatingViewHolder
import bedbrains.homesweethomeandroidclient.ui.device.light.LightViewHolder
import bedbrains.homesweethomeandroidclient.ui.dummy.DummyViewHolder

class DeviceListAdapter(var devices: List<Device>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class DeviceListDiffUtilCallback(val old: List<Device>, val new: List<Device>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return old.size
        }

        override fun getNewListSize(): Int {
            return new.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition] === new[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition] == new[newItemPosition]
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            Heating.TYPE -> {
                val heatingView = inflater.inflate(R.layout.device_heating, parent, false)
                HeatingViewHolder(
                    heatingView,
                    parent.context,
                    parent
                )
            }
            Light.TYPE -> {
                val lightView = inflater.inflate(R.layout.device_light, parent, false)
                LightViewHolder(lightView)
            }
            else -> {
                val dummyView = inflater.inflate(R.layout.dummy, parent, false)
                DummyViewHolder(dummyView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return devices[position].type
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val device = devices[position]

        when (device.type) {
            Heating.TYPE -> {
                val heatingViewHolder = holder as HeatingViewHolder
                val heating = device as Heating
                heatingViewHolder.bindView(heating)
            }
            Light.TYPE -> {
                val lightViewHolder = holder as LightViewHolder
                val light = device as Light
                lightViewHolder.bindView(light)
            }
        }
    }

    fun updateDevices(new: List<Device>) {
        val diff = DiffUtil.calculateDiff(DeviceListDiffUtilCallback(devices, new))
        diff.dispatchUpdatesTo(this)
    }

}