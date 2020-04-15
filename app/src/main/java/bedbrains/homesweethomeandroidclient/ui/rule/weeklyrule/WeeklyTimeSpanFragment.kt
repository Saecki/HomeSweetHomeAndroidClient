package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.FragmentWeeklyTimeSpanBinding
import bedbrains.homesweethomeandroidclient.ui.value.rulevalue.RuleValueView
import bedbrains.platform.Time
import bedbrains.shared.datatypes.time.WeeklyTimeSpan
import bedbrains.shared.datatypes.upsert
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import java.util.*

class WeeklyTimeSpanFragment() : Fragment() {

    private val weeklyTimeSpanViewModel: WeeklyTimeSpanViewModel by viewModels()
    private lateinit var locale: Locale
    private lateinit var days: Array<String>

    private lateinit var cancelButton: ImageView
    private lateinit var doneButton: MaterialButton
    private lateinit var startDay: TextView
    private lateinit var endDay: TextView
    private lateinit var startTime: TextView
    private lateinit var endTime: TextView
    private lateinit var ruleValueView: RuleValueView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        locale = Locale.getDefault()
        days = Array(7) { i -> Time.formatWeekDayFull(i, locale) }

        val binding = FragmentWeeklyTimeSpanBinding.inflate(inflater)

        cancelButton = binding.cancelButton
        doneButton = binding.doneButton
        startDay = binding.startDay
        endDay = binding.endDay
        startTime = binding.startTime
        endTime = binding.endTime
        ruleValueView = RuleValueView(binding.ruleValue, context)

        if (weeklyTimeSpanViewModel.initialCreation) {
            val ruleUid = arguments?.getString(resources.getString(R.string.rule_uid))
            val timeSpanUid = arguments?.getString(resources.getString(R.string.time_span_uid))

            if (ruleUid == null || timeSpanUid == null) {
                findNavController().popBackStack()
                Toast.makeText(context, R.string.resp_item_no_longer_exists, Toast.LENGTH_LONG).show()
            } else {
                weeklyTimeSpanViewModel.observe(viewLifecycleOwner, ruleUid, timeSpanUid)
            }
        }

        weeklyTimeSpanViewModel.timeSpan.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                findNavController().popBackStack()
                Toast.makeText(context, R.string.resp_item_no_longer_exists, Toast.LENGTH_LONG).show()
            } else {
                startDay.text = Time.formatWeekDayFull(it.start.localizedDay, locale)
                endDay.text = Time.formatWeekDayFull(it.end.localizedDay, locale)
                startTime.text = Time.formatTime(it.start.hour, it.start.minute, locale)
                endTime.text = Time.formatTime(it.end.hour, it.end.minute, locale)

                ruleValueView.bind(it.value) { value ->
                    val timeSpan = weeklyTimeSpanViewModel.timeSpan.value!!

                    timeSpan.value = value
                }
            }
        })

        cancelButton.setOnClickListener {
            MainActivity.bottomSheetBehavior.isHideable = true
            MainActivity.bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        doneButton.setOnClickListener {
            MainActivity.bottomSheetBehavior.isHideable = true
            MainActivity.bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            updateRule(weeklyTimeSpanViewModel.timeSpan.value!!)
        }

        startDay.setOnClickListener {
            showStartDayDialog()
        }
        endDay.setOnClickListener {
            showEndDayDialog()
        }
        startTime.setOnClickListener {
            showStartTimeDialog()
        }
        endTime.setOnClickListener {
            showEndTimeDialog()
        }

        return binding.root
    }

    private fun updateRule(timeSpan: WeeklyTimeSpan) {
        DataRepository.upsertRule(weeklyTimeSpanViewModel.rule.value!!.apply {
            timeSpans.upsert(timeSpan) { it.uid == timeSpan.uid }
        })
    }

    private fun applyToAll() {
        val rule = weeklyTimeSpanViewModel.rule.value!!
        val value = weeklyTimeSpanViewModel.timeSpan.value!!.value

        rule.applyValueToAllTimeSpans(value)

        DataRepository.upsertRule(rule)
    }

    private fun delete() {
        val rule = weeklyTimeSpanViewModel.rule.value!!
        val timeSpan = weeklyTimeSpanViewModel.timeSpan.value!!

        rule.timeSpans.remove(timeSpan)

        weeklyTimeSpanViewModel.timeSpan.removeObservers(viewLifecycleOwner)
        findNavController().popBackStack()

        DataRepository.upsertRule(rule)
    }

    private fun showStartDayDialog() {
        val timeSpan = weeklyTimeSpanViewModel.timeSpan.value!!

        AlertDialog.Builder(context!!)
            .setSingleChoiceItems(days, timeSpan.start.localizedDay) { dialog, which ->
                startDay.text = days[which]

                timeSpan.start.localizedDay = which
                //TODO preview changes

                dialog.cancel()
            }
            .show()
    }

    private fun showEndDayDialog() {
        val timeSpan = weeklyTimeSpanViewModel.timeSpan.value!!

        AlertDialog.Builder(context!!)
            .setSingleChoiceItems(days, timeSpan.end.localizedDay) { dialog, which ->
                endDay.text = days[which]

                timeSpan.end.localizedDay = which
                //TODO preview changes

                dialog.cancel()
            }
            .show()
    }

    private fun showStartTimeDialog() {
        val timespan = weeklyTimeSpanViewModel.timeSpan.value!!

        TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener
            { _, hourOfDay, minute ->
                startTime.text = Time.formatTime(hourOfDay, minute, locale)

                timespan.start.hour = hourOfDay
                timespan.start.minute = hourOfDay

                //TODO preview changes
            },
            timespan.start.hour,
            timespan.start.minute,
            DateFormat.is24HourFormat(context)
        ).show()
    }

    private fun showEndTimeDialog() {
        val timespan = weeklyTimeSpanViewModel.timeSpan.value!!

        TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                endTime.text = Time.formatTime(hourOfDay, minute, locale)

                timespan.end.hour = hourOfDay
                timespan.end.minute = minute

                //TODO preview changes
            },
            timespan.end.hour,
            timespan.end.minute,
            DateFormat.is24HourFormat(context)
        ).show()
    }

    private fun showApplyToAllDialog() {
        AlertDialog.Builder(context!!)
            .setTitle(R.string.weekly_time_span_apply_to_all_confirmation)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                applyToAll()
            }
            .show()
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(context!!)
            .setTitle(R.string.weekly_time_span_delete_confirmation)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                delete()
            }
            .show()
    }
}