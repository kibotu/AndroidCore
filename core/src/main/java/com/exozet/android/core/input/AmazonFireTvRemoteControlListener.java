package com.exozet.android.core.input;

import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * <p>
 * <a href="https://developer.amazon.com/appsandservices/solutions/devices/fire-tv/docs/amazon-fire-tv-remote-input">https://developer.amazon.com/appsandservices/solutions/devices/fire-tv/docs/amazon-fire-tv-remote-input</a>
 * <p>
 * <pre>
 *  ----------------------------------------------------------------------------------------------------
 * |                            |       (KEYCODE_MEDIA_RECORD)      |                                   |
 *  ----------------------------------------------------------------------------------------------------
 * |                                                                                                    |
 *  ----------------------------------------------------------------------------------------------------
 * |                            |       KEYCODE_DPAD_UP             |                                   |
 *  ----------------------------------------------------------------------------------------------------
 * |    KEYCODE_DPAD_LEFT       |       KEYCODE_DPAD_CENTER         |       KEYCODE_DPAD_RIGHT          |
 *  ----------------------------------------------------------------------------------------------------
 * |                            |       KEYCODE_DPAD_DOWN           |                                   |
 *  ----------------------------------------------------------------------------------------------------
 * |                                                                                                    |
 *  ----------------------------------------------------------------------------------------------------
 * |    KEYCODE_BACK            |        (KEYCODE_ESCAPE)           |       KEYCODE_MENU                |
 *  ----------------------------------------------------------------------------------------------------
 * |    KEYCODE_MEDIA_REWIND    |        KEYCODE_MEDIA_PLAY_PAUSE   |       KEYCODE_MEDIA_FAST_FORWARD  |
 *  ----------------------------------------------------------------------------------------------------
 *
 *  </pre>
 * <img src="https://github.com/exozet/AndroidCore/raw/master/documentation/AmazonFireTvRemoteControlListener.jpg" />
 * <pre>
 *  Button	                                    KeyEvent	                Default Behavior
 *  Home	                                    none	                    Return the user to the Home screen. This is a system event and cannot be intercepted.
 *  Back	                                    KEYCODE_BACK	            Return the user to the previous operation or screen (Activity).
 *  Menu	                                    KEYCODE_MENU	            Invoke the Android context menu (OptionsMenu).
 *  Microphone (Search) (Voice Remote only)     none	                    Invoke the system voice search. This is a system event and cannot be intercepted.
 *  Select (D-Pad Center)	                    KEYCODE_DPAD_CENTER	        Select the user interface item with the current focus.
 *  Up (D-Pad)	                                KEYCODE_DPAD_UP	            Move the focus upward in the user interface.
 *  Down (D-Pad)	                            KEYCODE_DPAD_DOWN	        Move the focus downward in the user interface.
 *  Left (D-Pad)	                            KEYCODE_DPAD_LEFT	        Move the focus left in the user interface.
 *  Right (D-Pad)	                            KEYCODE_DPAD_RIGHT	        Move the focus right in the user interface.
 *  Play/Pause	                                KEYCODE_MEDIA_PLAY_PAUSE	Control media playback. Play/Pause is a toggle.
 *  Rewind	                                    KEYCODE_MEDIA_REWIND	    Rewind or skip backwards in media playback contexts.
 *  Fast Forward	                            KEYCODE_MEDIA_FAST_FORWARD	Fast Forward or skip ahead in media playback contexts.
 *
 *  Play	                                    KEYCODE_KEYCODE_MEDIA_PLAY	Play media key - android tv
 *  Pause   	                                KEYCODE_KEYCODE_MEDIA_PAUSE	Pause media key - android tv
 * </pre>
 */
public class AmazonFireTvRemoteControlListener implements KeyListener, View.OnKeyListener, DialogInterface.OnKeyListener {

    protected View currentView;

    @Nullable
    public final View getCurrentView() {
        return this.currentView;
    }

