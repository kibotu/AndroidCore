package com.exozet.androidcommonutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.common.android.utils.ContextHelper;
import com.common.android.utils.extensions.FragmentExtensions;
import com.common.android.utils.logging.LogcatLogger;
import com.common.android.utils.logging.Logger;
import com.crashlytics.android.Crashlytics;
import com.exozet.basejava.misc.ConnectivityChangeListenerRx;
import com.exozet.basejava.misc.DefaultUserAgent;
import com.exozet.basejava.storage.LocalUser;
import com.facebook.stetho.Stetho;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration;

import net.danlew.android.joda.JodaTimeAndroid;
import net.kibotu.android.bloodhound.BloodHound;
import net.kibotu.android.deviceinfo.library.Device;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.os.Build.BOARD;
import static android.os.Build.BOOTLOADER;
import static android.os.Build.BRAND;
import static android.os.Build.CPU_ABI;
import static android.os.Build.CPU_ABI2;
import static android.os.Build.DEVICE;
import static android.os.Build.DISPLAY;
import static android.os.Build.FINGERPRINT;
import static android.os.Build.HARDWARE;
import static android.os.Build.HOST;
import static android.os.Build.ID;
import static android.os.Build.MANUFACTURER;
import static android.os.Build.MODEL;
import static android.os.Build.PRODUCT;
import static android.os.Build.SERIAL;
import static android.os.Build.TAGS;
import static android.os.Build.TIME;
import static android.os.Build.TYPE;
import static android.os.Build.USER;
import static android.os.Build.VERSION.CODENAME;
import static android.os.Build.VERSION.INCREMENTAL;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.N;
import static com.common.android.utils.extensions.JUnitExtensions.isJUnitTest;
import static com.exozet.basejava.BuildConfig.BRANCH;
import static com.exozet.basejava.BuildConfig.BUILD_DATE;
import static com.exozet.basejava.BuildConfig.BUILD_TYPE;
import static com.exozet.basejava.BuildConfig.CANONICAL_VERSION_NAME;
import static com.exozet.basejava.BuildConfig.COMMIT_HASH;
import static com.exozet.basejava.BuildConfig.FLAVOR;
import static com.exozet.basejava.BuildConfig.SIMPLE_VERSION_NAME;
import static com.exozet.basejava.BuildConfig.VERSION_CODE;
import static com.exozet.basejava.BuildConfig.VSC;


/**
 * Created by <a href="https://about.me/janrabe">Jan Rabe</a>.
 */

public class MainApplication extends MultiDexApplication {

    private static final String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        MultiDex.install(getApplicationContext());
        super.onCreate();

        JodaTimeAndroid.init(this);
        ContextHelper.with(this);
        Device.with(this);
        LocalUser.with(this);
        initLogger();

        if (isJUnitTest())
            return;

        logBuildConfig();

        initFabric();

        initCalligraphy();

        initConnectivityChangeListener();

        if (getResources().getBoolean(R.bool.use_stetho))
            Stetho.initializeWithDefaults(this);

