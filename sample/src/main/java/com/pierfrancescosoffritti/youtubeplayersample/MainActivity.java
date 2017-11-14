package com.pierfrancescosoffritti.youtubeplayersample;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.youtubeplayer.ui.PlayerUIController;

import java.io.IOException;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String APP_NAME = "YouTubePlayer_SampleApp";
    private static final String YOUTUBE_DATA_API_KEY = "AIzaSyAVeTsyAjfpfBBbUQq4E7jooWwtV2D_tjE";

    private YouTubePlayerView youTubePlayerView;
    private FullScreenManager fullScreenManager;

    private Button nextVideo;

    private String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY", "sop2V_MREEI"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextVideo = findViewById(R.id.next_video_button);

        fullScreenManager = new FullScreenManager(this);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(initializedYouTubePlayer -> {

            initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    initializedYouTubePlayer.loadVideo(videoIds[0], 0);
                    setVideoTitle(youTubePlayerView.getPlayerUIController(), videoIds[0]);
                }
            });

            initFullScreenListener(initializedYouTubePlayer);
            initButtonClickListener(initializedYouTubePlayer);

        }, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        youTubePlayerView.release();
    }

    private void initFullScreenListener(final YouTubePlayer youTubePlayer) {
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
        nextVideo.setOnClickListener(view -> {
            String videoId = videoIds[new Random().nextInt(videoIds.length)];
            youTubePlayer.loadVideo(videoId, 0);
            setVideoTitle(youTubePlayerView.getPlayerUIController(), videoId);
        });
    }

    private void setVideoTitle(final PlayerUIController playerUIController, final String videoId) {
        SingleOnSubscribe<String> onSubscribe = new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> emitter) throws Exception {
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
            }
        };
        Single<String> observable = Single.create(onSubscribe);

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        videoTitle -> playerUIController.setVideoTitle(videoTitle),
                        error -> { throw new RuntimeException(error); }
                );
    }
}
