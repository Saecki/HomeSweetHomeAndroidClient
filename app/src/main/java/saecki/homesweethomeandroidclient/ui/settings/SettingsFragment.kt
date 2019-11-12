package saecki.homesweethomeandroidclient.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import saecki.homesweethomeandroidclient.MainActivity
import saecki.homesweethomeandroidclient.R
import kotlin.math.roundToInt

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val key: String = MainActivity.res.getString(R.string.pref_animation_duration_key)
        val animationDurationPreference = findPreference<SeekBarPreference>(key)!!
        animationDurationPreference.summary = when (animationDurationPreference.value) {
            0 -> "Off"
            else -> animationDurationPreference.value.toString() + "ms"
        }
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

    }
}