package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure

import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener

internal class CastSessionManagerListener(private val castSessionListener: CastSessionListener) : SessionManagerListener<CastSession> {

    override fun onSessionEnding(castSession: CastSession) { }
    override fun onSessionSuspended(castSession: CastSession, p1: Int) { }

    override fun onSessionStarting(castSession: CastSession) = castSessionListener.onCastSessionConnecting()
    override fun onSessionResuming(castSession: CastSession, p1: String) = castSessionListener.onCastSessionConnecting()

    override fun onSessionEnded(castSession: CastSession, error: Int) = castSessionListener.onCastSessionDisconnected(castSession)
    override fun onSessionResumed(castSession: CastSession, wasSuspended: Boolean) = castSessionListener.onCastSessionConnected(castSession)
    override fun onSessionResumeFailed(castSession: CastSession, p1: Int) = castSessionListener.onCastSessionDisconnected(castSession)
    override fun onSessionStarted(castSession: CastSession, sessionId: String) = castSessionListener.onCastSessionConnected(castSession)
    override fun onSessionStartFailed(castSession: CastSession, p1: Int) = castSessionListener.onCastSessionConnected(castSession)

}