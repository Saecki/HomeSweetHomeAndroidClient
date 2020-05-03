package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.FragmentWeeklyTimeSpanBinding
import bedbrains.homesweethomeandroidclient.ui.value.rulevalue.RuleValueView
import bedbrains.platform.Time
import bedbrains.shared.datatypes.time.WeeklyTimeSpan
import com.google.android.material.button.MaterialButton
import java.util.*

class WeeklyTimeSpanFragment() : Fragment() {
    private val weeklyTimeSpanViewModel: WeeklyTimeSpanViewModel by viewModels()

    private lateinit var locale: Locale
    private lateinit var days: Array<String>

    private lateinit var cancelButton: ImageView
    private lateinit var deleteButton: ImageView
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
        deleteButton = binding.deleteButton
        doneButton = binding.doneButton
        startDay = binding.startDay
        endDay = binding.endDay
        startTime = binding.startTime
        endTime = binding.endTime
        ruleValueView = RuleValueView(binding.ruleValue, context)

        return binding.root
    }

    fun bind(timeSpan: WeeklyTimeSpan, onEdit: (WeeklyTimeSpanEditEvent) -> Unit) {
        weeklyTimeSpanViewModel.timeSpan = timeSpan
        weeklyTimeSpanViewModel.onEdit = onEdit

        onEdit(
            WeeklyTimeSpanEditEvent(
                WeeklyTimeSpanEditEvent.Action.PREVIEW,
                timeSpan
            )
        )

        startDay.text = Time.formatWeekDayFull(timeSpan.start.localizedDay, locale)
        endDay.text = Time.formatWeekDayFull(timeSpan.end.localizedDay, locale)
        startTime.text = Time.formatTime(timeSpan.start.hour, timeSpan.start.minute, locale)
        endTime.text = Time.formatTime(timeSpan.end.hour, timeSpan.end.minute, locale)

        ruleValueView.bind(timeSpan.value) {
            timeSpan.value = it
        }

        cancelButton.setOnClickListener {
            onEdit(
                WeeklyTimeSpanEditEvent(
                    WeeklyTimeSpanEditEvent.Action.CANCELED,
                    timeSpan
                )
            )
        }
        deleteButton.setOnClickListener {
            showDeleteDialog()
        }
        doneButton.setOnClickListener {
            onEdit(
                WeeklyTimeSpanEditEvent(
                    WeeklyTimeSpanEditEvent.Action.FINISHED,
                    timeSpan
                )
            )
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
    }

    private fun preview(timeSpan: WeeklyTimeSpan) {
        weeklyTimeSpanViewModel.onEdit(
            WeeklyTimeSpanEditEvent(
                WeeklyTimeSpanEditEvent.Action.PREVIEW,
                timeSpan
            )
        )
    }

    private fun showStartDayDialog() {
        val timeSpan = weeklyTimeSpanViewModel.timeSpan

        AlertDialog.Builder(context!!)
            .setSingleChoiceItems(days, timeSpan.start.localizedDay) { dialog, which ->
                startDay.text = days[which]

                timeSpan.start.localizedDay = which
                preview(timeSpan)

                dialog.cancel()
            }
            .show()
    }

    private fun showEndDayDialog() {
        val timeSpan = weeklyTimeSpanViewModel.timeSpan

        AlertDialog.Builder(context!!)
            .setSingleChoiceItems(days, timeSpan.end.localizedDay) { dialog, which ->
                endDay.text = days[which]

                timeSpan.end.localizedDay = which
                preview(timeSpan)

                dialog.cancel()
            }
            .show()
    }

    private fun showStartTimeDialog() {
        val timeSpan = weeklyTimeSpanViewModel.timeSpan

        TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener
            { _, hourOfDay, minute ->
                startTime.text = Time.formatTime(hourOfDay, minute, locale)

                timeSpan.start.hour = hourOfDay
                timeSpan.start.minute = minute
                timeSpan.start.second = 0
                timeSpan.start.millisecond = 0

                preview(timeSpan)
            },
            timeSpan.start.hour,
            timeSpan.start.minute,
            DateFormat.is24HourFormat(context)
        ).show()
    }

    private fun showEndTimeDialog() {
        val timeSpan = weeklyTimeSpanViewModel.timeSpan

        TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                endTime.text = Time.formatTime(hourOfDay, minute, locale)

                timeSpan.end.hour = hourOfDay
                timeSpan.end.minute = minute
                timeSpan.start.second = 0
                timeSpan.start.millisecond = 0

                preview(timeSpan)
            },
            timeSpan.end.hour,
            timeSpan.end.minute,
            DateFormat.is24HourFormat(context)
        ).show()
    }

    private fun showApplyToAllDialog() {
        AlertDialog.Builder(context!!)
            .setTitle(R.string.confirmation_weekly_time_span_apply_to_all)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                weeklyTimeSpanViewModel.onEdit(
                    WeeklyTimeSpanEditEvent(
                        WeeklyTimeSpanEditEvent.Action.APPLIED_TO_ALL,
                        weeklyTimeSpanViewModel.timeSpan
                    )
                )
            }
            .show()
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(context!!)
            .setTitle(R.string.confirmation_weekly_time_span_delete)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                weeklyTimeSpanViewModel.onEdit(
                    WeeklyTimeSpanEditEvent(
                        WeeklyTimeSpanEditEvent.Action.DELETED,
                        weeklyTimeSpanViewModel.timeSpan
                    )
                )
            }
            .show()
    }
}
