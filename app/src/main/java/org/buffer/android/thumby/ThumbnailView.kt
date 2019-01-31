package org.buffer.android.thumby

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView

class ThumbnailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    thumbnail: Thumbnail? = null
) : ImageView(context, attrs) {

    init {
        thumbnail?.let { setThumbnail(thumbnail) }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    private fun setThumbnail(thumbnail: Thumbnail) {
        layoutParams = ViewGroup.LayoutParams(thumbnail.bitmap.width, thumbnail.bitmap.height)
        setImageBitmap(thumbnail.bitmap)
        alpha = 0.4f
    }
}