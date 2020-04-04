package bedbrains.homesweethomeandroidclient.ui.device.heating

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.postDelayed
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.DeviceHeatingBinding
import bedbrains.homesweethomeandroidclient.ui.animation.CollapseAnimation
import bedbrains.homesweethomeandroidclient.ui.animation.ExpandAnimation
import bedbrains.shared.datatypes.devices.Heating
import com.google.android.material.snackbar.Snackbar

class HeatingViewHolder(private val viewBinding: DeviceHeatingBinding, private val context: Context, private val parent: View) :
    RecyclerView.ViewHolder(viewBinding.root) {

    private lateinit var heating: Heating
    private val room = viewBinding.room
    private val name = viewBinding.name
    private val actualTemp = viewBinding.actualTemp
    private val targetTemp = viewBinding.targetTemp
    private val arrow = viewBinding.arrow
    private val detailedView = viewBinding.detailedView
    private val detailedViewTargetTemp = viewBinding.detailedViewTargetTemp
    private val minus = viewBinding.minus
    private val plus = viewBinding.plus

    private val increment: Double
        get() {
            return MainActivity.getPrefStringAsDouble(
                R.string.pref_temperature_increment_key,
                0.5
            )
        }

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
            DataRepository.upsertDevice(heating)
        }
        plus.setOnClickListener {
            incrementTemp()
            DataRepository.upsertDevice(heating)
        }
        viewBinding.root.setOnClickListener {
            viewBinding.root.findNavController().navigate(R.id.action_nav_home_to_nav_heating)
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
        val duration = MainActivity.res.getInteger(android.R.integer.config_shortAnimTime).toLong()
        if (heating.extended) {
            collapse(duration)
        } else {
            expand(duration)
        }
    }

    private fun decrementTemp() {
        var temp = heating.targetTemp.global
        temp -= if (MainActivity.preferences.getBoolean(MainActivity.res.getString(R.string.pref_temperature_round_to_next_increment_key), true)) {
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
        temp += if (MainActivity.preferences.getBoolean(MainActivity.res.getString(R.string.pref_temperature_round_to_next_increment_key), true)) {
            increment - temp.rem(increment)
        } else {
            increment
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
        val input = EditText(context)
        input.setText(text)
        input.inputType = InputType.TYPE_CLASS_PHONE

        AlertDialog.Builder(context)
            .setTitle(R.string.pref_temperature_category_title)
            .setView(input)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                try {
                    heating.targetTemp.global = input.text.toString().replace(',', '.').toDouble()
                    update(heating)
                    DataRepository.upsertDevice(heating)
                } catch (e: Exception) {
                    Snackbar.make(
                        parent,
                        R.string.heating_snackbar_edit_text,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.heating_snackbar_edit) {
                            showInputDialog(input.text.toString())
                        }
                        .show()
                }
            }
            .show()

        input.requestFocusFromTouch()
        input.postDelayed(MainActivity.res.getInteger(R.integer.keyboard_show_delay).toLong()) {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}