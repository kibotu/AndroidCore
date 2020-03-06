package com.exozet.android.core.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import com.exozet.android.core.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapeAppearancePathProvider
import net.kibotu.resourceextension.dp


/** A Card view that clips the content of any shape, this should be done upstream in card,
 * working around it for now.
 */
open class CustomCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyle) {

    private val pathProvider = ShapeAppearancePathProvider()

    private val path: Path = Path()

    // context, attrs, defStyle, R.style.Widget_MaterialComponents_CardView
    private val shapeAppearance: ShapeAppearanceModel = ShapeAppearanceModel
        .Builder()
        .setTopLeftCorner(CornerFamily.ROUNDED, 6f.dp)
        .setTopRightCorner(CornerFamily.ROUNDED, 6f.dp)
        .build()

    private val rectF = RectF(0f, 0f, 0f, 0f)

    override fun onDraw(canvas: Canvas?) {
        rectF.right = width.toFloat()
        rectF.bottom = height.toFloat()
        pathProvider.calculatePath(shapeAppearance, 1f, rectF, path)
        canvas!!.clipPath(path)
        super.onDraw(canvas)
    }
}