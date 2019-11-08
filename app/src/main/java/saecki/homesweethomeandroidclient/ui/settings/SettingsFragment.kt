package saecki.homesweethomeandroidclient.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import saecki.homesweethomeandroidclient.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}