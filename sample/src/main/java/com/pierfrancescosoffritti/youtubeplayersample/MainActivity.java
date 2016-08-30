package com.pierfrancescosoffritti.youtubeplayersample;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayer;

public class MainActivity extends AppCompatActivity {

    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        youTubePlayer = (YouTubePlayer) findViewById(R.id.youtube_player);
//        youTubePlayer.setAutoPlayerHeight(this);

        findViewById(R.id.next_video_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youTubePlayer.loadVideo("LvetJ9U_tVY", 0);
            }
        });

        youTubePlayer.initialize("6JYIGclVQdw", new YouTubePlayer.YouTubeListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo("6JYIGclVQdw", 0);
            }

            @Override
            public void onStateChange(@YouTubePlayer.State.YouTubePlayerState int state, @NonNull YouTubePlayer youTubePlayer) {

            }

            @Override
            public void onPlaybackQualityChange(@YouTubePlayer.PlaybackQuality.Quality int playbackQuality, @NonNull YouTubePlayer youTubePlayer) {

            }

            @Override
            public void onPlaybackRateChange(double rate, @NonNull YouTubePlayer youTubePlayer) {

            }

            @Override
            public void onError(String arg, @NonNull YouTubePlayer youTubePlayer) {

            }

            @Override
            public void onApiChange(@NonNull YouTubePlayer youTubePlayer) {

            }

            @Override
            public void onCurrentSecond(double second, @NonNull YouTubePlayer youTubePlayer) {

            }

            @Override
            public void onDuration(double duration, @NonNull YouTubePlayer youTubePlayer) {

            }

            @Override
            public void onLog(String log, @NonNull YouTubePlayer youTubePlayer) {

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ViewGroup.LayoutParams params = youTubePlayer.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            youTubePlayer.setLayoutParams(params);
        } else {
            ViewGroup.LayoutParams params = youTubePlayer.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            youTubePlayer.setLayoutParams(params);
//            youTubePlayer.adjustHeight(this);
        }

        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        youTubePlayer.destroy();
    }
}
