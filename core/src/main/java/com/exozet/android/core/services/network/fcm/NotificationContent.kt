package tv.freenet.selfcare.models.gms

import org.parceler.Parcel

@Parcel
data class NotificationContent(
    val title: String,
    val text: String
)