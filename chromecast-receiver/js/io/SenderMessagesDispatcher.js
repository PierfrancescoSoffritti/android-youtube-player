const INIT_COMMUNICATION_CONSTANTS = "INIT_COMMUNICATION_CONSTANTS"

/**
 * Class responsible for dispatching messages received from the cast sender.
 * @param {*} communicationConstants Constants used for sender-receiver communication. Sent by the cast sender with the INIT_COMMUNICATION_CONSTANTS message.
 * @param {*} callbacks 
 */
function SenderMessagesDispatcher(communicationConstants, callbacks) {

    function onMessage(message) {
        console.log(message.data)

        if(message.data.command === INIT_COMMUNICATION_CONSTANTS) { 
            callbacks.onInitMessageReceived(message.data.communicationConstants)
        }
            
        else if(message.data.command === communicationConstants.LOAD) {
            callbacks.loadVideo(message.data.videoId, Number(message.data.startSeconds))
        }
        else if(message.data.command === communicationConstants.CUE) {
            callbacks.cueVideo(message.data.videoId, Number(message.data.startSeconds))
        }
        else if(message.data.command === communicationConstants.PLAY) {
            callbacks.playVideo()
        }
        else if(message.data.command === communicationConstants.PAUSE) {
            callbacks.pauseVideo()
        }
        else if(message.data.command === communicationConstants.MUTE) {
            callbacks.mute()
        }
        else if(message.data.command === communicationConstants.UNMUTE) {
            callbacks.unMute()
        }
        else if(message.data.command === communicationConstants.SET_VOLUME) {
            callbacks.setVolume(Number(message.data.volumePercent))
        }
        else if(message.data.command === communicationConstants.SEEK_TO) {
            callbacks.seekTo(Number(message.data.time))
        }
        else if(message.data.command === communicationConstants.SET_PLAYBACK_RATE) {
             callbacks.setPlaybackRate(Number(message.data.playbackRate))
        }
        else if(message.data.command === communicationConstants.NEXT_VIDEO) {
            callbacks.nextVideo()
        }
        else if(message.data.command === communicationConstants.PREVIOUS_VIDEO) {
            callbacks.previousVideo()
        }
        else if(message.data.command === communicationConstants.PLAY_VIDEO_AT) {
            callbacks.playVideoAt(Number(message.data.index))
        }
        else if(message.data.command === communicationConstants.SET_LOOP) {
            callbacks.setLoop(message.data.loop === "true")
        }
        else if(message.data.command === communicationConstants.SET_SHUFFLE) {
            callbacks.setShuffle(message.data.shuffle === "true")
        }
    }

    return {
        onMessage
    }
}

export default SenderMessagesDispatcher