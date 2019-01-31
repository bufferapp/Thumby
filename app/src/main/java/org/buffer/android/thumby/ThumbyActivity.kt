package org.buffer.android.thumby

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.support.v4.util.LongSparseArray
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_thumby.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThumbyActivity : AppCompatActivity() {

    companion object {

        private const val THUMBNAIL_COUNT = 20
        const val EXTRA_THUMBNAIL = "org.buffer.android.thumby.EXTRA_THUMBNAIL"
        const val EXTRA_URI = "org.buffer.android.thumby.EXTRA_URI"

        fun getStartIntent(context: Context, uri: Uri): Intent {
            val intent = Intent(context, ThumbyActivity::class.java)
            intent.putExtra(EXTRA_URI, uri)
            return intent
        }
    }

    private var bitmapSize: Int = 0
    private var videoBitmaps = LongSparseArray<Thumbnail>()
    private lateinit var videoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thumby)
        title = getString(R.string.picker_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bitmapSize = resources.getDimensionPixelOffset(R.dimen.frames_video_height)
        videoUri = intent.getParcelableExtra(EXTRA_URI) as Uri
        setupSeekBar()
        setupVideoContent()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.thumbnail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_menu_done -> {
                finishWithData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupVideoContent() {
        view_thumbnail.setDataSource(this, videoUri)

        CoroutineScope(Dispatchers.Main).launch {
            videoBitmaps = withContext(Dispatchers.Default) {
                getBitmap(videoUri)
            }
            progress.visibility = View.GONE
            thumbnail_seekbar.visibility = View.VISIBLE
            thumbnails.visibility = View.VISIBLE
            addVideoThumbnailsToSeekBar(videoBitmaps)
        }
    }

    private fun setupSeekBar() {
        thumbnail_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                handleSeekBar(seekBar)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun addVideoThumbnailsToSeekBar(bitmaps: LongSparseArray<Thumbnail>) {
        val timelineThumbnailCount = 7
        val positionGap = THUMBNAIL_COUNT / timelineThumbnailCount

        for (i in 0 until timelineThumbnailCount - 1) {
            val position = if (i == 0) {
                0
            } else {
                i * positionGap
            }
            thumbnails.addView(ThumbnailView(this@ThumbyActivity, thumbnail = bitmaps[position.toLong()]))
        }
        thumbnails.addView(ThumbnailView(this@ThumbyActivity, thumbnail = bitmaps[(bitmaps.size() - 1).toLong()]))
        thumbnail_seekbar.thumbnail = bitmaps[0]
    }

    private fun handleSeekBar(seekBar: SeekBar?) {
        seekBar?.let {
            view_thumbnail.seekTo((it.progress * view_thumbnail.getDuration()) / 100)
            thumbnail_seekbar.thumbnail = videoBitmaps[it.progress.toLong() / 5]
        }
    }

    private fun finishWithData() {
        val intent = Intent()
        intent.putExtra(EXTRA_THUMBNAIL, thumbnail_seekbar.thumbnail?.videoLocation)
        intent.putExtra(EXTRA_URI, videoUri)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun getBitmap(uri: Uri): LongSparseArray<Thumbnail> {
        val thumbnails = LongSparseArray<Thumbnail>()
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(this, uri)

        val videoLength = mediaMetadataRetriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_DURATION
        ).toInt() * 1000
        val numThumbs = THUMBNAIL_COUNT

        val interval = videoLength / numThumbs

        for (i in 0 until numThumbs - 1) {
            val frameTime = (i * interval).toLong()
            val bitmap = ThumbyUtils.getBitmapAtFrame(this, uri, frameTime, bitmapSize, bitmapSize)
            thumbnails.put(i.toLong(), Thumbnail(frameTime, bitmap))
        }
        mediaMetadataRetriever.release()
        return thumbnails
    }
}