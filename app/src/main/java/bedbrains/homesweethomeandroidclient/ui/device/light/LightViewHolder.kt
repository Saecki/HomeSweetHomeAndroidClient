package bedbrains.homesweethomeandroidclient.ui.device.light

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.DeviceLightBinding
import bedbrains.shared.datatypes.devices.Light

class LightViewHolder(val viewBinding: DeviceLightBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    private lateinit var light: Light
    private val room = viewBinding.room
    private val name = viewBinding.name
    private val state = viewBinding.state

    fun update(light: Light) {
        room.text = light.room
        name.text = light.name
        state.isChecked = light.state
    }

    fun bindView(light: Light) {
        this.light = light
        update(light)

        state.setOnCheckedChangeListener { _, isChecked ->
            DataRepository.upsertDevice(light.also { it.state = isChecked })
        }

        viewBinding.root.setOnClickListener {
            viewBinding.root.findNavController().navigate(R.id.action_nav_home_to_nav_light)
        }
    }
}