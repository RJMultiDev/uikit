package com.hwkid.uikit.overscroll

fun interface OverScrollStateListener {
    fun onOverScrollStateChange(overScrollDecor: OverScrollDecor, oldState: Int, newState: Int)
}
