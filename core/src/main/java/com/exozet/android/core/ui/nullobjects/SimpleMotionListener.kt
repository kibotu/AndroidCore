package com.exozet.android.core.ui.nullobjects

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import io.reactivex.Observable

/**
 * https://developer.android.com/reference/android/support/constraint/motion/MotionLayout.TransitionListener
 */
open class SimpleMotionListener : MotionLayout.TransitionListener {

    override fun onTransitionTrigger(motionLayout: MotionLayout, trigger: Int, positive: Boolean, progress: Float) {
    }

    override fun allowsTransition(transition: MotionScene.Transition): Boolean {
        return true
    }

    override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {
    }

    override fun onTransitionChange(motionLayout: MotionLayout, startId: Int, endId: Int, progress: Float) {
    }

    override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
    }
}

fun MotionLayout.onCollapseStateChanged(): Observable<Float> {

    var listener: MotionLayout.TransitionListener?

    return io.reactivex.subjects.BehaviorSubject.create<Float> { emitter ->

        if (emitter.isDisposed)
            return@create

        listener = object : SimpleMotionListener() {
            override fun onTransitionChange(motionLayout: MotionLayout, startId: Int, endId: Int, progress: Float) {
                emitter.onNext(progress)
            }
        }

        setTransitionListener(listener)

    }.doOnDispose {
        setTransitionListener(null)
    }.distinctUntilChanged()
}
