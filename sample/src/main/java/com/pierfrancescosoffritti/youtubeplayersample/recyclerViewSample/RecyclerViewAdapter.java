package com.pierfrancescosoffritti.youtubeplayersample.recyclerViewSample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.youtubeplayersample.R;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private String[] videoIds;

    RecyclerViewAdapter(String[] videoIds) {
        this.videoIds = videoIds;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        youTubePlayerView.getPlayerUIController().showFullscreenButton(false);
        return new ViewHolder(youTubePlayerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.cueVideo(videoIds[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return videoIds.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        YouTubePlayerView youTubePlayerView;
        YouTubePlayer youTubePlayer;
        String currentVideoId;

        ViewHolder(YouTubePlayerView ypw) {
            super(ypw);
            youTubePlayerView = ypw;

            youTubePlayerView.initialize(initializedYouTubePlayer -> {
                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        youTubePlayer = initializedYouTubePlayer;

                        youTubePlayer.cueVideo(currentVideoId, 0);
                    }
                });
            }, true);
        }

        void cueVideo(String videoId) {
            currentVideoId = videoId;

            if(youTubePlayer == null)
                return;

            youTubePlayer.cueVideo(videoId, 0);
        }
    }
}
