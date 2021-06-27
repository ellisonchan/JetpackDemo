package com.ellison.jetpackdemo.cameraX.analyzer

import android.util.Log
import androidx.camera.core.ImageAnalysis.Analyzer
import androidx.camera.core.ImageProxy
import com.ellison.jetpackdemo.cameraX.utils.Constants
import com.ellison.jetpackdemo.cameraX.viewmodel.CameraViewModel

class MyAnalyzer(
    private val viewModel: CameraViewModel,
    private val callback: AnalyzeCallback
): Analyzer {
    override fun analyze(image: ImageProxy) {
        Log.d(Constants.TAG_CAMERA, "analyze() image:$image")
        viewModel.analysePicture(image).also {
            Log.d(Constants.TAG_CAMERA, "analyze() result:$it")

            if (Constants.DEFAULT_ZOOM_SCALE != it.zoomScale
                && Constants.MIN_ZOOM_SCALE != it.zoomScale
            ) {
                callback.onZoomPreview(it.zoomScale)
            } else {
                callback.onAnalyzeResult(it)
            }
        }
    }
}