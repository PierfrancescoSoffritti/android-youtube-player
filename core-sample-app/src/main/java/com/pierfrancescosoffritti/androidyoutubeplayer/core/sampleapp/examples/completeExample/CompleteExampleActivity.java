package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.completeExample;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils.VideoIdsProvider;
import com.pierfrancescosoffritti.aytplayersample.R;

public class CompleteExampleActivity extends AppCompatActivity {

  private YouTubePlayerView youTubePlayerView;
  private boolean isPlayerMatchParent = false;

  // a list of videos not available in some countries, to test if they're handled gracefully.
  // private String[] nonPlayableVideoIds = { "sop2V_MREEI" };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basic_example);

    youTubePlayerView = findViewById(R.id.youtube_player_view);

    initYouTubePlayerView();
  }

  @Override
  public void onConfigurationChanged(@NonNull Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    // Checks the orientation of the screen
    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      youTubePlayerView.matchParent();
      isPlayerMatchParent = true;
    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
      youTubePlayerView.wrapContent();
      isPlayerMatchParent = false;
    }
  }

  @Override
  public void onBackPressed() {
    if (isPlayerMatchParent) {
      youTubePlayerView.wrapContent();
      isPlayerMatchParent = false;
    } else {
      super.onBackPressed();
    }
  }

  private void initYouTubePlayerView() {
    // The player will automatically release itself when the activity is destroyed.
    // The player will automatically pause when the activity is stopped
    // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.
    getLifecycle().addObserver(youTubePlayerView);

    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
      @Override
      public void onReady(@NonNull YouTubePlayer youTubePlayer) {
        YouTubePlayerUtils.loadOrCueVideo(
                youTubePlayer,
                getLifecycle(),
                VideoIdsProvider.getNextVideoId(),
                0f
        );

        setPlayNextVideoButtonClickListener(youTubePlayer);
        setPlaybackSpeedButtonsClickListeners(youTubePlayer);
      }

      @Override
      public void onPlaybackRateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {
        TextView playbackSpeedTextView = findViewById(R.id.playback_speed_text_view);
        String playbackSpeed = "Playback speed: ";
        playbackSpeedTextView.setText(playbackSpeed + playbackRate);
      }
    });
  }

  /**
   * Set a click listener on the "Play next video" button
   */
  private void setPlayNextVideoButtonClickListener(final YouTubePlayer youTubePlayer) {
    Button playNextVideoButton = findViewById(R.id.next_video_button);

    playNextVideoButton.setOnClickListener(view ->
            YouTubePlayerUtils.loadOrCueVideo(
                    youTubePlayer, getLifecycle(),
                    VideoIdsProvider.getNextVideoId(), 0f
            ));
  }

  /**
   * Set the click listeners for the "playback speed" buttons
   */
  private void setPlaybackSpeedButtonsClickListeners(YouTubePlayer youTubePlayer) {
    Button playbackSpeed_0_25 = findViewById(R.id.playback_speed_0_25);
    Button playbackSpeed_1 = findViewById(R.id.playback_speed_1);
    Button playbackSpeed_2 = findViewById(R.id.playback_speed_2);

    playbackSpeed_0_25.setOnClickListener(view -> youTubePlayer.setPlaybackRate(PlayerConstants.PlaybackRate.RATE_0_25));
    playbackSpeed_1.setOnClickListener(view -> youTubePlayer.setPlaybackRate(PlayerConstants.PlaybackRate.RATE_1));
    playbackSpeed_2.setOnClickListener(view -> youTubePlayer.setPlaybackRate(PlayerConstants.PlaybackRate.RATE_2));
  }
}
