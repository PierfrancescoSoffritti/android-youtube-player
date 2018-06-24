package com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample;

import android.content.Context;
import android.support.annotation.NonNull;

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
    public CastOptions getCastOptions(@NonNull Context appContext) {
        String receiverId = "C5CBE8CA";

        return new CastOptions.Builder()
                .setReceiverApplicationId(receiverId)
                .build();
    }

    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }
}
