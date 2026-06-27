package com.hwkid.uikit.overscroll

import android.view.MotionEvent
import android.view.View
import com.hwkid.uikit.overscroll.adapter.OverScrollDecoratorAdapter
import kotlin.math.abs

class HorizontalOverScrollBounceEffectDecorator @JvmOverloads constructor(
    adapter: OverScrollDecoratorAdapter,
    touchDragRatioFwd: Float = 1.5f,
    touchDragRatioBck: Float = 1.0f,
    decelerateFactor: Float = -0.5f,
) : OverScrollBounceEffectDecoratorBase(adapter, decelerateFactor, touchDragRatioFwd, touchDragRatioBck) {

    class MotionAttributesHorizontal : MotionAttributes() {
        override fun init(view: View, event: MotionEvent): Boolean {
            if (event.historySize == 0) return false
            val deltaY = event.getY(0) - event.getHistoricalY(0, 0)
            val deltaX = event.getX(0) - event.getHistoricalX(0, 0)
            if (abs(deltaX) < abs(deltaY)) return false
            deltaOffset = deltaX
            absOffset = view.translationX
            isForward = deltaOffset > 0.0f
            return true
        }
    }

    class AnimationAttributesHorizontal : AnimationAttributes() {
        init {
            property = View.TRANSLATION_X
        }

        override fun init(view: View) {
            absOffset = view.translationX
            maxOffset = view.width.toFloat()
        }
    }

    override fun createAnimationAttributes(): AnimationAttributes = AnimationAttributesHorizontal()

    override fun createMotionAttributes(): MotionAttributes = MotionAttributesHorizontal()

    override fun translateViewAndEvent(view: View, offset: Float, event: MotionEvent) {
        view.translationX = offset
        event.offsetLocation(offset - event.getX(0), 0.0f)
    }

    override fun translateView(view: View, offset: Float) {
        view.translationX = offset
    }
}
