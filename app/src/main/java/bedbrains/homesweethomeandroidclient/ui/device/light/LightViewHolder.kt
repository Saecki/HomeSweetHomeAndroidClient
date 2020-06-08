package bedbrains.homesweethomeandroidclient.ui.device.light

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.DeviceLightBinding
import bedbrains.shared.datatypes.devices.Light

class LightViewHolder(private val binding: DeviceLightBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var light: Light
    private val room = binding.room
    private val name = binding.name
    private val isOn = binding.state

    fun update(light: Light) {
        room.text = light.room
        name.text = light.name
        isOn.isChecked = light.isOn
    }

    fun bindView(light: Light) {
        this.light = light
        update(light)

        isOn.setOnCheckedChangeListener { _, isChecked ->
            DataRepository.updateDevice(light.apply { isOn = isChecked })
        }

        binding.root.setOnClickListener {
            val bundle = Bundle()

            bundle.putString(Res.resources.getString(R.string.uid), light.uid)
            binding.root.findNavController().navigate(
                R.id.action_nav_devices_to_nav_light,
                bundle
            )
        }
    }
}