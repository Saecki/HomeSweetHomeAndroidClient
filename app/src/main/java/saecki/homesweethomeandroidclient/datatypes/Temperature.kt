package saecki.homesweethomeandroidclient.datatypes

import saecki.homesweethomeandroidclient.Tools
import java.lang.Exception

class Temperature {

    private var temp: Double = 273.0
        private set(temp) {
            field = Tools.clamp(temp, 0.0, Double.MAX_VALUE)
        }

    constructor(temp: Double, unit: Unit) {
        set(temp, unit)
    }

    constructor()

    companion object {
        val MIN = Temperature(0.0, Unit.KELVIN)
        val DEFAULT_UNIT = Unit.CELSIUS
        val DEFAULT_DECIMALS = 1

        var globalUnit = DEFAULT_UNIT
        var globalDecimals = DEFAULT_DECIMALS

    }

    enum class Unit(val unit: String, val index: Int) {
        KELVIN("K", 0),
        CELSIUS("°C", 1),
        FAHRENHEIT("°F", 2)
    }

    fun getGlobal(): Double {
        return get(globalUnit)
    }

    fun setGlobal(temp: Double) {
        set(temp, globalUnit)
    }

    fun formatGlobal(appendUnit: Boolean): String {
        return format(globalUnit, globalDecimals, appendUnit)
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

    fun format(unit: Unit, decimals: Int, appendUnit: Boolean): String {
        return roundTo(get(unit), decimals) + when (appendUnit) {
            true -> unit.unit
            false -> ""
        }
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

    fun warmer(temp: Temperature): Boolean {
        return getKelvin() > temp.getKelvin()
    }

    fun colder(temp: Temperature): Boolean {
        return getKelvin() < temp.getKelvin()
    }

    override fun equals(other: Any?): Boolean {
        return try {
            val otherTemp = other as Temperature
            getKelvin() == otherTemp.getKelvin()
        } catch (e: Exception) {
            false
        }
    }

    private fun roundTo(value: Double, decimals: Int): String {
        val format: String = "%." + decimals + "f"
        return format.format(value)
    }
}