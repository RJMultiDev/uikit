package com.hwkid.uikit

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox

class HwKidCheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.checkboxStyle,
    defStyleRes: Int = 0,
) : CheckBox(context, attrs, defStyleAttr, defStyleRes)
