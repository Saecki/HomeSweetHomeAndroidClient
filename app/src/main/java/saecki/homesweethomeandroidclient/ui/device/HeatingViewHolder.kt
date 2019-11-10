package saecki.homesweethomeandroidclient.ui.device

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.hardware.input.InputManager
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.marginEnd
import androidx.preference.EditTextPreference
import androidx.recyclerview.widget.RecyclerView
import saecki.homesweethomeandroidclient.MainActivity
import saecki.homesweethomeandroidclient.R
import saecki.homesweethomeandroidclient.datatypes.devices.Heating
import saecki.homesweethomeandroidclient.ui.animation.CollapseAnimation
import saecki.homesweethomeandroidclient.ui.animation.ExpandAnimation

class HeatingViewHolder(val view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    val name: TextView = view.findViewById(R.id.name)
    val actualTemp: TextView = view.findViewById(R.id.actualTemp)
    val targetTemp: TextView = view.findViewById(R.id.targetTemp)
    val arrow: ImageView = view.findViewById(R.id.arrow)
    val detailedView: LinearLayout = view.findViewById(R.id.detailedView)
    val detailedViewTargetTemp: TextView = view.findViewById(R.id.detailedViewTargetTemp)
    val minus: ImageView = view.findViewById(R.id.minus)
    val plus: ImageView = view.findViewById(R.id.plus)

    val extendView: ExpandAnimation = ExpandAnimation(detailedView)
    val minimizeView: CollapseAnimation = CollapseAnimation(detailedView)

    init {
        arrow.setOnClickListener {
            val key: String = MainActivity.res.getString(R.string.pref_animation_duration_key)
            val duration: Long = MainActivity.getPrefInt(key, 250).toLong()
            if (detailedView.isGone) {
                extendView.duration = duration
                detailedView.visibility = View.VISIBLE
                targetTemp.animate().alpha(0f).setDuration(duration).start()
                detailedView.startAnimation(extendView)
                arrow.animate().rotation(180f).setDuration(duration).start()
            } else {
                minimizeView.duration = duration
                targetTemp.animate().alpha(1f).setDuration(duration).start()
                detailedView.startAnimation(minimizeView)
                arrow.animate().rotation(0f).setDuration(duration).start()
            }
        }
    }

    fun update(heating: Heating) {
        name.text = heating.name
        actualTemp.text = heating.actualTemp.formatGlobal()
        targetTemp.text = heating.targetTemp.formatGlobal()
        detailedViewTargetTemp.text = heating.targetTemp.formatGlobal()
    }

    fun bindView(heating: Heating) {
        update(heating)
        detailedViewTargetTemp.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle(MainActivity.res.getString(R.string.pref_temperature_category_title))
            val input: EditText = EditText(context)
            input.setText(heating.targetTemp.getGlobal().toString())
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
        minus.setOnClickListener {
            val key = MainActivity.res.getString(R.string.pref_temperature_increment_key)
            val increment = MainActivity.getPrefStringAsDouble(key, 0.5)
            heating.targetTemp.setGlobal(heating.targetTemp.getGlobal() - increment)
            update(heating)
        }
        plus.setOnClickListener {
            val key = MainActivity.res.getString(R.string.pref_temperature_increment_key)
            val increment = MainActivity.getPrefStringAsDouble(key, 0.5)
            heating.targetTemp.setGlobal(heating.targetTemp.getGlobal() + increment)
            targetTemp.text = heating.targetTemp.formatGlobal()
            detailedViewTargetTemp.text = heating.targetTemp.formatGlobal()
        }
    }
}