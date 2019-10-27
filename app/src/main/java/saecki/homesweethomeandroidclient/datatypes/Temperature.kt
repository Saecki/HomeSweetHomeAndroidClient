package saecki.homesweethomeandroidclient.datatypes

import kotlin.math.pow
import kotlin.math.round

class Temperature {

    private var temp: Double = 0.0

    enum class Unit(val unit: String) {
        KELVIN("K"),
        CELSIUS("°C"),
        FAHRENHEIT("°F")
    }

    companion object {
        var globalUnit: Unit = Unit.CELSIUS
        var globalDecimals: Int = 1
    }

    fun getGlobal(): Double {
        when (globalUnit) {
            Unit.KELVIN -> return getKelvin()
            Unit.CELSIUS -> return getCelsius()
            Unit.FAHRENHEIT -> return getFahrenheit()
        }
    }

    fun setGlobal(temp: Double) {
        when (globalUnit) {
            Unit.KELVIN -> setKelvin(temp)
            Unit.CELSIUS -> setCelsius(temp)
            Unit.FAHRENHEIT -> setFahrenheit(temp)
        }
    }

    fun formatGlobal(): String {
        when (globalUnit) {
            Unit.KELVIN -> return formatKelvin()
            Unit.CELSIUS -> return formatCelsius()
            Unit.FAHRENHEIT -> return formatFahrenheit()
        }
    }

    fun getKelvin(): Double {
        return temp
    }

    fun setKelvin(temp: Double) {
        this.temp = temp
    }

    fun formatKelvin(): String {
        return roundToDecimals(getKelvin(), globalDecimals).toString() + Unit.KELVIN.unit
    }

    fun getCelsius(): Double {
        return temp - 273
    }

    fun setCelsius(temp: Double) {
        this.temp = temp + 273
    }

    fun formatCelsius(): String {
        return roundToDecimals(getCelsius(), globalDecimals).toString() + Unit.CELSIUS.unit
    }

    fun getFahrenheit(): Double {
        return (9 / 5) * (temp - 273) + 32
    }

    fun setFahrenheit(temp: Double) {
        this.temp = (5 / 9) * (temp - 32) + 273
    }

    fun formatFahrenheit(): String {
        return roundToDecimals(getFahrenheit(), globalDecimals).toString() + Unit.FAHRENHEIT.unit
    }

    fun roundToDecimals(value: Double, decimals: Int): Double {
        val power = 10.0.pow(decimals)
        return round(value * power) / power
    }

}