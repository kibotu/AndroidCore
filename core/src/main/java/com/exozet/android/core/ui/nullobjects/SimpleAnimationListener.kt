package com.exozet.android.core.ui.nullobjects

import android.view.animation.Animation

open class SimpleAnimationListener : Animation.AnimationListener {

    override fun onAnimationStart(animation: Animation?) {
    }

    override fun onAnimationEnd(animation: Animation?) {
    }

    override fun onAnimationRepeat(animation: Animation?) {
    }
}

fun Animation.onAnimationStart(block: (Animation?) -> Unit) = setAnimationListener(object : SimpleAnimationListener() {
    override fun onAnimationStart(animation: Animation?) = block(animation)
})

fun Animation.onAnimationEnd(block: (Animation?) -> Unit) = setAnimationListener(object : SimpleAnimationListener() {
    override fun onAnimationEnd(animation: Animation?) = block(animation)
})

fun Animation.onAnimationRepeat(block: (Animation?) -> Unit) = setAnimationListener(object : SimpleAnimationListener() {
    override fun onAnimationRepeat(animation: Animation?) = block(animation)
})