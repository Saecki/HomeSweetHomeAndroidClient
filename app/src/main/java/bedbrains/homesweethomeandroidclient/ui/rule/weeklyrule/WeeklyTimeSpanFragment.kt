package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
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
import java.util.*

class WeeklyTimeSpanFragment() : Fragment() {

    private val weeklyTimeSpanViewModel: WeeklyTimeSpanViewModel by viewModels()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var ruleValueView: RuleValueView
    private lateinit var locale: Locale

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        locale = Locale.getDefault()

        val binding = FragmentWeeklyTimeSpanBinding.inflate(inflater)
        swipeRefreshLayout = binding.swipeRefreshLayout
        ruleValueView = RuleValueView(binding.ruleValue)

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
            0,
            0,
            DateFormat.is24HourFormat(context)
        )
        val endTimePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                endTime.text = Time.formatTime(hourOfDay, minute, locale)
            },
            0,
            0,
            DateFormat.is24HourFormat(context)
        )

        for (i in days.indices) {
            days[i] = Time.formatWeekDayFull(i, locale)
        }

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

                startTimePickerDialog.updateTime(it.start.hour, it.start.minute)
                endTimePickerDialog.updateTime(it.end.hour, it.end.minute)

                ruleValueView.bind(it.value) { }
            }
        })

        startDay.setOnClickListener {
            with(weeklyTimeSpanViewModel.timeSpan.value!!) {
                AlertDialog.Builder(context)
                    .setSingleChoiceItems(days, this.start.localizedDay) { dialog, which ->
                        startDay.text = days[which]
                        this.start.localizedDay = which
                        dialog.cancel()
                    }
                    .show()
            }
        }
        endDay.setOnClickListener {
            with(weeklyTimeSpanViewModel.timeSpan.value!!) {
                AlertDialog.Builder(context)
                    .setSingleChoiceItems(days, this.end.localizedDay) { dialog, which ->
                        endDay.text = days[which]
                        this.end.localizedDay = which
                        dialog.cancel()
                    }
                    .show()
            }
        }

        startTime.setOnClickListener {
            startTimePickerDialog.show()
        }
        endTime.setOnClickListener {
            endTimePickerDialog.show()
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
            R.id.action_apply_to_all -> Unit//TODO
            R.id.action_delete -> Unit//TODO
            else -> return super.onOptionsItemSelected(item)
        }

        return false
    }
}