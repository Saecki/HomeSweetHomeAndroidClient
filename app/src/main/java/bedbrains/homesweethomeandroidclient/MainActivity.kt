package bedbrains.homesweethomeandroidclient

import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {
        lateinit var preferences: SharedPreferences
        lateinit var res: Resources

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

        super.onCreate(savedInstanceState)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        res = resources

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_rules,
                R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}