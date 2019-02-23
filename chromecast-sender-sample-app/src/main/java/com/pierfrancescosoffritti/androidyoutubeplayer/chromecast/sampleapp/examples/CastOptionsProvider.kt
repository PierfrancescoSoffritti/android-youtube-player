package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.sampleapp.examples

import android.content.Context
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.SessionProvider
import com.google.android.gms.cast.framework.OptionsProvider

/**
 * Class providing setup info to the Chromecast framework, declared in manifest file.
 *
 * [see doc here](https://developers.google.com/cast/docs/android_sender_integrate#initialize_the_cast_context)
 */
internal class CastOptionsProvider : OptionsProvider {
    // This is the receiver id of the sample receiver.
    // Remember to change it with the ID of your own receiver. See documentation for more info.
    private val receiverId = "C5CBE8CA"

    override fun getCastOptions(appContext: Context): CastOptions {
        return CastOptions.Builder()
                .setReceiverApplicationId(receiverId)
                .build()
    }

    override fun getAdditionalSessionProviders(context: Context): List<SessionProvider>? {
        return null
    }
}