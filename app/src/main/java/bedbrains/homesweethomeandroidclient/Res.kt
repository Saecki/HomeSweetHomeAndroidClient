package bedbrains.homesweethomeandroidclient

import android.content.SharedPreferences
import android.content.res.Resources
import android.util.TypedValue
import bedbrains.homesweethomeandroidclient.ui.Theme
import bedbrains.shared.datatypes.temperature.Temperature

object Res {
    lateinit var preferences: SharedPreferences
    lateinit var resources: Resources
    lateinit var theme: Resources.Theme

    fun getPrefString(key: Int, defaultValue: String): String = try {
        preferences.getString(resources.getString(key), defaultValue)!!
    } catch (e: Exception) {
        defaultValue
    }

    fun putPrefString(key: Int, value: String) {
        preferences.edit()
            .putString(resources.getString(key), value)
            .apply()
    }

    fun getPrefInt(key: Int, defaultValue: Int): Int = try {
        preferences.getInt(resources.getString(key), defaultValue)
    } catch (e: Exception) {
        defaultValue
    }

    fun putPrefInt(key: Int, value: Int) {
        preferences.edit()
            .putInt(resources.getString(key), value)
            .apply()
    }

    fun getPrefBool(key: Int, defaultValue: Boolean): Boolean = try {
        preferences.getBoolean(resources.getString(key), defaultValue)
    } catch (e: Exception) {
        defaultValue
    }

    fun putPrefBool(key: Int, value: Boolean) {
        preferences.edit()
            .putBoolean(resources.getString(key), value)
            .apply()
    }

    fun getPrefStringAsInt(key: Int, defaultValue: Int): Int = try {
        preferences.getString(resources.getString(key), defaultValue.toString())!!.toInt()
    } catch (e: Exception) {
        defaultValue
    }

    fun getPrefStringAsDouble(key: Int, defaultValue: Double): Double = try {
        preferences.getString(resources.getString(key), defaultValue.toString())!!.toDouble()
    } catch (e: Exception) {
        defaultValue
    }

    fun getAttrColor(resId: Int, resolveRefs: Boolean = true): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(resId, typedValue, resolveRefs)

        return typedValue.data
    }

    fun setTempDecimals(value: Any) {
        Temperature.globalDecimals = try {
            value.toString().toInt()
        } catch (e: Exception) {
            Temperature.DEFAULT_DECIMALS
        }
    }

    fun setTempUnit(value: Any) {
        try {
            val ordinal = value.toString().toInt()
            Temperature.globalUnit = Temperature.Unit.values()[ordinal]
        } catch (e: Exception) {
            Temperature.globalUnit = Temperature.DEFAULT_UNIT
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