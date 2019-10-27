package saecki.homesweethomeandroidclient.ui.custom

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import saecki.homesweethomeandroidclient.R
import saecki.homesweethomeandroidclient.datatypes.devices.Heating

class HeatingViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val name: TextView = view.findViewById(R.id.name)
    val actualTemp: TextView = view.findViewById(R.id.actualTemp)
    val targetTemp: TextView = view.findViewById(R.id.targetTemp)
    val arrow: ImageView = view.findViewById(R.id.arrow)
    val detailedView: LinearLayout = view.findViewById(R.id.detailedView)
    val minus: ImageView = view.findViewById(R.id.minus)
    val plus: ImageView = view.findViewById(R.id.plus)
    var extended = false

    init {
        arrow.setOnClickListener {
            if (extended) {
                arrow.animate().rotation(0f).setDuration(250).start()
                targetTemp.visibility = View.VISIBLE
                detailedView.visibility = View.GONE
            } else {
                arrow.animate().rotation(180f).setDuration(250).start()
                targetTemp.visibility = View.INVISIBLE
                detailedView.visibility = View.VISIBLE
            }
            extended = !extended
        }
    }

    fun bindView(heating: Heating) {
        name.text = heating.name
        actualTemp.text = heating.actualTemp.formatGlobal()
        targetTemp.text = heating.targetTemp.formatGlobal()
        extended = false
    }
}