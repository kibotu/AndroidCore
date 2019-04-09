package com.exozet.android.core.ui.custom

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.exozet.android.core.extensions.resColor
import com.exozet.android.core.math.Line
import com.exozet.android.core.math.Vector2
import com.exozet.android.core.utils.MathExtensions.dpToPx
import java.util.*

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

class LinesView : View {

    private lateinit var paint: Paint
    lateinit var lines: MutableList<Line>

    var strokeColor: Int = 0
    var strokeStrength: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        if (isInEditMode)
            return

        paint = Paint()
        lines = ArrayList()
        strokeColor = android.R.color.black.resColor
        strokeStrength = dpToPx(1)

        paint.color = strokeColor
        paint.strokeWidth = 5f
    }

    fun addLine(line: Line) {
        lines.add(line)

        postInvalidate()
    }

    fun addLine(start: Vector2, end: Vector2) {
        lines.add(Line(start, end))

        postInvalidate()
    }

    fun setLines(vararg l: Line) {
        if (l.isEmpty())
            return
        lines.clear()
        lines.addAll(Arrays.asList(*l))

        postInvalidate()
    }

    public override fun onDraw(canvas: Canvas) {
        if (isInEditMode)
            return
        for (line in lines)
            canvas.drawLine(line.start.x, line.start.y, line.end.x, line.end.y, paint)
    }
}
