package saecki.homesweethomeandroidclient.datatypes.rules

class WeeklyRule : Rule() {
    var timespans: List<WeeklyTimeSpan> = ArrayList()

    fun sort() {
        timespans = timespans.sortedBy { weeklyTimeSpan -> weeklyTimeSpan.start.inSeconds() }
    }
}