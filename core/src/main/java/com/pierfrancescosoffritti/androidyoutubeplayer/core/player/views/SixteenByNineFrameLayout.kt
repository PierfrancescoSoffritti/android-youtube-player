package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RestrictTo

/**
 * A FrameLayout with an aspect ration of 16:9, when the height is set to wrap_content.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
open class SixteenByNineFrameLayout: FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            val sixteenNineHeight = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec) * 9 / 16, View.MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, sixteenNineHeight)
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}