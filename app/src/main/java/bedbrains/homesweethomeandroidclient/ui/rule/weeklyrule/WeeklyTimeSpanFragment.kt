package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.FragmentWeeklyTimeSpanBinding
import bedbrains.platform.Time
import java.util.*

class WeeklyTimeSpanFragment() : Fragment() {

    private val weeklyTimeSpanViewModel: WeeklyTimeSpanViewModel by viewModels()

    lateinit var locale: Locale

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        locale = Locale.getDefault()

        val binding = FragmentWeeklyTimeSpanBinding.inflate(inflater)
        val days = Array(7) { "" }
        val startDay = binding.startDay
        val endDay = binding.endDay
        val startTime = binding.startTime
        val endTime = binding.endTime
        val doneButton = binding.doneButton
        val startTimePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                startTime.text = Time.formatTime(hourOfDay, minute, locale)
            },
            weeklyTimeSpanViewModel.timeSpan.start.hour,
            weeklyTimeSpanViewModel.timeSpan.start.minute,
            DateFormat.is24HourFormat(context)
        )
        val endTimePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                endTime.text = Time.formatTime(hourOfDay, minute, locale)
            },
            weeklyTimeSpanViewModel.timeSpan.start.hour,
            weeklyTimeSpanViewModel.timeSpan.start.minute,
            DateFormat.is24HourFormat(context)
        )

        //day
        for (i in days.indices) {
            days[i] = Time.formatWeekDayFull(i, locale)
        }

        //start day
        startDay.text = Time.formatWeekDayFull(weeklyTimeSpanViewModel.timeSpan.start.localizedDay, locale)
        startDay.setOnClickListener {

            AlertDialog.Builder(context).apply {
                setNegativeButton(android.R.string.cancel, null)
                setSingleChoiceItems(days, weeklyTimeSpanViewModel.timeSpan.start.localizedDay) { dialog, which ->
                    startDay.text = days[which]
                    weeklyTimeSpanViewModel.timeSpan.start.localizedDay = which
                    dialog.cancel()
                }
            }.show()
        }

        //end day
        endDay.text = Time.formatWeekDayFull(weeklyTimeSpanViewModel.timeSpan.end.localizedDay, locale)
        endDay.setOnClickListener {

            AlertDialog.Builder(context).apply {
                setNegativeButton(android.R.string.cancel, null)
                setSingleChoiceItems(days, weeklyTimeSpanViewModel.timeSpan.end.localizedDay) { dialog, which ->
                    endDay.text = days[which]
                    weeklyTimeSpanViewModel.timeSpan.end.localizedDay = which
                    dialog.cancel()
                }
            }.show()
        }

        //start time
        startTime.text = Time.formatTime(weeklyTimeSpanViewModel.timeSpan.start.hour, weeklyTimeSpanViewModel.timeSpan.start.minute, locale)
        startTime.setOnClickListener {
            startTimePickerDialog.show()
        }

        //end time
        endTime.text = Time.formatTime(weeklyTimeSpanViewModel.timeSpan.end.hour, weeklyTimeSpanViewModel.timeSpan.end.minute, locale)
        endTime.setOnClickListener {
            endTimePickerDialog.show()
        }

        doneButton.setOnClickListener {
            binding.root.findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.weekly_time_span, menu)
    }

}