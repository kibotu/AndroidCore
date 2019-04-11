package com.exozet.android.core.debug

import android.view.View
import com.exozet.android.core.R
import com.exozet.android.core.extensions.onTrue
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

    val isDrawerOpen
        get() = drawer?.isDrawerOpen == true

    fun closeDrawer() = drawer?.closeDrawer()

    fun build() = R.bool.enable_debug_menu.resBoolean.onTrue {
        drawer = DrawerBuilder()
            .withTranslucentStatusBar(false)
            .withActivity(getActivity()!!)
            .addDrawerItems(*createDebugMenuItems())
            .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean = onDrawerItemClicked(view, position, drawerItem)
            })
            .build()
    }

    private fun createDebugMenuItems(): Array<out IDrawerItem<*>> = arrayOf(
    )

    private fun onDrawerItemClicked(@Suppress("UNUSED_PARAMETER") view: View?, @Suppress("UNUSED_PARAMETER") position: Int, @Suppress("UNUSED_PARAMETER") drawerItem: IDrawerItem<*>): Boolean {

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
}