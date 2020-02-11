package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
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

    var customOnTouchListener: OnTouchListener? =null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            val sixteenNineHeight = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec) * 9 / 16, View.MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, sixteenNineHeight)
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * This method is usually used to intercept all touch screen motion events.
     * This allows to watch events as they are dispatched to the children,
     * and take ownership of the current gesture at any point.
     *
     * We're using it to call onTouchEvent() without stopping
     * the event from reaching the children.
     * @param ev: The motion event being dispatched down the hierarchy.
     * @return Return true to steal motion events from the children
     * and have them dispatched to this ViewGroup through onTouchEvent()
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        // Even though click and long click events are being called, the touch listener
        // doesn't seem to work. So we need to register and use our own touch listener
       val isTouchEventHandled = customOnTouchListener?.onTouch(rootView,ev)

        if(isTouchEventHandled == null || !isTouchEventHandled)
            onTouchEvent(ev)

        return super.onInterceptTouchEvent(ev)
    }

    override fun setOnTouchListener(listener: OnTouchListener?) {
        customOnTouchListener = listener;
    }
}