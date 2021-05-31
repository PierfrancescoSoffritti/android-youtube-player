package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.playlist;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils.FullScreenHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils.VideoIdsProvider;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils.VideoInfo;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils.YouTubeDataEndpoint;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.menu.MenuItem;
import com.pierfrancescosoffritti.aytplayersample.R;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlaylistActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private FullScreenHelper fullScreenHelper = new FullScreenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_example);

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

                addFullScreenListenerToPlayer();
                addPlaylistButtonsToPlayer(youTubePlayer);
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

    private void addFullScreenListenerToPlayer() {
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                fullScreenHelper.enterFullScreen();
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullScreenHelper.exitFullScreen();

                removeCustomActionsFromPlayer();
            }
        });
    }

    /**
     * This method adds a new custom action to the player.
     * Custom actions are shown next to the Play/Pause button in the middle of the player.
     */
    private void addPlaylistButtonsToPlayer(YouTubePlayer youTubePlayer) {
        Drawable customAction1Icon = ContextCompat.getDrawable(this, R.drawable.ic_fast_rewind_white_24dp);
        Drawable customAction2Icon = ContextCompat.getDrawable(this, R.drawable.ic_fast_forward_white_24dp);
        assert customAction1Icon != null;
        assert customAction2Icon != null;

        youTubePlayerView.getPlayerUiController().setCustomAction1(customAction1Icon, view -> {
                VideoIdsProvider.decrementIndex();
                YouTubePlayerUtils.loadOrCueVideo(
                youTubePlayer, getLifecycle(),
                VideoIdsProvider.getNextVideoId(),0f
                );
            }
        );

        youTubePlayerView.getPlayerUiController().setCustomAction2(customAction2Icon, view -> {
                VideoIdsProvider.incrementIndex();
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, getLifecycle(),
                        VideoIdsProvider.getNextVideoId(),0f
                );
        });
    }

    private void removeCustomActionsFromPlayer() {
        youTubePlayerView.getPlayerUiController().showCustomAction1(false);
        youTubePlayerView.getPlayerUiController().showCustomAction2(false);
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
