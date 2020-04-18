package bedbrains.homesweethomeandroidclient.ui.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class DodgeNavigationComponents : CoordinatorLayout.Behavior<View> {
    private var appBarHeight = 0
    private var bottomNavHeight = 0

    constructor()

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return when (dependency) {
            is AppBarLayout -> true
            is BottomNavigationView -> true
            else -> false
        }
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        var changed = true

        when (dependency) {
            is AppBarLayout -> appBarHeight = dependency.height
            is BottomNavigationView -> bottomNavHeight = dependency.height
            else -> changed = false
        }

        child.setPadding(0, appBarHeight, 0, bottomNavHeight)

        return false
    }
}