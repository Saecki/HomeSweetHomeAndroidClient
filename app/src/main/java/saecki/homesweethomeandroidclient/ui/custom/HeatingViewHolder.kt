package saecki.homesweethomeandroidclient.ui.custom

import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import saecki.homesweethomeandroidclient.R
import saecki.homesweethomeandroidclient.datatypes.devices.Heating

class HeatingViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val typeIcon: ImageView = view.findViewById(R.id.typeIcon)
    val name: TextView = view.findViewById(R.id.name)
    val actualTemp: TextView = view.findViewById(R.id.actualTemp)
    val targetTemp: TextView = view.findViewById(R.id.targetTemp)
    val arrow: ImageView = view.findViewById(R.id.arrow)
    val detailedView: LinearLayout = view.findViewById(R.id.detailedView)
    val detailedViewTargetTemp: TextView = view.findViewById(R.id.detailedViewTargetTemp)
    val minus: ImageView = view.findViewById(R.id.minus)
    val plus: ImageView = view.findViewById(R.id.plus)
    val edit: ImageView = view.findViewById(R.id.edit)
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
        detailedViewTargetTemp.text = heating.targetTemp.formatGlobal()
        extended = false
    }
}