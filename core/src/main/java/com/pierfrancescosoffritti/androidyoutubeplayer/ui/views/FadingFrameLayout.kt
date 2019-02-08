package com.pierfrancescosoffritti.androidyoutubeplayer.ui.views

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.pierfrancescosoffritti.androidyoutubeplayer.R
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener

class FadingFrameLayout(context: Context, attrs: AttributeSet? = null): FrameLayout(context, attrs), YouTubePlayerListener {

    private var isPlaying = false

    private var canFade = false
    private var isVisible = true

    private var fadeOut: Runnable = Runnable{ fade(0f) }

    var isDisabled = false

    /**
     * Duration of the fade animation in milliseconds.
     */
    val animationDuration: Long

    /**
     * Delay after which the view automatically fades out.
     */
    val fadeOutDelay: Long

    init {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.FadingFrameLayout, 0, 0)

        animationDuration = typedArray.getInt(R.styleable.FadingFrameLayout_animationDuration, 300).toLong()
        fadeOutDelay = typedArray.getInt(R.styleable.FadingFrameLayout_fadeOutDelay, 3000).toLong()

        typedArray.recycle()
    }

    fun toggleVisibility() {
        fade(if (isVisible) 0f else 1f)
    }

    private fun fade(finalAlpha: Float) {
        if (!canFade || isDisabled)
            return

        isVisible = finalAlpha != 0f

        // if the controls are shown and the player is playing they should automatically fade after a while.
        // otherwise don't do anything automatically
        if (finalAlpha == 1f && isPlaying)
            handler.postDelayed(fadeOut, fadeOutDelay)
        else
            handler.removeCallbacks(fadeOut)

        animate()
                .alpha(finalAlpha)
                .setDuration(animationDuration)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {
                        if (finalAlpha == 1f) visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animator: Animator) {
                        if (finalAlpha == 0f) visibility = View.GONE
                    }

                    override fun onAnimationCancel(animator: Animator) {}
                    override fun onAnimationRepeat(animator: Animator) {}
                }).start()
    }

    private fun updateState(state: PlayerConstants.PlayerState) {
        when (state) {
            PlayerConstants.PlayerState.ENDED -> isPlaying = false
            PlayerConstants.PlayerState.PAUSED -> isPlaying = false
            PlayerConstants.PlayerState.PLAYING -> isPlaying = true
            PlayerConstants.PlayerState.UNSTARTED -> { }
            else -> { }
        }
    }

    override fun onStateChange(state: PlayerConstants.PlayerState) {
        updateState(state)

        when(state) {
            PlayerConstants.PlayerState.PLAYING, PlayerConstants.PlayerState.PAUSED, PlayerConstants.PlayerState.VIDEO_CUED -> {
                canFade = true
                if (state == PlayerConstants.PlayerState.PLAYING)
                    handler.postDelayed(fadeOut, fadeOutDelay)
                else
                    handler.removeCallbacks(fadeOut)
            }
            PlayerConstants.PlayerState.BUFFERING, PlayerConstants.PlayerState.UNSTARTED -> {
                fade(1f)
                canFade = false
            }
            PlayerConstants.PlayerState.UNKNOWN -> fade(1f)
            PlayerConstants.PlayerState.ENDED -> fade(1f)
        }
    }

    override fun onReady() { }
    override fun onPlaybackQualityChange(playbackQuality: PlayerConstants.PlaybackQuality) { }
    override fun onPlaybackRateChange(playbackRate: PlayerConstants.PlaybackRate) { }
    override fun onError(error: PlayerConstants.PlayerError) { }
    override fun onApiChange() { }
    override fun onCurrentSecond(second: Float) { }
    override fun onVideoDuration(duration: Float) { }
    override fun onVideoLoadedFraction(loadedFraction: Float) { }
    override fun onVideoId(videoId: String) { }
}