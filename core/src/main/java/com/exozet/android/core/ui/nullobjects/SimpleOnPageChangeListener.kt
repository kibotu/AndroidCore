package com.exozet.android.core.ui.nullobjects

import androidx.viewpager.widget.ViewPager

open class SimpleOnPageChangeListener : ViewPager.OnPageChangeListener {

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
    }
}

fun ViewPager.onPageScrollStateChanged(block: (Int) -> Unit) = addOnPageChangeListener(object : SimpleOnPageChangeListener() {
    override fun onPageScrollStateChanged(state: Int) = block(state)
})

fun ViewPager.onPageScrolled(block: (Int, Float, Int) -> Unit) = addOnPageChangeListener(object : SimpleOnPageChangeListener() {
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = block(position, positionOffset, positionOffsetPixels)
})

fun ViewPager.onPageSelected(block: (Int) -> Unit) = addOnPageChangeListener(object : SimpleOnPageChangeListener() {
    override fun onPageSelected(position: Int) = block(position)
})