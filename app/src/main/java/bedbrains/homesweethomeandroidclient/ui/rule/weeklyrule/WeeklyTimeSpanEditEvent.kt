package bedbrains.homesweethomeandroidclient.ui.rule.weeklyrule

import bedbrains.shared.datatypes.time.WeeklyTimeSpan

data class WeeklyTimeSpanEditEvent(val action: Action, val timeSpan: WeeklyTimeSpan) {

    enum class Action {
        EDITED,
        CANCELED,
        DELETED,
        APPLIED_TO_ALL,
        PREVIEW,
    }
}
