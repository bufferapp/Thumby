package org.buffer.android.thumby

import android.content.Context
import android.util.DisplayMetrics

object DisplayMetricsUtil {

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}