package saecki.homesweethomeandroidclient.ui.device

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import saecki.homesweethomeandroidclient.MainActivity
import saecki.homesweethomeandroidclient.R
import saecki.homesweethomeandroidclient.datatypes.devices.Heating
import saecki.homesweethomeandroidclient.ui.animation.CollapseAnimation
import saecki.homesweethomeandroidclient.ui.animation.ExpandAnimation

class HeatingViewHolder(
    val view: View,
    val context: Context,
    val parent: View
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
            toggleDetailedView()
        }
        detailedViewTargetTemp.setOnClickListener {
            showInputDialog(heating.targetTemp.formatGlobal(false))
        }
        minus.setOnClickListener {
            decrementTemp()
        }
        plus.setOnClickListener {
            incrementTemp()
        }
    }

    fun update(heating: Heating) {
        name.text = heating.name
        actualTemp.text = heating.actualTemp.formatGlobal(true)
        targetTemp.text = heating.targetTemp.formatGlobal(true)
        detailedViewTargetTemp.text = heating.targetTemp.formatGlobal(true)
    }

    fun toggleDetailedView() {
        if (heating.extended) {
            collapse(getDuration())
        } else {
            expand(getDuration())
        }
    }

    fun decrementTemp() {
        var temp = heating.targetTemp.getGlobal()
        temp -= if (MainActivity.preferences.getBoolean(
                MainActivity.res.getString(R.string.pref_temperature_round_to_next_increment_key),
                true
            )
        ) {
            if (temp.rem(getIncrement()) == 0.0) {
                getIncrement()
            } else {
                temp.rem(getIncrement())
            }
        } else {
            getIncrement()
        }
        heating.targetTemp.setGlobal(temp)
        update(heating)
    }

    fun incrementTemp() {
        var temp = heating.targetTemp.getGlobal()
        temp += if (MainActivity.preferences.getBoolean(
                MainActivity.res.getString(R.string.pref_temperature_round_to_next_increment_key),
                true
            )
        ) {
            getIncrement() - temp.rem(getIncrement())
        } else {
            getIncrement()
        }
        heating.targetTemp.setGlobal(temp)
        update(heating)
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

    fun showInputDialog(text: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(MainActivity.res.getString(R.string.pref_temperature_category_title))
        val input = EditText(context)
        input.setText(text)
        input.inputType = InputType.TYPE_CLASS_PHONE
        builder.setView(input)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            try {
                heating.targetTemp.setGlobal(input.text.toString().replace(',', '.').toDouble())
                update(heating)
            } catch (e: Exception) {
                Log.d("INPUT", "error parsing double from string")
                val snack = Snackbar.make(
                    parent,
                    MainActivity.res.getString(R.string.heating_snackbar_edit_text),
                    Snackbar.LENGTH_LONG
                )
                snack.setAction(
                    MainActivity.res.getString(R.string.heating_snackbar_edit),
                    View.OnClickListener {
                        showInputDialog(input.text.toString())
                    })
                snack.show()
            }

        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
        input.requestFocusFromTouch()
        input.postDelayed(Runnable {
            val keyboard: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)
        }, 0)
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