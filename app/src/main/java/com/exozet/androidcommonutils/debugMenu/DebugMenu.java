package com.exozet.androidcommonutils.debugMenu;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.common.android.utils.extensions.FragmentExtensions;
import com.common.android.utils.logging.Logger;
import com.common.android.utils.misc.Bundler;
import com.exozet.basejava.BuildConfig;
import com.exozet.basejava.MainApplication;
import com.exozet.basejava.R;
import com.exozet.basejava.storage.LocalUser;
import com.exozet.basejava.ui.markdown.MarkdownFragment;
import com.exozet.basejava.ui.markdown.RawOutputFragment;
import com.exozet.basejava.ui.splash.SplashScreenFragment;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import net.kibotu.android.deviceinfo.library.memory.Ram;
import net.kibotu.android.deviceinfo.library.memory.RamUsage;
import net.kibotu.android.deviceinfo.library.misc.UpdateTimer;

import java.util.Map;

import static com.common.android.utils.ContextHelper.getActivity;
import static com.common.android.utils.ContextHelper.getApplication;
import static com.common.android.utils.ContextHelper.getContext;
import static com.common.android.utils.extensions.FragmentExtensions.replaceToBackStackByFading;
import static com.common.android.utils.extensions.ResourceExtensions.getString;
import static com.common.android.utils.extensions.StringExtensions.capitalize;
import static com.common.android.utils.misc.GsonProvider.getGsonPrettyPrinting;
import static com.exozet.basejava.MainApplication.createDeviceBuild;
import static com.exozet.basejava.storage.LocalUser.switchToDefault;
import static com.exozet.basejava.storage.LocalUser.switchToKorean;
import static com.exozet.basejava.ui.markdown.MarkdownFragment.MARKDOWN_FILENAME;
import static net.kibotu.android.deviceinfo.library.ViewHelper.formatBytes;


/**
 * Created by <a href="https://about.me/janrabe">Jan Rabe</a>.
 */

public class DebugMenu {

    public static final String DEBUG = "SHOW_DEBUG_LOGS";

    private static final String TAG = DebugMenu.class.getSimpleName();
    private Drawer drawer;

    private RamUsage ramUsage;

    public DebugMenu() {

        if (getContext().getResources().getBoolean(R.bool.enable_debug_menu))
            setupDebugMenu();
    }

    private void setupDebugMenu() {

        drawer = new DrawerBuilder()
                .withTranslucentStatusBar(true)
                .withActivity(getActivity())
                .addDrawerItems(createDebugMenuItems())
                .withOnDrawerItemClickListener(createDebugMenuClickListener())
                .build();
    }

    @NonNull
    private IDrawerItem[] createDebugMenuItems() {
        return new IDrawerItem[]{

                new SectionDrawerItem().withName(getString(R.string.app_name) + " - " + capitalize(BuildConfig.BUILD_TYPE.toLowerCase()) + " Menu").withIdentifier(R.string.section).withDivider(false),

                new DividerDrawerItem(),

                new SecondaryDrawerItem().withIdentifier(R.string.screen_markdown_readme).withName(BuildConfig.CANONICAL_VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")"),
                new SecondaryDrawerItem().withName(R.string.screen_markdown_changelog).withIdentifier(R.string.screen_markdown_changelog),

                new DividerDrawerItem(),

                new ExpandableDrawerItem().withName("App").withIdentifier(R.string.section).withIsExpanded(true)
                        .withSubItems(new SecondaryDrawerItem().withName(R.string.debug_build_info).withIdentifier(R.string.debug_build_info))
                        .withSubItems(new SecondaryDrawerItem().withName(R.string.restart_app).withIdentifier(R.string.restart_app))
                        .withSubItems(new SecondaryDrawerItem().withName(R.string.reset_app).withIdentifier(R.string.reset_app)),

                new DividerDrawerItem(),

                new ExpandableDrawerItem().withName("Debug").withIdentifier(R.string.section).withIsExpanded(false)
                        .withSubItems(new SecondaryDrawerItem().withName(R.string.debug_ram).withIdentifier(R.string.debug_ram))
                        .withSubItems(new SecondaryDrawerItem().withName(R.string.debug_current_threads).withIdentifier(R.string.debug_current_threads))
                        .withSubItems(new SecondaryDrawerItem().withName(R.string.debug_current_backstack).withIdentifier(R.string.debug_current_backstack))
                        .withSubItems(new SecondaryDrawerItem().withName(R.string.debug_current_fragment).withIdentifier(R.string.debug_current_fragment)),

                new DividerDrawerItem(),

                new ExpandableDrawerItem().withName("Language").withIdentifier(R.string.section).withIsExpanded(false)
                        .withSubItems(new SecondaryDrawerItem().withIdentifier(R.string.language_default).withName(R.string.language_default))
                        .withSubItems(new SecondaryDrawerItem().withIdentifier(R.string.language_korean).withName(R.string.language_korean)),


                new ExpandableDrawerItem().withName("Screens").withIdentifier(R.string.section).withIsExpanded(true)
                        .withSubItems(new SecondaryDrawerItem().withIdentifier(R.string.screen_splash).withName(R.string.screen_splash))
        };
    }


