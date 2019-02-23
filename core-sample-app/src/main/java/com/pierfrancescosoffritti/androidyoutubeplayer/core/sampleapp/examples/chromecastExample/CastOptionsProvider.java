package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.chromecastExample;

import android.content.Context;

import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;

import java.util.List;

/**
 * Class providing setup info to the Chromecast framework, declared in manifest file.
 *
 * <a href="https://developers.google.com/cast/docs/android_sender_integrate#initialize_the_cast_context">See doc here</a>
 */
public final class CastOptionsProvider implements OptionsProvider {
    public CastOptions getCastOptions(Context appContext) {
        /*
          This is the receiver id of the sample receiver.
          Remember to change it with the ID of your own receiver. See documentation for more info.
          <a href="https://github.com/PierfrancescoSoffritti/chromecast-youtube-player">chromecast-youtube-player</a>
          */
        String receiverId = "C5CBE8CA";

        return new CastOptions.Builder()
                .setReceiverApplicationId(receiverId)
                .build();
    }

    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }
}
