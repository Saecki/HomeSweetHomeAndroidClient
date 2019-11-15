package saecki.homesweethomeandroidclient.ui.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class RiseDownAnimation(var view: View) : Animation() {

    var initialElevation = view.elevation

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        view.elevation = initialElevation * (1 - interpolatedTime)
    }

    fun updateInitialElevation() {
        initialElevation = view.elevation
    }

}