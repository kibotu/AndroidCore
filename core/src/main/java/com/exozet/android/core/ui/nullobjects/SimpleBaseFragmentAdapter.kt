package com.exozet.android.core.ui.nullobjects

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SimpleBaseFragmentAdapter(fm: FragmentManager, val size: Int, fragmentProvider: () -> Fragment) : FragmentStatePagerAdapter(fm) {

    val fragments: List<Fragment> = (0 until size).map { fragmentProvider() }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = size
}