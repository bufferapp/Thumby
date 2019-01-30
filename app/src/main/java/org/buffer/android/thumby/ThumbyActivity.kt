package org.buffer.android.thumby

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.support.v4.util.LongSparseArray
import android.support.v7.app.AppCompatActivity
import android.view.ViewTreeObserver
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_thumby.*

class ThumbyActivity: AppCompatActivity() {

    companion object {
        fun getStartIntent(context: Context, uri: Uri): Intent {
            val intent = Intent(context, ThumbyActivity::class.java)
            intent.putExtra("URI", uri)
            return intent
        }
    }

    private var mHeightView: Int = 0
    var mBitmapList: LongSparseArray<Thumbnail>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thumby)
        mHeightView = resources.getDimensionPixelOffset(R.dimen.frames_video_height)

        val uri = intent.getParcelableExtra("URI") as Uri
        thumbnail.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                getBitmap(thumbnail.width, uri)
                thumbnail.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
     //   thumbnail.seekListener = seekListener
        view_thumbnail.setVideoURI(uri)
        view_thumbnail.seekTo(0)

        view_thumbnail.setOnPreparedListener { thumbnail_seekbar.max = 100 }

        thumbnail_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                view_thumbnail.seekTo(progress)
                thumbnail_seekbar.setCurrentFrame(mBitmapList!![progress.toLong()]!!.bitmap)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        thumbnail_seekbar.bringToFront()
    }

    private fun getBitmap(width: Int, uri: Uri): LongSparseArray<Thumbnail> {
        val thumbnailList = LongSparseArray<Thumbnail>()
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(this, uri)

        // Retrieve media data
        val videoLengthInMs =
            (Integer.parseInt(
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) * 1000).toLong()
        val thumbWidth = mHeightView
        val thumbHeight = mHeightView

        val numThumbs = 101

        val interval = videoLengthInMs / numThumbs

        for (i in 0 until numThumbs) {
            val frameTime = i * interval
            var bitmap = mediaMetadataRetriever.getFrameAtTime(frameTime.toLong(),
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            try {
                bitmap = Bitmap.createScaledBitmap(bitmap, thumbWidth, thumbHeight, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            thumbnailList.put(i.toLong(), Thumbnail(frameTime.toLong(), bitmap))
        }
        mediaMetadataRetriever.release()
        mBitmapList = thumbnailList
        thumbnail.mBitmapList = thumbnailList
        thumbnail_seekbar.setCurrentFrame(thumbnailList[0]!!.bitmap)
        return thumbnailList
    }

    private fun getBitmapForProgress(frameTime: Long): Bitmap {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(this, intent.getParcelableExtra("URI") as Uri)
        var bitmap = mediaMetadataRetriever.getFrameAtTime(frameTime, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        try {
            bitmap = Bitmap.createScaledBitmap(bitmap, mHeightView, mHeightView, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }
}