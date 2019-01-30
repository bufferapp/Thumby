package org.buffer.android.thumby

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView

class ThumbnailSeekView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ImageView(context, attrs) {

    var min = 0
    var max = 0

    init {
        layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        isFocusable = true
        isFocusableInTouchMode = true
    }

    fun setCurrentFrame(bitmap: Bitmap) {
        setImageBitmap(addWhiteBorderToBitmap(bitmap, 8))
    }

    private fun addWhiteBorderToBitmap(bmp: Bitmap, borderSize: Int): Bitmap {
        val bmpWithBorder = Bitmap.createBitmap(bmp.width, bmp.height, bmp.config)
        val canvas = Canvas(bmpWithBorder)
        canvas.drawBitmap(bmp, 0f, 0f, null)

        val paint = Paint()
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderSize.toFloat()
        paint.isAntiAlias = true

        val borderRect = Rect(
            borderSize / 2,
            borderSize / 2,
            canvas.width - borderSize / 2,
            canvas.height - borderSize / 2
        )

        canvas.drawRect(borderRect,paint)
        return bmpWithBorder
    }


}