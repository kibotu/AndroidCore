@file:JvmName("SystemServiceExtensions")

package com.exozet.android.core.extensions


import android.accounts.AccountManager
import android.annotation.TargetApi
import android.app.*
import android.app.admin.DevicePolicyManager
import android.app.job.JobScheduler
import android.app.usage.NetworkStatsManager
import android.app.usage.UsageStatsManager
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothManager
import android.content.ClipboardManager
import android.content.Context
import android.content.RestrictionsManager
import android.content.pm.LauncherApps
import android.hardware.ConsumerIrManager
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.hardware.fingerprint.FingerprintManager
import android.hardware.input.InputManager
import android.hardware.usb.UsbManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaRouter
import android.media.midi.MidiManager
import android.media.projection.MediaProjectionManager
import android.media.session.MediaSessionManager
import android.media.tv.TvInputManager
import android.net.ConnectivityManager
import android.net.nsd.NsdManager
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.nfc.NfcManager
import android.os.*
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.*
import android.os.storage.StorageManager
import android.print.PrintManager
import android.telecom.TelecomManager
import android.telephony.CarrierConfigManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.CaptioningManager
import android.view.inputmethod.InputMethodManager
import android.view.textservice.TextServicesManager
import net.kibotu.ContextHelper.getApplication

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 *
 * @see [getSystemService](http://developer.android.com/reference/android/content/Context.html.getSystemService%28java.lang.Class%3CT%3E%29)
 */

/**
 * For giving the user feedback for UI events through the registered event listeners.
 */
@get:TargetApi(DONUT)
val accessibilityManager by lazy {
    getApplication()?.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
}

/**
 * For receiving intents at a time of your choosing.
 */
@get:TargetApi(ECLAIR)
val accountManager by lazy {
    getApplication()?.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
}

/**
 * A ActivityManager for interacting with the global activity state of the system.
 */
val activityManager by lazy {
    getApplication()?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
}

/**
 * A AlarmManager for receiving intents at the time of your choosing.
 */
val alarmManager by lazy {
    getApplication()?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
}

/**
 * For accessing AppWidgets.
 */
@get:TargetApi(LOLLIPOP)
val appWidgetManager by lazy {
    if (SDK_INT >= LOLLIPOP) {
        getApplication()?.getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager
    }
}

/**
 * For handling management of volume, ringer modes and audio routing.
 */
val audioManager by lazy {
    getApplication()?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
}

/**
 * A BatteryManager for managing battery state
 */
@get:TargetApi(LOLLIPOP)
val batteryManager by lazy {
    getApplication()?.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
}

/**
 * For using Bluetooth.
 */
@get:TargetApi(JELLY_BEAN_MR2)
val bluetoothManager by lazy {
    if (SDK_INT >= JELLY_BEAN_MR2) {
        getApplication()?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }
}

/**
 * For interacting with camera devices.
 */
@get:TargetApi(LOLLIPOP)
val cameraManager by lazy {
    if (SDK_INT >= LOLLIPOP) {
        getApplication()?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
}

/**
 * For obtaining captioning properties and listening for changes in captioning preferences.
 */
val captioningManager by lazy {
    if (SDK_INT >= KITKAT) {
        getApplication()?.getSystemService(Context.CAPTIONING_SERVICE) as CaptioningManager
    }
}

/**
 * For obtaining captioning properties and listening for changes in captioning preferences.
 */
@get:TargetApi(M)
val carrierConfigManager by lazy {
    if (SDK_INT >= M) {
        getApplication()?.getSystemService(Context.CARRIER_CONFIG_SERVICE) as CarrierConfigManager
    }
}

/**
 * For accessing and modifying ClipboardManager for accessing and modifying the contents of the global clipboard.
 */
val clipboardManager by lazy {
    getApplication()?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
}

/**
 * A ConnectivityManager for handling management of network connections.
 */
val connectivityManager by lazy {
    getApplication()?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}

/**
 * For transmitting infrared signals from the device.
 */
@get:TargetApi(KITKAT)
val consumerIrManager by lazy {
    if (SDK_INT >= KITKAT) {
        getApplication()?.getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager
    }
}

/**
 * For working with global device policy management.
 */
val devicePolicyManager by lazy {
    getApplication()?.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
}

/**
 * For interacting with display devices.
 */
@get:TargetApi(JELLY_BEAN_MR1)
val displayManager by lazy {
    if (SDK_INT >= JELLY_BEAN_MR1) {
        getApplication()?.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }
}

/**
 * A DownloadManager for requesting HTTP downloads
 */
val downloadManager by lazy {
    getApplication()?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
}

/**
 * Instance for recording diagnostic logs.
 */
val dropBoxManager by lazy {
    getApplication()?.getSystemService(Context.DROPBOX_SERVICE) as DropBoxManager
}

/**
 * For handling management of fingerprints.
 */
@get:TargetApi(M)
val fingerprintManager by lazy {
    if (SDK_INT >= M) {
        getApplication()?.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
    }
}

/**
 * An InputMethodManager for management of input methods.
 */
val inputMethodManager by lazy {
    getApplication()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
}

/**
 * For interacting with input devices.
 */
@get:TargetApi(JELLY_BEAN)
val inputManager by lazy {
    if (SDK_INT >= JELLY_BEAN) {
        getApplication()?.getSystemService(Context.INPUT_SERVICE) as InputManager
    }
}

/**
 * A JobScheduler for managing scheduled tasks
 */
@get:TargetApi(LOLLIPOP)
val jobScheduler by lazy {
    if (SDK_INT >= LOLLIPOP) {
        getApplication()?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    }
}

/**
 * A KeyguardManager for controlling keyguard.
 */
val keyguardManager by lazy {
    getApplication()?.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
}

/**
 * For querying and monitoring launchable apps across profiles of a user.
 */
@get:TargetApi(LOLLIPOP)
val launcherApps by lazy {
    if (SDK_INT >= LOLLIPOP) {
        getApplication()?.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
    }
}

/**
 * A LayoutInflater for inflating layout resources in this context.
 */
val layoutInflater by lazy {
    getApplication()?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
}

/**
 * A LocationManager for controlling location (e.g., GPS) updates.
 */
val locationManager by lazy {
    getApplication()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
}

/**
 * Instance for managing media projection sessions.
 */
@get:TargetApi(LOLLIPOP)
val mediaProjectionManager by lazy {
    if (SDK_INT >= LOLLIPOP) {
        getApplication()?.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }
}

/**
 * For controlling and managing routing of media.
 */
@get:TargetApi(JELLY_BEAN)
val mediaRouter by lazy {
    if (SDK_INT >= JELLY_BEAN) {
        getApplication()?.getSystemService(Context.MEDIA_ROUTER_SERVICE) as MediaRouter
    }
}

/**
 * For managing media Sessions.
 */
@get:TargetApi(LOLLIPOP)
val mediaSessionManager by lazy {
    if (SDK_INT >= LOLLIPOP) {
        getApplication()?.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
    }
}

/**
 * For accessing the MIDI service.
 */
@get:TargetApi(M)
val midiManager by lazy {
    if (SDK_INT >= M) {
        getApplication()?.getSystemService(Context.MIDI_SERVICE) as MidiManager
    }
}

/**
 * A NetworkStatsManager for querying network usage statistics.
 */
@get:TargetApi(M)
val networkStatsManager by lazy {
    if (SDK_INT >= M) {
        getApplication()?.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
    }
}

/**
 * For using NFC.
 */
val nfcManager by lazy {
    getApplication()?.getSystemService(Context.NFC_SERVICE) as NfcManager
}

/**
 * A NotificationManager for informing the user of background events.
 */
val notificationManager by lazy {
    getApplication()?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}

/**
 * A PowerManager for controlling power management.
 */
val powerManager by lazy {
    getApplication()?.getSystemService(Context.POWER_SERVICE) as PowerManager
}

/**
 * For printing and managing printers and print tasks.
 */
@get:TargetApi(KITKAT)
val printManager by lazy {
    if (SDK_INT >= KITKAT) {
        getApplication()?.getSystemService(Context.PRINT_SERVICE) as PrintManager
    }
}

/**
 * For retrieving application restrictions and requesting permissions for restricted operations.
 */
@get:TargetApi(LOLLIPOP)
val restrictionsManager by lazy {
    if (SDK_INT >= LOLLIPOP) {
        getApplication()?.getSystemService(Context.RESTRICTIONS_SERVICE) as RestrictionsManager
    }
}

/**
 * A SearchManager for handling search.
 */
val searchManager by lazy {
    getApplication()?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
}

/**
 * For accessing sensors.
 */
val sensorManager by lazy {
    getApplication()?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
}

/**
 * For accessing system storage functions.
 */
val storageManager by lazy {
    getApplication()?.getSystemService(Context.STORAGE_SERVICE) as StorageManager
}

/**
 * To manage telecom-related features of the device.
 */
@get:TargetApi(LOLLIPOP)
val telecomManager by lazy {
    getApplication()?.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
}

/**
 * For handling management the telephony features of the device
 */
@get:TargetApi(LOLLIPOP_MR1)
val telephonyManager by lazy {
    getApplication()?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
}

/**
 * For handling management the telephony subscriptions of the device.
 */
@get:TargetApi(LOLLIPOP_MR1)
val subscriptionManager by lazy {
    getApplication()?.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
}

/**
 * For accessing text services.
 */
@get:TargetApi(ICE_CREAM_SANDWICH)
val textServicesManager by lazy {
    getApplication()?.getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE) as TextServicesManager
}

/**
 * For accessing text services.
 */
@get:TargetApi(LOLLIPOP)
val tvInputManager by lazy {
    if (SDK_INT >= LOLLIPOP) {
        getApplication()?.getSystemService(Context.TV_INPUT_SERVICE) as TvInputManager
    }
}

/**
 * An UiModeManager for controlling UI modes.
 */
val uiModeManager by lazy {
    getApplication()?.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
}

/**
 * For querying device usage stats.
 */
@get:TargetApi(LOLLIPOP_MR1)
val usageStatsManager by lazy {
    if (SDK_INT >= LOLLIPOP) {
        getApplication()?.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }
}

/**
 * For access to USB devices (as a USB host) and for controlling this device's behavior as a USB device.
 */
val usbManager by lazy {
    getApplication()?.getSystemService(Context.USB_SERVICE) as UsbManager
}

/**
 * For managing users on devices that support multiple users.
 */
@get:TargetApi(JELLY_BEAN_MR1)
val userManager by lazy {
    if (SDK_INT >= JELLY_BEAN_MR1) {
        getApplication()?.getSystemService(Context.USER_SERVICE) as UserManager
    }
}

/**
 * A Vibrator for interacting with the vibrator hardware.
 */
val vibratorby by lazy {
    getApplication()?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
}

/**
 * A Vibrator for interacting with the vibrator hardware.
 */
val wallpaperServiceby by lazy {
    getApplication()?.getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
}

/**
 * A WifiP2pManager for management of Wi-Fi Direct connectivity.
 */
val wifiP2pManager by lazy {
    getApplication()?.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
}

/**
 * A WifiManager for management of Wi-Fi connectivity.
 */
val wifiManager by lazy {
    getApplication()?.getSystemService(Context.WIFI_SERVICE) as WifiManager
}

/**
 * The top-level window manager in which you can place custom windows. The returned object is a WindowManager.
 */
val windowManager by lazy {
    getApplication()?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
}

/**
 * For handling management of network service discovery
 */
@get:TargetApi(JELLY_BEAN)
val ndsManager by lazy {
    if (SDK_INT >= JELLY_BEAN) {
        getApplication()?.getSystemService(Context.NSD_SERVICE) as NsdManager
    }
}