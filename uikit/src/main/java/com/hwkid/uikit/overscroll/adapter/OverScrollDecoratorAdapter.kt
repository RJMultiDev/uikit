package com.hwkid.uikit.overscroll.adapter

import android.view.View

interface OverScrollDecoratorAdapter {
    fun getView(): View
    fun isInAbsoluteEnd(): Boolean
    fun isInAbsoluteStart(): Boolean
}
