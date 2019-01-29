package tv.freenet.selfcare.models.gms

import org.parceler.Parcel

/**
 * FCM message
 * <code>
 * {
 *     "to": "/topics/global",
 *     "notification": {
 *         "title": "hello",
 *         "text": "world"
 *     },
 *     "data": {
 *         "key1": "value1",
 *         "key2": "value2"
 *     }
 * }
 * </code>
 */
@Parcel
data class FcmMessage(
    val to: String,
    val notification: NotificationContent,
    val data: Map<String, String>? = null
) {
    constructor(recipient: String, title: String, text: String, data: Map<String, String>? = null)
            : this(recipient, NotificationContent(title, text), data)
}