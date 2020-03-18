package bedbrains.homesweethomeandroidclient

import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import bedbrains.homesweethomeandroidclient.databinding.ActivityMainBinding
import bedbrains.homesweethomeandroidclient.ui.Theme
import com.google.android.material.appbar.AppBarLayout

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {
        lateinit var activity: MainActivity
        lateinit var preferences: SharedPreferences
        lateinit var res: Resources
        lateinit var appBarLayout: AppBarLayout
        lateinit var toolbar: Toolbar

        fun getPrefInt(key: String, defaultValue: Int): Int {
            return try {
                preferences.getInt(key, defaultValue)
            } catch (e: Exception) {
                Log.d("PREF", "couldn't retrieve shared preference with key: $key")
                defaultValue
            }
        }

        fun getPrefStringAsInt(key: String, defaultValue: Int): Int {
            return try {
                preferences.getString(key, defaultValue.toString())!!.toInt()
            } catch (e: Exception) {
                Log.d("PREF", "couldn't retrieve shared preference with key: $key")
                defaultValue
            }
        }

        fun getPrefStringAsDouble(key: String, defaultValue: Double): Double {
            return try {
                preferences.getString(key, defaultValue.toString())!!.toDouble()
            } catch (e: Exception) {
                Log.d("PREF", "couldn't retrieve shared preference with key: $key")
                defaultValue
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        activity = this
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        res = resources

        setTheme(R.style.Theme_App)
        Theme.setMode(getPrefStringAsInt(getString(R.string.pref_theme_key), Theme.DEFAULT))

        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appBarLayout = binding.appBarMain.appBarLayout
        toolbar = binding.appBarMain.toolbar

        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_rules,
                R.id.nav_values
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.bottomNav.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
