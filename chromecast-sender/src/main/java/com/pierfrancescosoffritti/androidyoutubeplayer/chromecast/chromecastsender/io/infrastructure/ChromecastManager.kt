package com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.infrastructure

import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.ChromecastYouTubePlayerContext
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.utils.JSONUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.youtube.ChromecastCommunicationConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.chromecastsender.io.youtube.ChromecastYouTubeIOChannel

/**
 * Class responsible for chromecast sessions.
 */
internal class ChromecastManager(
        private val chromecastYouTubePlayerContext: ChromecastYouTubePlayerContext,
        private val sessionManager: SessionManager,
        private val chromecastConnectionListeners: Set<ChromecastConnectionListener>) : CastSessionListener {

    val chromecastCommunicationChannel = ChromecastYouTubeIOChannel(sessionManager)
    private val castSessionManagerListener = CastSessionManagerListener(this)

    override fun onCastSessionConnecting() {
        chromecastConnectionListeners.forEach { it.onChromecastConnecting() }
    }

    override fun onCastSessionConnected(castSession: CastSession) {
        castSession.removeMessageReceivedCallbacks(chromecastCommunicationChannel.namespace)
        castSession.setMessageReceivedCallbacks(chromecastCommunicationChannel.namespace, chromecastCommunicationChannel)

        sendCommunicationConstants(chromecastCommunicationChannel)

        chromecastYouTubePlayerContext.onChromecastConnected(chromecastYouTubePlayerContext)
        chromecastConnectionListeners.forEach { it.onChromecastConnected(chromecastYouTubePlayerContext) }
    }

    override fun onCastSessionDisconnected(castSession: CastSession) {
        castSession.removeMessageReceivedCallbacks(chromecastCommunicationChannel.namespace)

        chromecastYouTubePlayerContext.onChromecastDisconnected()
        chromecastConnectionListeners.forEach { it.onChromecastDisconnected() }
    }

    fun restoreSession() {
        val currentCastSessions = sessionManager.currentCastSession
        if(currentCastSessions != null)
            onCastSessionConnected(currentCastSessions)
    }

    fun endCurrentSession() {
        sessionManager.endCurrentSession(true)
    }

    fun addSessionManagerListener() = sessionManager.addSessionManagerListener(castSessionManagerListener, CastSession::class.java)
    fun removeSessionManagerListener() = sessionManager.removeSessionManagerListener(castSessionManagerListener, CastSession::class.java)

    fun release() {
        removeSessionManagerListener()
    }

    private fun sendCommunicationConstants(chromecastCommunicationChannel: ChromecastCommunicationChannel) {
        val communicationConstants = ChromecastCommunicationConstants.asJson()

        val message = JSONUtils.buildCommunicationConstantsJson(
                "command" to ChromecastCommunicationConstants.INIT_COMMUNICATION_CONSTANTS,
                "communicationConstants" to communicationConstants
        )

        chromecastCommunicationChannel.sendMessage(message)
    }
}