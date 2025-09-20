package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.customUiExample;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils.VideoIdsProvider;
import com.pierfrancescosoffritti.aytplayersample.R;

public class CustomUiActivity extends AppCompatActivity {

  private YouTubePlayerView youTubePlayerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_custom_ui_example);

    youTubePlayerView = findViewById(R.id.youtube_player_view);
    getLifecycle().addObserver(youTubePlayerView);

    View customPlayerUi = youTubePlayerView.inflateCustomPlayerUi(R.layout.custom_player_ui);

    YouTubePlayerListener listener = new AbstractYouTubePlayerListener() {
      @Override
      public void onReady(@NonNull YouTubePlayer youTubePlayer) {
        CustomPlayerUiController customPlayerUiController = new CustomPlayerUiController(CustomUiActivity.this, customPlayerUi, youTubePlayer, youTubePlayerView);
        youTubePlayer.addListener(customPlayerUiController);

        YouTubePlayerUtils.loadOrCueVideo(
                youTubePlayer, getLifecycle(),
                VideoIdsProvider.getNextVideoId(), 0f
        );
      }
    };

    // disable web ui
    IFramePlayerOptions options = new IFramePlayerOptions.Builder(getApplicationContext()).controls(0).build();

    youTubePlayerView.initialize(listener, options);
  }

  @Override
  public void onConfigurationChanged(@NonNull Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    // Checks the orientation of the screen
    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      youTubePlayerView.matchParent();
    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
      youTubePlayerView.wrapContent();
    }
  }
}
