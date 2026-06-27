package com.hwkid.uikit

import android.content.Context
import android.util.AttributeSet
import android.widget.AdapterView
import android.widget.ListAdapter
import android.widget.ListView
import com.hwkid.uikit.overscroll.OverScrollDecor
import com.hwkid.uikit.overscroll.OverScrollDecoratorHelper

class HwKidListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.listViewStyle,
) : ListView(context, attrs, defStyleAttr) {

    private var animationEnabled = false
    private var isMoveClear = false
    private var savedItemClickListener: AdapterView.OnItemClickListener? = null
    private var savedItemLongClickListener: AdapterView.OnItemLongClickListener? = null

    init {
        animationEnabled = context.resources.getBoolean(R.bool.hw_kid_list_animation_enable)
    }

    override fun setAdapter(adapter: ListAdapter?) {
        super.setAdapter(adapter)
        if (getAnimationEnable()) {
            OverScrollDecoratorHelper.setUpOverScroll(this).setOverScrollStateListener { decor: OverScrollDecor, oldState: Int, newState: Int ->
                if (newState != 0) {
                    isMoveClear = true
                    setOnItemClickListener(null)
                    setOnItemLongClickListener(null)
                } else {
                    isMoveClear = false
                    setOnItemClickListener(savedItemClickListener)
                    setOnItemLongClickListener(savedItemLongClickListener)
                }
            }
        }
    }

    override fun setOnItemClickListener(listener: AdapterView.OnItemClickListener?) {
        if (!isMoveClear && getAnimationEnable()) {
            savedItemClickListener = listener
        }
        super.setOnItemClickListener(listener)
    }

    override fun setOnItemLongClickListener(listener: AdapterView.OnItemLongClickListener?) {
        if (!isMoveClear && getAnimationEnable()) {
            savedItemLongClickListener = listener
        }
        super.setOnItemLongClickListener(listener)
    }

    private fun getAnimationEnable(): Boolean = animationEnabled
}
