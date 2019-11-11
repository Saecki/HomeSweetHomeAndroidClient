package saecki.homesweethomeandroidclient.ui.device

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import saecki.homesweethomeandroidclient.MainActivity
import saecki.homesweethomeandroidclient.R
import saecki.homesweethomeandroidclient.datatypes.devices.Heating
import saecki.homesweethomeandroidclient.ui.animation.CollapseAnimation
import saecki.homesweethomeandroidclient.ui.animation.ExpandAnimation

class HeatingViewHolder(
    val view: View,
    val context: Context
) : RecyclerView.ViewHolder(view) {

    lateinit var heating: Heating
    val name: TextView = view.findViewById(R.id.name)
    val actualTemp: TextView = view.findViewById(R.id.actualTemp)
    val targetTemp: TextView = view.findViewById(R.id.targetTemp)
    val arrow: ImageView = view.findViewById(R.id.arrow)
    val detailedView: LinearLayout = view.findViewById(R.id.detailedView)
    val detailedViewTargetTemp: TextView = view.findViewById(R.id.detailedViewTargetTemp)
    val minus: ImageView = view.findViewById(R.id.minus)
    val plus: ImageView = view.findViewById(R.id.plus)

    val extendView: ExpandAnimation = ExpandAnimation(detailedView)
    val collapseView: CollapseAnimation = CollapseAnimation(detailedView)

    fun bindView(heating: Heating) {
        this.heating = heating
        update(heating)
        if (heating.extended) {
            expand()
        } else {
            collapse()
        }
        arrow.setOnClickListener {
            if (heating.extended) {
                collapse(getDuration())
            } else {
                expand(getDuration())
            }
        }
        detailedViewTargetTemp.setOnClickListener {
            showInputDialog()
        }
        minus.setOnClickListener {
            var temp = heating.targetTemp.getGlobal()
            temp -= if (temp.rem(getIncrement()) == 0.0) {
                getIncrement()
            } else {
                temp.rem(getIncrement())
            }
            heating.targetTemp.setGlobal(temp)
            update(heating)
        }
        plus.setOnClickListener {
            var temp = heating.targetTemp.getGlobal()
            temp += getIncrement() - temp.rem(getIncrement())
            heating.targetTemp.setGlobal(temp)
            update(heating)
        }
    }

    fun update(heating: Heating) {
        name.text = heating.name
        actualTemp.text = heating.actualTemp.formatGlobal(true)
        targetTemp.text = heating.targetTemp.formatGlobal(true)
        detailedViewTargetTemp.text = heating.targetTemp.formatGlobal(true)
    }

    fun showInputDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(MainActivity.res.getString(R.string.pref_temperature_category_title))
        val input = EditText(context)
        input.setText(heating.targetTemp.formatGlobal(false))
        input.inputType = InputType.TYPE_CLASS_PHONE
        builder.setView(input)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            heating.targetTemp.setGlobal(input.text.toString().toDouble())
            update(heating)
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
        input.requestFocusFromTouch()
    }

    fun expand(duration: Long) {
        heating.extended = true
        extendView.duration = duration
        detailedView.visibility = View.VISIBLE
        extendView.updateInitialHeight()
        detailedView.startAnimation(extendView)
        targetTemp.animate().alpha(0f).setDuration(duration).start()
        arrow.animate().rotation(180f).setDuration(duration).start()
    }

    fun expand() {
        heating.extended = true
        detailedView.visibility = View.VISIBLE
        detailedView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        detailedView.alpha = 1f
        targetTemp.alpha = 0f
        arrow.rotation = 180f
        detailedView.requestLayout()
    }

    fun collapse(duration: Long) {
        heating.extended = false
        collapseView.duration = duration
        collapseView.updateInitialHeight()
        detailedView.startAnimation(collapseView)
        targetTemp.animate().alpha(1f).setDuration(duration).start()
        arrow.animate().rotation(0f).setDuration(duration).start()
    }

    fun collapse() {
        heating.extended = false
        detailedView.layoutParams.height = 0
        detailedView.alpha = 0f
        targetTemp.alpha = 1f
        arrow.rotation = 0f
        detailedView.requestLayout()
        detailedView.visibility = View.GONE
    }

    fun getIncrement(): Double {
        val key = MainActivity.res.getString(R.string.pref_temperature_increment_key)
        return MainActivity.getPrefStringAsDouble(key, 0.5)
    }

    private fun getDuration(): Long {
        val key: String = MainActivity.res.getString(R.string.pref_animation_duration_key)
        return MainActivity.getPrefInt(key, 250).toLong()
    }
}