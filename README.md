# Thumby
Thumby is an easy-to-use, drop in video thumbnail picker for android.

![Thumby](thumby.gif)

# Using Thumby

As shown in the sample, you can launch thumby and pass in the desired uri like so:

    startActivityForResult(ThumbyActivity.getStartIntent(this, someUri), RESULT_CODE_PICK_THUMBNAIL)

Once a thumbnail is selected, you can make use of the data that thumby returns. This is both the Uri of the video and the location of the thumbnail. You can then use this data to retrieve the frame for the given video, this can be done using a Thumby utility class:

    val imageUri = data?.getParcelableExtra(EXTRA_URI) as Uri
    val location = data.getLongExtra(EXTRA_THUMBNAIL, 0)
    val bitmap = ThumbyUtils.getBitmapAtFrame(this, imageUri, location, someWidth, someHeight)
