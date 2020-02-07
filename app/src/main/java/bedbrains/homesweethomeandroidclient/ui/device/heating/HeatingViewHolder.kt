package bedbrains.homesweethomeandroidclient.ui.device.heating

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.ui.animation.CollapseAnimation
import bedbrains.homesweethomeandroidclient.ui.animation.ExpandAnimation
import bedbrains.shared.datatypes.devices.Heating
import com.google.android.material.snackbar.Snackbar

class HeatingViewHolder(private val view: View, private val context: Context, private val parent: View) : RecyclerView.ViewHolder(view) {

    private lateinit var heating: Heating
    private val room: TextView = view.findViewById(R.id.room)
    private val name: TextView = view.findViewById(R.id.name)
    private val actualTemp: TextView = view.findViewById(R.id.actual_temp)
    private val targetTemp: TextView = view.findViewById(R.id.target_temp)
    private val arrow: ImageView = view.findViewById(R.id.arrow)
    private val detailedView: Group = view.findViewById(R.id.detailed_view)
    private val detailedViewTargetTemp: TextView = view.findViewById(R.id.detailed_view_target_temp)
    private val minus: ImageView = view.findViewById(R.id.minus)
    private val plus: ImageView = view.findViewById(R.id.plus)

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
        view.setOnClickListener {
            view.findNavController().navigate(R.id.action_nav_home_to_nav_heating)
        }
    }

    private fun update(heating: Heating) {
        room.text = heating.room
        name.text = heating.name
        actualTemp.text = heating.actualTemp.formatGlobal(true)
        targetTemp.text = heating.targetTemp.formatGlobal(true)
        detailedViewTargetTemp.text = heating.targetTemp.formatGlobal(true)
    }

    private fun toggleDetailedView() {
        if (heating.extended) {
            collapse(getAnimationDuration())
        } else {
            expand(getAnimationDuration())
        }
    }

    private fun decrementTemp() {
        var temp = heating.targetTemp.global
        temp -= if (MainActivity.preferences.getBoolean(MainActivity.res.getString(R.string.pref_temperature_round_to_next_increment_key), true)) {
            if (temp.rem(getIncrement()) == 0.0) {
                getIncrement()
            } else {
                temp.rem(getIncrement())
            }
        } else {
            getIncrement()
        }
        heating.targetTemp.global = temp
        update(heating)
    }

    private fun incrementTemp() {
        var temp = heating.targetTemp.global
        temp += if (MainActivity.preferences.getBoolean(MainActivity.res.getString(R.string.pref_temperature_round_to_next_increment_key), true)) {
            getIncrement() - temp.rem(getIncrement())
        } else {
            getIncrement()
        }
        heating.targetTemp.global = temp
        update(heating)
    }

    private fun expand(duration: Long) {
        val extendView = ExpandAnimation(detailedViewTargetTemp, minus, plus)
        extendView.duration = duration
        detailedView.visibility = View.VISIBLE
        detailedView.startAnimation(extendView)
        targetTemp.animate().alpha(0f).setDuration(duration).start()
        arrow.animate().rotation(180f).setDuration(duration).start()
        heating.extended = true
    }

    private fun expand() {
        detailedViewTargetTemp.visibility = View.VISIBLE
        minus.visibility = View.VISIBLE
        plus.visibility = View.VISIBLE
        detailedViewTargetTemp.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        minus.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        plus.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        targetTemp.alpha = 0f
        arrow.rotation = 180f
        detailedView.requestLayout()
        heating.extended = true
    }

    private fun collapse(duration: Long) {
        val collapseView = CollapseAnimation(detailedViewTargetTemp, minus, plus)
        collapseView.duration = duration
        detailedView.startAnimation(collapseView)
        targetTemp.animate().alpha(1f).setDuration(duration).start()
        arrow.animate().rotation(0f).setDuration(duration).start()
        heating.extended = false
    }

    private fun collapse() {
        detailedViewTargetTemp.layoutParams.height = 0
        minus.layoutParams.height = 0
        plus.layoutParams.height = 0
        targetTemp.alpha = 1f
        arrow.rotation = 0f
        detailedViewTargetTemp.requestLayout()
        minus.requestLayout()
        plus.requestLayout()
        detailedViewTargetTemp.visibility = View.GONE
        minus.visibility = View.GONE
        plus.visibility = View.GONE
        heating.extended = false
    }

    private fun showInputDialog(text: String) {
        val builder = AlertDialog.Builder(context)
        val input = EditText(context)

        builder.setTitle(MainActivity.res.getString(R.string.pref_temperature_category_title))
        input.setText(text)
        input.inputType = InputType.TYPE_CLASS_PHONE
        builder.setView(input)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            try {
                heating.targetTemp.global = input.text.toString().replace(',', '.').toDouble()
                update(heating)
            } catch (e: Exception) {
                Log.d("INPUT", "error parsing double from string")
                val snack = Snackbar.make(
                    parent,
                    MainActivity.res.getString(R.string.heating_snackbar_edit_text),
                    Snackbar.LENGTH_LONG
                )
                snack.setAction(
                    MainActivity.res.getString(R.string.heating_snackbar_edit)
                ) {
                    showInputDialog(input.text.toString())
                }
                snack.show()
            }

        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
        input.requestFocusFromTouch()

        input.postDelayed({
            val keyboard: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)
        }, 0)
    }

    private fun getIncrement(): Double {
        val key = MainActivity.res.getString(R.string.pref_temperature_increment_key)
        return MainActivity.getPrefStringAsDouble(key, 0.5)
    }

    private fun getAnimationDuration(): Long {
        val key: String = MainActivity.res.getString(R.string.pref_animation_duration_key)
        return MainActivity.getPrefInt(key, 250).toLong()
    }
}