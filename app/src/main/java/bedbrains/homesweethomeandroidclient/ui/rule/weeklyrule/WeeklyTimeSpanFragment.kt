package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.databinding.FragmentWeeklyTimeSpanBinding
import bedbrains.homesweethomeandroidclient.ui.component.refresh
import bedbrains.homesweethomeandroidclient.ui.value.rulevalue.RuleValueView
import bedbrains.platform.Time
import bedbrains.shared.datatypes.time.WeeklyTimeSpan
import bedbrains.shared.datatypes.upsert
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class WeeklyTimeSpanFragment() : Fragment() {

    private val weeklyTimeSpanViewModel: WeeklyTimeSpanViewModel by viewModels()
    private lateinit var locale: Locale
    private lateinit var days: Array<String>

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var ruleValueView: RuleValueView
    private lateinit var startDay: TextView
    private lateinit var endDay: TextView
    private lateinit var startTime: TextView
    private lateinit var endTime: TextView
    private lateinit var doneButton: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        locale = Locale.getDefault()
        days = Array(7) { i -> Time.formatWeekDayFull(i, locale) }

        val binding = FragmentWeeklyTimeSpanBinding.inflate(inflater)
        swipeRefreshLayout = binding.swipeRefreshLayout
        ruleValueView = RuleValueView(binding.ruleValue, context)
        startDay = binding.startDay
        endDay = binding.endDay
        startTime = binding.startTime
        endTime = binding.endTime
        doneButton = binding.doneButton

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

                    updateRule(timeSpan)
                }
            }
        })

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

        doneButton.setOnClickListener {
            binding.root.findNavController().popBackStack()
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.refresh(viewLifecycleOwner, context)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.weekly_time_span, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> swipeRefreshLayout.refresh(viewLifecycleOwner, context)
            R.id.action_apply_to_all -> showApplyToAllDialog()
            R.id.action_delete -> showDeleteDialog()
            else -> return super.onOptionsItemSelected(item)
        }

        return false
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

        AlertDialog.Builder(context)
            .setSingleChoiceItems(days, timeSpan.start.localizedDay) { dialog, which ->
                startDay.text = days[which]

                timeSpan.start.localizedDay = which
                updateRule(timeSpan)

                dialog.cancel()
            }
            .show()
    }

    private fun showEndDayDialog() {
        val timeSpan = weeklyTimeSpanViewModel.timeSpan.value!!

        AlertDialog.Builder(context)
            .setSingleChoiceItems(days, timeSpan.end.localizedDay) { dialog, which ->
                endDay.text = days[which]

                timeSpan.end.localizedDay = which
                updateRule(timeSpan)

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

                updateRule(timespan)
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

                updateRule(timespan)
            },
            timespan.end.hour,
            timespan.end.minute,
            DateFormat.is24HourFormat(context)
        ).show()
    }

    private fun showApplyToAllDialog() {
        AlertDialog.Builder(context)
            .setTitle(R.string.weekly_time_span_apply_to_all_confirmation)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                applyToAll()
            }
            .show()
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(context)
            .setTitle(R.string.weekly_time_span_delete_confirmation)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                delete()
            }
            .show()
    }
}