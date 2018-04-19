@file:JvmName("AudioManagerExtensions")

package com.exozet.android.core.extensions

import android.media.AudioManager
import android.os.Build

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

fun AudioManager.muteAll() {
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

fun AudioManager.unmuteAll() {
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