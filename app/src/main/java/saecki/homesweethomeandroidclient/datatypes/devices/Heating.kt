package saecki.homesweethomeandroidclient.datatypes.devices

import java.math.BigDecimal
import kotlin.math.pow
import kotlin.math.round

class Heating(id: String, name: String, var actualTemp: Double, var targetTemp: Double) :
    Device(id, type, name) {

    companion object {
        val type = "Heating"
    }

    fun formatActualTemp(): String{
        return roundToDecimals(actualTemp, 1).toString()
    }

    fun formatTargetTemp(): String {
        return roundToDecimals(targetTemp, 1).toString()
    }

    fun roundToDecimals(value: Double, decimals: Int): Double{
        val power = 10.0.pow(decimals)
        return round(value * power) / power
    }
}