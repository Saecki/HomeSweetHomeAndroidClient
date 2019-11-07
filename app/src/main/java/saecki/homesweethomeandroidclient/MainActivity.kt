package saecki.homesweethomeandroidclient

import android.app.UiModeManager
import android.content.SharedPreferences
import android.content.res.Configuration
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

        fun getPrefInt(key: String): Int {
            return try {
                preferences.getString(key, "")!!.toInt()
            } catch (e: Exception) {
                Log.d("PREF", "couldn't retrieve shared preference with key %s".format(key))
                -1
            }
        }

        fun getPrefDouble(key: String): Double {
            return try {
                preferences.getString(key, "")!!.toDouble()
            } catch (e: Exception) {
                Log.d("PREF", "couldn't retrieve shared preference with key %s".format(key))
                -1.0
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        res = resources

        val currentNightMode = super.getResources().configuration.uiMode

        Log.d("UI", "currentNightMode: %s".format(currentNightMode))
        Log.d("UI", "UI_MODE_NIGHT_UNDEFINED: %s".format(Configuration.UI_MODE_NIGHT_UNDEFINED))
        Log.d("UI", "UI_MODE_TYPE_NORMAL: %s".format(Configuration.UI_MODE_TYPE_NORMAL))
        Log.d("UI", "UI_MODE_TYPE_DESK: %s".format(Configuration.UI_MODE_TYPE_DESK))
        Log.d("UI", "UI_MODE_TYPE_CAR: %s".format(Configuration.UI_MODE_TYPE_CAR))
        Log.d("UI", "UI_MODE_TYPE_TELEVISION: %s".format(Configuration.UI_MODE_TYPE_TELEVISION))
        Log.d("UI", "UI_MODE_TYPE_APPLIANCE: %s".format(Configuration.UI_MODE_TYPE_APPLIANCE))
        Log.d("UI", "UI_MODE_TYPE_WATCH: %s".format(Configuration.UI_MODE_TYPE_WATCH))
        Log.d("UI", "UI_MODE_TYPE_HEADSET: %s".format(Configuration.UI_MODE_TYPE_VR_HEADSET))
        Log.d("UI", "UI_MODE_TYPE_MASK: %s".format(Configuration.UI_MODE_TYPE_MASK))
        Log.d("UI", "UI_MODE_NIGHT_NO: %s".format(Configuration.UI_MODE_NIGHT_NO))
        Log.d("UI", "UI_MODE_NIGHT_YES: %s".format(Configuration.UI_MODE_NIGHT_YES))

        Log.d("UI", "UiModeManager.MODE_NIGHT_YES: %s".format(UiModeManager.MODE_NIGHT_YES))
        Log.d("UI", "UiModeManager.MODE_NIGHT_NO: %s".format(UiModeManager.MODE_NIGHT_NO))
        Log.d("UI", "UiModeManager.MODE_NIGHT_AUTO: %s".format(UiModeManager.MODE_NIGHT_AUTO))

        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                Log.d("UI", "UI_MODE_NIGHT_YES")
                TODO()
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                Log.d("UI", "UI_MODE_NIGHT_NO")
                TODO()
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                Log.d("UI", "UI_MODE_NIGHT_UNDEFINED")
                TODO()
            }
            Configuration.UI_MODE_NIGHT_MASK -> {
                Log.d("UI", "UI_MODE_NIGHT_MASK")
                TODO()
            }
            else -> {
                Log.d("UI", "else")
            }
        }

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_settings,
                R.id.nav_share
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
