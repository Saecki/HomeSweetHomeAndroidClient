package bedbrains.homesweethomeandroidclient.ui.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class RiseUpAnimation(private vararg val views: View, private val elevation: Float) : Animation() {

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        views.forEach { it.elevation = elevation * interpolatedTime }
    }

}