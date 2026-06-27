package com.hwkid.uikit.overscroll.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class RecyclerViewOverScrollDecorAdapter(
    private val recyclerView: RecyclerView,
) : OverScrollDecoratorAdapter {

    private var isItemTouchInEffect = false
    private val impl: Impl

    init {
        val layoutManager = recyclerView.layoutManager
        impl = when (layoutManager) {
            is LinearLayoutManager -> {
                if (layoutManager.orientation == RecyclerView.HORIZONTAL) ImplHorizLayout() else ImplVerticalLayout()
            }
            is StaggeredGridLayoutManager -> {
                if (layoutManager.orientation == StaggeredGridLayoutManager.HORIZONTAL) ImplHorizLayout() else ImplVerticalLayout()
            }
            else -> throw IllegalArgumentException(
                "Recycler views with custom layout managers are not supported by this adapter out of the box."
            )
        }
    }

    override fun getView(): View = recyclerView

    override fun isInAbsoluteStart(): Boolean {
        return !isItemTouchInEffect && impl.isInAbsoluteStart()
    }

    override fun isInAbsoluteEnd(): Boolean {
        return !isItemTouchInEffect && impl.isInAbsoluteEnd()
    }

    private interface Impl {
        fun isInAbsoluteEnd(): Boolean
        fun isInAbsoluteStart(): Boolean
    }

    private inner class ImplHorizLayout : Impl {
        override fun isInAbsoluteEnd(): Boolean = !recyclerView.canScrollHorizontally(1)
        override fun isInAbsoluteStart(): Boolean = !recyclerView.canScrollHorizontally(-1)
    }

    private inner class ImplVerticalLayout : Impl {
        override fun isInAbsoluteEnd(): Boolean = !recyclerView.canScrollVertically(1)
        override fun isInAbsoluteStart(): Boolean = !recyclerView.canScrollVertically(-1)
    }
}
