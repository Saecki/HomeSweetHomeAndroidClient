package bedbrains.homesweethomeandroidclient.ui.device

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.DeviceHeatingBinding
import bedbrains.homesweethomeandroidclient.databinding.DeviceLightBinding
import bedbrains.homesweethomeandroidclient.ui.device.heating.HeatingViewHolder
import bedbrains.homesweethomeandroidclient.ui.device.light.LightViewHolder
import bedbrains.homesweethomeandroidclient.ui.dummy.DummyViewHolder
import bedbrains.shared.datatypes.devices.Device
import bedbrains.shared.datatypes.devices.Heating
import bedbrains.shared.datatypes.devices.Light

class DeviceListAdapter(private var devices: List<Device>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class DeviceListDiffUtilCallback(private val old: List<Device>, private val new: List<Device>) : DiffUtil.Callback() {

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
                val heatingViewBinding = DeviceHeatingBinding.inflate(inflater)
                HeatingViewHolder(heatingViewBinding, parent.context, parent)
            }
            Light.TYPE -> {
                val lightViewBinding = DeviceLightBinding.inflate(inflater)
                LightViewHolder(lightViewBinding)
            }
            else -> {
                val dummyView = inflater.inflate(R.layout.dummy, parent)
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