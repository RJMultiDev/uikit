package com.hwkid.uikit.overscroll

import android.view.MotionEvent
import android.view.View
import com.hwkid.uikit.overscroll.adapter.OverScrollDecoratorAdapter
import kotlin.math.abs

class VerticalOverScrollBounceEffectDecorator @JvmOverloads constructor(
    adapter: OverScrollDecoratorAdapter,
    touchDragRatioFwd: Float = 1.5f,
    touchDragRatioBck: Float = 1.0f,
    decelerateFactor: Float = -0.5f,
) : OverScrollBounceEffectDecoratorBase(adapter, decelerateFactor, touchDragRatioFwd, touchDragRatioBck) {

    class MotionAttributesVertical : MotionAttributes() {
        override fun init(view: View, event: MotionEvent): Boolean {
            if (event.historySize == 0) return false
            val deltaY = event.getY(0) - event.getHistoricalY(0, 0)
            if (abs(event.getX(0) - event.getHistoricalX(0, 0)) > abs(deltaY)) return false
            deltaOffset = deltaY
            isForward = deltaOffset > 0.0f
            absOffset = view.translationY
            return true
        }
    }

    class AnimationAttributesVertical : AnimationAttributes() {
        init {
            property = View.TRANSLATION_Y
        }

        override fun init(view: View) {
            maxOffset = view.height.toFloat()
            absOffset = view.translationY
        }
    }

    override fun createAnimationAttributes(): AnimationAttributes = AnimationAttributesVertical()

    override fun createMotionAttributes(): MotionAttributes = MotionAttributesVertical()

    override fun translateViewAndEvent(view: View, offset: Float, event: MotionEvent) {
        view.translationY = offset
        event.offsetLocation(offset - event.getY(0), 0.0f)
    }

    override fun translateView(view: View, offset: Float) {
        view.translationY = offset
    }
}
