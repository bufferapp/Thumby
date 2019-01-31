package org.buffer.android.thumby

import android.content.Context
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView

class CenterCropVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextureView(context, attrs), TextureView.SurfaceTextureListener {

    private var mediaPlayer: MediaPlayer? = null

    private var videoHeight = 0f
    private var videoWidth = 0f

    init {
        surfaceTextureListener = this
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onSurfaceTextureSizeChanged(
        surface: SurfaceTexture?,
        width: Int,
        height: Int
    ) { }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) { }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean  = false

    override fun onSurfaceTextureAvailable(
        surface: SurfaceTexture?,
        width: Int,
        height: Int
    ) {
        mediaPlayer?.setSurface(Surface(surfaceTexture))
    }

    fun setDataSource(context: Context, uri: Uri) {
        initPlayer()
        mediaPlayer?.setDataSource(context, uri)
        prepare()
    }

    fun seekTo(milliseconds: Int) {
        mediaPlayer?.seekTo(milliseconds)
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    private fun prepare() {
        mediaPlayer?.setOnVideoSizeChangedListener { mp, width, height ->
            videoWidth = width.toFloat()
            videoHeight = height.toFloat()
            updateTextureViewSize()
            seekTo(0)
        }
        mediaPlayer?.prepareAsync()
    }

    private fun updateTextureViewSize() {
        var scaleX = 1.0f
        var scaleY = 1.0f

        if (videoWidth > width && videoHeight > height) {
            scaleX = videoWidth / width
            scaleY = videoHeight / height
        } else if (videoWidth < width && videoHeight < height) {
            scaleY = width / videoWidth
            scaleX = height / videoHeight
        } else if (width > videoWidth) {
            scaleY = width / videoWidth / (height / videoHeight)
        } else if (height > videoHeight) {
            scaleX = height / videoHeight / (width / videoWidth)
        }

        val matrix = Matrix().apply {
            setScale(scaleX, scaleY, (width / 2).toFloat(), (height / 2).toFloat())
        }

        setTransform(matrix)
    }

    private fun initPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer?.reset()
        }
    }
}