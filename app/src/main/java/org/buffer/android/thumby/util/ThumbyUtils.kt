package org.buffer.android.thumby.util

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore

object ThumbyUtils {

    fun getBitmapAtFrame(
        context: Context,
        uri: Uri,
        frameTime: Long,
        width: Int,
        height: Int
    ): Bitmap {
        val mMMR = MediaMetadataRetriever()
        mMMR.setDataSource(context, uri)
        var bitmap = mMMR.getFrameAtTime(frameTime)
        /*   val mediaMetadataRetriever = MediaMetadataRetriever()
           if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.Q)) {
               mediaMetadataRetriever.setDataSource(getRealPathFromURI(context,uri))
           } else {
               mediaMetadataRetriever.setDataSource(context, uri)
           }
           var bitmap = mediaMetadataRetriever.getFrameAtTime(frameTime,
               MediaMetadataRetriever.OPTION_CLOSEST_SYNC)*/
        try {
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    fun getRealPathFromURI(
        context: Context,
        contentUri: Uri?
    ): String? {
        var cursor: Cursor? = null
        return try {
            val proj =
                arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

}