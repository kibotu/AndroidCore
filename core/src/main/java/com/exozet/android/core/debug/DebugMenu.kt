package com.exozet.android.core.debug

import android.os.Build
import android.os.StrictMode
import android.view.View
import com.exozet.android.core.R
import com.exozet.android.core.extensions.resBoolean
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import net.kibotu.ContextHelper.getActivity


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

class DebugMenu {

    private var drawer: Drawer? = null

    init {
        if (R.bool.enable_debug_menu.resBoolean)
            setupDebugMenu()
    }

    val isDrawerOpen: Boolean
        get() = drawer?.isDrawerOpen == true

    fun closeDrawer() {
        drawer?.closeDrawer()
    }

    private fun setupDebugMenu() {

        drawer = DrawerBuilder()
            .withTranslucentStatusBar(true)
            .withActivity(getActivity() ?: return)
            .addDrawerItems(*createDebugMenuItems())
            .withOnDrawerItemClickListener { view, position, iDrawerItem -> this.onDrawerItemClicked(view, position, iDrawerItem) }
            .build()
    }

    private fun createDebugMenuItems(): Array<IDrawerItem<*, *>> {
        return arrayOf()
    }

    private fun onDrawerItemClicked(@Suppress("UNUSED_PARAMETER") view: View, @Suppress("UNUSED_PARAMETER") position: Int, drawerItem: IDrawerItem<*, *>): Boolean {

        val identifier = drawerItem.identifier.toInt()
        when (identifier) {
            0 -> {
            }
            else -> {
            }
        }

        if (identifier != R.string.section)
            drawer?.closeDrawer()

        return false
    }

    companion object {

        private val TAG = DebugMenu::class.java.simpleName
    }
}