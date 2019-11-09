package saecki.homesweethomeandroidclient.datatypes.rules

class WeeklyRule(id: String, name: String) : Rule(id, type, name) {

    companion object {
        val type = 1
    }

    var timespans: List<WeeklyTimeSpan> = ArrayList()

    fun sort() {
        timespans = timespans.sortedBy { weeklyTimeSpan -> weeklyTimeSpan.start.inSeconds() }
    }
}