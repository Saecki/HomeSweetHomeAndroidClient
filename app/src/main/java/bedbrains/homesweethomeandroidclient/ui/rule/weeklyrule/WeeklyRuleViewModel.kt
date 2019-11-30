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
                WeeklyTime(3, 5, 0, 0, 0),
                WeeklyTime(3, 9, 15, 0, 0)
            )
        )
    }
}