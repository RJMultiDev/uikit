package com.hwkid.uikit.overscroll

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.Log
import android.util.Property
import android.view.MotionEvent
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.PathInterpolator
import com.hwkid.uikit.overscroll.adapter.OverScrollDecoratorAdapter
import kotlin.math.abs
import kotlin.math.max

abstract class OverScrollBounceEffectDecoratorBase(
    private val viewAdapter: OverScrollDecoratorAdapter,
    decelerateFactor: Float,
    touchDragRatioFwd: Float,
    touchDragRatioBck: Float,
) : OverScrollDecor, View.OnTouchListener {

    private val startAttr = OverScrollStartAttributes()
    private var stateListener: OverScrollStateListener = ListenerStubs.OverScrollStateListenerStub()
    private var updateListener: OverScrollUpdateListener = ListenerStubs.OverScrollUpdateListenerStub()
    private val idleState = IdleState()
    private val overScrollingState = OverScrollingState(touchDragRatioFwd, touchDragRatioBck)
    private val bounceBackState = BounceBackState(decelerateFactor)
    private var currentState: DecoratorState = idleState
    private var velocity = 0f

    init {
        attach()
    }

    protected abstract fun createAnimationAttributes(): AnimationAttributes
    protected abstract fun createMotionAttributes(): MotionAttributes
    protected abstract fun translateView(view: View, offset: Float)
    protected abstract fun translateViewAndEvent(view: View, offset: Float, event: MotionEvent)

    abstract class AnimationAttributes {
        var absOffset: Float = 0f
        var maxOffset: Float = 0f
        lateinit var property: Property<View, Float>
        abstract fun init(view: View)
    }

    interface DecoratorState {
        fun getStateId(): Int
        fun handleEntryTransition(oldState: DecoratorState)
        fun handleMoveTouchEvent(event: MotionEvent): Boolean
        fun handleUpOrCancelTouchEvent(event: MotionEvent): Boolean
    }

    abstract class MotionAttributes {
        var isForward: Boolean = false
        var absOffset: Float = 0f
        var deltaOffset: Float = 0f
        abstract fun init(view: View, event: MotionEvent): Boolean
    }

    class OverScrollStartAttributes {
        var isForward: Boolean = false
        var absOffset: Float = 0f
        var pointerId: Int = 0
    }

    inner class IdleState : DecoratorState {
        private val moveAttr: MotionAttributes = createMotionAttributes()

        override fun handleMoveTouchEvent(event: MotionEvent): Boolean {
            if (!moveAttr.init(viewAdapter.getView(), event)) return false
            val atStart = viewAdapter.isInAbsoluteStart() && moveAttr.isForward
            val atEnd = viewAdapter.isInAbsoluteEnd() && !moveAttr.isForward
            if (!atStart && !atEnd) return false

            startAttr.pointerId = event.getPointerId(0)
            startAttr.absOffset = moveAttr.absOffset
            startAttr.isForward = moveAttr.isForward
            handleStateTransition(overScrollingState)
            return overScrollingState.handleMoveTouchEvent(event)
        }

        override fun getStateId(): Int = STATE_IDLE

        override fun handleUpOrCancelTouchEvent(event: MotionEvent): Boolean = false

        override fun handleEntryTransition(oldState: DecoratorState) {
            stateListener.onOverScrollStateChange(this@OverScrollBounceEffectDecoratorBase, oldState.getStateId(), getStateId())
        }
    }

    inner class OverScrollingState(
        private val touchDragRatioFwd: Float,
        private val touchDragRatioBck: Float,
    ) : DecoratorState {
        private var currDragState = STATE_DRAG_START_SIDE
        private val moveAttr: MotionAttributes = createMotionAttributes()

        override fun getStateId(): Int = currDragState

        override fun handleMoveTouchEvent(event: MotionEvent): Boolean {
            if (startAttr.pointerId != event.getPointerId(0)) {
                handleStateTransition(bounceBackState)
                return true
            }

            val view = viewAdapter.getView()
            if (!moveAttr.init(view, event)) return true

            val dragRatio = if (moveAttr.isForward != startAttr.isForward) touchDragRatioBck else touchDragRatioFwd
            val delta = moveAttr.deltaOffset / dragRatio
            val newOffset = moveAttr.absOffset + delta
            val backToStartFromStart = startAttr.isForward && !moveAttr.isForward && newOffset <= startAttr.absOffset
            val backToStartFromEnd = !startAttr.isForward && moveAttr.isForward && newOffset >= startAttr.absOffset

            if (backToStartFromStart || backToStartFromEnd) {
                translateViewAndEvent(view, startAttr.absOffset, event)
                updateListener.onOverScrollUpdate(this@OverScrollBounceEffectDecoratorBase, currDragState, 0f)
                handleStateTransition(idleState)
                return true
            }

            view.parent?.requestDisallowInterceptTouchEvent(true)
            val eventTime = event.eventTime - event.getHistoricalEventTime(0)
            if (eventTime > 0) {
                velocity = delta / eventTime.toFloat()
            }
            translateView(view, newOffset)
            updateListener.onOverScrollUpdate(this@OverScrollBounceEffectDecoratorBase, currDragState, newOffset)
            return true
        }

        override fun handleEntryTransition(oldState: DecoratorState) {
            currDragState = if (startAttr.isForward) STATE_DRAG_START_SIDE else STATE_DRAG_END_SIDE
            stateListener.onOverScrollStateChange(this@OverScrollBounceEffectDecoratorBase, oldState.getStateId(), getStateId())
        }

        override fun handleUpOrCancelTouchEvent(event: MotionEvent): Boolean {
            handleStateTransition(bounceBackState)
            return false
        }
    }

    inner class BounceBackState(
        decelerateFactor: Float,
    ) : DecoratorState, Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
        private val animAttributes: AnimationAttributes = createAnimationAttributes()
        private val bounceBackInterpolator: Interpolator = PathInterpolator(0.0f, 0.0f, 0.2f, 1.0f)
        private val decelerateFactor: Float = decelerateFactor
        private val doubleDecelerateFactor: Float = 2.0f * decelerateFactor

        override fun getStateId(): Int = STATE_BOUNCE_BACK

        override fun handleEntryTransition(oldState: DecoratorState) {
            stateListener.onOverScrollStateChange(this@OverScrollBounceEffectDecoratorBase, oldState.getStateId(), getStateId())
            createAnimator().apply {
                addListener(this@BounceBackState)
                start()
            }
        }

        override fun handleMoveTouchEvent(event: MotionEvent): Boolean = true

        override fun handleUpOrCancelTouchEvent(event: MotionEvent): Boolean = true

        override fun onAnimationUpdate(animation: ValueAnimator) {
            val animatedValue = animation.animatedValue
            if (animatedValue is Float) {
                updateListener.onOverScrollUpdate(this@OverScrollBounceEffectDecoratorBase, STATE_BOUNCE_BACK, animatedValue)
            }
        }

        override fun onAnimationEnd(animation: Animator) {
            handleStateTransition(idleState)
        }

        override fun onAnimationStart(animation: Animator) = Unit
        override fun onAnimationRepeat(animation: Animator) = Unit
        override fun onAnimationCancel(animation: Animator) = Unit

        private fun createAnimator(): Animator {
            val view = viewAdapter.getView()
            animAttributes.init(view)
            val velocityAsInt = velocity.toInt()
            val wrongDirection = velocityAsInt < 0 && startAttr.isForward
            if (velocityAsInt == 0 || wrongDirection || (velocityAsInt > 0 && !startAttr.isForward)) {
                return createBounceBackAnimator(animAttributes.absOffset)
            }

            val slowdownDuration = max(((-velocity) / decelerateFactor).toInt(), 0)
            val slowdownEndOffset = (((-velocity) * velocity) / doubleDecelerateFactor) + animAttributes.absOffset
            val slowdownAnimator = createSlowdownAnimator(view, slowdownDuration, slowdownEndOffset)
            val bounceBackAnimator = createBounceBackAnimator(slowdownEndOffset)
            return AnimatorSet().apply {
                playSequentially(slowdownAnimator, bounceBackAnimator)
            }
        }

        private fun createSlowdownAnimator(view: View, durationMs: Int, offset: Float): ObjectAnimator {
            return ObjectAnimator.ofFloat(view, animAttributes.property, offset).apply {
                duration = durationMs.toLong()
                interpolator = bounceBackInterpolator
                addUpdateListener(this@BounceBackState)
            }
        }

        private fun createBounceBackAnimator(fromOffset: Float): ObjectAnimator {
            val durationMs = max(((abs(fromOffset) / animAttributes.maxOffset) * 220.0f).toInt(), 50)
            return ObjectAnimator.ofFloat(viewAdapter.getView(), animAttributes.property, startAttr.absOffset).apply {
                duration = durationMs.toLong()
                interpolator = bounceBackInterpolator
                addUpdateListener(this@BounceBackState)
            }
        }
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> currentState.handleUpOrCancelTouchEvent(event)
            MotionEvent.ACTION_MOVE -> currentState.handleMoveTouchEvent(event)
            else -> {
                Log.d(TAG, "no match event")
                false
            }
        }
    }

    override fun setOverScrollStateListener(overScrollStateListener: OverScrollStateListener?) {
        stateListener = overScrollStateListener ?: ListenerStubs.OverScrollStateListenerStub()
    }

    fun getView(): View = viewAdapter.getView()

    private fun handleStateTransition(newState: DecoratorState) {
        val oldState = currentState
        currentState = newState
        currentState.handleEntryTransition(oldState)
    }

    private fun attach() {
        getView().setOnTouchListener(this)
        getView().overScrollMode = View.OVER_SCROLL_NEVER
    }

    companion object {
        private const val TAG = "OverScrollDecor"
        const val STATE_IDLE = 0
        const val STATE_DRAG_START_SIDE = 1
        const val STATE_DRAG_END_SIDE = 2
        const val STATE_BOUNCE_BACK = 3
    }
}
