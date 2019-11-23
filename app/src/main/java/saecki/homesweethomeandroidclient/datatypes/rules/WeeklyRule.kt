package saecki.homesweethomeandroidclient.datatypes.rules

class WeeklyRule(id: String, name: String) : Rule(id, TYPE, name) {

    companion object {
        val TYPE = 1
    }

    var timespans: MutableList<WeeklyTimeSpan> = mutableListOf()

    fun sort() {
        timespans.sortBy { weeklyTimeSpan ->
            weeklyTimeSpan.start.inMillis()
        }
    }

}