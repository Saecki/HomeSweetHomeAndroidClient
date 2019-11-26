package bedbrains.homesweethomeandroidclient.ui.device.light

import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.R
import bedbrains.shared.datatypes.devices.Light

class LightViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    lateinit var light: Light
    val name: TextView = view.findViewById(R.id.name)
    val state: Switch = view.findViewById(R.id.state)

    fun update(light: Light) {
        name.text = light.name
        state.isChecked = light.state
    }

    fun bindView(light: Light) {
        this.light = light
        update(light)
        state.setOnCheckedChangeListener { _, isChecked ->
            light.state = isChecked
        }
        view.setOnClickListener {
            view.findNavController().navigate(R.id.action_nav_home_to_nav_light)
        }
    }
}