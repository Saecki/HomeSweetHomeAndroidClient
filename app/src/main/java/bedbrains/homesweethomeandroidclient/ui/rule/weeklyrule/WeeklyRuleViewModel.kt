package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import androidx.lifecycle.ViewModel
import bedbrains.shared.datatypes.rules.WeeklyRule
import bedbrains.shared.datatypes.rules.WeeklyTime
import bedbrains.shared.datatypes.rules.WeeklyTimeSpan

class WeeklyRuleViewModel : ViewModel() {
    var rule: WeeklyRule = WeeklyRule("lkasjf", "cool rule")
    var initialCreation = true

    init {
        rule.timeSpans.add(
            WeeklyTimeSpan(
                WeeklyTime(0, 13, 0, 0, 0),
                WeeklyTime(0, 24, 0, 0, 0)
            )
        )
        rule.timeSpans.add(
            WeeklyTimeSpan(
                WeeklyTime(1, 13, 0, 0, 0),
                WeeklyTime(1, 24, 0, 0, 0)
            )
        )
        rule.timeSpans.add(
            WeeklyTimeSpan(
                WeeklyTime(2, 13, 0, 0, 0),
                WeeklyTime(2, 24, 0, 0, 0)
            )
        )
        rule.timeSpans.add(
            WeeklyTimeSpan(
                WeeklyTime(3, 13, 0, 0, 0),
                WeeklyTime(3, 24, 0, 0, 0)
            )
        )
        rule.timeSpans.add(
            WeeklyTimeSpan(
                WeeklyTime(4, 13, 0, 0, 0),
                WeeklyTime(5, 2, 0, 0, 0)
            )
        )
        rule.timeSpans.add(
            WeeklyTimeSpan(
                WeeklyTime(5, 13, 0, 0, 0),
                WeeklyTime(6, 2, 0, 0, 0)
            )
        )
        rule.timeSpans.add(
            WeeklyTimeSpan(
                WeeklyTime(6, 13, 0, 0, 0),
                WeeklyTime(6, 24, 0, 0, 0)
            )
        )
    }
}