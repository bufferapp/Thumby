package org.buffer.android.thumby

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.support.v4.util.LongSparseArray
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
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
        title = getString(R.string.picker_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mHeightView = resources.getDimensionPixelOffset(R.dimen.frames_video_height)

        val uri = intent.getParcelableExtra("URI") as Uri
        getBitmap(uri)

        view_thumbnail.setVideoURI(uri)
        view_thumbnail.seekTo(0)

        view_thumbnail.setOnPreparedListener { thumbnail_seekbar.max = 100 }

        thumbnail_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                view_thumbnail.seekTo((progress * view_thumbnail.duration) / 100)
                thumbnail_seekbar.setCurrentFrame(mBitmapList!![progress.toLong()]!!.bitmap)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        thumbnail_seekbar.bringToFront()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finishWithData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun finishWithData() {
        val intent = Intent()
        intent.putExtra("editTextValue", view_thumbnail.currentPosition)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun getBitmap(uri: Uri): LongSparseArray<Thumbnail> {
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

        val thumbList = LongSparseArray<Thumbnail>()
        val thumbCount = 7
        val positionGap = numThumbs / thumbCount

        for (i in 0 until thumbCount) {
            val position = if (i == 0) {
                0
            } else {
                i * positionGap
            }
            thumbList.put(i.toLong(), Thumbnail(i.toLong(), mBitmapList!![position.toLong()]!!.bitmap))
        }


        thumbnail.mBitmapList = thumbList
        thumbnail_seekbar.setCurrentFrame(thumbnailList[0]!!.bitmap)
        return thumbnailList
    }


}