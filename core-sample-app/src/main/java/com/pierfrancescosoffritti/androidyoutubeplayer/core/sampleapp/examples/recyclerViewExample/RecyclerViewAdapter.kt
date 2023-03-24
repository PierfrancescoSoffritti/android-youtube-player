package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.recyclerViewExample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.aytplayersample.R

internal class RecyclerViewAdapter(
  private val videoIds: List<String>,
  private val lifecycle: Lifecycle
) : RecyclerView.Adapter<RecyclerViewAdapter.YouTubePlayerViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YouTubePlayerViewHolder {
    val itemView = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.recycler_view_item, parent, false)

    return YouTubePlayerViewHolder(lifecycle, itemView)
  }

  override fun onBindViewHolder(viewHolder: YouTubePlayerViewHolder, position: Int) {
    viewHolder.cueVideo(videoIds[position])
  }

  override fun getItemCount() = videoIds.size

  /**
   * ViewHolder containing a YouTubePlayer. When the list is scrolled only the video id changes.
   */
  internal class YouTubePlayerViewHolder(
    lifecycle: Lifecycle, view: View
  ) : RecyclerView.ViewHolder(view) {
    private var youTubePlayer: YouTubePlayer? = null
    private var currentVideoId: String? = null

    init {
      val youTubePlayerView = view.findViewById<YouTubePlayerView>(R.id.youtube_player_view)
      lifecycle.addObserver(youTubePlayerView)

      // the overlay view is used to intercept clicks when scrolling the recycler view
      // without it touch events used to scroll might accidentally trigger clicks in the player
      val overlayView = view.findViewById<View>(R.id.overlay_view)
      // when the overlay is clicked it starts playing the video
      overlayView.setOnClickListener { youTubePlayer?.play() }

      youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
          // store youtube player reference for later
          this@YouTubePlayerViewHolder.youTubePlayer = youTubePlayer
          // cue the video if it's available
          currentVideoId?.let { youTubePlayer.cueVideo(it, 0f) }
        }

        override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
          when (state) {
            // when the video is CUED, show the overlay.
            PlayerConstants.PlayerState.VIDEO_CUED -> overlayView.visibility = View.VISIBLE
            // remove the overlay for every other state, so that we don't intercept clicks and the
            // user can interact with the player.
            else -> overlayView.visibility = View.GONE
          }
        }
      })
    }

    fun cueVideo(videoId: String) {
      currentVideoId = videoId
      // cue the video if the youtube player is available
      youTubePlayer?.cueVideo(videoId, 0f)
    }
  }
}