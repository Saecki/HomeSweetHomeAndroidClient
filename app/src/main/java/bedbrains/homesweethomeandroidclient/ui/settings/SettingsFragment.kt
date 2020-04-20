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
        netHostPreference.setOnPreferenceChangeListener { _, newValue ->
            netHostPreference.summary = newValue.toString()

            DataRepository.updateRestClient()
            true
        }
        netHostPreference.summary = netHostPreference.text

        //network port
        val netPortKey = resources.getString(R.string.pref_network_port_key)
        val netPortPreference = findPreference<EditTextPreference>(netPortKey)!!
        netPortPreference.setOnPreferenceChangeListener { _, newValue ->
            netPortPreference.summary = newValue.toString()

            DataRepository.updateRestClient()
            true
        }
        netPortPreference.summary = netPortPreference.text

        //network automatic update delay
        val netAutomaticUpdateDelayKey =
            resources.getString(R.string.pref_network_automatic_update_delay_key)
        val netAutomaticUpdateDelayPreference =
            findPreference<ListPreference>(netAutomaticUpdateDelayKey)!!
        netAutomaticUpdateDelayPreference.setOnPreferenceChangeListener { _, newValue ->
            setNetworkUpdate(newValue)
            true
        }
        setNetworkUpdate(netAutomaticUpdateDelayPreference.value)

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

    private fun setTempDecimals(value: Any) {
        Temperature.globalDecimals = try {
            value.toString().toInt()
        } catch (e: ClassCastException) {
            Temperature.DEFAULT_DECIMALS
        }
    }

    private fun setTempUnit(value: Any) {
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

    private fun setDarkMode(value: Any) {
        val mode = try {
            value.toString().toInt()
        } catch (e: ClassCastException) {
            Theme.DEFAULT
        }
        Theme.setMode(mode)
    }

    private fun setNetworkUpdate(value: Any) {
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