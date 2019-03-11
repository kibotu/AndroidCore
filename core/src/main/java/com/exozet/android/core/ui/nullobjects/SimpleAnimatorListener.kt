package com.exozet.android.core.ui.nullobjects

import android.animation.Animator

open class SimpleAnimatorListener : Animator.AnimatorListener {

    override fun onAnimationStart(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationRepeat(animation: Animator?) {
    }
}

@Deprecated("use androidktx", ReplaceWith("doOnStart"))
fun Animator.onAnimationStart(block: (Animator?) -> Unit) = addListener(object : SimpleAnimatorListener() {
    override fun onAnimationStart(animation: Animator?) = block(animation)
})

@Deprecated("use androidktx", ReplaceWith("doOnEnd"))
fun Animator.onAnimationEnd(block: (Animator?) -> Unit) = addListener(object : SimpleAnimatorListener() {
    override fun onAnimationEnd(animation: Animator?) = block(animation)
})

@Deprecated("use androidktx", ReplaceWith("doOnCancel"))
fun Animator.onAnimationCancel(block: (Animator?) -> Unit) = addListener(object : SimpleAnimatorListener() {
    override fun onAnimationCancel(animation: Animator?) = block(animation)
})

@Deprecated("use androidktx", ReplaceWith("doOnRepeat"))
fun Animator.onAnimationRepeat(block: (Animator?) -> Unit) = addListener(object : SimpleAnimatorListener() {
    override fun onAnimationRepeat(animation: Animator?) = block(animation)
})