package com.exozet.android.core.ui.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff.Mode
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.exozet.android.core.R
import com.exozet.android.core.extensions.waitForLayout
import net.kibotu.logger.Logger.d
import net.kibotu.resourceextension.resDrawable


class MaskableFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var looper = Handler()

    private var drawableMask: Drawable? = null

    private var finalMask: Bitmap? = null

    private lateinit var paint: Paint

    private var porterDuffXferMode: PorterDuffXfermode? = null

    var mode: Mode? = null
        set(value) {
            field = value
            porterDuffXferMode = PorterDuffXfermode(value ?: return)
        }

    var antiAliasing: Boolean = false
        set(value) {
            field = value
            paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                isAntiAlias = antiAliasing
                xfermode = porterDuffXferMode
            }
        }

    init {
        isDrawingCacheEnabled = true
        setLayerType(View.LAYER_TYPE_SOFTWARE, null) // Only works for software layers

        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MaskableLayout,
            defStyleAttr,
            defStyleRes
        )

        try {
            drawableMask = a.getResourceId(R.styleable.MaskableLayout_mask, -1).resDrawable
            if (drawableMask is AnimationDrawable) {
                drawableMask!!.callback = this
            }

            porterDuffXferMode = getModeFromInteger(a.getInteger(R.styleable.MaskableLayout_porterDuffXfermode, DST_IN))

            antiAliasing = a.getBoolean(R.styleable.MaskableLayout_anti_aliasing, false)

        } finally {
            a.recycle()
        }

        registerMeasure()
    }

    private fun makeBitmapMask(drawable: Drawable?): Bitmap? {
        if (drawable != null) {
            return if (measuredWidth > 0 && measuredHeight > 0) {
                val mask: Bitmap? = Bitmap.createBitmap(
                    measuredWidth, measuredHeight,
                    Config.ARGB_8888
                )
                val canvas = Canvas(mask!!)
                drawable.setBounds(0, 0, measuredWidth, measuredHeight)
                drawable.draw(canvas)
                mask
            } else {
                log("Can't create a mask with height 0 or width 0. Or the layout has no children and is wrap content")
                null
            }
        } else {
            log("No bitmap mask loaded, view will NOT be masked !")
        }
        return null
    }

    fun setMask(drawable: Drawable?) {
        drawableMask = drawable
        swapBitmapMask(makeBitmapMask(drawableMask))
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setSize(w, h)
    }

    private fun setSize(width: Int, height: Int) {
        if (width > 0 && height > 0) {
            if (drawableMask != null) {
                //Remake the 9patch

                swapBitmapMask(makeBitmapMask(drawableMask))
            }
        } else {
            log("Width and height must be higher than 0")
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (finalMask != null) {
            paint.xfermode = porterDuffXferMode
            canvas.drawBitmap(finalMask!!, 0.0f, 0.0f, paint)
            paint.xfermode = null
        } else {
            log("Mask or paint is null ...")
        }
    }

    /**
     * Once inflated we have no height or width for the mask. Wait for the layout.
     */
    private fun registerMeasure() = waitForLayout {
        swapBitmapMask(makeBitmapMask(drawableMask))
    }

    override fun invalidateDrawable(drawable: Drawable) {
        drawableMask = drawable
        swapBitmapMask(makeBitmapMask(drawable))
        invalidate()
    }

    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
        looper.postAtTime(what, `when`)
    }

    override fun unscheduleDrawable(who: Drawable, what: Runnable) {
        looper.removeCallbacks(what)
    }

    private fun swapBitmapMask(newMask: Bitmap?) {
        if (newMask == null)
            return

        if (finalMask?.isRecycled == false) {
            finalMask!!.recycle()
        }
        finalMask = newMask
    }

    private fun getModeFromInteger(index: Int): PorterDuffXfermode = PorterDuffXfermode(
        when (index) {
            ADD -> Mode.ADD
            CLEAR -> Mode.CLEAR
            DARKEN -> Mode.DARKEN
            DST -> Mode.DST
            DST_ATOP -> Mode.DST_ATOP
            DST_IN -> Mode.DST_IN
            DST_OUT -> Mode.DST_OUT
            DST_OVER -> Mode.DST_OVER
            LIGHTEN -> Mode.LIGHTEN
            MULTIPLY -> Mode.MULTIPLY
            OVERLAY -> Mode.OVERLAY
            SCREEN -> Mode.SCREEN
            SRC -> Mode.SRC
            SRC_ATOP -> Mode.SRC_ATOP
            SRC_IN -> Mode.SRC_IN
            SRC_OUT -> Mode.SRC_OUT
            SRC_OVER -> Mode.SRC_OVER
            XOR -> Mode.XOR
            else -> Mode.DST_IN
        }
    )

    private companion object {
        const val ADD = 0
        const val CLEAR = 1
        const val DARKEN = 2
        const val DST = 3
        const val DST_ATOP = 4
        const val DST_IN = 5
        const val DST_OUT = 6
        const val DST_OVER = 7
        const val LIGHTEN = 8
        const val MULTIPLY = 9
        const val OVERLAY = 10
        const val SCREEN = 11
        const val SRC = 12
        const val SRC_ATOP = 13
        const val SRC_IN = 14
        const val SRC_OUT = 15
        const val SRC_OVER = 16
        const val XOR = 17
    }

    var enableLogging = false

    //Logging
    private fun log(message: String) {
        if (enableLogging)
            d(message)
    }
}