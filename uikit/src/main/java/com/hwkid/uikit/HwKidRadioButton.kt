package com.hwkid.uikit

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.RadioButton

class HwKidRadioButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.radioButtonStyle,
) : RadioButton(context, attrs, defStyleAttr) {

    private var defaultWidth = 0
    private var defaultHeight = 0

    init {
        initView(context)
    }

    private fun initView(context: Context?) {
        if (context == null) return
        defaultWidth = context.resources.getDimensionPixelSize(R.dimen.hw_kid_radio_button_default_width)
        defaultHeight = context.resources.getDimensionPixelSize(R.dimen.hw_kid_radio_button_default_height)
        val drawable: Drawable = context.getDrawable(R.drawable.radio_hw_animated_selector) ?: return
        setButtonDrawable(null)
        background = null
        drawable.setBounds(0, 0, defaultWidth, defaultHeight)
        setCompoundDrawables(drawable, null, null, null)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode == View.MeasureSpec.AT_MOST || widthMode == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultWidth, defaultHeight)
        } else {
            setMeasuredDimension(widthSize, heightSize)
        }
    }
}
