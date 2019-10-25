package saecki.homesweethomeandroidclient.ui.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Switch
import android.widget.TextView
import saecki.homesweethomeandroidclient.R
import saecki.homesweethomeandroidclient.datatypes.devices.Device
import saecki.homesweethomeandroidclient.datatypes.devices.Heating
import saecki.homesweethomeandroidclient.datatypes.devices.Lamp
import kotlin.math.round

class DeviceListAdapter(private val context: Context, var devices: List<Device>) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val device = devices.get(position)

        if (device.type == Heating.type) {
            val heating = device as Heating
            val heatingView = inflater.inflate(R.layout.device_heating, parent, false)
            val name: TextView = heatingView.findViewById(R.id.name)
            val type: TextView = heatingView.findViewById(R.id.type)
            val actualTemp: TextView = heatingView.findViewById(R.id.actualTemp)
            val targetTemp: TextView = heatingView.findViewById(R.id.targetTemp)

            name.text = heating.name
            type.text = heating.type
            actualTemp.text = (round(heating.actualTemp * 10) / 10).toString()
            targetTemp.text = (round(heating.targetTemp * 10) / 10).toString()

            return heatingView
        } else if (device.type == Lamp.type) {
            val lamp = device as Lamp
            val lampView = inflater.inflate(R.layout.device_lamp, parent, false)
            val name: TextView = lampView.findViewById(R.id.name)
            val type: TextView = lampView.findViewById(R.id.type)
            val state: Switch = lampView.findViewById(R.id.state)

            name.text = lamp.name
            type.text = lamp.type
            state.isChecked = lamp.state

            return lampView
        }

        return null
    }

    override fun getItem(position: Int): Any {
        return devices.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return devices.size
    }

}