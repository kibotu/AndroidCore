package com.exozet.android.core.models

import org.parceler.Parcel

@Parcel(Parcel.Serialization.BEAN)
data class LoadingInfo(
    val name: String = "",
    val isLoading: Boolean = true,
    val time: Long = System.currentTimeMillis()
)