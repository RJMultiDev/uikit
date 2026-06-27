package com.hwkid.uikit

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.hwkid.uikit.overscroll.OverScrollDecoratorHelper
import java.math.BigDecimal

class HwKidRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RecyclerView(context, attrs, defStyleAttr) {

    private var animationEnabled = false

    init {
        setAnimationEnable(context.resources.getBoolean(R.bool.hw_kid_list_animation_enable))
    }

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        if (!getAnimationEnable()) return super.fling(velocityX, velocityY)
        val scaledVelocityY = BigDecimal(velocityY).multiply(BigDecimal(0.550000011920929)).toInt()
        return super.fling(velocityX, scaledVelocityY)
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        if (layout == null || !getAnimationEnable()) return
        Log.d(TAG, "setUpOverScroll enable:${getAnimationEnable()}")
        OverScrollDecoratorHelper.setUpOverScroll(
            this,
            if (layout.canScrollVertically()) OverScrollDecoratorHelper.ORIENTATION_VERTICAL else OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL,
        )
    }

    private fun getAnimationEnable(): Boolean = animationEnabled

    fun setAnimationEnable(enabled: Boolean) {
        animationEnabled = enabled
    }

    companion object {
        private const val TAG = "HwKidRecyclerView"
    }
}
