package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import androidx.lifecycle.ViewModel
import bedbrains.shared.datatypes.rules.WeeklyTimeSpan

class WeeklyTimeSpanViewModel : ViewModel() {
    var timeSpan = WeeklyTimeSpan().apply {
        start.localizedDay = 0
        end.localizedDay = 0
    }
}