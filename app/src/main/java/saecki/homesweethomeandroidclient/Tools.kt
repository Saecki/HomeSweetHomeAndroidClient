package saecki.homesweethomeandroidclient

class Tools {
    companion object {
        fun clamp(value: Int, min: Int, max: Int): Int {
            return when {
                value < min -> min
                value > max -> max
                else -> value
            }
        }

        fun clamp(value: Double, min: Double, max: Double): Double {
            return when {
                value < min -> min
                value > max -> max
                else -> value
            }
        }
    }
}