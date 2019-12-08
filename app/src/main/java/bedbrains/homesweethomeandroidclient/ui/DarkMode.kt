package bedbrains.homesweethomeandroidclient.ui

import androidx.appcompat.app.AppCompatDelegate

object DarkMode {
    const val DARK = 0
    const val LIGHT = 1
    const val SYSTEM = 2

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
        }
    }
}