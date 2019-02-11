package com.pierfrancescosoffritti.androidyoutubeplayer.core.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

import com.pierfrancescosoffritti.androidyoutubeplayer.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.LegacyYouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.menu.YouTubePlayerMenu
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.menu.defaultMenu.DefaultYouTubePlayerMenu
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.utils.FadeViewHelper
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBarListener
import androidx.core.content.ContextCompat

internal class DefaultPlayerUiController(private val youTubePlayerView: LegacyYouTubePlayerView, private val youTubePlayer: YouTubePlayer) : PlayerUiController, YouTubePlayerListener, YouTubePlayerFullScreenListener, YouTubePlayerSeekBarListener {

    private var youTubePlayerMenu: YouTubePlayerMenu

    /**
     * View used for for intercepting clicks and for drawing a black background.
     * Could have used controlsContainer, but in this way I'm able to hide all the control at once by hiding controlsContainer
     */
    private val panel: View

    private val controlsContainer: View
    private val extraViewsContainer: LinearLayout

    private val videoTitle: TextView
    private val liveVideoIndicator: TextView

    private val progressBar: ProgressBar
    private val menuButton: ImageView
    private val playPauseButton: ImageView
    private val youTubeButton: ImageView
    private val fullScreenButton: ImageView

    private val customActionLeft: ImageView
    private val customActionRight: ImageView

    private val youtubePlayerSeekBar: YouTubePlayerSeekBar

    private var onFullScreenButtonListener: View.OnClickListener
    private var onMenuButtonClickListener: View.OnClickListener

    private val fadeControlsContainer: FadeViewHelper

    private var isPlaying = false
    private var isPlayPauseButtonEnabled = true
    private var isCustomActionLeftEnabled = false
    private var isCustomActionRightEnabled = false

    init {

        val controlsView = View.inflate(youTubePlayerView.context, R.layout.ayp_default_player_ui, youTubePlayerView)

        youTubePlayerMenu = DefaultYouTubePlayerMenu(youTubePlayerView.context)

        panel = controlsView.findViewById(R.id.panel)
        controlsContainer = controlsView.findViewById(R.id.controls_container)
        extraViewsContainer = controlsView.findViewById(R.id.extra_views_container)

        videoTitle = controlsView.findViewById(R.id.video_title)
        liveVideoIndicator = controlsView.findViewById(R.id.live_video_indicator)

        progressBar = controlsView.findViewById(R.id.progress)
        menuButton = controlsView.findViewById(R.id.menu_button)
        playPauseButton = controlsView.findViewById(R.id.play_pause_button)
        youTubeButton = controlsView.findViewById(R.id.youtube_button)
        fullScreenButton = controlsView.findViewById(R.id.fullscreen_button)

        customActionLeft = controlsView.findViewById(R.id.custom_action_left_button)
        customActionRight = controlsView.findViewById(R.id.custom_action_right_button)

        youtubePlayerSeekBar = controlsView.findViewById(R.id.youtube_player_seekbar)

        fadeControlsContainer = FadeViewHelper(controlsContainer)

        onFullScreenButtonListener = View.OnClickListener { youTubePlayerView.toggleFullScreen() }
        onMenuButtonClickListener = View.OnClickListener { youTubePlayerMenu.show(menuButton) }

        initClickListeners()
    }

    private fun initClickListeners() {
        youTubePlayer.addListener(youtubePlayerSeekBar)
        youTubePlayer.addListener(fadeControlsContainer)

        youtubePlayerSeekBar.youtubePlayerSeekBarListener = this
        panel.setOnClickListener { fadeControlsContainer.toggleVisibility() }
        playPauseButton.setOnClickListener { onPlayButtonPressed() }
        fullScreenButton.setOnClickListener { onFullScreenButtonListener.onClick(fullScreenButton) }
        menuButton.setOnClickListener { onMenuButtonClickListener.onClick(menuButton) }
    }

    override fun showVideoTitle(show: Boolean): PlayerUiController {
        videoTitle.visibility = if (show) View.VISIBLE else View.GONE
        return this
    }

    override fun setVideoTitle(videoTitle: String): PlayerUiController {
        this.videoTitle.text = videoTitle
        return this
    }

    override fun showUi(show: Boolean): PlayerUiController {
        fadeControlsContainer.isDisabled = !show
        controlsContainer.visibility = if (show) View.VISIBLE else View.INVISIBLE
        return this
    }

    override fun showPlayPauseButton(show: Boolean): PlayerUiController {
        playPauseButton.visibility = if (show) View.VISIBLE else View.GONE

        isPlayPauseButtonEnabled = show
        return this
    }

    override fun enableLiveVideoUi(enable: Boolean): PlayerUiController {
        youtubePlayerSeekBar.visibility = if (enable) View.INVISIBLE else View.VISIBLE
        liveVideoIndicator.visibility = if (enable) View.VISIBLE else View.GONE
        return this
    }

    override fun setCustomAction1(icon: Drawable, clickListener: View.OnClickListener?): PlayerUiController {
        customActionLeft.setImageDrawable(icon)
        customActionLeft.setOnClickListener(clickListener)
        showCustomAction1(true)
        return this
    }

    override fun setCustomAction2(icon: Drawable, clickListener: View.OnClickListener?): PlayerUiController {
        customActionRight.setImageDrawable(icon)
        customActionRight.setOnClickListener(clickListener)
        showCustomAction2(true)
        return this
    }

