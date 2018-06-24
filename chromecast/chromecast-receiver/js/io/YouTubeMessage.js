export function YouTubeMessage(type="", data = "") {
    return { type, data }
}

/**
 * Checkes if message is a YouTubeMessage or not.
 * @param {*} message 
 */
export function isMessagePoperlyFormatted(message) {
    const sampleMessage = new YouTubeMessage();

    for (const key in sampleMessage) 
        if (!message.hasOwnProperty(key))
            return false

    return true
}