package org.buffer.android.thumby

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout

class ThumbnailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ImageView(context, attrs) {

    init {
        scaleType = ScaleType.CENTER_CROP
        alpha = 0.4f
        val dimension = resources.getDimensionPixelSize(R.dimen.frames_video_height)
        layoutParams = LinearLayout.LayoutParams(dimension, dimension).apply { weight = 1f }
    }
}