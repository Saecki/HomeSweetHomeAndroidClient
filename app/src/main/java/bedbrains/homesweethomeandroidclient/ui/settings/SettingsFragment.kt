package bedbrains.homesweethomeandroidclient.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.ui.Theme
import bedbrains.shared.datatypes.temperature.Temperature
import java.lang.ClassCastException
import kotlin.math.roundToInt

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        //temperature unit
        val tempUnitKey = resources.getString(R.string.pref_temperature_unit_key)
        val tempUnitPreference = findPreference<ListPreference>(tempUnitKey)!!
        tempUnitPreference.setOnPreferenceChangeListener { _, newValue ->
            setTempUnit(newValue)
            true
        }
        setTempUnit(tempUnitPreference.value)

        //temperature decimals
        val tempDecimalsKey = resources.getString(R.string.pref_temperature_decimals_key)
        val tempDecimalsPreference = findPreference<ListPreference>(tempDecimalsKey)!!
        tempDecimalsPreference.setOnPreferenceChangeListener { _, newValue ->
            setTempDecimals(newValue)
            true
        }
        setTempDecimals(tempDecimalsPreference.value)

        //dark mode
        val darkModeKey = resources.getString(R.string.pref_theme_key)
        val darkModePreference = findPreference<ListPreference>(darkModeKey)!!
        darkModePreference.setOnPreferenceChangeListener { _, newValue ->
            setDarkMode(newValue)
            true
        }

        //animation duration
        val animationDurationKey = resources.getString(R.string.pref_animation_duration_key)
        val animationDurationPreference = findPreference<SeekBarPreference>(animationDurationKey)!!
        animationDurationPreference.updatesContinuously = true
        animationDurationPreference.setOnPreferenceChangeListener { preference, newValue ->
            val roundedValue = (newValue as Int / 10.0).roundToInt() * 10
            preference.summary = when (roundedValue) {
                0 -> resources.getString(R.string.state_off)
                else -> roundedValue.toString() + "ms"
            }
            animationDurationPreference.value = roundedValue
            false
        }
        animationDurationPreference.summary = when (animationDurationPreference.value) {
            0 -> resources.getString(R.string.state_off)
            else -> animationDurationPreference.value.toString() + "ms"
        }
    }

    fun setTempDecimals(value: Any) {
        Temperature.globalDecimals = try {
            value.toString().toInt()
        } catch (e: ClassCastException) {
            Log.e("PREF", "Couldn't cast value: '$value'")
            Temperature.DEFAULT_DECIMALS
        }
    }

    fun setTempUnit(value: Any) {
        val index = try {
            value.toString().toInt()
        } catch (e: ClassCastException) {
            Log.e("PREF", "Couldn't cast value: '$value'")
            Temperature.DEFAULT_UNIT.index
        }
        Temperature.globalUnit = when (index) {
            Temperature.Unit.KELVIN.index -> Temperature.Unit.KELVIN
            Temperature.Unit.CELSIUS.index -> Temperature.Unit.CELSIUS
            Temperature.Unit.FAHRENHEIT.index -> Temperature.Unit.FAHRENHEIT
            else -> Temperature.DEFAULT_UNIT
        }
    }

    fun setDarkMode(value: Any) {
        val mode = try {
            value.toString().toInt()
        } catch (e: ClassCastException) {
            Log.e("PREF", "Couldn't cast value: '$value'")
            Theme.DEFAULT
        }
        Theme.setMode(mode)
    }
}