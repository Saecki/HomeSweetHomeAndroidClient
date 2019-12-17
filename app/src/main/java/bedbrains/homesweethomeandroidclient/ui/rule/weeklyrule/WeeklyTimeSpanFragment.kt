package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import bedbrains.homesweethomeandroidclient.R
import bedbrains.platform.Tools
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class WeeklyTimeSpanFragment() : Fragment() {

    lateinit var weeklyTimeSpanViewModel: WeeklyTimeSpanViewModel
    lateinit var locale: Locale

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        weeklyTimeSpanViewModel = ViewModelProviders.of(this).get(WeeklyTimeSpanViewModel::class.java)
        locale = Locale.getDefault()

        val root = inflater.inflate(R.layout.fragment_weekly_time_span, container, false)
        val days = Array(7) { "" }
        val startDay = root.findViewById<TextView>(R.id.start_day)
        val endDay = root.findViewById<TextView>(R.id.end_day)
        val startTime = root.findViewById<TextView>(R.id.start_time)
        val endTime = root.findViewById<TextView>(R.id.end_time)
        val doneButton = root.findViewById<FloatingActionButton>(R.id.done_button)
        val startTimePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                startTime.text = Tools.formatTime(hourOfDay, minute, locale, context)
            },
            weeklyTimeSpanViewModel.timeSpan.start.hour,
            weeklyTimeSpanViewModel.timeSpan.start.minute,
            DateFormat.is24HourFormat(context)
        )
        val endTimePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                endTime.text = Tools.formatTime(hourOfDay, minute, locale, context)
            },
            weeklyTimeSpanViewModel.timeSpan.start.hour,
            weeklyTimeSpanViewModel.timeSpan.start.minute,
            DateFormat.is24HourFormat(context)
        )

        //day
        for (i in days.indices) {
            days[i] = Tools.formatWeekDayFull(i, locale)
        }

        //start day
        startDay.text = Tools.formatWeekDayFull(weeklyTimeSpanViewModel.timeSpan.start.localizedDay, locale)
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
        endDay.text = Tools.formatWeekDayFull(weeklyTimeSpanViewModel.timeSpan.end.localizedDay, locale)
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
        startTime.text = Tools.formatTime(weeklyTimeSpanViewModel.timeSpan.start.hour, weeklyTimeSpanViewModel.timeSpan.start.minute, locale, context)
        startTime.setOnClickListener {
            startTimePickerDialog.show()
        }

        //end time
        endTime.text = Tools.formatTime(weeklyTimeSpanViewModel.timeSpan.end.hour, weeklyTimeSpanViewModel.timeSpan.end.minute, locale, context)
        endTime.setOnClickListener {
            endTimePickerDialog.show()
        }

        doneButton.setOnClickListener {
            root.findNavController().popBackStack()
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.weekly_time_span, menu)
    }

}