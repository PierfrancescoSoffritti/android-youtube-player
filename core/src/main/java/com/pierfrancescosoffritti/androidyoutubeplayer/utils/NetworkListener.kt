package com.pierfrancescosoffritti.androidyoutubeplayer.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

internal class NetworkListener : BroadcastReceiver() {

    var onNetworkUnavailable = { }
    var onNetworkAvailable = { }

    override fun onReceive(context: Context, intent: Intent) {
        if (InternalUtilities.isOnline(context))
            onNetworkAvailable()
        else
            onNetworkUnavailable()
    }
}
