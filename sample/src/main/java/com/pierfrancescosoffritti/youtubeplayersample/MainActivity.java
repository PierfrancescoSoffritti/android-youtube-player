package com.pierfrancescosoffritti.youtubeplayersample;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.youtubeplayer.ui.PlayerUIController;
import com.pierfrancescosoffritti.youtubeplayer.ui.menu.MenuItem;

import java.io.IOException;
import java.util.Random;

import javax.annotation.Nullable;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String APP_NAME = "YouTubePlayer_SampleApp";
    private static final String YOUTUBE_DATA_API_KEY = "AIzaSyAVeTsyAjfpfBBbUQq4E7jooWwtV2D_tjE";

    private YouTubePlayerView youTubePlayerView;
    private FullScreenManager fullScreenManager;

    private @Nullable YouTubePlayer initializedYouTubePlayer;

    private Button nextVideoButton;

    // the last video is not playable in some countries. It's here to test what happens for non-playable videos.
    private String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY", "sop2V_MREEI"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextVideoButton = findViewById(R.id.next_video_button);

        fullScreenManager = new FullScreenManager(this);

        youTubePlayerView = findViewById(R.id.youtube_player_view);

        this.getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.initialize(initializedYouTubePlayer -> {

            initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    MainActivity.this.initializedYouTubePlayer = initializedYouTubePlayer;

                    initializedYouTubePlayer.loadVideo(videoIds[0], 0);
                    setVideoTitle(youTubePlayerView.getPlayerUIController(), videoIds[0]);
                }
            });

            addFullScreenListenerToPlayer(initializedYouTubePlayer);
            initButtonClickListener(initializedYouTubePlayer);

        }, true);

        // menu
        youTubePlayerView.getPlayerUIController().showMenuButton(true);
        youTubePlayerView.getPlayerUIController().getMenu().addItem(new MenuItem("example", R.drawable.ic_settings_24dp, null));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // It's not necessary to call release if you register youTubePlayerView as a lifecycle observer of this Activity
//        youTubePlayerView.release();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (initializedYouTubePlayer != null)
            initializedYouTubePlayer.pause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        youTubePlayerView.getPlayerUIController().getMenu().dismiss();
    }

    private void addFullScreenListenerToPlayer(final YouTubePlayer youTubePlayer) {
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                fullScreenManager.enterFullScreen();

                Drawable icon = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_pause_36dp);
                youTubePlayerView.getPlayerUIController().setCustomAction1(icon, view -> {
                    if(youTubePlayer != null) youTubePlayer.pause();
                });
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullScreenManager.exitFullScreen();

                youTubePlayerView.getPlayerUIController().showCustomAction1(false);
            }
        });
    }

    private void initButtonClickListener(final YouTubePlayer youTubePlayer) {
        nextVideoButton.setOnClickListener(view -> {
            String videoId = videoIds[new Random().nextInt(videoIds.length)];
            youTubePlayer.loadVideo(videoId, 0);
            setVideoTitle(youTubePlayerView.getPlayerUIController(), videoId);
        });
    }

    /**
     * This method is called every time a new video is being loaded/cued.
     * It uses the YouTube Data APIs to get the video title from the video ID. You can learn more here https://developers.google.com/youtube/v3/docs/videos/list and here https://developers.google.com/apis-explorer/#p/youtube/v3/youtube.videos.list?part=snippet&id=6JYIGclVQdw&fields=items(snippet(title))&_h=9&
     * The YouTube Data APIs are nothing more then a wrapper over the YouTube REST API.
     *
     * youTubeDataAPIEndPoint.execute() does network operations, therefore it cannot be executed on the main thread.
     * For simplicity I have used RxJava to implement the asynchronous logic. You can you whatever you want: Threads, AsyncTask ecc.
     */
    private void setVideoTitle(PlayerUIController playerUIController, String videoId) {

        Single<String> observable = getVideoTitleFromYouTubeDataAPIs(videoId);

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        videoTitle -> playerUIController.setVideoTitle(videoTitle),
                        error -> { Log.e(getClass().getSimpleName(), "Can't retrieve video title, are you connected to the internet?"); }
                );
    }

    private Single<String> getVideoTitleFromYouTubeDataAPIs(String videoId) {
        SingleOnSubscribe<String> onSubscribe = emitter -> {
            try {
                YouTube youTubeDataAPIEndPoint = new YouTube
                        .Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setApplicationName(APP_NAME)
                        .build();

                VideoListResponse videoListResponse = youTubeDataAPIEndPoint
                        .videos()
                        .list("snippet")
                        .setFields("items(snippet(title))")
                        .setId(videoId)
                        .setKey(YOUTUBE_DATA_API_KEY)
                        .execute();

                if(videoListResponse.getItems().size() != 1)
                    throw new RuntimeException("There should be exactly one video with the provided id");

                Video video = videoListResponse.getItems().get(0);
                String videoTitle = video.getSnippet().getTitle();
                emitter.onSuccess(videoTitle);

            } catch (IOException e) {
                emitter.onError(e);
            }
        };

        return Single.create(onSubscribe);
    }
}
