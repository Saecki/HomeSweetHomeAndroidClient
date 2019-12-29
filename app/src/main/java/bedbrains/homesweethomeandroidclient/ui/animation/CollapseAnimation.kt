package bedbrains.homesweethomeandroidclient.ui.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class CollapseAnimation(private vararg val views: View) : Animation() {

    private var initialHeights = IntArray(views.size)

    init {
        for (i in views.indices) {
            initialHeights[i] = views[i].measuredHeight
        }
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        if (interpolatedTime == 1f) {
            views.forEach { it.visibility = View.GONE }
        } else {
            views.forEachIndexed { index, view ->
                view.layoutParams.height = (initialHeights[index] * (1 - interpolatedTime)).toInt()
            }
        }
        views.forEach { it.requestLayout() }
    }

    override fun willChangeBounds(): Boolean {
        return true
    }
}