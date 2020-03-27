package com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

/**
 * Creates an animation that's *specifically* made for each of the triangles in the fast forward/rewind icon.
 */
internal class FastForwardRewindTrianglesAnimation(fromAlpha: Float, toAlpha: Float) : AlphaAnimation(fromAlpha, toAlpha) {

    /**
     * @param fromAlpha Initial alpha
     * @param toAlpha Final alpha
     * @param startOffset What time to start the animation. This is used to specify the current triangle animation's sequence
     * @param singleTriangleAnimationDuration How long this triangle should take to fade in
     * @param entireAnimationDuration How long all triangles take to execute a single animation cycle
     * @param triangleView View of the triangle that's using this animation
     */
    constructor(fromAlpha: Float, toAlpha: Float, startOffset: Long, singleTriangleAnimationDuration: Long, entireAnimationDuration: Long, triangleView: View) : this(fromAlpha, toAlpha) {
        this.duration = singleTriangleAnimationDuration
        this.startOffset = startOffset
        this.initialStartOffset = startOffset
        this.entireAnimationDuration = entireAnimationDuration
        this.triangleView = triangleView
    }

    private var entireAnimationDuration = 0L
    private var initialStartOffset = 0L
    private lateinit var triangleView: View

    init {
        this.repeatCount = Animation.INFINITE
        this.setAnimationListener(object : AnimationListener {
            /**
             * Change the startOffset so this triangle's animation loops after all triangles have
             * completed a single cycle
             */
            override fun onAnimationRepeat(animation: Animation?) {
                animation?.startOffset = entireAnimationDuration
            }

            override fun onAnimationStart(animation: Animation?) {
                triangleView.visibility = View.VISIBLE
            }

            /**
             * Reset startOffset so this triangle's animation restarts properly next time
             * (so it doesn't start where it was stopped)
             */
            override fun onAnimationEnd(animation: Animation?) {
                triangleView.visibility = View.INVISIBLE
                animation?.startOffset = initialStartOffset
            }
        })
    }
}
