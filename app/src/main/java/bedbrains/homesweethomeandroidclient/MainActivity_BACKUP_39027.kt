package bedbrains.homesweethomeandroidclient

import android.os.Bundle
import android.os.Handler
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import bedbrains.homesweethomeandroidclient.databinding.ActivityMainBinding
import bedbrains.homesweethomeandroidclient.ui.Theme
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var activity: MainActivity
        lateinit var appBarLayout: AppBarLayout
        lateinit var toolbar: Toolbar
        lateinit var bottomNav: BottomNavigationView
        lateinit var bottomSheet: LinearLayout
        lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
        lateinit var fab: ExtendedFloatingActionButton
        lateinit var fabBehavior: HideBottomViewOnScrollBehavior<ExtendedFloatingActionButton>

        fun showFab() {
            fabBehavior.slideUp(fab)
            fab.show()
        }

        fun showFabDelayed() {
            val delay = Res.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            Handler().postDelayed({
                fabBehavior.slideUp(fab)
                fab.show()
<<<<<<< HEAD
            }, duration)
=======
            }, (delay))
>>>>>>> 862eb53af22c3ab1598dab3993a1048765ba370c
        }

        fun hideFab() {
            fabBehavior.slideDown(fab)

            val delay = Res.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            Handler().postDelayed({
                fab.hide()
            }, delay)
        }
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        activity = this
        Res.preferences = PreferenceManager.getDefaultSharedPreferences(this)
        Res.resources = resources

        setTheme(R.style.Theme_App)
        Theme.setMode(Res.getPrefStringAsInt(R.string.pref_theme_key, Theme.DEFAULT))

        Res.setNetworkUpdate(Res.getPrefString(R.string.pref_network_automatic_update_key, ""))
        Res.setTempUnit(Res.getPrefString(R.string.pref_temperature_unit_key, ""))
        Res.setTempDecimals(Res.getPrefString(R.string.pref_temperature_decimals_key, ""))

        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        appBarLayout = binding.appBarMain.appBarLayout
        toolbar = binding.appBarMain.toolbar
        bottomNav = binding.appBarMain.bottomNav
        bottomSheet = binding.appBarMain.bottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        fab = binding.appBarMain.fab
        fabBehavior = (fab.layoutParams as CoordinatorLayout.LayoutParams)
            .behavior as HideBottomViewOnScrollBehavior<ExtendedFloatingActionButton>

        setSupportActionBar(toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_devices,
                R.id.nav_rules,
                R.id.nav_values
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, _, _ ->
            hideFab()
            fab.setOnClickListener(null)
            fab.text = null
            fab.icon = null
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
