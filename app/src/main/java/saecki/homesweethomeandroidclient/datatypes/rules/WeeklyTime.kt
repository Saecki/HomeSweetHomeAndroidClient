package saecki.homesweethomeandroidclient.datatypes.rules

class WeeklyTime {

    companion object {
        fun seconds(seconds: Int): WeeklyTime {
            return WeeklyTime(
                seconds / (24 * 60 * 60) % 7,
                seconds / (60 * 60) % 24,
                seconds / 60 % 60,
                seconds
            )
        }
    }

    constructor(day: Int, hour: Int, minute: Int, second: Int) {
        this.day = day
        this.hour = hour
        this.minute = minute
        this.second = second
    }

    constructor()

    private var day = 0
        set(day: Int) {
            field = clamp(day, 0, 6)
        }

    private var hour = 0
        set(hour: Int) {
            field = clamp(hour, 0, 24)
        }

    private var minute = 0
        set(hour: Int) {
            field = clamp(minute, 0, 59)
        }

    private var second: Int = 0
        set(second: Int) {
            field = clamp(second, 0, 59)
        }

    fun inDays(): Double {
        return day + hour / 60.0 + minute / (24.0 * 60.0) + second / (24.0 * 60.0 * 60.0)
    }

    fun inHours(): Double {
        return day * 24 + hour + minute / 60.0 + second / (60.0 * 60.0)
    }

    fun inMinutes(): Double {
        return day * 24 * 60 + hour * 60 + minute + second / 60.0
    }

    fun inSeconds(): Int {
        return day * 24 * 60 * 60 + hour * 60 * 60 + minute + 60 + second
    }

    private fun clamp(value: Int, min: Int, max: Int): Int {
        return when {
            value < min -> min
            value > max -> max
            else -> value
        }
    }
}