import { isMessagePoperlyFormatted } from "./YouTubeMessage.js"

function ChromecastCommunicationChannel(namespace) {
    const context = cast.framework.CastReceiverContext.getInstance()

    function sendMessage(data) {
        if(!isMessagePoperlyFormatted(data)) {
            console.error("object not properly formatted.")
            console.error(data)

            return;
        }
        
        context.getSenders().forEach( sender => context.sendCustomMessage(namespace, sender.id, data) )
    }

    return {
        sendMessage: sendMessage
    }
}

export default ChromecastCommunicationChannel