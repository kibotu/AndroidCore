package com.exozet.android.core.services.network.fcm

import org.parceler.Parcel

@Parcel
data class NotificationContent(
    val title: String,
    val text: String
)