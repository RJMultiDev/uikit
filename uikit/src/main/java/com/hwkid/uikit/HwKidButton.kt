package com.hwkid.uikit

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.core.content.ContextCompat

class HwKidButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.buttonStyle,
) : Button(context, attrs, defStyleAttr) {

    private var hwKidButtonBackgroundColor = 0
    private var hwKidButtonBackgroundColorFalse = 0
    private var hwKidButtonBackgroundColorPress = 0
    private var hwKidButtonBackgroundTextColor = 0
    private var hwKidButtonBackgroundTextColorFalse = 0
    private var hwKidButtonBackgroundTextColorPress = 0
    private var hwKidButtonBackgroundTextSize = 0
    private var hwKidButtonBorder = 0
    private var hwKidButtonBorderColor = 0
    private var hwKidButtonBorderColorFalse = 0
    private var hwKidButtonBorderColorPress = 0
    private var hwKidButtonRadius = 0
    private var hwKidStyle = 0
    private var isOnlyFont = false
    private var mrHeight = 0
    private var mrLongWidth = 0
    private var mrShortWidth = 0
    private var mrTestSize = 0

    init {
        isOnlyFont = false
        initDefaultParameters(context)
        setLayerType(LAYER_TYPE_HARDWARE, null)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.HwKidButton)
        try {
            hwKidButtonRadius = ta.getDimensionPixelSize(R.styleable.HwKidButton_hkb_radius, hwKidButtonRadius)
            hwKidButtonBackgroundColor = ta.getColor(R.styleable.HwKidButton_hkb_background_color, hwKidButtonBackgroundColor)
            hwKidButtonBackgroundColorPress = ta.getColor(R.styleable.HwKidButton_hkb_background_color_press, hwKidButtonBackgroundColorPress)
            hwKidButtonBackgroundColorFalse = ta.getColor(R.styleable.HwKidButton_hkb_background_color_false, hwKidButtonBackgroundColorFalse)
            hwKidButtonBackgroundTextColor = ta.getColor(R.styleable.HwKidButton_hkb_background_text_color, hwKidButtonBackgroundTextColor)
            hwKidButtonBackgroundTextSize = ta.getDimensionPixelSize(R.styleable.HwKidButton_hkb_background_text_size, hwKidButtonBackgroundTextSize)
            hwKidButtonBackgroundTextColorPress = ta.getColor(R.styleable.HwKidButton_hkb_background_text_color_press, hwKidButtonBackgroundTextColorPress)
            hwKidButtonBackgroundTextColorFalse = ta.getColor(R.styleable.HwKidButton_hkb_background_text_color_false, hwKidButtonBackgroundTextColorFalse)
            hwKidButtonBorder = ta.getDimensionPixelSize(R.styleable.HwKidButton_hkb_background_border, hwKidButtonBorder)
            hwKidButtonBorderColor = ta.getColor(R.styleable.HwKidButton_hkb_background_border_color, hwKidButtonBorderColor)
            hwKidButtonBorderColorPress = ta.getColor(R.styleable.HwKidButton_hkb_background_border_color_press, hwKidButtonBorderColorPress)
            hwKidButtonBorderColorFalse = ta.getColor(R.styleable.HwKidButton_hkb_background_border_color_false, hwKidButtonBorderColorFalse)
            hwKidStyle = ta.getColor(R.styleable.HwKidButton_hkb_style, hwKidStyle)
        } finally {
            ta.recycle()
        }
        gravity = android.view.Gravity.CENTER
        setHwStyle(hwKidStyle)
        setFontFamily()
        isEnabled = isEnabled
        includeFontPadding = false
    }

    private fun initDefaultParameters(context: Context?) {
        if (context == null) return
        val res = context.resources
        hwKidButtonRadius = res.getDimensionPixelSize(R.dimen.hw_kid_button_radius)
        hwKidButtonBackgroundColor = ContextCompat.getColor(context, R.color.hw_kid_button_background_normal_color)
        hwKidButtonBackgroundColorPress = ContextCompat.getColor(context, R.color.hw_kid_button_background_press_color)
        hwKidButtonBackgroundColorFalse = ContextCompat.getColor(context, R.color.hw_kid_button_background_press_false_color)
        hwKidButtonBackgroundTextColor = ContextCompat.getColor(context, R.color.hw_kid_button_text_color)
        hwKidButtonBackgroundTextSize = res.getDimensionPixelSize(R.dimen.hw_kid_button_text_size)
        hwKidButtonBackgroundTextColorPress = ContextCompat.getColor(context, R.color.hw_kid_button_text_press_color)
        hwKidButtonBackgroundTextColorFalse = ContextCompat.getColor(context, R.color.hw_kid_button_text_press_false_color)
        hwKidButtonBorder = res.getInteger(R.integer.hw_kid_button_border)
        hwKidButtonBorderColor = ContextCompat.getColor(context, R.color.hw_kid_button_border_normal_color)
        hwKidButtonBorderColorPress = ContextCompat.getColor(context, R.color.hw_kid_button_border_press_color)
        hwKidButtonBorderColorFalse = ContextCompat.getColor(context, R.color.hw_kid_button_border_press_color)
        hwKidStyle = res.getInteger(R.integer.hw_kid_button_default_style)
        mrLongWidth = res.getDimensionPixelSize(R.dimen.hw_kid_button_long_width)
        mrShortWidth = res.getDimensionPixelSize(R.dimen.hw_kid_button_short_width)
        mrHeight = res.getDimensionPixelSize(R.dimen.hw_kid_button_default_height)
        mrTestSize = res.getDimensionPixelSize(R.dimen.hw_kid_button_text_size)
    }

    private fun setHwStyle(style: Int) {
        Log.d(TAG, "you button style is$style")
        isOnlyFont = false
        when (style) {
            0 -> setStressLongStyle()
            1 -> setStressShortStyle()
            2 -> setNormalLongLightStyle()
            3 -> setNormalShortLightStyle()
            4 -> setNormalLongDeepStyle()
            5 -> setNormalShortDeepStyle()
            6 -> setWarningLongLightStyle()
            7 -> setWarningShortLightStyle()
            8 -> setWarningLongDeepStyle()
            9 -> setWarningShortDeepStyle()
            18 -> setOnlyFontNormal()
            19 -> setOnlyFontWarning()
            else -> setStressLongStyle()
        }
    }

    private fun setStressLongStyle() {
        setSize(11)
        sethkbTextColor(Color.parseColor("#FFFFFF"))
        setBackgroundResource(R.drawable.button_stress_long)
    }

    private fun setStressShortStyle() {
        setSize(10)
        sethkbTextColor(Color.parseColor("#FFFFFF"))
        setBackgroundResource(R.drawable.button_stress_short)
    }

    private fun setNormalLongDeepStyle() {
        setSize(11)
        sethkbTextColor(Color.parseColor("#5EA1FF"))
        setBackgroundResource(R.drawable.button_normal_long_deep)
    }

    private fun setNormalLongLightStyle() {
        setSize(11)
        sethkbTextColor(Color.parseColor("#5EA1FF"))
        setBackgroundResource(R.drawable.button_normal_long_light)
    }

    private fun setNormalShortDeepStyle() {
        setSize(10)
        sethkbTextColor(Color.parseColor("#5EA1FF"))
        setBackgroundResource(R.drawable.button_normal_short_deep)
    }

    private fun setNormalShortLightStyle() {
        setSize(10)
        sethkbTextColor(Color.parseColor("#5EA1FF"))
        setBackgroundResource(R.drawable.button_normal_short_light)
    }

    private fun setWarningLongDeepStyle() {
        setSize(11)
        sethkbTextColor(Color.parseColor("#FFFFFF"))
        setBackgroundResource(R.drawable.button_warning_long_deep)
    }

    private fun setWarningLongLightStyle() {
        setSize(11)
        sethkbTextColor(Color.parseColor("#E62E31"))
        setBackgroundResource(R.drawable.button_warning_long_light)
    }

    private fun setWarningShortDeepStyle() {
        setSize(10)
        sethkbTextColor(Color.parseColor("#FFFFFF"))
        setBackgroundResource(R.drawable.button_warning_short_deep)
    }

    private fun setWarningShortLightStyle() {
        setSize(10)
        sethkbTextColor(Color.parseColor("#E62E31"))
        setBackgroundResource(R.drawable.button_warning_short_light)
    }

    private fun setOnlyFontNormal() {
        setTextSize(0, mrTestSize.toFloat())
        setPadding(6, 0, 6, 0)
        setBackgroundResource(R.drawable.button_onlyfont_normal)
        sethkbTextColor(Color.parseColor("#5EA1FF"))
        isOnlyFont = true
    }

    private fun setOnlyFontWarning() {
        setTextSize(0, mrTestSize.toFloat())
        setPadding(6, 0, 6, 0)
        setBackgroundResource(R.drawable.button_onlyfont_warning)
        sethkbTextColor(Color.parseColor("#E62E31"))
        isOnlyFont = true
    }

    private fun setSize(size: Int) {
        when (size) {
            10 -> {
                width = mrShortWidth
                height = mrHeight
                setTextSize(0, mrTestSize.toFloat())
            }
            else -> {
                width = mrLongWidth
                height = mrHeight
                setTextSize(0, mrTestSize.toFloat())
            }
        }
    }

    fun setStyle(style: Int) {
        setHwStyle(style)
    }

    private fun setFontFamily() {
        typeface = Typeface.create("HarmonyOSHans-Medium", Typeface.NORMAL)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d(TAG, "button click style is$isEnabled")
        if (isEnabled) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> if (!isOnlyFont) startButtonAnimation(R.anim.button_down_scale)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> if (!isOnlyFont) startButtonAnimation(R.anim.button_up_scale)
            }
            return super.onTouchEvent(event)
        }
        return true
    }

    fun sethkbTextColor(color: Int) {
        setTextColor(color)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        alpha = if (enabled) 1.0f else 0.4f
    }

    private fun startButtonAnimation(animRes: Int) {
        try {
            Log.d(TAG, "startAnimation")
            if (AnimationUtils.loadAnimation(context, animRes) is AnimationSet) {
                @Suppress("DEPRECATION")
                startAnimation(AnimationUtils.loadAnimation(context, animRes) as AnimationSet)
            }
        } catch (e: Resources.NotFoundException) {
            Log.d(TAG, "something error : ${e.message}${e.fillInStackTrace()}")
        }
    }

    companion object {
        private const val TAG = "HwKidButton"
    }
}