    @NonNull
    private Drawer.OnDrawerItemClickListener createDebugMenuClickListener() {
        return (view, position, drawerItem) -> {

            final int identifier = (int) drawerItem.getIdentifier();
            switch (identifier) {

                // app commands
                case R.string.restart_app:
                    restartApp();
                    break;

                case R.string.reset_app:
                    resetApp();
                    restartApp();
                    break;

                // app commands
                case R.string.debug_ram:

                    if (ramUsage != null) {
                        ramUsage.stop();
                        ramUsage = null;
                        break;
                    }

                    ramUsage = new RamUsage();
                    ramUsage.addObserver(new UpdateTimer.UpdateListener<Ram>() {

                        @Override
                        protected void update(Ram ram) {
                            Logger.v(TAG, String.format(
                                    "[Available=%s Free=%s Total=%s Used=%s]",
                                    formatBytes(ram.getAvailableInBytes()),
                                    formatBytes(ram.getFreeInBytes()),
                                    formatBytes(ram.getTotalInBytes()),
                                    formatBytes(ram.getUsedInBytes())
                            ));
                        }
                    }).setUpdateInterval(10000).start();

                    break;

                case R.string.debug_current_backstack:
                    FragmentExtensions.printBackStack();
                    break;
                case R.string.debug_current_fragment:
                    Logger.v(TAG, "Current Fragment " + FragmentExtensions.currentFragment());
                    break;
                case R.string.debug_build_info:
                    Map<String, String> info = MainApplication.createAppBuildInfo();
                    info.putAll(createDeviceBuild(getContext()));
                    replaceToBackStackByFading(new RawOutputFragment().setArgument(new Bundler().putString(RawOutputFragment.RAW_OUTPUT_TEXT, getGsonPrettyPrinting().toJson(info)).get()));
                    break;
                case R.string.debug_current_threads:
                    for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
                        for (StackTraceElement element : entry.getValue())
                            Logger.v(TAG, entry.getKey() + " -> " + element);
                    }
                    break;

                // language
                case R.string.language_default:
                    switchToDefault();
                    break;
                case R.string.language_korean:
                    switchToKorean();
                    break;

                // screens
                case R.string.screen_markdown_readme:
                    replaceToBackStackByFading(new MarkdownFragment().setArgument(new Bundler().putString(MARKDOWN_FILENAME, "README.md").get()));
                    break;

                case R.string.screen_markdown_changelog:
                    replaceToBackStackByFading(new MarkdownFragment().setArgument(new Bundler().putString(MARKDOWN_FILENAME, "CHANGELOG.md").get()));
                    break;

                case R.string.screen_splash:
                    replaceToBackStackByFading(new SplashScreenFragment());
            }

            if (identifier != R.string.section)
                drawer.closeDrawer();

            return true;
        };
    }

    public static Bundle createDebugArguments() {
        return new Bundler().putBoolean(DebugMenu.DEBUG, true).get();
    }

    public boolean isDrawerOpen() {
        return drawer != null && drawer.isDrawerOpen();
    }

    public void closeDrawer() {
        if (drawer != null)
            drawer.closeDrawer();
    }

    public static boolean isDebug(Bundle bundle) {
        return bundle != null && bundle.getBoolean(DebugMenu.DEBUG);
    }

    public static boolean isDebug(Fragment fragment) {
        return isDebug(fragment.getArguments());
    }

    public static void resetApp() {
        LocalUser.clear();
        Application application = getApplication();
    }

    public static void restartApp() {
        ProcessPhoenix.triggerRebirth(getContext());
    }
}
