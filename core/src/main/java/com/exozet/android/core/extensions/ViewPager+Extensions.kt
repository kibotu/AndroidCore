@file:JvmName("ViewPagerExtensions")


package com.exozet.android.core.extensions

import androidx.viewpager.widget.ViewPager

fun ViewPager.scrollRight(pages: Int = 1) {
    setCurrentItem(currentItem.plus(pages).clamp(0, (adapter?.count ?: 0) - 1), true)
}

fun ViewPager.scrollLeft(pages: Int = 1) {
    setCurrentItem(currentItem.minus(pages).clamp(0, (adapter?.count ?: 0) - 1), true)
}