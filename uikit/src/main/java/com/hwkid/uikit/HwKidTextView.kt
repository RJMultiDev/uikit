package com.hwkid.uikit

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

class HwKidTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : TextView(context, attrs) {
    override fun onDraw(canvas: android.graphics.Canvas) {
        super.onDraw(canvas)
    }
}
