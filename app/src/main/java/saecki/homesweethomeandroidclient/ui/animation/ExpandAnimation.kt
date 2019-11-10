package saecki.homesweethomeandroidclient.ui.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class ExpandAnimation(var view: View) : Animation() {

    private val matchParentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec((view.getParent() as View).width, View.MeasureSpec.EXACTLY)
    private val wrapContentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

    init {
        view.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    }

    val targetHeight: Int = view.measuredHeight

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        view.alpha = interpolatedTime
        view.layoutParams.height = (targetHeight * interpolatedTime).toInt()
        view.requestLayout()
    }

    override fun willChangeBounds(): Boolean {
        return true
    }
}