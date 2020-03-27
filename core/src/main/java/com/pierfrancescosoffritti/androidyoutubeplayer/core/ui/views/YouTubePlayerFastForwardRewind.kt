package com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.pierfrancescosoffritti.androidyoutubeplayer.R


/**
 *  A view used for fast forwarding or fast rewinding the player.
 *  This view itself doesn't perform the seek event, but it exposes [addOnSeekAction] for other components
 *  to add callbacks that will be executed whenever a seek action occurs.
 */

class YouTubePlayerFastForwardRewind(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private val fastForwardRewindIndicator: LinearLayout

    private val fastForwardRewindLeftTriangle: ImageView
    private val fastForwardRewindMidImageTriangle: ImageView
    private val fastForwardRewindRightImageTriangle: ImageView
    private lateinit var fadeInLeftTriangleAnim: FastForwardRewindTrianglesAnimation
    private lateinit var fadeInMidTriangleAnim: FastForwardRewindTrianglesAnimation
    private lateinit var fadeInRightTriangleAnim: FastForwardRewindTrianglesAnimation

    private val fastForwardRewindText: TextView

    private val fastForwardRewindTimer: CountDownTimer
    private val msFastForwardRewindWaitTime = 1000L
    private var secToSeek = 0F
    private var secToIncrementPerClick = 0F

    private var hasBeenClickedOnce = false

    private val seekActions = HashSet<(Float) -> Unit>()
    private val otherFastForwardRewindViews = HashSet<YouTubePlayerFastForwardRewind>()

    init {
        inflate(context, R.layout.ayp_fast_forward_rewind, this)

        val fastForwardRewindLayout: RelativeLayout = findViewById(R.id.fast_forward_rewind_layout)

        fastForwardRewindIndicator = findViewById(R.id.fast_forward_rewind_indicator)

        fastForwardRewindLeftTriangle = findViewById(R.id.fast_forward_rewind_left_image)
        fastForwardRewindMidImageTriangle = findViewById(R.id.fast_forward_rewind_mid_image)
        fastForwardRewindRightImageTriangle = findViewById(R.id.fast_forward_rewind_right_image)

        fastForwardRewindText = findViewById(R.id.fast_forward_rewind_text)


        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.YouTubePlayerFastForwardRewind, 0, 0)
        val shouldUseFastRewindLayout = typedArray.getBoolean(R.styleable.YouTubePlayerFastForwardRewind_useFastRewindLayout, false)

        useFastRewindLayout(shouldUseFastRewindLayout)

        fastForwardRewindTimer = object : CountDownTimer(msFastForwardRewindWaitTime, msFastForwardRewindWaitTime) {
            override fun onTick(millisUntilFinished: Long) { }

            /**
             * Run all [seekActions] with [secToSeek] as a parameter
             */
            override fun onFinish() {
                // Fire seek actions only if [secToSeek] is valid. Can be invalid if timer ends after just a single click
                if (secToSeek != 0F) {
                    for (seekAction in seekActions)
                        seekAction(secToSeek)
                }
                resetUIAndVariables()
            }
        }

        fastForwardRewindLayout.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Start or restart timer
                    fastForwardRewindTimer.cancel()
                    fastForwardRewindTimer.start()

                    // Need to stop other fastForwardRewindViews, this will avoid running multiple seekers in parallel
                    for (otherFastForwardRewindView in otherFastForwardRewindViews)
                        otherFastForwardRewindView.interruptSeek()

                    // If has been clicked before, then fast forward
                    if (hasBeenClickedOnce) {

                        secToSeek += secToIncrementPerClick

                        fastForwardRewindText.text = "${Math.abs(secToSeek.toInt())} seconds"

                        // Make text and triangles parent visible, then start fast forward/rewind animation
                        if (fastForwardRewindIndicator.visibility == View.INVISIBLE) {
                            fastForwardRewindIndicator.visibility = View.VISIBLE
                            fastForwardRewindIndicator.animate().alpha(1f).setDuration(200)

                            fastForwardRewindLeftTriangle.startAnimation(fadeInLeftTriangleAnim)
                            fastForwardRewindMidImageTriangle.startAnimation(fadeInMidTriangleAnim)
                            fastForwardRewindRightImageTriangle.startAnimation(fadeInRightTriangleAnim)
                        }
                    } else hasBeenClickedOnce = true
                }
            }
            v?.onTouchEvent(event) ?: true
        }
    }

    fun useFastRewindLayout(shouldUseFastRewindLayout: Boolean) {
        val animationDurationForSingleTriangle = msFastForwardRewindWaitTime / 3

        if (shouldUseFastRewindLayout) {
            secToIncrementPerClick = -10F

            fastForwardRewindLeftTriangle.rotation = -90f
            fastForwardRewindMidImageTriangle.rotation = -90f
            fastForwardRewindRightImageTriangle.rotation = -90f

            fadeInRightTriangleAnim = FastForwardRewindTrianglesAnimation(0f, 1f, 0, animationDurationForSingleTriangle, msFastForwardRewindWaitTime, fastForwardRewindLeftTriangle)
            fadeInMidTriangleAnim = FastForwardRewindTrianglesAnimation(0f, 1f, animationDurationForSingleTriangle / 2, animationDurationForSingleTriangle, msFastForwardRewindWaitTime, fastForwardRewindMidImageTriangle)
            fadeInLeftTriangleAnim = FastForwardRewindTrianglesAnimation(0f, 1f, animationDurationForSingleTriangle, animationDurationForSingleTriangle, msFastForwardRewindWaitTime, fastForwardRewindRightImageTriangle)

        } else {
            secToIncrementPerClick = 10F

            fastForwardRewindLeftTriangle.rotation = 90f
            fastForwardRewindMidImageTriangle.rotation = 90f
            fastForwardRewindRightImageTriangle.rotation = 90f

            fadeInLeftTriangleAnim = FastForwardRewindTrianglesAnimation(0f, 1f, 0, animationDurationForSingleTriangle, msFastForwardRewindWaitTime, fastForwardRewindLeftTriangle)
            fadeInMidTriangleAnim = FastForwardRewindTrianglesAnimation(0f, 1f, animationDurationForSingleTriangle / 2, animationDurationForSingleTriangle, msFastForwardRewindWaitTime, fastForwardRewindMidImageTriangle)
            fadeInRightTriangleAnim = FastForwardRewindTrianglesAnimation(0f, 1f, animationDurationForSingleTriangle, animationDurationForSingleTriangle, msFastForwardRewindWaitTime, fastForwardRewindRightImageTriangle)
        }
    }

    /**
     *  Resets the view to the way it was before any seek event was started.
     *  Note: This will not stop an already running seek event. If you want to
     *  stop an already running seek event, see [interruptSeek]
     */
    fun resetUIAndVariables() {
        secToSeek = 0F
        hasBeenClickedOnce = false
        fastForwardRewindIndicator.animate().alpha(0f).setDuration(200).withEndAction { fastForwardRewindIndicator.visibility = View.INVISIBLE }

        fastForwardRewindLeftTriangle.clearAnimation()
        fastForwardRewindMidImageTriangle.clearAnimation()
        fastForwardRewindRightImageTriangle.clearAnimation()
    }

    /**
     *  Stops the currently running seek event then resets the view to the way it was before
     *  any seek event was started.
     */
    fun interruptSeek() {
        fastForwardRewindTimer.cancel()
        resetUIAndVariables()
    }

    /**
     * @param func Callback executed when a seek action occurs
     */
    fun addOnSeekAction(func: (Float) -> Unit): Boolean = seekActions.add(func)
    fun removeOnSeekAction(func: (Float) -> Unit): Boolean = seekActions.remove(func)

    /**
     * @param otherFastForwardRewindView Other [YouTubePlayerFastForwardRewind] that should be interrupted while using this one.
     */
    fun addOtherFastForwardRewindView(otherFastForwardRewindView: YouTubePlayerFastForwardRewind): Boolean = otherFastForwardRewindViews.add(otherFastForwardRewindView)
    fun removeOtherFastForwardRewindView(otherFastForwardRewindView: YouTubePlayerFastForwardRewind): Boolean = otherFastForwardRewindViews.remove(otherFastForwardRewindView)
}