        initGA();
    }

    private void initGA() {
        if (!getResources().getBoolean(R.bool.enable_google_analytics))
            return;

        BloodHound.with(this, getResources().getString(R.string.google_analytics_tracking_id))
                .enableExceptionReporting(false)
                .enableAdvertisingIdCollection(true)
                .enableAutoActivityTracking(false)
                .setSessionTimeout(300)
                .setSampleRate(100)
                .setLocalDispatchPeriod(300)
                .enableLogging(true)
                .enableDryRun(BuildConfig.DEBUG)
                .setSessionLimit(500);
    }

    private void initCalligraphy() {
        // Default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                // .setDefaultFontPath(getString(R.string.amazing_font))
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    private void initLogger() {

        Logger.addLogger(new LogcatLogger());
        Logger.setLogLevel(getResources().getBoolean(R.bool.enable_logging)
                ? Logger.Level.VERBOSE
                : Logger.Level.SILENT);

        FragmentExtensions.setLoggingEnabled(getResources().getBoolean(R.bool.enable_logging));
    }

    private void initConnectivityChangeListener() {
        ConnectionBuddy.getInstance().init(new ConnectionBuddyConfiguration.Builder(this).build());
        ConnectivityChangeListenerRx.with(this);
        ConnectivityChangeListenerRx.getObservable()
                .subscribe(connectivityEvent -> {
                    Logger.v(TAG, "[connectivityEvent] " + connectivityEvent);
                }, Throwable::printStackTrace);
    }

    private void initFabric() {
        Fabric.with(this, new Crashlytics());
        for (Map.Entry<String, String> entry : createInfo().entrySet())
            Crashlytics.setString(entry.getKey(), entry.getValue());
    }

    private void logBuildConfig() {
        for (Map.Entry<String, String> entry : createInfo().entrySet()) {
            Logger.i(TAG, entry.getKey() + " : " + entry.getValue());
        }
    }

    public Map<String, String> createInfo() {
        Map<String, String> info = createAppBuildInfo();
        info.putAll(createDeviceBuild(this));
        return info;
    }

    public static Map<String, String> createAppBuildInfo() {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("DEVICE_ID", "" + Build.SERIAL);
        info.put("CANONICAL_VERSION_NAME", CANONICAL_VERSION_NAME);
        info.put("SIMPLE_VERSION_NAME", SIMPLE_VERSION_NAME);
        info.put("VERSION_NAME", "" + BuildConfig.VERSION_NAME);
        info.put("VERSION_CODE", "" + VERSION_CODE);
        info.put("BUILD_TYPE", BUILD_TYPE);
        info.put("FLAVOR", FLAVOR);
        Calendar d = Calendar.getInstance();
        d.setTimeInMillis(Long.parseLong(BUILD_DATE));
        info.put("BUILD_DATE", "" + d.getTime());
        info.put("BRANCH", BRANCH);
        info.put("COMMIT_HASH", COMMIT_HASH);
        info.put("COMMIT_URL", VSC + "commits/" + COMMIT_HASH);
        info.put("TREE_URL", VSC + "src/" + COMMIT_HASH);
        return info;
    }

    public static Map<String, String> createDeviceBuild(Context context) {
        Map<String, String> info = new LinkedHashMap<>();
        // http://developer.android.com/reference/android/os/Build.html

        info.put("Model", MODEL);
        info.put("Manufacturer", MANUFACTURER);
        info.put("Release", Build.VERSION.RELEASE);
        info.put("SDK_INT", String.valueOf(SDK_INT));
        // info.put("Android Id", BuildInfo.getAndroidId());
        info.put("TIME", new Date(TIME).toString());

        if (SDK_INT >= LOLLIPOP)
            info.put("SUPPORTED_ABIS", Arrays.toString(android.os.Build.SUPPORTED_ABIS));

        info.put("CPU_ABI", CPU_ABI);
        info.put("CPU_ABI2", CPU_ABI2);

        info.put("Board", BOARD);
        info.put("Bootloader", BOOTLOADER);
        info.put("Brand", BRAND);
        info.put("Device", DEVICE);
        info.put("Display", DISPLAY);
        info.put("Fingerprint", FINGERPRINT);
        info.put("Hardware", HARDWARE);
        info.put("Host", HOST);
        info.put("Id", ID);
        info.put("Product", PRODUCT);
        info.put("Serial", SERIAL);
        info.put("Tags", TAGS);
        info.put("Type", TYPE);
        info.put("User", USER);

        // http://developer.android.com/reference/android/os/Build.VERSION.html
        info.put("Codename", CODENAME);
        info.put("Incremental", INCREMENTAL);
        info.put("User Agent", DefaultUserAgent.getDefaultUserAgent(context));
        info.put("HTTP Agent", System.getProperty("http.agent"));

        return info;
    }

    @SuppressLint("NewApi")
    public static Locale getLocaleFrom(Configuration configuration) {
        return SDK_INT >= N
                ? configuration.getLocales().get(0)
                : configuration.locale;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Logger.v(TAG, "[onConfigurationChanged] " + newConfig);
        LocalUser.setDefaultLocale(getLocaleFrom(newConfig));
    }

    @Override
    public void onTerminate() {
        ContextHelper.onTerminate();
        Device.onTerminate();
        ConnectivityChangeListenerRx.onTerminate(this);
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
