package saecki.homesweethomeandroidclient.datatypes

import saecki.homesweethomeandroidclient.MainActivity
import saecki.homesweethomeandroidclient.R

class Temperature {

    private var temp: Double = 273.0

    constructor(temp: Double, unit: Unit) {
        set(temp, unit)
    }

    constructor()

    companion object {

        fun globalUnit(): Unit {
            val key: String = MainActivity.res.getString(R.string.pref_temperature_unit_key)
            val index = MainActivity.getPrefStringAsInt(key, 1)

            for (u in Unit.values()) {
                if (index == u.index) {
                    return u
                }
            }

            return Unit.CELSIUS
        }

        fun globalDecimals(): Int {
            val key: String = MainActivity.res.getString(R.string.pref_temperature_decimals_key)
            val decimals: Int = MainActivity.getPrefStringAsInt(key, 1)
            return if (decimals == -1) {
                1
            } else {
                decimals
            }
        }
    }

    enum class Unit(val unit: String, val index: Int) {
        KELVIN("K", 0),
        CELSIUS("°C", 1),
        FAHRENHEIT("°F", 2)
    }

    fun getGlobal(): Double {
        return get(globalUnit())
    }

    fun setGlobal(temp: Double) {
        set(temp, globalUnit())
    }

    fun formatGlobal(): String {
        return format(globalUnit(), globalDecimals())
    }

    fun get(unit: Unit): Double {
        when (unit) {
            Unit.KELVIN -> return getKelvin()
            Unit.CELSIUS -> return getCelsius()
            Unit.FAHRENHEIT -> return getFahrenheit()
        }
    }

    fun set(temp: Double, unit: Unit) {
        when (unit) {
            Unit.KELVIN -> setKelvin(temp)
            Unit.CELSIUS -> setCelsius(temp)
            Unit.FAHRENHEIT -> setFahrenheit(temp)
        }
    }

    fun format(unit: Unit, decimals: Int): String {
        return roundToDecimals(get(unit), decimals) + unit.unit
    }

    fun getKelvin(): Double {
        return temp
    }

    fun setKelvin(temp: Double) {
        this.temp = temp
    }

    fun getCelsius(): Double {
        return temp - 273
    }

    fun setCelsius(temp: Double) {
        this.temp = temp + 273
    }

    fun getFahrenheit(): Double {
        return (9.0 / 5) * (temp - 273) + 32
    }

    fun setFahrenheit(temp: Double) {
        this.temp = (5.0 / 9) * (temp - 32) + 273
    }

    private fun roundToDecimals(value: Double, decimals: Int): String {
        val format: String = "%." + decimals + "f"
        return format.format(value)
    }
}