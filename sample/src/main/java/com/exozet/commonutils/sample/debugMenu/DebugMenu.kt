package com.exozet.commonutils.sample.debugMenu

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.common.android.utils.ContextHelper
import com.common.android.utils.extensions.FragmentExtensions
import com.common.android.utils.extensions.ResourceExtensions
import com.common.android.utils.extensions.StringExtensions
import com.common.android.utils.logging.Logger
import com.common.android.utils.misc.Bundler
import com.common.android.utils.misc.GsonProvider
import com.exozet.androidcommonutils.sample.ui.BaseFragment
import com.exozet.commonutils.sample.BuildConfig
import com.exozet.commonutils.sample.MainApplication
import com.exozet.commonutils.sample.R
import com.exozet.commonutils.sample.storage.LocalUser
import com.exozet.commonutils.sample.ui.markdown.MarkdownFragment
import com.exozet.commonutils.sample.ui.markdown.RawOutputFragment
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import net.kibotu.android.deviceinfo.library.ViewHelper
import net.kibotu.android.deviceinfo.library.memory.Ram
import net.kibotu.android.deviceinfo.library.memory.RamUsage
import net.kibotu.android.deviceinfo.library.misc.UpdateTimer

/**
 * Created by armando.shkurti on 24/10/17.
 */
class DebugMenu {
    private var drawer: Drawer? = null

    private var ramUsage: RamUsage? = null

    init {

        if (ContextHelper.getContext()!!.resources.getBoolean(R.bool.enable_debug_menu))
            setupDebugMenu()
    }

    private fun setupDebugMenu() {

        drawer = DrawerBuilder()
                .withTranslucentStatusBar(true)
                .withActivity(ContextHelper.getActivity()!!)
                .addDrawerItems(*createDebugMenuItems())
                .withOnDrawerItemClickListener { view, position, iDrawerItem -> this.onDrawerItemClicked(view, position, iDrawerItem) }
                .build()
    }

    private fun createDebugMenuItems(): Array<IDrawerItem<*, *>> {
        return arrayOf(

                SectionDrawerItem().withName(ResourceExtensions.getString(R.string.app_name) + " - " + StringExtensions.capitalize(BuildConfig.BUILD_TYPE.toLowerCase()) + " Menu").withIdentifier(R.string.section.toLong()).withDivider(false),

                DividerDrawerItem(),

                SecondaryDrawerItem().withIdentifier(R.string.screen_markdown_readme.toLong()).withName(BuildConfig.CANONICAL_VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")"), SecondaryDrawerItem().withName(R.string.screen_markdown_changelog).withIdentifier(R.string.screen_markdown_changelog.toLong()),

                DividerDrawerItem(),

                ExpandableDrawerItem().withName("App").withIdentifier(R.string.section.toLong()).withIsExpanded(true)
                        .withSubItems(SecondaryDrawerItem().withName(R.string.debug_build_info).withIdentifier(R.string.debug_build_info.toLong()))
                        .withSubItems(SecondaryDrawerItem().withName(R.string.restart_app).withIdentifier(R.string.restart_app.toLong()))
                        .withSubItems(SecondaryDrawerItem().withName(R.string.reset_app).withIdentifier(R.string.reset_app.toLong())),

                DividerDrawerItem(),

                ExpandableDrawerItem().withName("Debug").withIdentifier(R.string.section.toLong()).withIsExpanded(false)
                        .withSubItems(SecondaryDrawerItem().withName(R.string.debug_ram).withIdentifier(R.string.debug_ram.toLong()))
                        .withSubItems(SecondaryDrawerItem().withName(R.string.debug_current_threads).withIdentifier(R.string.debug_current_threads.toLong()))
                        .withSubItems(SecondaryDrawerItem().withName(R.string.debug_current_backstack).withIdentifier(R.string.debug_current_backstack.toLong()))
                        .withSubItems(SecondaryDrawerItem().withName(R.string.debug_current_fragment).withIdentifier(R.string.debug_current_fragment.toLong())),

                DividerDrawerItem(),

                ExpandableDrawerItem().withName("Language").withIdentifier(R.string.section.toLong()).withIsExpanded(false)
                        .withSubItems(SecondaryDrawerItem().withIdentifier(R.string.language_default.toLong()).withName(R.string.language_default)),


                ExpandableDrawerItem().withName("Screens").withIdentifier(R.string.section.toLong()).withIsExpanded(true)
//                        .withSubItems(SecondaryDrawerItem().withIdentifier(R.string.screen_camera.toLong()).withName(R.string.screen_camera))
//                        .withSubItems(SecondaryDrawerItem().withIdentifier(R.string.screen_login.toLong()).withName(R.string.screen_login))
                        .withSubItems(SecondaryDrawerItem().withIdentifier(R.string.screen_home.toLong()).withName(R.string.screen_home))
                        .withSubItems(SecondaryDrawerItem().withIdentifier(R.string.screen_start.toLong()).withName(R.string.screen_start))
                        .withSubItems(SecondaryDrawerItem().withIdentifier(R.string.screen_message.toLong()).withName(R.string.screen_message))
                        .withSubItems(SecondaryDrawerItem().withIdentifier(R.string.screen_history.toLong()).withName(R.string.screen_history))
                        .withSubItems(SecondaryDrawerItem().withIdentifier(R.string.screen_documents.toLong()).withName(R.string.screen_documents))
                        .withSubItems(SecondaryDrawerItem().withIdentifier(R.string.screen_user.toLong()).withName(R.string.screen_user)))
    }


