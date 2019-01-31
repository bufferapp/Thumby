package org.buffer.android.thumby

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.util.LongSparseArray
import android.util.AttributeSet
import android.view.View

class ThumbnailTimeline @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var mHeightView: Int = 0
    var mBitmapList: LongSparseArray<Thumbnail>? = null
    set(value) {
        field = value
        invalidate()
    }
    init {
        mHeightView = context.resources.getDimensionPixelOffset(R.dimen.frames_video_height)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minW = paddingLeft + paddingRight + suggestedMinimumWidth
        val w = View.resolveSizeAndState(minW, widthMeasureSpec, 1)

        val minH = paddingBottom + paddingTop + mHeightView
        val h = View.resolveSizeAndState(minH, heightMeasureSpec, 1)

        setMeasuredDimension(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mBitmapList != null) {
            canvas.save()
            var x = 0
            for (i in 0 until mBitmapList!!.size()) {
                val bitmap = mBitmapList!!.get(i.toLong())

                if (bitmap != null) {
                    val paint = Paint()
                    paint.alpha = 120
                    canvas.drawBitmap(bitmap.bitmap, x.toFloat(), 0F, paint)
                    x += bitmap.bitmap.width
                }
            }
        }

    }



}