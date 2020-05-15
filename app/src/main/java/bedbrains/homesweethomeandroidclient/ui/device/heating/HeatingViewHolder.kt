package bedbrains.homesweethomeandroidclient.ui.device.heating

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res
import bedbrains.homesweethomeandroidclient.databinding.DeviceHeatingBinding
import bedbrains.shared.datatypes.devices.Heating

class HeatingViewHolder(
    private val viewBinding: DeviceHeatingBinding,
    private val context: Context,
    private val parent: View
) :
    RecyclerView.ViewHolder(viewBinding.root) {

    private lateinit var heating: Heating
    private val room = viewBinding.room
    private val name = viewBinding.name
    private val actualTemp = viewBinding.actualTemp
    private val targetTemp = viewBinding.targetTemp
    private val minus = viewBinding.minus
    private val plus = viewBinding.plus

    private val increment: Double
        get() {
            return Res.getPrefStringAsDouble(
                R.string.pref_temperature_step_key,
                0.5
            )
        }

    fun bindView(heating: Heating) {
        this.heating = heating
        update(heating)
        minus.setOnClickListener {
            decrementTemp()
            DataRepository.updateDevice(heating)
        }
        plus.setOnClickListener {
            incrementTemp()
            DataRepository.updateDevice(heating)
        }

        viewBinding.root.setOnClickListener {
            val bundle = Bundle()

            bundle.putString(Res.resources.getString(R.string.uid), heating.uid)
            viewBinding.root.findNavController().navigate(
                R.id.action_nav_home_to_nav_heating,
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