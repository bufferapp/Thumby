package org.buffer.android.thumby.sample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import org.buffer.android.thumby.ThumbyActivity
import org.buffer.android.thumby.ThumbyActivity.Companion.EXTRA_THUMBNAIL_POSITION
import org.buffer.android.thumby.ThumbyActivity.Companion.EXTRA_URI
import org.buffer.android.thumby.util.ThumbyUtils

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_PICK_MEDIA = 8080
    private val REQUEST_CODE_PICK_THUMBNAIL = 8081

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent()
        intent.type = "video/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select video"
            ), REQUEST_CODE_PICK_MEDIA
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_MEDIA) {
                data?.data?.let {
                    startActivityForResult(ThumbyActivity.getStartIntent(this, it), REQUEST_CODE_PICK_THUMBNAIL)
                }
            } else if (requestCode == REQUEST_CODE_PICK_THUMBNAIL) {
                val imageUri = data?.getParcelableExtra(EXTRA_URI) as Uri
                val location = data.getLongExtra(EXTRA_THUMBNAIL_POSITION, 0)
                val bitmap = ThumbyUtils.getBitmapAtFrame(this, imageUri, location,
                    200, 200)
                image.setImageBitmap(bitmap)
            }
        }
    }
}
