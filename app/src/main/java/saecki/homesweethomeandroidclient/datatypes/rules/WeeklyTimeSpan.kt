package saecki.homesweethomeandroidclient.datatypes.rules

class WeeklyTimeSpan() {

    var start: WeeklyTime = WeeklyTime()
    var end: WeeklyTime = WeeklyTime()

    fun length(): WeeklyTime {
        val max = WeeklyTime.MAX.inMillis()
        return WeeklyTime((((end.inMillis() - start.inMillis()) % max) + max) % max)
    }

}
