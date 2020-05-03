package bedbrains.homesweethomeandroidclient.ui.settings

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import bedbrains.homesweethomeandroidclient.DataRepository
import bedbrains.homesweethomeandroidclient.R
import bedbrains.homesweethomeandroidclient.Res

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        //network ip address
        val netHostKey = resources.getString(R.string.pref_network_host_key)
        val netHostPreference = findPreference<EditTextPreference>(netHostKey)!!
        netHostPreference.setOnPreferenceChangeListener { _, newValue ->
            netHostPreference.summary = newValue.toString()

            DataRepository.restClient = DataRepository.buildRestClient(host = newValue.toString())
            true
        }
        netHostPreference.summary = netHostPreference.text

        //network port
        val netPortKey = resources.getString(R.string.pref_network_port_key)
        val netPortPreference = findPreference<EditTextPreference>(netPortKey)!!
        netPortPreference.setOnPreferenceChangeListener { _, newValue ->
            netPortPreference.summary = newValue.toString()

            DataRepository.restClient = DataRepository.buildRestClient(port = newValue.toString())
            true
        }
        netPortPreference.summary = netPortPreference.text

        //network automatic update delay
        val netAutomaticUpdateKey = resources.getString(R.string.pref_network_automatic_update_key)
        val netAutomaticUpdatePreference = findPreference<ListPreference>(netAutomaticUpdateKey)!!
        netAutomaticUpdatePreference.setOnPreferenceChangeListener { _, newValue ->
            Res.setNetworkUpdate(newValue)
            true
        }

        //temperature unit
        val tempUnitKey = resources.getString(R.string.pref_temperature_unit_key)
        val tempUnitPreference = findPreference<ListPreference>(tempUnitKey)!!
        tempUnitPreference.setOnPreferenceChangeListener { _, newValue ->
            Res.setTempUnit(newValue)
            true
        }

        //temperature decimals
        val tempDecimalsKey = resources.getString(R.string.pref_temperature_decimals_key)
        val tempDecimalsPreference = findPreference<ListPreference>(tempDecimalsKey)!!
        tempDecimalsPreference.setOnPreferenceChangeListener { _, newValue ->
            Res.setTempDecimals(newValue)
            true
        }

        //dark mode
        val darkModeKey = resources.getString(R.string.pref_theme_key)
        val darkModePreference = findPreference<ListPreference>(darkModeKey)!!
        darkModePreference.setOnPreferenceChangeListener { _, newValue ->
            Res.setTheme(newValue)
            true
        }
    }
}