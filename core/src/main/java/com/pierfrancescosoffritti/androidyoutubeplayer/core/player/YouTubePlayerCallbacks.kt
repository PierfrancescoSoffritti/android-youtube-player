package com.pierfrancescosoffritti.androidyoutubeplayer.core.player

import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import androidx.annotation.RestrictTo
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/** Bridge used to extract values from Javascript and pass them to the YouTubePlayer. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class YouTubePlayerCallbacks {
    private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())

    /** Callbacks registered by clients of this class to retrieve boolean values form Javascript. */
    private val booleanCallbacks = ConcurrentHashMap<Long, BooleanProvider>()

    private val requestId = AtomicLong(0)

    /**
     * Registers a callback to be called when a boolean value is received from Javascript.
     * @return the requestId for this callback.
     */
    fun registerBooleanCallback(callback: BooleanProvider): Long {
        val requestId = requestId.incrementAndGet()
        booleanCallbacks[requestId] = callback
        return requestId
    }

    @JavascriptInterface
    fun sendBooleanValue(requestId: Long, value: Boolean) {
        mainThreadHandler.post {
            val callback = booleanCallbacks.remove(requestId)
            callback?.accept(value)
        }
    }
}
