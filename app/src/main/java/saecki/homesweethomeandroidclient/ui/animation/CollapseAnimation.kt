package saecki.homesweethomeandroidclient.ui.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class CollapseAnimation(var view: View) : Animation() {

    val initialHeight = view.measuredHeight

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        view.alpha = 1 - interpolatedTime
        view.layoutParams.height = (initialHeight * (1 - interpolatedTime)).toInt()
        if (hasEnded()) {
            view.visibility = View.GONE
        }
        view.requestLayout()
    }

    override fun willChangeBounds(): Boolean {
        return true
    }
}