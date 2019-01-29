package com.exozet.android.core.ui.nullobjects

import com.google.android.material.tabs.TabLayout

open class SimpleTabSelectionListener : TabLayout.OnTabSelectedListener {

    override fun onTabSelected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }
}

fun TabLayout.onTabSelected(block: (TabLayout.Tab?) -> Unit) = addOnTabSelectedListener(object : SimpleTabSelectionListener() {
    override fun onTabSelected(tab: TabLayout.Tab?) = block(tab)
})

fun TabLayout.onTabUnselected(block: (TabLayout.Tab?) -> Unit) = addOnTabSelectedListener(object : SimpleTabSelectionListener() {
    override fun onTabUnselected(tab: TabLayout.Tab?) = block(tab)
})

fun TabLayout.onTabReselected(block: (TabLayout.Tab?) -> Unit) = addOnTabSelectedListener(object : SimpleTabSelectionListener() {
    override fun onTabReselected(tab: TabLayout.Tab?) = block(tab)
})