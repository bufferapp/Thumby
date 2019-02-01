package org.buffer.android.thumby.sample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.buffer.android.thumby.ThumbyActivity
import org.buffer.android.thumby.ThumbyActivity.Companion.EXTRA_THUMBNAIL
import org.buffer.android.thumby.ThumbyActivity.Companion.EXTRA_URI
import org.buffer.android.thumby.util.ThumbyUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent()
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select video"
            ), 1111
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1111) {
                val mImageUri = data?.data
                startActivityForResult(ThumbyActivity.getStartIntent(this, mImageUri!!),
                    9999)
            } else if (requestCode == 9999) {
                val imageUri = data?.getParcelableExtra(EXTRA_URI) as Uri
                val location = data.getLongExtra(EXTRA_THUMBNAIL, 0)
                val bitmap = ThumbyUtils.getBitmapAtFrame(this, imageUri, location,
                    200, 200)
                image.setImageBitmap(bitmap)
            }
        }
    }
}
