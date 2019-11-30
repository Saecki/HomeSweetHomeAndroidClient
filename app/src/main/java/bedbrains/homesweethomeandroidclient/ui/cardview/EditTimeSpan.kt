package bedbrains.homesweethomeandroidclient.ui.cardview

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import bedbrains.homesweethomeandroidclient.R

class EditTimeSpan(context: Context) : ConstraintLayout(context) {

    val handleDiameter = resources.getDimensionPixelSize(R.dimen.card_view_handle_diameter)
    val handleElevation = resources.getDimension(R.dimen.card_view_handle_elevation)
    val borderWidth = resources.getDimensionPixelSize(R.dimen.card_view_border_width)
    val cardRadius = resources.getDimensionPixelSize(R.dimen.card_view_radius)
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val constraintSet = ConstraintSet()
    val card = View(context).apply {
        id = View.generateViewId()
        background = ContextCompat.getDrawable(context, R.drawable.card_view_border)
    }
    val upperHandle = View(context).apply {
        id = View.generateViewId()
        background = ContextCompat.getDrawable(context, R.drawable.card_view_handle)
        elevation = handleElevation
    }
    val lowerHandle = View(context).apply {
        id = View.generateViewId()
        background = ContextCompat.getDrawable(context, R.drawable.card_view_handle)
        elevation = handleElevation
    }

    init {
        id = View.generateViewId()

        upperHandle.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN)
                vibrator.vibrate(VibrationEffect.createOneShot(100L, VibrationEffect.DEFAULT_AMPLITUDE))
            true
        }
        lowerHandle.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN)
                vibrator.vibrate(VibrationEffect.createOneShot(100L, VibrationEffect.DEFAULT_AMPLITUDE))
            true
        }

        addView(card)
        addView(upperHandle)
        addView(lowerHandle)

        constraintSet.connect(card.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, handleDiameter / 2)
        constraintSet.connect(card.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, handleDiameter / 2)
        constraintSet.connect(card.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        constraintSet.connect(card.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)

        constraintSet.connect(upperHandle.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, borderWidth)
        constraintSet.connect(upperHandle.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, cardRadius / 2)
        constraintSet.constrainWidth(upperHandle.id, handleDiameter)
        constraintSet.constrainHeight(upperHandle.id, handleDiameter)

        constraintSet.connect(lowerHandle.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, borderWidth)
        constraintSet.connect(lowerHandle.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, cardRadius / 2)
        constraintSet.constrainWidth(lowerHandle.id, handleDiameter)
        constraintSet.constrainHeight(lowerHandle.id, handleDiameter)

        constraintSet.applyTo(this)
    }

}