package bedbrains.homesweethomeandroidclient.ui.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class ExpandAnimation(var view: View) : Animation() {

    private val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec((view.parent as View).width, View.MeasureSpec.EXACTLY)
    private val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

    init {
        view.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    }

    var initialHeight: Int = view.layoutParams.height
    val targetHeight: Int = view.measuredHeight

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        val difference: Int = targetHeight - initialHeight
        view.layoutParams.height = initialHeight + (difference * interpolatedTime).toInt()
        view.requestLayout()
    }

    override fun willChangeBounds(): Boolean {
        return true
    }

    fun updateInitialHeight() {
        initialHeight = view.layoutParams.height
    }
}