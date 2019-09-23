package com.exozet.android.core.input

import android.content.DialogInterface
import android.view.KeyEvent
import android.view.View
import android.view.View.OnKeyListener
import com.exozet.android.core.misc.weak

/**
 *
 *
 * [https://developer.amazon.com/appsandservices/solutions/devices/fire-tv/docs/amazon-fire-tv-remote-input](https://developer.amazon.com/appsandservices/solutions/devices/fire-tv/docs/amazon-fire-tv-remote-input)
 *
 *
 * <pre>
 * ----------------------------------------------------------------------------------------------------
 * |                            |       (KEYCODE_MEDIA_RECORD)      |                                   |
 * ----------------------------------------------------------------------------------------------------
 * |                                                                                                    |
 * ----------------------------------------------------------------------------------------------------
 * |                            |       KEYCODE_DPAD_UP             |                                   |
 * ----------------------------------------------------------------------------------------------------
 * |    KEYCODE_DPAD_LEFT       |       KEYCODE_DPAD_CENTER         |       KEYCODE_DPAD_RIGHT          |
 * ----------------------------------------------------------------------------------------------------
 * |                            |       KEYCODE_DPAD_DOWN           |                                   |
 * ----------------------------------------------------------------------------------------------------
 * |                                                                                                    |
 * ----------------------------------------------------------------------------------------------------
 * |    KEYCODE_BACK            |        (KEYCODE_ESCAPE)           |       KEYCODE_MENU                |
 * ----------------------------------------------------------------------------------------------------
 * |    KEYCODE_MEDIA_REWIND    |        KEYCODE_MEDIA_PLAY_PAUSE   |       KEYCODE_MEDIA_FAST_FORWARD  |
 * ----------------------------------------------------------------------------------------------------
 *
</pre> *
 * <img src="https://github.com/exozet/AndroidCore/raw/master/documentation/AmazonFireTvRemoteControlListener.jpg"></img>
 * <pre>
 * Button	                                    KeyEvent	                Default Behavior
 * Home	                                    none	                    Return the user to the Home screen. This is a system event and cannot be intercepted.
 * Back	                                    KEYCODE_BACK	            Return the user to the previous operation or screen (Activity).
 * Menu	                                    KEYCODE_MENU	            Invoke the Android context menu (OptionsMenu).
 * Microphone (Search) (Voice Remote only)     none	                    Invoke the system voice search. This is a system event and cannot be intercepted.
 * Select (D-Pad Center)	                    KEYCODE_DPAD_CENTER	        Select the user interface item with the current focus.
 * Up (D-Pad)	                                KEYCODE_DPAD_UP	            Move the focus upward in the user interface.
 * Down (D-Pad)	                            KEYCODE_DPAD_DOWN	        Move the focus downward in the user interface.
 * Left (D-Pad)	                            KEYCODE_DPAD_LEFT	        Move the focus left in the user interface.
 * Right (D-Pad)	                            KEYCODE_DPAD_RIGHT	        Move the focus right in the user interface.
 * Play/Pause	                                KEYCODE_MEDIA_PLAY_PAUSE	Control media playback. Play/Pause is a toggle.
 * Rewind	                                    KEYCODE_MEDIA_REWIND	    Rewind or skip backwards in media playback contexts.
 * Fast Forward	                            KEYCODE_MEDIA_FAST_FORWARD	Fast Forward or skip ahead in media playback contexts.
 *
 * Play	                                    KEYCODE_KEYCODE_MEDIA_PLAY	Play media key - android tv
 * Pause   	                                KEYCODE_KEYCODE_MEDIA_PAUSE	Pause media key - android tv
</pre> *
 */
open class AmazonFireTvRemoteControlListener : KeyListener, OnKeyListener, DialogInterface.OnKeyListener {

    var currentView by weak<View?>()
        protected set

    override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
        currentView = v
        return if (event.action == KeyEvent.ACTION_DOWN) onKeyDown(keyCode, event) else event.action == KeyEvent.ACTION_UP && onKeyUp(keyCode, event)
    }

    override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean =
        if (event.action == KeyEvent.ACTION_DOWN) onKeyDown(keyCode, event) else event.action == KeyEvent.ACTION_UP && onKeyUp(keyCode, event)

    override fun onKeyUp(keyCode: Int, keyEvent: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> onKeyUpDpadUp(keyEvent)
            KeyEvent.KEYCODE_DPAD_DOWN -> onKeyUpDpadDown(keyEvent)
            KeyEvent.KEYCODE_DPAD_LEFT -> onKeyUpDpadLeft(keyEvent)
            KeyEvent.KEYCODE_DPAD_RIGHT -> onKeyUpDpadRight(keyEvent)
            KeyEvent.KEYCODE_DPAD_CENTER -> onKeyUpDpadCenter(keyEvent)
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> onKeyUpMediaPlayPause(keyEvent)
            KeyEvent.KEYCODE_MEDIA_REWIND -> onKeyUpMediaRewind(keyEvent)
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> onKeyUpMediaFastForward(keyEvent)
            KeyEvent.KEYCODE_BACK -> onKeyUpBack(keyEvent)
            KeyEvent.KEYCODE_MENU -> onKeyUpMenu(keyEvent)
            KeyEvent.KEYCODE_MEDIA_PLAY -> onKeyUpMediaPlay(keyEvent)
            KeyEvent.KEYCODE_MEDIA_PAUSE -> onKeyUpMediaPause(keyEvent)
            else -> false
        }
    }

    protected fun onKeyUpDpadUp(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyUpDpadDown(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyUpDpadLeft(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyUpDpadRight(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyUpDpadCenter(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyUpMediaPlayPause(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyUpMediaRewind(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyUpMediaFastForward(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyUpBack(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyUpMenu(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyUpMediaPlay(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyUpMediaPause(keyEvent: KeyEvent): Boolean = false

    override fun onKeyDown(keyCode: Int, keyEvent: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> onKeyDownDpadUp(keyEvent)
            KeyEvent.KEYCODE_DPAD_DOWN -> onKeyDownDpadDown(keyEvent)
            KeyEvent.KEYCODE_DPAD_LEFT -> onKeyDownDpadLeft(keyEvent)
            KeyEvent.KEYCODE_DPAD_RIGHT -> onKeyDownDpadRight(keyEvent)
            KeyEvent.KEYCODE_DPAD_CENTER -> onKeyDownDpadCenter(keyEvent)
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> onKeyDownMediaPlayPause(keyEvent)
            KeyEvent.KEYCODE_MEDIA_REWIND -> onKeyDownMediaRewind(keyEvent)
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> onKeyDownMediaFastForward(keyEvent)
            KeyEvent.KEYCODE_BACK -> onKeyDownBack(keyEvent)
            KeyEvent.KEYCODE_MENU -> onKeyDownMenu(keyEvent)
            KeyEvent.KEYCODE_MEDIA_PLAY -> onKeyDownMediaPlay(keyEvent)
            KeyEvent.KEYCODE_MEDIA_PAUSE -> onKeyDownMediaPause(keyEvent)
            else -> false
        }
    }

    protected fun onKeyDownDpadUp(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyDownDpadDown(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyDownDpadLeft(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyDownDpadRight(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyDownDpadCenter(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyDownMediaPlayPause(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyDownMediaRewind(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyDownMediaFastForward(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyDownBack(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyDownMenu(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyDownMediaPlay(keyEvent: KeyEvent): Boolean = false

    protected fun onKeyDownMediaPause(keyEvent: KeyEvent): Boolean = false

    override fun toString(): String = "AmazonFireTvRemoteControlListener{currentView=$currentView}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}