    private fun onDrawerItemClicked(view: View, position: Int, drawerItem: IDrawerItem<*, *>): Boolean {


        val identifier = drawerItem.identifier.toInt()
        when (identifier) {

        // app commands

            R.string.reset_app -> {
                resetApp()
            }

        // app commands
            R.string.debug_ram -> {

                if (ramUsage != null) {
                    ramUsage!!.stop()
                    ramUsage = null
                }

                ramUsage = RamUsage()
                ramUsage!!.addObserver(object : UpdateTimer.UpdateListener<Ram>() {

                    override fun update(ram: Ram) {
                        Logger.v(TAG, String.format(
                                "[Available=%s Free=%s Total=%s Used=%s]",
                                ViewHelper.formatBytes(ram.availableInBytes),
                                ViewHelper.formatBytes(ram.freeInBytes),
                                ViewHelper.formatBytes(ram.totalInBytes),
                                ViewHelper.formatBytes(ram.usedInBytes)
                        ))
                    }
                }).setUpdateInterval(10000).start()
            }

            R.string.debug_current_backstack -> FragmentExtensions.printBackStack()
            R.string.debug_current_fragment -> Logger.v(TAG, "Current Fragment " + FragmentExtensions.currentFragment())
            R.string.debug_build_info -> {
                val info = MainApplication.createInfo(ContextHelper.getContext()!!)
                FragmentExtensions.replaceToBackStackByFading<BaseFragment>(RawOutputFragment().setArgument(Bundler().putString(RawOutputFragment.RAW_OUTPUT_TEXT, GsonProvider.getGsonPrettyPrinting().toJson(info)).get()))
            }
            R.string.debug_current_threads -> for ((key, value) in Thread.getAllStackTraces()) {
                for (element in value)
                    Logger.v(TAG, key.toString() + " -> " + element)
            }

        // language
            R.string.language_default -> LocalUser.switchToDefault()

        // screens
            R.string.screen_markdown_readme -> FragmentExtensions.replaceToBackStackByFading<BaseFragment>(MarkdownFragment().setArgument(Bundler().putString(MarkdownFragment.MARKDOWN_FILENAME, "README.md").get()))

            R.string.screen_markdown_changelog -> FragmentExtensions.replaceToBackStackByFading<BaseFragment>(MarkdownFragment().setArgument(Bundler().putString(MarkdownFragment.MARKDOWN_FILENAME, "CHANGELOG.md").get()))

           // R.string.screen_camera -> (ContextHelper.getActivity() as MainActivity).openCameraAskingPermission()

            //R.string.screen_login -> (ContextHelper.getActivity() as MainActivity).openLoginFragment()

            //R.string.screen_home -> FragmentExtensions.replaceToBackStackByFading(HomeFragment())

//            R.string.screen_start -> FragmentExtensions.replaceToBackStackByFading(StartFragment())

//            R.string.screen_message -> replaceToBackStackByFading(MessageFragment())
//
//            R.string.screen_history -> replaceToBackStackByFading(HistoryFragment())
//
//            R.string.screen_documents -> replaceToBackStackByFading(DocumentsFragment())

            //R.string.screen_user -> FragmentExtensions.replaceToBackStackByFading(UserFragment())

        }

        if (identifier != R.string.section)
            drawer!!.closeDrawer()

        return false

    }

    val isDrawerOpen: Boolean
        get() = drawer != null && drawer!!.isDrawerOpen

    fun closeDrawer() {
        if (drawer != null)
            drawer!!.closeDrawer()
    }

    companion object {

        val DEBUG = "SHOW_DEBUG_LOGS"

        private val TAG = DebugMenu::class.java.simpleName

        fun createDebugArguments(): Bundle {
            return Bundler().putBoolean(DEBUG, true).get()
        }

        fun isDebug(bundle: Bundle?): Boolean {
            return bundle != null && bundle.getBoolean(DEBUG)
        }

        fun isDebug(fragment: Fragment): Boolean {
            return isDebug(fragment.arguments)
        }

        fun resetApp() {
            LocalUser.clear()
            val application = ContextHelper.getApplication()
        }


    }
}