    override fun showCustomAction1(show: Boolean): PlayerUiController {
        isCustomActionLeftEnabled = show
        customActionLeft.visibility = if (show) View.VISIBLE else View.GONE
        return this
    }

    override fun showCustomAction2(show: Boolean): PlayerUiController {
        isCustomActionRightEnabled = show
        customActionRight.visibility = if (show) View.VISIBLE else View.GONE
        return this
    }

    override fun showMenuButton(show: Boolean): PlayerUiController {
        menuButton.visibility = if (show) View.VISIBLE else View.GONE
        return this
    }

    override fun setMenuButtonClickListener(customMenuButtonClickListener: View.OnClickListener): PlayerUiController {
        onMenuButtonClickListener = customMenuButtonClickListener
        return this
    }

    override fun showCurrentTime(show: Boolean): PlayerUiController {
        youtubePlayerSeekBar.videoCurrentTimeTextView.visibility = if (show) View.VISIBLE else View.GONE
        return this
    }

    override fun showDuration(show: Boolean): PlayerUiController {
        youtubePlayerSeekBar.videoDurationTextView.visibility = if (show) View.VISIBLE else View.GONE
        return this
    }

    override fun showSeekBar(show: Boolean): PlayerUiController {
        youtubePlayerSeekBar.seekBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
        return this
    }

    override fun showBufferingProgress(show: Boolean): PlayerUiController {
        youtubePlayerSeekBar.showBufferingProgress = show
        return this
    }

    override fun showYouTubeButton(show: Boolean): PlayerUiController {
        youTubeButton.visibility = if (show) View.VISIBLE else View.GONE
        return this
    }

    override fun addView(view: View): PlayerUiController {
        extraViewsContainer.addView(view, 0)
        return this
    }

    override fun removeView(view: View): PlayerUiController {
        extraViewsContainer.removeView(view)
        return this
    }

    override fun getMenu(): YouTubePlayerMenu = youTubePlayerMenu

    override fun showFullscreenButton(show: Boolean): PlayerUiController {
        fullScreenButton.visibility = if (show) View.VISIBLE else View.GONE
        return this
    }

    override fun setFullScreenButtonClickListener(customFullScreenButtonClickListener: View.OnClickListener): PlayerUiController {
        onFullScreenButtonListener = customFullScreenButtonClickListener
        return this
    }

    private fun onPlayButtonPressed() {
        if (isPlaying)
            youTubePlayer.pause()
        else
            youTubePlayer.play()
    }

    override fun onYouTubePlayerEnterFullScreen() =
        fullScreenButton.setImageResource(R.drawable.ayp_ic_fullscreen_exit_24dp)

    override fun onYouTubePlayerExitFullScreen() =
        fullScreenButton.setImageResource(R.drawable.ayp_ic_fullscreen_24dp)

    private fun updateState(state: PlayerConstants.PlayerState) {
        when (state) {
            PlayerConstants.PlayerState.ENDED -> isPlaying = false
            PlayerConstants.PlayerState.PAUSED -> isPlaying = false
            PlayerConstants.PlayerState.PLAYING -> isPlaying = true
            else -> { }
        }

        updatePlayPauseButtonIcon(!isPlaying)
    }

    private fun updatePlayPauseButtonIcon(playing: Boolean) =
        playPauseButton.setImageResource(if (playing) R.drawable.ayp_ic_pause_36dp else R.drawable.ayp_ic_play_36dp)

    override fun seekTo(time: Float) = youTubePlayer.seekTo(time)

    // YouTubePlayer callbacks

    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
        updateState(state)

        if (state === PlayerConstants.PlayerState.PLAYING || state === PlayerConstants.PlayerState.PAUSED || state === PlayerConstants.PlayerState.VIDEO_CUED) {
            panel.setBackgroundColor(ContextCompat.getColor(panel.context, android.R.color.transparent))
            progressBar.visibility = View.GONE

            if (isPlayPauseButtonEnabled) playPauseButton.visibility = View.VISIBLE
            if (isCustomActionLeftEnabled) customActionLeft.visibility = View.VISIBLE
            if (isCustomActionRightEnabled) customActionRight.visibility = View.VISIBLE

            updatePlayPauseButtonIcon(state === PlayerConstants.PlayerState.PLAYING)

        } else {
            updatePlayPauseButtonIcon(false)

            if (state === PlayerConstants.PlayerState.BUFFERING) {
                progressBar.visibility = View.VISIBLE
                panel.setBackgroundColor(ContextCompat.getColor(panel.context, android.R.color.transparent))
                if (isPlayPauseButtonEnabled) playPauseButton.visibility = View.INVISIBLE

                customActionLeft.visibility = View.GONE
                customActionRight.visibility = View.GONE
            }

            if (state === PlayerConstants.PlayerState.UNSTARTED) {
                progressBar.visibility = View.GONE
                if (isPlayPauseButtonEnabled) playPauseButton.visibility = View.VISIBLE
            }
        }
    }

    override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
        youTubeButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId + "#t=" + youtubePlayerSeekBar.seekBar.progress))
            youTubeButton.context.startActivity(intent)
        }
    }

    override fun onReady(youTubePlayer: YouTubePlayer) {}
    override fun onPlaybackQualityChange(youTubePlayer: YouTubePlayer, playbackQuality: PlayerConstants.PlaybackQuality) {}
    override fun onPlaybackRateChange(youTubePlayer: YouTubePlayer, playbackRate: PlayerConstants.PlaybackRate) {}
    override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {}
    override fun onApiChange(youTubePlayer: YouTubePlayer) {}
    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {}
    override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {}
    override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {}
}
