package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import androidx.lifecycle.ViewModel
import bedbrains.shared.datatypes.rules.WeeklyRule
import bedbrains.shared.datatypes.rules.WeeklyTime
import bedbrains.shared.datatypes.rules.WeeklyTimeSpan

class WeeklyRuleViewModel : ViewModel() {
    var weeklyRule: WeeklyRule = WeeklyRule("lkasjf", "lasjf")

    init {

        weeklyRule.timeSpans.add(
            WeeklyTimeSpan(
                WeeklyTime(3, 5, 0, 0, 0),
                WeeklyTime(3, 9, 15, 0, 0)
            )
        )
    }
}