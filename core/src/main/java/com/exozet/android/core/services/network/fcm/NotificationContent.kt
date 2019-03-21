package com.exozet.android.core.services.network.fcm

import org.parceler.Parcel

@Parcel(Parcel.Serialization.BEAN)
data class NotificationContent(
    val title: String? = null,
    val text: String? = null
)