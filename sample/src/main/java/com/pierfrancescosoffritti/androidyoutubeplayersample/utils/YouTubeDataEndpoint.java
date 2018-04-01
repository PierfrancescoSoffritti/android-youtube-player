package com.pierfrancescosoffritti.androidyoutubeplayersample.utils;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;

public class YouTubeDataEndpoint {

    private static final String APP_NAME = "YouTubePlayer_SampleApp";
    private static final String YOUTUBE_DATA_API_KEY = "AIzaSyAVeTsyAjfpfBBbUQq4E7jooWwtV2D_tjE";

    public static Single<String> getVideoTitleFromYouTubeDataAPIs(String videoId) {
        SingleOnSubscribe<String> onSubscribe = emitter -> {
            try {

                YouTube youTubeDataAPIEndpoint = buildYouTubeEndpoint();

                YouTube.Videos.List query = buildQuery(youTubeDataAPIEndpoint, videoId);
                VideoListResponse videoListResponse = query.execute();

                if(videoListResponse.getItems().size() != 1)
                    throw new RuntimeException("There should be exactly one video with the specified id");

                Video video = videoListResponse.getItems().get(0);
                String videoTitle = video.getSnippet().getTitle();

                emitter.onSuccess(videoTitle);

            } catch (IOException e) {
                emitter.onError(e);
            }
        };

        return Single.create(onSubscribe);
    }

    private static YouTube.Videos.List buildQuery(YouTube youTubeDataAPIEndpoint, String videoId) throws IOException {
        return youTubeDataAPIEndpoint
                .videos()
                .list("snippet")
                .setFields("items(snippet(title))")
                .setId(videoId)
                .setKey(YOUTUBE_DATA_API_KEY);
    }

    private static YouTube buildYouTubeEndpoint() {
        return new YouTube
                .Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setApplicationName(APP_NAME)
                .build();
    }
}
