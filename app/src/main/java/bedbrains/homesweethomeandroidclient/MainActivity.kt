package bedbrains.homesweethomeandroidclient

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var activity: MainActivity
        lateinit var appBarLayout: AppBarLayout
        lateinit var toolbar: Toolbar
        lateinit var bottomNav: BottomNavigationView
        lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        activity = this
        Res.preferences = PreferenceManager.getDefaultSharedPreferences(this)
        Res.resources = resources

        setTheme(R.style.Theme_App)
        Theme.setMode(Res.getPrefStringAsInt(R.string.pref_theme_key, Theme.DEFAULT))

        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        appBarLayout = binding.appBarMain.appBarLayout
        toolbar = binding.appBarMain.toolbar
        bottomNav = binding.appBarMain.bottomNav
        bottomSheetBehavior = BottomSheetBehavior.from(binding.appBarMain.bottomSheet)

        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_rules,
                R.id.nav_values
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        bottomNav.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
