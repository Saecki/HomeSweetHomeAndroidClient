package bedbrains.homesweethomeandroidclient.ui.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.math.max

class DodgeNavigationComponents : CoordinatorLayout.Behavior<View> {
    private var appBarHeight = 0
    private var bottomNavHeight = 0
    private var bottomSheetHeight = 0

    constructor()

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        val params = dependency.layoutParams as CoordinatorLayout.LayoutParams

        return when {
            dependency is AppBarLayout -> true
            dependency is BottomNavigationView -> true
            params.behavior is BottomSheetBehavior -> true
            else -> false
        }
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        val params = dependency.layoutParams as CoordinatorLayout.LayoutParams

        when {
            dependency is AppBarLayout -> appBarHeight = dependency.height
            dependency is BottomNavigationView -> bottomNavHeight = dependency.height
            params.behavior is BottomSheetBehavior -> bottomSheetHeight = parent.height - dependency.y.toInt()
            else -> return false
        }

        child.setPadding(0, appBarHeight, 0, max(bottomNavHeight, bottomSheetHeight))

        return true
    }
}