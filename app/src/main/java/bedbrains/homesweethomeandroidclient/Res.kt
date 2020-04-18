package bedbrains.homesweethomeandroidclient

import android.content.SharedPreferences
import android.content.res.Resources

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
}