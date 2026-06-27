package com.hwkid.uikit.overscroll

fun interface OverScrollUpdateListener {
    fun onOverScrollUpdate(overScrollDecor: OverScrollDecor, state: Int, offset: Float)
}
