package org.buffer.android.thumby

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.activity_thumby.*
import org.buffer.android.thumby.listener.SeekListener

class ThumbyActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_THUMBNAIL = "org.buffer.android.thumby.EXTRA_THUMBNAIL"
        const val EXTRA_URI = "org.buffer.android.thumby.EXTRA_URI"

        fun getStartIntent(context: Context, uri: Uri): Intent {
            val intent = Intent(context, ThumbyActivity::class.java)
            intent.putExtra(EXTRA_URI, uri)
            return intent
        }
    }

    private lateinit var videoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thumby)
        title = getString(R.string.picker_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        videoUri = intent.getParcelableExtra(EXTRA_URI) as Uri

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
        thumbs.seekListener = seekListener
        thumbs.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                thumbs.viewTreeObserver.removeOnGlobalLayoutListener(this)
                thumbs.uri = videoUri
            }
        })
    }

    private fun finishWithData() {
        val intent = Intent()
        intent.putExtra(EXTRA_THUMBNAIL,
            ((view_thumbnail.getDuration() / 100) * thumbs.currentProgress).toLong() * 1000)
        intent.putExtra(EXTRA_URI, videoUri)
        setResult(RESULT_OK, intent)
        finish()
    }

    private val seekListener = object  : SeekListener {
        override fun onVideoSeeked(percentage: Double) {
            view_thumbnail.seekTo((percentage.toInt() * view_thumbnail.getDuration()) / 100)
        }
    }
}