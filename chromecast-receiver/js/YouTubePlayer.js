import YouTubePlayerRemoteBridge from "./io/YouTubePlayerRemoteBridge.js"

function YouTubePlayer(communicationConstants, communicationChannel) {
    const UNSTARTED = "UNSTARTED"
    const ENDED = "ENDED"
    const PLAYING = "PLAYING"
    const PAUSED = "PAUSED"
    const BUFFERING = "BUFFERING"
    const CUED = "CUED"

    const YouTubePlayerBridge = new YouTubePlayerRemoteBridge(communicationConstants, communicationChannel)

    let player
    let lastState
    let lastVideoId

    function initialize() {        
        YouTubePlayerBridge.sendYouTubeIframeAPIReady()
        
        player = new YT.Player('youTubePlayerDOM', {
            
            height: '100%',
            width: '100%',
            
            events: {
                onReady: () => YouTubePlayerBridge.sendReady(),
                onStateChange: event  => sendPlayerStateChange(event.data),
                onPlaybackQualityChange: event => YouTubePlayerBridge.sendPlaybackQualityChange(event.data),
                onPlaybackRateChange: event => YouTubePlayerBridge.sendPlaybackRateChange(event.data),
                onError: error => YouTubePlayerBridge.sendError(error.data),
                onApiChange: () => YouTubePlayerBridge.sendApiChange()
            },
            playerVars: {
                autoplay: 0,
                autohide: 1,
                controls: 0,
                enablejsapi: 1,
                fs: 0,
                origin: 'https://www.youtube.com',
                rel: 0,
                iv_load_policy: 3
            }            
        })
    }

    function restoreCommunication() {
        YouTubePlayerBridge.sendYouTubeIframeAPIReady()
        sendPlayerStateChange(lastState)
        YouTubePlayerBridge.sendVideoId(lastVideoId)
    }

    function sendPlayerStateChange(playerState) {
        lastState = playerState

        let timerTaskId
        clearInterval(timerTaskId)

        switch (playerState) {
            case YT.PlayerState.UNSTARTED:
                sendStateChange(UNSTARTED)
                return

            case YT.PlayerState.ENDED:
                sendStateChange(ENDED)
                return

            case YT.PlayerState.PLAYING:
                sendStateChange(PLAYING)
                timerTaskId = setInterval( () => YouTubePlayerBridge.sendVideoCurrentTime( player.getCurrentTime() ), 100 )
                sendVideoData(player)
                return

            case YT.PlayerState.PAUSED:
                sendStateChange(PAUSED)
                return

            case YT.PlayerState.BUFFERING:
                sendStateChange(BUFFERING)
                return

            case YT.PlayerState.CUED:
                sendStateChange(CUED)
                return
        }

        function sendVideoData(player) {
            const videoDuration = player.getDuration()
            YouTubePlayerBridge.sendVideoDuration(videoDuration)
        }

        function sendStateChange(newState) {
            YouTubePlayerBridge.sendStateChange(newState)
        }
    }

    // JAVA to WEB functions
    function seekTo(startSeconds) {        	
        player.seekTo(startSeconds, true)
    }

    function pauseVideo() {
        player.pauseVideo()
    }

    function playVideo() {
        player.playVideo()
    }

    function loadVideo(videoId, startSeconds) {
        lastVideoId = videoId

        player.loadVideoById(videoId, startSeconds)
        YouTubePlayerBridge.sendVideoId(videoId)
    }

    function cueVideo(videoId, startSeconds) {
        lastVideoId = videoId

        player.cueVideoById(videoId, startSeconds)
        YouTubePlayerBridge.sendVideoId(videoId)
    }

    function mute() {
        player.mute()
    }

    function unMute() {
        player.unMute()
    }

    function setVolume(volumePercent) {
        player.setVolume(volumePercent)
    }

    function setPlaybackRate(playbackRate) {
      player.setPlaybackRate(playbackRate)
    }

    function nextVideo() {
      player.nextVideo()
    }

    function previousVideo() {
      player.previousVideo()
    }

    function playVideoAt(index) {
      player.playVideoAt(index)
    }

    function getActions() {
        return actions
    }

    function setLoop(loop) {
        player.setLoop(loop)
    }

    function setShuffle(shuffle) {
        player.setShuffle(shuffle);
      }

    const actions = { 
        seekTo, 
        pauseVideo,
        playVideo,
        loadVideo, 
        cueVideo, 
        mute, 
        unMute,
        setVolume,
        setPlaybackRate,
        nextVideo,
        previousVideo,
        playVideoAt,
        setLoop,
        setShuffle
    }
    
    return {
        initialize,
        restoreCommunication,
        getActions
    }
}

export default YouTubePlayer