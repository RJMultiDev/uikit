package com.hwkid.uikit

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast

object HwKidToast {
    private var mToastLessThanSixNumWidth = 0
    private var mToastSixNumWidth = 0
    private var mToastMoreThanSixNumWidth = 0

    @JvmStatic
    fun show(context: Context, text: CharSequence?, duration: Int): Toast {
        val toast = Toast.makeText(
            context.applicationContext,
            text,
            if (duration == 1) Toast.LENGTH_LONG else Toast.LENGTH_SHORT,
        )

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater
        val view = inflater?.inflate(R.layout.hw_kid_toast_layout, null)
        val container = view?.findViewById<RelativeLayout>(R.id.hw_kid_toast_text_container)
        val editText = view?.findViewById<EditText>(R.id.hw_kid_toast_text)
        Log.d(TAG, "show : toastText == null :${editText == null}")

        if (editText != null && text != null) {
            if (container != null) setToastViewWidth(context, container, text.length)
            editText.text = null
            editText.setText(text)
        }
        // Toast.setView is deprecated in API 30; suppress to keep parity with the
        // original decompiled HwKidToast.show() behaviour. The replacement API
        // requires the caller to own a custom toast subclass, which we cannot
        // reach from this object.
        @Suppress("DEPRECATION")
        toast.view = view
        toast.setGravity(android.view.Gravity.CENTER, 0, 0)
        toast.show()
        return toast
    }

    private fun setToastViewWidth(context: Context, view: View, length: Int) {
        mToastLessThanSixNumWidth = context.resources.getDimensionPixelSize(R.dimen.hw_kid_toast_less_than_six_width)
        mToastSixNumWidth = context.resources.getDimensionPixelSize(R.dimen.hw_kid_toast_six_width)
        mToastMoreThanSixNumWidth = context.resources.getDimensionPixelSize(R.dimen.hw_kid_toast_more_than_six_width)
        val lp = view.layoutParams as? RelativeLayout.LayoutParams ?: return
        lp.width = when {
            length < 6 -> mToastLessThanSixNumWidth
            length == 6 -> mToastSixNumWidth
            else -> mToastMoreThanSixNumWidth
        }
        view.layoutParams = lp
    }

    private const val TAG = "HwKidToast"
}
