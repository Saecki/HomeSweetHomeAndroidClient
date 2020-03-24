package bedbrains.homesweethomeandroidclient.ui.device

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.DeviceHeatingBinding
import bedbrains.homesweethomeandroidclient.databinding.DeviceLightBinding
import bedbrains.homesweethomeandroidclient.ui.adapter.ListDiffUtilCallback
import bedbrains.homesweethomeandroidclient.ui.device.heating.HeatingViewHolder
import bedbrains.homesweethomeandroidclient.ui.device.light.LightViewHolder
import bedbrains.homesweethomeandroidclient.ui.dummy.DummyViewHolder
import bedbrains.shared.datatypes.devices.Device
import bedbrains.shared.datatypes.devices.Heating
import bedbrains.shared.datatypes.devices.Light

class DeviceListAdapter(private var devices: List<Device>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            Heating.TYPE -> {
                val heatingViewBinding = DeviceHeatingBinding.inflate(inflater, parent, false)
                HeatingViewHolder(heatingViewBinding, parent.context, parent)
            }
            Light.TYPE -> {
                val lightViewBinding = DeviceLightBinding.inflate(inflater, parent, false)
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
        val diff = DiffUtil.calculateDiff(ListDiffUtilCallback(devices, new))
        devices = new
        diff.dispatchUpdatesTo(this)
    }

}