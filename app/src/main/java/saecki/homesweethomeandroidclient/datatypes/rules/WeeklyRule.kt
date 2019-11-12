package saecki.homesweethomeandroidclient.datatypes.rules

class WeeklyRule(id: String, name: String) : Rule(id, type, name) {

    companion object {
        val type = 1
    }

    var timespans: MutableList<WeeklyTimeSpan> = mutableListOf()

    fun sort() {
        timespans.sortBy { weeklyTimeSpan -> weeklyTimeSpan.start.inSeconds() }
    }

    fun combineOverlapping() {
        sort()
        for (i in 0..timespans.size - 2) {
            while (i < timespans.size - 1 && timespans[i].end.inSeconds() >= timespans[i + 1].start.inSeconds()) {
                timespans[i].end = timespans[i + 1].end
                timespans.removeAt(i + 1)
            }
        }
    }
}