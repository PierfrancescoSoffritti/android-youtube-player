package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure

import com.google.android.gms.cast.framework.CastSession

internal interface CastSessionListener {
    fun onCastSessionConnecting()
    fun onCastSessionConnected(castSession: CastSession)
    fun onCastSessionDisconnected(castSession: CastSession)
}