package com.pierfrancescosoffritti.cyplayersample.examples.localPlayerExample

import android.view.View
import android.widget.Button
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure.ChromecastConnectionListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.YouTubePlayerStateTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView
import com.pierfrancescosoffritti.cyplayersample.R
import com.pierfrancescosoffritti.cyplayersample.ui.SimpleChromecastUIController
import com.pierfrancescosoffritti.cyplayersample.utils.PlaybackUtils

/**
 * Class used to manage the two YouTubePlayers, local and cast.
 *
 * The local YouTubePlayer is supposed to stop playing when the cast player stars and vice versa.
 *
 * When one of the two players stops, the other has to resume the playback from where the previous player stopped.
 */
class YouTubePlayersManager(
        localYouTubePlayerInitListener: LocalYouTubePlayerInitListener,
        private val youtubePlayerView: YouTubePlayerView, chromecastControls: View,
        private val chromecastPlayerListener: YouTubePlayerListener) : ChromecastConnectionListener {

    private val nextVideoButton = chromecastControls.findViewById<Button>(R.id.next_video_button)

    val chromecastUIController = SimpleChromecastUIController(chromecastControls)

    private var localYouTubePlayer: YouTubePlayer? = null
    private var chromecastYouTubePlayer: YouTubePlayer? = null

    private val chromecastPlayerStateTracker = YouTubePlayerStateTracker()
    private val localPlayerStateTracker = YouTubePlayerStateTracker()

    private var playingOnCastPlayer = false

    init {
        initLocalYouTube(localYouTubePlayerInitListener)
        nextVideoButton.setOnClickListener { chromecastYouTubePlayer?.loadVideo(PlaybackUtils.getNextVideoId(), 0f) }
    }

    override fun onChromecastConnecting() {
        localYouTubePlayer?.pause()
    }

    override fun onChromecastConnected(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
        initializeCastPlayer(chromecastYouTubePlayerContext)

        playingOnCastPlayer = true
    }

    override fun onChromecastDisconnected() {
        if(chromecastPlayerStateTracker.currentState == PlayerConstants.PlayerState.PLAYING)
            localYouTubePlayer?.loadVideo(chromecastPlayerStateTracker.videoId!!, chromecastPlayerStateTracker.currentSecond)
        else
            localYouTubePlayer?.cueVideo(chromecastPlayerStateTracker.videoId!!, chromecastPlayerStateTracker.currentSecond)

        chromecastUIController.resetUI()

        playingOnCastPlayer = false
    }

    fun togglePlayback() {
        if(playingOnCastPlayer)
            if(chromecastPlayerStateTracker.currentState == PlayerConstants.PlayerState.PLAYING)
                chromecastYouTubePlayer?.pause()
            else
                chromecastYouTubePlayer?.play()
        else
            if(localPlayerStateTracker.currentState == PlayerConstants.PlayerState.PLAYING)
                localYouTubePlayer?.pause()
            else
                localYouTubePlayer?.play()
    }

    private fun initLocalYouTube(localYouTubePlayerInitListener: LocalYouTubePlayerInitListener) {
        youtubePlayerView.initialize({ youtubePlayer ->

            this.localYouTubePlayer = youtubePlayer
            youtubePlayer.addListener(localPlayerStateTracker)

            youtubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {
                    if(!playingOnCastPlayer)
                        youtubePlayer.loadVideo(PlaybackUtils.getNextVideoId(), chromecastPlayerStateTracker.currentSecond)

                    localYouTubePlayerInitListener.onLocalYouTubePlayerInit()
                }

                override fun onCurrentSecond(second: Float) {
                    if(playingOnCastPlayer && localPlayerStateTracker.currentState == PlayerConstants.PlayerState.PLAYING)
                        youtubePlayer.pause()
                }
            })
        }, true)
    }

    private fun initializeCastPlayer(chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext) {
        chromecastYouTubePlayerContext.initialize( YouTubePlayerInitListener { youtubePlayer ->

            chromecastYouTubePlayer = youtubePlayer

            chromecastUIController.youTubePlayer = youtubePlayer

            youtubePlayer.addListener(chromecastPlayerListener)
            youtubePlayer.addListener(chromecastPlayerStateTracker)
            youtubePlayer.addListener(chromecastUIController)

            youtubePlayer.addListener(object: AbstractYouTubePlayerListener() {
                override fun onReady() {
                    youtubePlayer.loadVideo(localPlayerStateTracker.videoId!!, localPlayerStateTracker.currentSecond)
                }
            })
        })
    }

    /**
     * Interface used to notify its listeners than the local YouTubePlayer is ready to play videos.
     */
    interface LocalYouTubePlayerInitListener {
        fun onLocalYouTubePlayerInit()
    }
}