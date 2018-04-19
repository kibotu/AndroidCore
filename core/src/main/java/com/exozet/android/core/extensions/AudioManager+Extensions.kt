@file:JvmName("AudioManagerExtensions")

package com.exozet.android.core.extensions

import android.media.AudioManager
import android.media.AudioManager.*
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M
import android.support.v4.math.MathUtils.clamp

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

private var _isMuted: Boolean = false

var AudioManager.isMuted: Boolean
    get() = _isMuted
    set(value) {
        _isMuted = value
    }

@Suppress("DEPRECATION")
fun AudioManager.mute(mute: Boolean = true) {
    isMuted = mute
    if (SDK_INT >= M) {
        val flag = if (mute) ADJUST_MUTE else ADJUST_UNMUTE
        adjustStreamVolume(STREAM_NOTIFICATION, flag, 0)
        adjustStreamVolume(STREAM_ALARM, flag, 0)
        adjustStreamVolume(STREAM_MUSIC, flag, 0)
        adjustStreamVolume(STREAM_RING, flag, 0)
        adjustStreamVolume(STREAM_SYSTEM, flag, 0)
    }
//    else {
    setStreamMute(STREAM_NOTIFICATION, mute)
    setStreamMute(STREAM_ALARM, mute)
    setStreamMute(STREAM_MUSIC, mute)
    setStreamMute(STREAM_RING, mute)
    setStreamMute(STREAM_SYSTEM, mute)
//    }
}

@Suppress("DEPRECATION")
fun AudioManager.unmute() {
    mute(false)
}

fun AudioManager.decreaseVolume() {
    changeVolume(-10)
}

fun AudioManager.increaseVolume() {
    changeVolume(10)
}

fun AudioManager.toggleMute() {
    mute(!isMuted)
}

fun AudioManager.changeVolume(dt: Int) {
    val streamMaxVolume = audioManager.getStreamVolume(STREAM_MUSIC)
    val volume = clamp(streamMaxVolume + dt, 0, streamMaxVolume)
    audioManager.setStreamVolume(STREAM_MUSIC, volume, 0)
}