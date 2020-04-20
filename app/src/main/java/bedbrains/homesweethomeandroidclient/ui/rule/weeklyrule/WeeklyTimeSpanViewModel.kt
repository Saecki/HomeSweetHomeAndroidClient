package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import androidx.lifecycle.ViewModel
import bedbrains.shared.datatypes.time.WeeklyTimeSpan

class WeeklyTimeSpanViewModel : ViewModel() {
    lateinit var timeSpan: WeeklyTimeSpan
    lateinit var onEdit: (WeeklyTimeSpanEditEvent) -> Unit
}