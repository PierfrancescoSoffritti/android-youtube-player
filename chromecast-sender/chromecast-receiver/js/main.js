import SenderMessagesDispatcher from "./io/SenderMessagesDispatcher.js"
import ChromecastCommunicationChannel from "./io/ChromecastCommunicationChannel.js"
import YouTubePlayer from "./YouTubePlayer.js"

const namespace = "urn:x-cast:com.pierfrancescosoffritti.androidyoutubeplayer.chromecast.communication"
let communicationConstants = {}

const context = cast.framework.CastReceiverContext.getInstance()

const communicationChannel = new ChromecastCommunicationChannel(namespace)
const youTubePlayer = new YouTubePlayer(communicationConstants, communicationChannel)
const senderMessagesDispatcher = new SenderMessagesDispatcher(communicationConstants, { onInitMessageReceived, ...youTubePlayer.getActions() })

let isYouTubeIframeAPIReady = false

context.addCustomMessageListener(namespace, senderMessagesDispatcher.onMessage)
context.start()

function onInitMessageReceived(parsedCommunicationConstants) {
    if(!isYouTubeIframeAPIReady) {
        initCommunicationConstants(parsedCommunicationConstants)
        loadYouTubeIFrameAPIs()
    } else
        youTubePlayer.restoreCommunication()
}

function initCommunicationConstants(parsedCommunicationConstants) {
    for (let key in parsedCommunicationConstants)
        communicationConstants[key] = parsedCommunicationConstants[key]
}

function loadYouTubeIFrameAPIs() {
    const script = document.createElement('script')
    script.src = "https://www.youtube.com/iframe_api"
    document.getElementsByTagName('head')[0].appendChild(script)
}

// called automatically by the IFrame APIs
function onYouTubeIframeAPIReady() {
    isYouTubeIframeAPIReady = true
    youTubePlayer.initialize()
}

window.main_onYouTubeIframeAPIReady = onYouTubeIframeAPIReady