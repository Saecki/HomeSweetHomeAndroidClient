package bedbrains.homesweethomeandroidclient.ui.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class RiseDownAnimation(private vararg val views: View) : Animation() {

    val initialElevations = views.map { it.elevation }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        views.forEachIndexed { index, view ->
            view.elevation = initialElevations[index] * (1 - interpolatedTime)
        }
    }

}