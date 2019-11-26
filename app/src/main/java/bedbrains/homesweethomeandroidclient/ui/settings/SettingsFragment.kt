package bedbrains.homesweethomeandroidclient.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import bedbrains.homesweethomeandroidclient.MainActivity
import bedbrains.homesweethomeandroidclient.R
import bedbrains.shared.datatypes.Temperature
import java.lang.Exception
import kotlin.math.roundToInt

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        //temperature unit
        val tempUnitKey = MainActivity.res.getString(R.string.pref_temperature_unit_key)
        val tempUnitPreference = findPreference<ListPreference>(tempUnitKey)!!
        tempUnitPreference.setOnPreferenceChangeListener { _, newValue ->
            setTempUnit(newValue)
            true
        }
        setTempUnit(tempUnitPreference.value)

        //temperature decimals
        val tempDecimalsKey = MainActivity.res.getString(R.string.pref_temperature_decimals_key)
        val tempDecimalsPreference = findPreference<ListPreference>(tempDecimalsKey)!!
        tempDecimalsPreference.setOnPreferenceChangeListener { _, newValue ->
            setTempDecimals(newValue)
            true
        }
        setTempDecimals(tempDecimalsPreference.value)

        //animation duration
        val animationDurationKey = MainActivity.res.getString(R.string.pref_animation_duration_key)
        val animationDurationPreference = findPreference<SeekBarPreference>(animationDurationKey)!!
        animationDurationPreference.updatesContinuously = true
        animationDurationPreference.setOnPreferenceChangeListener { preference, newValue ->
            val roundedValue = (newValue as Int / 10.0).roundToInt() * 10
            preference.summary = when (roundedValue) {
                0 -> "Off"
                else -> roundedValue.toString() + "ms"
            }
            animationDurationPreference.value = roundedValue
            false
        }
        animationDurationPreference.summary = when (animationDurationPreference.value) {
            0 -> "Off"
            else -> animationDurationPreference.value.toString() + "ms"
        }
    }

    fun setTempDecimals(value: Any) {
        Temperature.globalDecimals = try {
            value.toString().toInt()
        } catch (e: Exception) {
            Log.e("PREF", "Couldn't cat value: '$value'")
            Temperature.DEFAULT_DECIMALS
        }
    }

    fun setTempUnit(value: Any) {
        val index = try {
            value.toString().toInt()
        } catch (e: Exception) {
            Log.e("PREF", "Couldn't cat value: '$value'")
            Temperature.Unit.CELSIUS.index
        }
        Temperature.globalUnit = when (index) {
            Temperature.Unit.KELVIN.index -> Temperature.Unit.KELVIN
            Temperature.Unit.CELSIUS.index -> Temperature.Unit.CELSIUS
            Temperature.Unit.FAHRENHEIT.index -> Temperature.Unit.FAHRENHEIT
            else -> Temperature.DEFAULT_UNIT
        }
    }
}