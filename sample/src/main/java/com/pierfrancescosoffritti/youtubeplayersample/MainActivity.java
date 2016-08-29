package com.pierfrancescosoffritti.youtubeplayersample;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayer;

public class MainActivity extends AppCompatActivity {

    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        youTubePlayer = (YouTubePlayer) findViewById(R.id.youtube_player);
//        youTubePlayer.adjustHeight(this);

        youTubePlayer.initialize("6JYIGclVQdw", null);
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
