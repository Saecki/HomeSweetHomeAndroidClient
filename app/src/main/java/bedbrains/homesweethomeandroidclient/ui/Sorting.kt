package bedbrains.homesweethomeandroidclient.ui

import bedbrains.homesweethomeandroidclient.R

class Sorting {

    enum class DeviceCriterion(val resId: Int) {
        Manually(R.string.sorting_criteria_manually),
        Name(R.string.sorting_criteria_name),
        Room(R.string.sorting_criteria_room),
    }

    companion object {
        const val ASCENDING = true
        const val DESCENDING = false

        val DEFAULT_DEVICE_CRITERION = DeviceCriterion.Manually
        const val DEFAULT_ORDER = ASCENDING
    }
}
