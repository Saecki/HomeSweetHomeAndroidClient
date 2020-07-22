package bedbrains.homesweethomeandroidclient.ui.device.heating

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.DeviceHeatingBinding
import bedbrains.shared.datatypes.devices.Heating

class HeatingViewHolder(private val binding: DeviceHeatingBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var heating: Heating
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

    val itemDetails
        get() = object : ItemDetailsLookup.ItemDetails<String>() {
            override fun getSelectionKey(): String? = heating.uid
            override fun getPosition(): Int = adapterPosition
        }

    fun bind(heating: Heating, isSelected: Boolean = false) {
        this.heating = heating
        update(heating)

        binding.root.isSelected = isSelected

        minus.setOnClickListener {
            decrementTemp()
            DataRepository.updateDevice(heating)
        }
        plus.setOnClickListener {
            incrementTemp()
            DataRepository.updateDevice(heating)
        }

        binding.root.setOnClickListener {
            val bundle = Bundle()

            bundle.putString(Res.resources.getString(R.string.uid), heating.uid)
            binding.root.findNavController().navigate(
                R.id.action_nav_devices_to_nav_heating,
                bundle
            )
        }
    }

    private fun update(heating: Heating) {
        room.text = heating.room
        name.text = heating.name
        actualTemp.text = heating.actualTemp.formatGlobal(true)
        targetTemp.text = heating.targetTemp.formatGlobal(true)
    }

    private fun decrementTemp() {
        var temp = heating.targetTemp.global
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
        heating.targetTemp.global = temp
        update(heating)
    }

    private fun incrementTemp() {
        var temp = heating.targetTemp.global
        temp += if (Res.preferences.getBoolean(
                Res.resources.getString(R.string.pref_temperature_round_to_next_step_key),
                true
            )
        ) {
            increment - temp.rem(increment)
        } else {
            increment
        }
        heating.targetTemp.global = temp
        update(heating)
    }
}