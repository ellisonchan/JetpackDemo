package com.ellison.jetpackdemo.cameraX.analysis

import android.content.Context
import android.graphics.Rect
import android.util.Log
import androidx.camera.core.ImageProxy
import com.ellison.jetpackdemo.cameraX.utils.Constants
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer

class ZXingAnalysis(): RealTimeAnalysis {
    private val multiFormatReader = MultiFormatReader()

    override fun analyzeContent(imageProxy: ImageProxy, context: Context): AnalysisResult {
        val byteBuffer = imageProxy.planes[0].buffer
        val data = ByteArray(byteBuffer.remaining())
        byteBuffer[data]

        val width = imageProxy.width
        val height = imageProxy.height
        val source = PlanarYUVLuminanceSource(
            data, width, height, 0, 0, width, height, false
        )

        val bitmap = BinaryBitmap(HybridBinarizer(source))

        var result = ""
        try {
            result = multiFormatReader.decode(bitmap).text
            Log.d("Camera", "result:$result")
        } catch (e: Exception) {
            Log.e("Camera", "Error decoding barcode")
        }

        imageProxy.close()
        return AnalysisResult(result, Constants.DEFAULT_ZOOM_SCALE, Rect())
    }
}