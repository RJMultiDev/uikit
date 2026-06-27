package com.hwkid.uikit.overscroll

object ListenerStubs {
    class OverScrollStateListenerStub : OverScrollStateListener {
        override fun onOverScrollStateChange(overScrollDecor: OverScrollDecor, oldState: Int, newState: Int) = Unit
    }

    class OverScrollUpdateListenerStub : OverScrollUpdateListener {
        override fun onOverScrollUpdate(overScrollDecor: OverScrollDecor, state: Int, offset: Float) = Unit
    }
}
