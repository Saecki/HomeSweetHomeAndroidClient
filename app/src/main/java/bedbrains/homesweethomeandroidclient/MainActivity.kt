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

        fun getPrefString(key: Int, defaultValue: String): String {
            return try {
                preferences.getString(res.getString(key), defaultValue)!!
            } catch (e: Exception) {
                defaultValue
            }
        }

        fun getPrefInt(key: Int, defaultValue: Int): Int {
            return try {
                preferences.getInt(res.getString(key), defaultValue)
            } catch (e: Exception) {
                defaultValue
            }
        }

        fun getPrefStringAsInt(key: Int, defaultValue: Int): Int {
            return try {
                preferences.getString(res.getString(key), defaultValue.toString())!!.toInt()
            } catch (e: Exception) {
                defaultValue
            }
        }

        fun getPrefStringAsDouble(key: Int, defaultValue: Double): Double {
            return try {
                preferences.getString(res.getString(key), defaultValue.toString())!!.toDouble()
            } catch (e: Exception) {
                defaultValue
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        activity = this
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        res = resources

        setTheme(R.style.Theme_App)
        Theme.setMode(getPrefStringAsInt(R.string.pref_theme_key, Theme.DEFAULT))

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
