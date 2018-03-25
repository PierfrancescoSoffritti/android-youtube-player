package com.pierfrancescosoffritti.youtubeplayersample.youtubePlayerLibraryExamples.baseExample;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.youtubeplayer.ui.PlayerUIController;
import com.pierfrancescosoffritti.youtubeplayer.ui.menu.MenuItem;
import com.pierfrancescosoffritti.youtubeplayersample.R;
import com.pierfrancescosoffritti.youtubeplayersample.utils.FullScreenManager;
import com.pierfrancescosoffritti.youtubeplayersample.utils.YouTubeDataEndpoint;

import java.util.Random;

import javax.annotation.Nullable;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BasicExampleActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private FullScreenManager fullScreenManager = new FullScreenManager(this);

    private @Nullable YouTubePlayer initializedYouTubePlayer;

    private Button nextVideoButton;

    private String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY"};

    // a list of videos not available in some countries, to test if they're handled gracefully.
    // private String[] nonPlayableVideoIds = { "sop2V_MREEI" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_example);

        nextVideoButton = findViewById(R.id.next_video_button);
        youTubePlayerView = findViewById(R.id.youtube_player_view);

        initYouTubePlayerView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        youTubePlayerView.getPlayerUIController().getMenu().dismiss();
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
        // The player will automatically pause when the activity is paused
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.initialize(initializedYouTubePlayer -> {

            initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    BasicExampleActivity.this.initializedYouTubePlayer = initializedYouTubePlayer;
                    loadVideo(initializedYouTubePlayer, videoIds[0]);
                }
            });

            addFullScreenListenerToPlayer(initializedYouTubePlayer);
            setNextVideoButtonClickListener(initializedYouTubePlayer);

        }, true);
    }

    private void initPlayerMenu() {
        youTubePlayerView.getPlayerUIController().showMenuButton(true);
        youTubePlayerView.getPlayerUIController().getMenu().addItem(
                new MenuItem("example", R.drawable.ic_settings_24dp, (view) -> Toast.makeText(this, "item clicked", Toast.LENGTH_SHORT).show())
        );
    }

    private void loadVideo(YouTubePlayer youTubePlayer, String videoId) {
        youTubePlayer.loadVideo(videoId, 0);
        setVideoTitle(youTubePlayerView.getPlayerUIController(), videoIds[0]);
    }

    private void addFullScreenListenerToPlayer(final YouTubePlayer youTubePlayer) {
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                fullScreenManager.enterFullScreen();

                addCustomActionToPlayer(youTubePlayer);
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullScreenManager.exitFullScreen();

                removeCustomActionFromPlayer();
            }
        });
    }

    private void addCustomActionToPlayer(YouTubePlayer youTubePlayer) {
        Drawable icon = ContextCompat.getDrawable(BasicExampleActivity.this, R.drawable.ic_pause_36dp);

        youTubePlayerView.getPlayerUIController().setCustomAction1(icon, view -> {
            if(youTubePlayer != null) youTubePlayer.pause();
        });
    }

    private void removeCustomActionFromPlayer() {
        youTubePlayerView.getPlayerUIController().showCustomAction1(false);
    }

    private void setNextVideoButtonClickListener(final YouTubePlayer youTubePlayer) {
        nextVideoButton.setOnClickListener(view -> {
            String videoId = videoIds[new Random().nextInt(videoIds.length)];

            loadVideo(youTubePlayer, videoId);
        });
    }

    /**
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
    private void setVideoTitle(PlayerUIController playerUIController, String videoId) {

        Single<String> observable = YouTubeDataEndpoint.getVideoTitleFromYouTubeDataAPIs(videoId);

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        videoTitle -> playerUIController.setVideoTitle(videoTitle),
                        error -> { Log.e(getClass().getSimpleName(), "Can't retrieve video title, are you connected to the internet?"); }
                );
    }
}
