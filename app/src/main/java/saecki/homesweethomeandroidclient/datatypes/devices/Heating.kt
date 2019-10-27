package saecki.homesweethomeandroidclient.datatypes.devices

import saecki.homesweethomeandroidclient.datatypes.Temperature
import java.math.BigDecimal
import kotlin.math.pow
import kotlin.math.round

class Heating(id: String, name: String, var actualTemp: Temperature, var targetTemp: Temperature) :
    Device(id, type, name) {

    companion object {
        const val type = 1
    }
}