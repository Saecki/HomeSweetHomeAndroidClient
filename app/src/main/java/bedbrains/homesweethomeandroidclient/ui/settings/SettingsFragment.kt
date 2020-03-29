package bedbrains.homesweethomeandroidclient.ui.settings

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.ui.Theme
import bedbrains.shared.datatypes.temperature.Temperature

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        //network ip address
        val netHostKey = resources.getString(R.string.pref_network_host_key)
        val netHostPreference = findPreference<EditTextPreference>(netHostKey)!!
        netHostPreference.setOnPreferenceChangeListener { _, _ ->
            DataRepository.buildNewRestClient()
            true
        }

        //network port
        val netPortKey = resources.getString(R.string.pref_network_port_key)
        val netPortPreference = findPreference<EditTextPreference>(netPortKey)!!
        netPortPreference.setOnPreferenceChangeListener { _, _ ->
            DataRepository.buildNewRestClient()
            true
        }

        //network update delay
        val netUpdateDelayKey = resources.getString(R.string.pref_network_update_delay_key)
        val netUpdateDelayPreference = findPreference<ListPreference>(netUpdateDelayKey)!!
        netUpdateDelayPreference.setOnPreferenceChangeListener { _, newValue ->
            //TODO
            true
        }

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

    fun setDarkMode(value: Any) {
        val mode = try {
            value.toString().toInt()
        } catch (e: ClassCastException) {
            Theme.DEFAULT
        }
        Theme.setMode(mode)
    }
}