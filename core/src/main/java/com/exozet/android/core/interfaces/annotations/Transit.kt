package com.exozet.android.core.interfaces.annotations


import androidx.annotation.IntDef


import androidx.fragment.app.FragmentTransaction.*

@IntDef(TRANSIT_NONE, TRANSIT_FRAGMENT_OPEN, TRANSIT_FRAGMENT_CLOSE, TRANSIT_FRAGMENT_FADE)
@Retention(AnnotationRetention.SOURCE)
annotation class Transit