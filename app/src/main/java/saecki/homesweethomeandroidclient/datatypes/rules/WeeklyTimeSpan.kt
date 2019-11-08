package saecki.homesweethomeandroidclient.datatypes.rules

import kotlin.math.absoluteValue

class WeeklyTimeSpan() {

    var start: WeeklyTime = WeeklyTime()
    var end: WeeklyTime = WeeklyTime()

    fun length(): WeeklyTime {
        return WeeklyTime.seconds((end.inSeconds() - start.inSeconds()).absoluteValue)
    }

}
