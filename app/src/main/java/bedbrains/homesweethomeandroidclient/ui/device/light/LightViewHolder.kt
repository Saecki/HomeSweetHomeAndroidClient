package bedbrains.homesweethomeandroidclient.ui.device.light

import android.os.Bundle
import androidx.navigation.findNavController
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.DeviceLightBinding
import bedbrains.homesweethomeandroidclient.ui.adapter.ListItemViewHolder
import bedbrains.shared.datatypes.devices.Light

class LightViewHolder(val binding: DeviceLightBinding) : ListItemViewHolder<Light>(binding.root) {

    override fun onBind(value: Light, isSelected: Boolean, isEditing: Boolean) {
        binding.room.text = value.room
        binding.name.text = value.name
        binding.state.isChecked = value.isOn

        binding.state.setOnCheckedChangeListener { _, isChecked ->
            DataRepository.updateDevice(value.apply { isOn = isChecked })
        }

        binding.root.setOnClickListener {
            navigate {
                val bundle = Bundle()

                bundle.putString(Res.resources.getString(R.string.uid), value.uid)
                binding.root.findNavController().navigate(
                    R.id.action_nav_devices_to_nav_light,
                    bundle
                )
            }
        }
    }
}