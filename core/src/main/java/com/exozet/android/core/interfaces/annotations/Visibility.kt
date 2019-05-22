package com.exozet.android.core.interfaces.annotations


import android.view.View.*
import androidx.annotation.IntDef

@IntDef(VISIBLE, INVISIBLE, GONE)
@Retention(AnnotationRetention.SOURCE)
annotation class Visibility