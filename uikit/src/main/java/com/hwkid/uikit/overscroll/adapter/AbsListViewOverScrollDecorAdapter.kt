package com.hwkid.uikit.overscroll.adapter

import android.view.View
import android.widget.AbsListView

class AbsListViewOverScrollDecorAdapter(
    private val view: AbsListView,
) : OverScrollDecoratorAdapter {

    override fun getView(): View = view

    override fun isInAbsoluteEnd(): Boolean {
        return view.childCount > 0 && !canScrollListDown()
    }

    override fun isInAbsoluteStart(): Boolean {
        return view.childCount > 0 && !canScrollListUp()
    }

    fun canScrollListUp(): Boolean {
        return view.firstVisiblePosition > 0 || view.getChildAt(0).top < view.listPaddingTop
    }

    fun canScrollListDown(): Boolean {
        val firstVisiblePosition = view.firstVisiblePosition
        val childCount = view.childCount
        return firstVisiblePosition + childCount < view.count ||
            view.getChildAt(childCount - 1).bottom > view.height - view.listPaddingBottom
    }
}
