package com.pierfrancescosoffritti.cyplayersample.ui

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.Utils
import com.pierfrancescosoffritti.cyplayersample.R

/**
* Class used to control a simple UI for the cast player.
*/
class SimpleChromecastUIController(private val controls_view: View) : AbstractYouTubePlayerListener(), SeekBar.OnSeekBarChangeListener {
    lateinit var youTubePlayer: YouTubePlayer

    private var isPlaying = false

    private val progressBar = controls_view.findViewById<View>(R.id.progress_bar)
    private val playPauseButton = controls_view.findViewById<ImageView>(R.id.toggle_playback_button)
    private val currentTimeTextView = controls_view.findViewById<TextView>(R.id.current_time_text_view)
    private val totalTimeTextView = controls_view.findViewById<TextView>(R.id.total_time_text_view)
    private val seekBar = controls_view.findViewById<SeekBar>(R.id.seek_bar)
    private val youTubeButton = controls_view.findViewById<ImageView>(R.id.youtube_button)

    private val newViewsContainer = controls_view.findViewById<FrameLayout>(R.id.cast_button_container)

    init {
        seekBar.setOnSeekBarChangeListener(this)
        playPauseButton.setOnClickListener { onPlayButtonPressed() }
    }

    override fun onStateChange(state: Int) {
        newSeekBarProgress = -1

        updateControlsState(state)

        if (state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.VIDEO_CUED || state == PlayerConstants.PlayerState.UNSTARTED) {
            progressBar.visibility = View.INVISIBLE
            playPauseButton.visibility = View.VISIBLE

        } else if(state == PlayerConstants.PlayerState.BUFFERING) {
            progressBar.visibility = View.VISIBLE
            playPauseButton.visibility = View.INVISIBLE
        }

        val playing = state == PlayerConstants.PlayerState.PLAYING
        updatePlayPauseButtonIcon(playing)
    }

    override fun onVideoDuration(duration: Float) {
        totalTimeTextView.text = Utils.formatTime(duration)
        seekBar.max = duration.toInt()
    }

    override fun onCurrentSecond(currentSecond: Float) {
        if (seekBarTouchStarted)
            return

        // ignore if the current time is older than what the user selected with the SeekBar
        if (newSeekBarProgress > 0 && Utils.formatTime(currentSecond) != Utils.formatTime(newSeekBarProgress.toFloat()))
            return

        newSeekBarProgress = -1

        seekBar.progress = currentSecond.toInt()
    }

    override fun onVideoLoadedFraction(loadedFraction: Float) {
        seekBar.secondaryProgress = loadedFraction.toInt()
    }

    override fun onVideoId(videoId: String) {
        youTubeButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$videoId"))
            controls_view.context.startActivity(intent)
        }
    }

    fun addView(view: View) {
        newViewsContainer.addView(view)
    }

    fun removeView(view: View) {
        newViewsContainer.removeView(view)
    }

    private fun updateControlsState(state: Int) {
        when (state) {
            PlayerConstants.PlayerState.ENDED -> isPlaying = false
            PlayerConstants.PlayerState.PAUSED -> isPlaying = false
            PlayerConstants.PlayerState.PLAYING -> isPlaying = true
            PlayerConstants.PlayerState.BUFFERING -> isPlaying = false
            PlayerConstants.PlayerState.UNSTARTED -> resetUI()
            else -> {
            }
        }

        updatePlayPauseButtonIcon(!isPlaying)
    }

    fun resetUI() {
        seekBar.progress = 0
        seekBar.max = 0
        playPauseButton.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        currentTimeTextView.post { currentTimeTextView.text = "" }
        totalTimeTextView.post { totalTimeTextView.text = "" }
    }

    private fun updatePlayPauseButtonIcon(playing: Boolean) {
        val img = if (playing) R.drawable.ic_pause_36dp else R.drawable.ic_play_36dp
        playPauseButton.setImageResource(img)
    }

    private fun onPlayButtonPressed() {
        if (isPlaying)
            youTubePlayer.pause()
        else
            youTubePlayer.play()
    }

    // -- SeekBar, this code will be refactored

    private var seekBarTouchStarted = false
    // I need this variable because onCurrentSecond gets called every 100 mill, so without the proper checks on this variable in onCurrentSeconds the seek bar glitches when touched.
    private var newSeekBarProgress = -1

    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
        currentTimeTextView.text = Utils.formatTime(i.toFloat())
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        seekBarTouchStarted = true
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        if (isPlaying)
            newSeekBarProgress = seekBar.progress

        youTubePlayer.seekTo(seekBar.progress.toFloat())
        seekBarTouchStarted = false
    }
}