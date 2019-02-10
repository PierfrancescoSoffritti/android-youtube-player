package com.pierfrancescosoffritti.aytplayersample.examples.completeExample;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.PlayerUiController;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.menu.MenuItem;
import com.pierfrancescosoffritti.aytplayersample.utils.VideoIdsProvider;
import com.pierfrancescosoffritti.aytplayersample.utils.VideoInfo;
import com.pierfrancescosoffritti.aytplayersample.R;
import com.pierfrancescosoffritti.aytplayersample.utils.FullScreenHelper;
import com.pierfrancescosoffritti.aytplayersample.utils.YouTubeDataEndpoint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CompleteExampleActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private FullScreenHelper fullScreenHelper = new FullScreenHelper(this);

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
    public void onConfigurationChanged(Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        youTubePlayerView.getPlayerUiController().getMenu().dismiss();
    }

    @Override
    public void onBackPressed() {
        if (youTubePlayerView.isFullScreen())
            youTubePlayerView.exitFullScreen();
        else
            super.onBackPressed();
    }

    private void initYouTubePlayerView() {
        initPlayerMenu();

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

                addFullScreenListenerToPlayer(youTubePlayer);
                setPlayNextVideoButtonClickListener(youTubePlayer);
            }
        });
    }

    /**
     * Shows the menu button in the player and adds an item to it.
     */
    private void initPlayerMenu() {
        youTubePlayerView.getPlayerUiController()
                .showMenuButton(true)
                .getMenu()
                    .addItem(new MenuItem("menu item1", R.drawable.ic_android_black_24dp,
                                    view -> Toast.makeText(this, "item1 clicked", Toast.LENGTH_SHORT).show())
                    ).addItem(new MenuItem("menu item2", R.drawable.ic_mood_black_24dp,
                                    view -> Toast.makeText(this, "item2 clicked", Toast.LENGTH_SHORT).show())
                    ).addItem(new MenuItem("menu item no icon",
                                    view -> Toast.makeText(this, "item no icon clicked", Toast.LENGTH_SHORT).show()));
    }

    private void addFullScreenListenerToPlayer(final YouTubePlayer youTubePlayer) {
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                fullScreenHelper.enterFullScreen();

                addCustomActionToPlayer(youTubePlayer);
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullScreenHelper.exitFullScreen();

                removeCustomActionFromPlayer();
            }
        });
    }

    /**
     * This method adds a new custom action to the player.
     * Custom actions are shown next to the Play/Pause button in the middle of the player.
     */
    private void addCustomActionToPlayer(YouTubePlayer youTubePlayer) {
        Drawable customActionIcon = ContextCompat.getDrawable(this, R.drawable.ayp_ic_pause_36dp);

        youTubePlayerView.getPlayerUiController().setCustomAction1(customActionIcon, view -> {
            if(youTubePlayer != null) youTubePlayer.pause();
        });
    }

    private void removeCustomActionFromPlayer() {
        youTubePlayerView.getPlayerUiController().showCustomAction1(false);
    }

    /**
     * Set a click listener on the "Play next video" button
     */
    private void setPlayNextVideoButtonClickListener(final YouTubePlayer youTubePlayer) {
        Button playNextVideoButton = findViewById(R.id.next_video_button);

        playNextVideoButton.setOnClickListener(view ->
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, getLifecycle(),
                        VideoIdsProvider.getNextVideoId(),0f
                ));
    }

    /**
     * This method is here just for reference, it is not being used because the IFrame player already shows the title of the video.
     *
     * This method is called every time a new video is being loaded/cued.
     * It uses the YouTube Data APIs to fetch the video title from the video ID.
     * The YouTube Data APIs are nothing more then a wrapper over the YouTube REST API.
     * You can learn more at the following urls:
     * https://developers.google.com/youtube/v3/docs/videos/list
     * https://developers.google.com/apis-explorer/#p/youtube/v3/youtube.videos.list?part=snippet&id=6JYIGclVQdw&fields=items(snippet(title))&_h=9&
     *
     * This method does network operations, therefore it cannot be executed on the main thread.
     * For simplicity I have used RxJava to implement the asynchronous logic. You can use whatever you want: Threads, AsyncTask ecc.
     */
    @SuppressLint("CheckResult")
    private void setVideoTitle(PlayerUiController playerUiController, String videoId) {

        Single<VideoInfo> observable = YouTubeDataEndpoint.getVideoInfoFromYouTubeDataAPIs(videoId);

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        videoInfo -> playerUiController.setVideoTitle(videoInfo.getVideoTitle()),
                        error -> { Log.e(getClass().getSimpleName(), "Can't retrieve video title, are you connected to the internet?"); }
                );
    }
}
