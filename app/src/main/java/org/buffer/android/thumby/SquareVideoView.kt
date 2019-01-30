package org.buffer.android.thumby

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView

class SquareVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : VideoView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}