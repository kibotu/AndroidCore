package com.exozet.androidcommonutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.common.android.utils.extensions.FragmentExtensions;
import com.common.android.utils.extensions.SnackbarExtensions;
import com.common.android.utils.interfaces.Backpress;
import com.common.android.utils.interfaces.DispatchTouchEvent;
import com.common.android.utils.logging.Logger;
import com.common.android.utils.misc.Bundler;
import com.exozet.basejava.debugMenu.DebugMenu;
import com.exozet.basejava.ui.splash.SplashScreenFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import net.kibotu.timebomb.TimeBomb;

import io.nlopez.smartlocation.SmartLocation;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.text.TextUtils.isEmpty;
import static com.common.android.utils.ContextHelper.getActivity;
import static com.common.android.utils.extensions.DeviceExtensions.hideKeyboard;
import static com.common.android.utils.extensions.FragmentExtensions.currentFragment;
import static com.common.android.utils.extensions.FragmentExtensions.replace;
import static com.common.android.utils.extensions.JUnitExtensions.isJUnitTest;
import static com.common.android.utils.extensions.KeyGuardExtensions.unlockScreen;
import static io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS;
import static net.kibotu.android.deviceinfo.library.services.SystemService.getLocationManager;
import static net.kibotu.android.deviceinfo.library.services.SystemService.getWifiManager;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    DebugMenu debugMenu;
    private Intent intent;
    private SmartLocation.LocationControl locationControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isJUnitTest())
            return;

        intent = getIntent();
        Logger.v(TAG, "[onCreate] savedInstanceState=" + savedInstanceState + " intent=" + intent);

        // Keep the screen always on
        if (getResources().getBoolean(R.bool.flag_keep_screen_on))
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // unlock screen
        if (getResources().getBoolean(R.bool.unlock_screen_on_start))
            unlockScreen(this);

        setContentView(R.layout.activity_main);

        debugMenu = new DebugMenu();

        if (!consumeIntent())
            replace(new SplashScreenFragment());

        /*
        new GPVersionChecker.Builder(getActivity())
                .setCheckingStrategy(CheckingStrategy.ALWAYS)
                .showDialog(true)
                .forceUpdate(getResources().getBoolean(R.bool.force_update))
                // .setCustomPackageName("net.kibotu.base")
                .setVersionInfoListener(version -> {
                    Logger.v(TAG, "version=" + version);
                })
                .create();
                */
    }

    private boolean consumeIntent() {
        if (intent == null)
            return false;

        String dataString = intent.getDataString();
        if (isEmpty(dataString))
            return false;

        Logger.v(TAG, "[consumeIntent] " + dataString);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Bundler()
                        .putString(SplashScreenFragment.class.getCanonicalName(), dataString)
                        .into(new SplashScreenFragment()))
                .commitNowAllowingStateLoss();

        intent = null;

        return true;
    }

    public void startLocationTracking() {
        if (locationControl != null)
            return;

        locationControl = SmartLocation.with(this).location();
        locationControl.start(location -> {
            Logger.v(TAG, "[onLocationUpdated] location=" + location);
            locationControl.stop();
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        TimeBomb.bombAfterDays(this, BuildConfig.BUILD_DATE, getResources().getInteger(R.integer.time_bomb_delay));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {

        // hide keyboard
        hideKeyboard();

        // close menu
        if (debugMenu.isDrawerOpen()) {
            debugMenu.closeDrawer();
            return;
        }

        // let fragments handle back press
        final Fragment fragment = currentFragment();
        if (fragment instanceof Backpress && ((Backpress) fragment).onBackPressed())
            return;

        // pop back stack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            FragmentExtensions.printBackStack();
            return;
        }

        // quit app
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    // region hide keyboard if raycast for specific view fails

    @Override
    public boolean dispatchTouchEvent(@NonNull final MotionEvent ev) {

        final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof DispatchTouchEvent)
            return ((DispatchTouchEvent) fragment).dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);

        return super.dispatchTouchEvent(ev);
    }

    // endregion

    // region location permission

    @NeedsPermission({ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    void scanWifi() {
        Logger.v(TAG, "[scanWifi]");

        startLocationTracking();
        displayLocationSettingsRequest(this);
        getWifiManager().startScan();
    }

    @OnShowRationale({ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    void showRationaleForLocation(PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_location_rationale)
                .setPositiveButton(R.string.button_allow, (dialog, button) -> request.proceed())
                .setNegativeButton(R.string.button_deny, (dialog, button) -> request.cancel())
                .show();
    }

    @OnPermissionDenied({ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    void showDeniedForLocation() {
        SnackbarExtensions.showWarningSnack(getString(R.string.permission_location_denied));
    }

    @OnNeverAskAgain({ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    void showNeverAskForLocation() {
        SnackbarExtensions.showWarningSnack(getString(R.string.permission_location_neverask));
    }

    // endregion

    // region public global permission trigger

    public static void startWifiScanning() {
        final Activity activity = getActivity();
        if (activity instanceof MainActivity)
            MainActivityPermissionsDispatcher.scanWifiWithCheck((MainActivity) activity);
    }

    // endregion

    // region external input

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Logger.v(TAG, "[onConfigurationChanged] " + newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.v(TAG, "[onActivityResult] requestCode=" + requestCode + " resultCode=" + resultCode + " data=" + data);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.v(TAG, "[onNewIntent] " + intent);

        this.intent = intent;

        consumeIntent();
    }

    // endregion

    public static boolean isGPSProviderEnabled() {
        return getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void displayLocationSettingsRequest(final Activity context) {
        if (isGPSProviderEnabled())
            return;

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(r -> {
            final Status status = r.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // Logger.i(TAG, "All location settings are satisfied.");
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Logger.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(context, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        // Logger.i(TAG, "PendingIntent unable to execute request.");
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Logger.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                    break;
            }
        });
    }
}