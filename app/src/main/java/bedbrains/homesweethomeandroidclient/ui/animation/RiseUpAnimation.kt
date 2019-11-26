package bedbrains.homesweethomeandroidclient.ui.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class RiseUpAnimation(var view: View) : Animation() {

    var elevation = 8

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        view.elevation = elevation * interpolatedTime
    }

}