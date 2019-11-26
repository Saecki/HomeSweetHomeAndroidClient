package bedbrains.homesweethomeandroidclient.ui.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class CollapseAnimation(var view: View) : Animation() {

    var initialHeight = view.measuredHeight

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        if (interpolatedTime == 1f) {
            view.visibility = View.GONE
        } else {
            view.alpha = 1 - interpolatedTime
            view.layoutParams.height = (initialHeight * (1 - interpolatedTime)).toInt()
        }
        view.requestLayout()
    }

    override fun willChangeBounds(): Boolean {
        return true
    }

    fun updateInitialHeight() {
        view.requestLayout()
        initialHeight = view.height
    }
}