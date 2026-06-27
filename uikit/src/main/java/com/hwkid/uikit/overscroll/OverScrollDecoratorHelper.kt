package com.hwkid.uikit.overscroll

class OverScrollDecoratorHelper private constructor() {
    companion object {
        @JvmStatic
        fun setUpOverScroll(listView: android.widget.ListView): OverScrollDecor {
            return VerticalOverScrollBounceEffectDecorator(
                com.hwkid.uikit.overscroll.adapter.AbsListViewOverScrollDecorAdapter(listView)
            )
        }

        @JvmStatic
        fun setUpOverScroll(recyclerView: androidx.recyclerview.widget.RecyclerView, orientation: Int): OverScrollDecor {
            return when (orientation) {
                ORIENTATION_VERTICAL -> VerticalOverScrollBounceEffectDecorator(
                    com.hwkid.uikit.overscroll.adapter.RecyclerViewOverScrollDecorAdapter(recyclerView)
                )
                ORIENTATION_HORIZONTAL -> HorizontalOverScrollBounceEffectDecorator(
                    com.hwkid.uikit.overscroll.adapter.RecyclerViewOverScrollDecorAdapter(recyclerView)
                )
                else -> throw IllegalArgumentException("orientation")
            }
        }

        const val ORIENTATION_VERTICAL = 0
        const val ORIENTATION_HORIZONTAL = 1
    }
}