    @Override
    final public boolean onKey(final View v, final int keyCode, @NonNull final KeyEvent event) {
        this.currentView = v;
        return event.getAction() == KeyEvent.ACTION_DOWN
                ? onKeyDown(keyCode, event)
                : event.getAction() == KeyEvent.ACTION_UP && onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKey(final DialogInterface dialog, final int keyCode, @NonNull final KeyEvent event) {
        return event.getAction() == KeyEvent.ACTION_DOWN
                ? onKeyDown(keyCode, event)
                : event.getAction() == KeyEvent.ACTION_UP && onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                return onKeyUpDpadUp(keyEvent);
            case KeyEvent.KEYCODE_DPAD_DOWN:
                return onKeyUpDpadDown(keyEvent);
            case KeyEvent.KEYCODE_DPAD_LEFT:
                return onKeyUpDpadLeft(keyEvent);
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                return onKeyUpDpadRight(keyEvent);
            case KeyEvent.KEYCODE_DPAD_CENTER:
                return onKeyUpDpadCenter(keyEvent);
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                return onKeyUpMediaPlayPause(keyEvent);
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                return onKeyUpMediaRewind(keyEvent);
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                return onKeyUpMediaFastForward(keyEvent);
            case KeyEvent.KEYCODE_BACK:
                return onKeyUpBack(keyEvent);
            case KeyEvent.KEYCODE_MENU:
                return onKeyUpMenu(keyEvent);
            case KeyEvent.KEYCODE_MEDIA_PLAY: // android tv
                return onKeyUpMediaPlay(keyEvent);
            case KeyEvent.KEYCODE_MEDIA_PAUSE: // android tv
                return onKeyUpMediaPause(keyEvent);
            default:
                return false;
        }
    }

    protected boolean onKeyUpDpadUp(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyUpDpadDown(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyUpDpadLeft(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyUpDpadRight(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyUpDpadCenter(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyUpMediaPlayPause(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyUpMediaRewind(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyUpMediaFastForward(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyUpBack(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyUpMenu(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyUpMediaPlay(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyUpMediaPause(KeyEvent keyEvent) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                return onKeyDownDpadUp(keyEvent);
            case KeyEvent.KEYCODE_DPAD_DOWN:
                return onKeyDownDpadDown(keyEvent);
            case KeyEvent.KEYCODE_DPAD_LEFT:
                return onKeyDownDpadLeft(keyEvent);
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                return onKeyDownDpadRight(keyEvent);
            case KeyEvent.KEYCODE_DPAD_CENTER:
                return onKeyDownDpadCenter(keyEvent);
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                return onKeyDownMediaPlayPause(keyEvent);
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                return onKeyDownMediaRewind(keyEvent);
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                return onKeyDownMediaFastForward(keyEvent);
            case KeyEvent.KEYCODE_BACK:
                return onKeyDownBack(keyEvent);
            case KeyEvent.KEYCODE_MENU:
                return onKeyDownMenu(keyEvent);
            case KeyEvent.KEYCODE_MEDIA_PLAY: // android tv
                return onKeyDownMediaPlay(keyEvent);
            case KeyEvent.KEYCODE_MEDIA_PAUSE: // android tv
                return onKeyDownMediaPause(keyEvent);
            default:
                return false;
        }
    }

    protected boolean onKeyDownDpadUp(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyDownDpadDown(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyDownDpadLeft(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyDownDpadRight(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyDownDpadCenter(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyDownMediaPlayPause(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyDownMediaRewind(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyDownMediaFastForward(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyDownBack(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyDownMenu(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyDownMediaPlay(KeyEvent keyEvent) {
        return false;
    }

    protected boolean onKeyDownMediaPause(KeyEvent keyEvent) {
        return false;
    }

    @NonNull
    public String tag() {
        return AmazonFireTvRemoteControlListener.class.getSimpleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AmazonFireTvRemoteControlListener that = (AmazonFireTvRemoteControlListener) o;

        return currentView != null ? currentView.equals(that.currentView) : that.currentView == null;

    }

    @Override
    public int hashCode() {
        return currentView != null ? currentView.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AmazonFireTvRemoteControlListener{" +
                "currentView=" + currentView +
                '}';
    }
}
