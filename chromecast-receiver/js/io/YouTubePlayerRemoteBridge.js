import { YouTubeMessage } from "./YouTubeMessage.js"

/**
 * Class responsible for sending messages to the cast sender
 * @param {*} communicationConstants 
 * @param {*} communicationChannel 
 */
function YouTubePlayerRemoteBridge(communicationConstants, communicationChannel) {

    function sendYouTubeIframeAPIReady() {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.IFRAME_API_READY))
    }

    function sendReady() {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.READY))
    }

    function sendStateChange(data) {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.STATE_CHANGED, data))
    }

    function sendPlaybackQualityChange(data) {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.PLAYBACK_QUALITY_CHANGED, data))
    }

    function sendPlaybackRateChange(data) {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.PLAYBACK_RATE_CHANGED, data))
    }

    function sendError(data) {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.ERROR, data))
    }

    function sendApiChange() {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.API_CHANGED))
    }

    function sendVideoCurrentTime(data) {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.VIDEO_CURRENT_TIME, data))
    }

    function sendVideoDuration(data) {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.VIDEO_DURATION, data))
    }
    
    function sendVideoId(data) {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.VIDEO_ID, data))
    }

    function sendPlaylistId(playlist) {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.PLAYLIST_ID, playlist))
    }
    function sendPlaylistIndex(playlist) {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.PLAYLIST_INDEX, index))
    }
    function sendPlaylistType(playlistType) {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.PLAYLIST_TYPE, playlistType))
    }
    function sendPlaylistLength(length) {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.PLAYLIST_LENGTH, length))
    }
    function sendVideoList(videos) {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.VIDEO_LIST, videos))
    }
    function sendLoopStatus(loop) {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.LOOP_STATUS, loop))
    }
    function sendShuffleStatus(shuffle) {
        communicationChannel.sendMessage(new YouTubeMessage(communicationConstants.SHUFFLE_STATUS, shuffle))
    }

    return {
        sendYouTubeIframeAPIReady: sendYouTubeIframeAPIReady,
        sendReady: sendReady,
        sendStateChange: sendStateChange,
        sendPlaybackQualityChange: sendPlaybackQualityChange,
        sendPlaybackRateChange: sendPlaybackRateChange,
        sendError: sendError,
        sendApiChange: sendApiChange,
        sendVideoCurrentTime: sendVideoCurrentTime,
        sendVideoDuration: sendVideoDuration,
        sendVideoId: sendVideoId,
        sendPlaylistIndex: sendPlaylistIndex,
        sendPlaylistId: sendPlaylistId,
        sendPlaylistType: sendPlaylistType,
        sendPlaylistLength: sendPlaylistLength,
        sendVideoList: sendVideoList,
    }
}

export default YouTubePlayerRemoteBridge