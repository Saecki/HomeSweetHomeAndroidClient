package bedbrains.homesweethomeandroidclient

import android.content.SharedPreferences
import android.content.res.Resources
import bedbrains.homesweethomeandroidclient.ui.Theme
import bedbrains.shared.datatypes.temperature.Temperature

object Res {
    lateinit var preferences: SharedPreferences
    lateinit var resources: Resources

    fun getPrefString(key: Int, defaultValue: String): String {
        return try {
            preferences.getString(resources.getString(key), defaultValue)!!
        } catch (e: Exception) {
            defaultValue
        }
    }

    fun getPrefInt(key: Int, defaultValue: Int): Int {
        return try {
            preferences.getInt(resources.getString(key), defaultValue)
        } catch (e: Exception) {
            defaultValue
        }
    }

    fun getPrefStringAsInt(key: Int, defaultValue: Int): Int {
        return try {
            preferences.getString(resources.getString(key), defaultValue.toString())!!.toInt()
        } catch (e: Exception) {
            defaultValue
        }
    }

    fun getPrefStringAsDouble(key: Int, defaultValue: Double): Double {
        return try {
            preferences.getString(resources.getString(key), defaultValue.toString())!!.toDouble()
        } catch (e: Exception) {
            defaultValue
        }
    }

    fun setTempDecimals(value: Any) {
        Temperature.globalDecimals = try {
            value.toString().toInt()
        } catch (e: ClassCastException) {
            Temperature.DEFAULT_DECIMALS
        }
    }

    fun setTempUnit(value: Any) {
        val index = try {
            value.toString().toInt()
        } catch (e: ClassCastException) {
            Temperature.DEFAULT_UNIT.index
        }
        Temperature.globalUnit = when (index) {
            Temperature.Unit.KELVIN.index -> Temperature.Unit.KELVIN
            Temperature.Unit.CELSIUS.index -> Temperature.Unit.CELSIUS
            Temperature.Unit.FAHRENHEIT.index -> Temperature.Unit.FAHRENHEIT
            else -> Temperature.DEFAULT_UNIT
        }
    }

    fun setTheme(value: Any) {
        val mode = try {
            value.toString().toInt()
        } catch (e: ClassCastException) {
            Theme.DEFAULT
        }
        Theme.setMode(mode)
    }

    fun setNetworkUpdate(value: Any) {
        val delay = try {
            value.toString().toLong()
        } catch (e: Exception) {
            5L
        }

        if (delay == -1L) {
            DataRepository.stopAutomaticUpdate()
        } else {
            DataRepository.startAutomaticUpdate(delay)
        }
    }
}