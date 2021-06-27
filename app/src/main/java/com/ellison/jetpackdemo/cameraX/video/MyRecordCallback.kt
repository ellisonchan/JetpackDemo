package com.ellison.jetpackdemo.cameraX.video

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.VideoCapture
import com.ellison.jetpackdemo.cameraX.utils.Constants

class MyRecordCallback(
    private val context: Context,
    private val listener: () -> Unit
): VideoCapture.OnVideoSavedCallback {
    override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
        Log.d(
            Constants.TAG_CAMERA, "onVideoSaved outputFileResults:"
                    + outputFileResults.savedUri!!.path
        )
        Toast.makeText(
            context,
            "Video got" + (if (outputFileResults.savedUri != null) " @ " + outputFileResults.savedUri!!
                .path else "")
                    + ".", Toast.LENGTH_LONG
        ).show()
        listener.invoke()
    }

    override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
        Log.d(
            Constants.TAG_CAMERA, "onError videoCaptureError:"
                    + videoCaptureError + " message:" + message, cause
        )
        listener.invoke()
    }
}