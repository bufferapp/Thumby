package org.buffer.android.thumby

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatSeekBar
import android.util.AttributeSet

class ThumbnailSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatSeekBar(context, attrs) {

    var thumbnail: Thumbnail? = null
    set(value) {
        field = value
        field?.let { setCurrentFrame(it.bitmap) }
    }

    init {
        progressDrawable = ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent))
        background = ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent))
    }

    private fun setCurrentFrame(bitmap: Bitmap) {
        thumb = BitmapDrawable(resources, addWhiteBorderToBitmap(bitmap, 4))
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