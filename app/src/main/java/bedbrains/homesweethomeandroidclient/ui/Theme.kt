package bedbrains.homesweethomeandroidclient.ui

import androidx.appcompat.app.AppCompatDelegate

object Theme {
    const val DARK = 0
    const val LIGHT = 1
    const val SYSTEM = 2
    const val BATTERY = 3
    const val DEFAULT = DARK

    fun setMode(mode: Int) {
        when (mode) {
            LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            BATTERY -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            }
        }
    }
}