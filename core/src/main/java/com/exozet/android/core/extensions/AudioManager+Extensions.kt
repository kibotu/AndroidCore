@file:JvmName("AudioManagerExtensions")

package com.exozet.android.core.extensions

import android.media.AudioManager
import android.os.Build
import android.support.v4.math.MathUtils.clamp

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

private var _isMuted = false

val AudioManager.isMuted: Boolean
    get() = _isMuted

@Suppress("DEPRECATION")
fun AudioManager.muteAll() {
    _isMuted = true
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0)
        adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0)
        adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
        adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0)
        adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0)
    } else {
        setStreamMute(AudioManager.STREAM_NOTIFICATION, true)
        setStreamMute(AudioManager.STREAM_ALARM, true)
        setStreamMute(AudioManager.STREAM_MUSIC, true)
        setStreamMute(AudioManager.STREAM_RING, true)
        setStreamMute(AudioManager.STREAM_SYSTEM, true)
    }
}

@Suppress("DEPRECATION")
fun AudioManager.unmuteAll() {
    _isMuted = false
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0)
        adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_UNMUTE, 0)
        adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0)
        adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0)
        adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0)
    } else {
        setStreamMute(AudioManager.STREAM_NOTIFICATION, false)
        setStreamMute(AudioManager.STREAM_ALARM, false)
        setStreamMute(AudioManager.STREAM_MUSIC, false)
        setStreamMute(AudioManager.STREAM_RING, false)
        setStreamMute(AudioManager.STREAM_SYSTEM, false)
    }
}


fun AudioManager.decreaseVolume() {
    changeVolume(-10)
}

fun AudioManager.increaseVolume() {
    changeVolume(10)
}

fun AudioManager.toggleMute() {
    if (isMuted)
        unmuteAll()
    else
        muteAll()
}

fun AudioManager.changeVolume(dt: Int) {
    val streamMaxVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    val volume = clamp(streamMaxVolume + dt, 0, streamMaxVolume)
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
}