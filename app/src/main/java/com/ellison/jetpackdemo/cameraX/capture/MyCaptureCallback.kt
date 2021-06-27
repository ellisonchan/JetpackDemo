package com.ellison.jetpackdemo.cameraX.capture

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import com.ellison.jetpackdemo.cameraX.utils.Constants

class MyCaptureCallback(
    private val picCount: Int,
    private val context: Context
): ImageCapture.OnImageSavedCallback {
    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
        val uri = outputFileResults.savedUri
        Log.d(
            Constants.TAG_CAMERA, "outputFileResults:$uri picCount:$picCount"
        )

        val path = if (uri != null) (" @ " + uri.path) else "none"

        Toast.makeText(
            context, "Picture got:$path.", Toast.LENGTH_SHORT
        ).show()
    }

    override fun onError(exception: ImageCaptureException) {
        Log.d(Constants.TAG_CAMERA, "onError:${exception.imageCaptureError}")
    }
}