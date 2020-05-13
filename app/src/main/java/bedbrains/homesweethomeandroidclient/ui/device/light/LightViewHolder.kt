package bedbrains.homesweethomeandroidclient.ui.device.light

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.DeviceLightBinding
import bedbrains.shared.datatypes.devices.Light

class LightViewHolder(val viewBinding: DeviceLightBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    private lateinit var light: Light
    private val room = viewBinding.room
    private val name = viewBinding.name
    private val isOn = viewBinding.state

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

        viewBinding.root.setOnClickListener {
            val bundle = Bundle()

            bundle.putString(Res.resources.getString(R.string.uid), light.uid)
            viewBinding.root.findNavController().navigate(
                R.id.action_nav_home_to_nav_light,
                bundle
            )
        }
    }
}