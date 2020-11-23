package bedbrains.homesweethomeandroidclient.ui.device.heating

import android.os.Bundle
import androidx.navigation.findNavController
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.DeviceHeatingBinding
import bedbrains.homesweethomeandroidclient.ui.adapter.ListItemViewHolder
import bedbrains.shared.datatypes.devices.Heating

class HeatingViewHolder(val binding: DeviceHeatingBinding) : ListItemViewHolder<Heating>(binding.root) {

    private val room = binding.room
    private val name = binding.name
    private val actualTemp = binding.actualTemp
    private val targetTemp = binding.targetTemp
    private val minus = binding.minus
    private val plus = binding.plus

    private val increment: Double
        get() = Res.getPrefStringAsDouble(
            R.string.pref_temperature_step_key,
            0.5
        )

    override fun onBind(value: Heating, isSelected: Boolean, isEditing: Boolean) {
        update(this.value)

        minus.setOnClickListener {
            decrementTemp()
            DataRepository.updateDevice(this.value)
        }
        plus.setOnClickListener {
            incrementTemp()
            DataRepository.updateDevice(this.value)
        }

        binding.root.setOnClickListener {
            navigate {
                val bundle = Bundle()

                bundle.putString(Res.resources.getString(R.string.uid), this.value.uid)
                binding.root.findNavController().navigate(
                    R.id.action_nav_devices_to_nav_heating,
                    bundle
                )
            }
        }
    }

    private fun update(heating: Heating) {
        room.text = heating.room
        name.text = heating.name
        actualTemp.text = heating.actualTemp.formatGlobal(true)
        targetTemp.text = heating.targetTemp.formatGlobal(true)
    }

    private fun decrementTemp() {
        var temp = value.targetTemp.global
        temp -= if (Res.preferences.getBoolean(
                Res.resources.getString(R.string.pref_temperature_round_to_next_step_key),
                true
            )
        ) {
            if (temp.rem(increment) == 0.0) {
                increment
            } else {
                temp.rem(increment)
            }
        } else {
            increment
        }
        value.targetTemp.global = temp
        update(value)
    }

    private fun incrementTemp() {
        var temp = value.targetTemp.global
        temp += if (Res.preferences.getBoolean(
                Res.resources.getString(R.string.pref_temperature_round_to_next_step_key),
                true
            )
        ) {
            increment - temp.rem(increment)
        } else {
            increment
        }
        value.targetTemp.global = temp
        update(value)
    }
}