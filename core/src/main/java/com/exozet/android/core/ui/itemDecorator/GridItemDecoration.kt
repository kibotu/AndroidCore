package com.exozet.android.core.ui.itemDecorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/***
 * Made by Lokesh Desai (Android4Dev)
 * https://www.android4dev.com/how-to-use-recyclerview-with-gridlayoutmanager-android/
 */
class GridItemDecoration(private val gridSpacing: Int, private val gridSize: Int) : RecyclerView.ItemDecoration() {

    private var needLeftSpacing = false

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val frameWidth = ((parent.width - gridSpacing.toFloat() * (gridSize - 1)) / gridSize).toInt()
        val padding = parent.width / gridSize - frameWidth
        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        if (itemPosition < gridSize) {
            outRect.top = 0
        } else {
            outRect.top = gridSpacing
        }
        if (itemPosition % gridSize == 0) {
            outRect.left = 0
            outRect.right = padding
            needLeftSpacing = true
        } else if ((itemPosition + 1) % gridSize == 0) {
            needLeftSpacing = false
            outRect.right = 0
            outRect.left = padding
        } else if (needLeftSpacing) {
            needLeftSpacing = false
            outRect.left = gridSpacing - padding
            if ((itemPosition + 2) % gridSize == 0) {
                outRect.right = gridSpacing - padding
            } else {
                outRect.right = gridSpacing / 2
            }
        } else if ((itemPosition + 2) % gridSize == 0) {
            needLeftSpacing = false
            outRect.left = gridSpacing / 2
            outRect.right = gridSpacing - padding
        } else {
            needLeftSpacing = false
            outRect.left = gridSpacing / 2
            outRect.right = gridSpacing / 2
        }
        outRect.bottom = 0
    }
}