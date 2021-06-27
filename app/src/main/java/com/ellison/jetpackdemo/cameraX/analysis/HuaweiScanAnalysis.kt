package com.ellison.jetpackdemo.cameraX.analysis

import android.content.Context
import android.graphics.*
import android.util.Log
import androidx.camera.core.ImageProxy
import com.ellison.jetpackdemo.cameraX.utils.Constants
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer


class HuaweiScanAnalysis: RealTimeAnalysis {
    override fun analyzeContent(imageProxy: ImageProxy, context: Context): AnalysisResult {
        val bitmap = proxyToBitmap(imageProxy)
        imageProxy.close()

        val options = HmsScanAnalyzerOptions.Creator()
            .setHmsScanTypes(HmsScan.ALL_SCAN_TYPE) // Indicating all supported barcode formats.
            .setPhotoMode(false)
            .create()

        val result = ScanUtil.decodeWithBitmap(
            context,
            bitmap,
            options
        )

        Log.d(Constants.TAG_CAMERA, "scan kit analyzeContent result:$result")
        val content = if (result != null && result.isNotEmpty() && result[0].originalValue != null)
            result[0].originalValue else ""
        val scale = if (result != null && result.isNotEmpty())
            result[0].getZoomValue() else Constants.DEFAULT_ZOOM_SCALE

        if (result != null && result.isNotEmpty())
            result[0].cornerPoints else null

        if (result != null && result.isNotEmpty()) {
            Log.d(Constants.TAG_CAMERA, "scan kit analyzeContent corner points:${result[0].cornerPoints.contentToString()}")
            Log.d(Constants.TAG_CAMERA, "scan kit analyzeContent border rect:${result[0].borderRect}")
        }

        val rect = if (result != null && result.isNotEmpty())
            result[0].borderRect else Rect()

        // return AnalysisResult(content, scale, points)
        return AnalysisResult(content, scale, rect)
        // return if (result != null && result.isNotEmpty()) result[0].originalValue else ""
    }

    private fun proxyToBitmap(image: ImageProxy): Bitmap {
        Log.d(Constants.TAG_CAMERA, "proxyToBitmap start")
        val planes: Array<ImageProxy.PlaneProxy> = image.planes
        val yBuffer: ByteBuffer = planes[0].buffer
        val uBuffer: ByteBuffer = planes[1].buffer
        val vBuffer: ByteBuffer = planes[2].buffer

        val ySize: Int = yBuffer.remaining()
        val uSize: Int = uBuffer.remaining()
        val vSize: Int = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)
        //U and V are swapped
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage =
            YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 75, out)
        Log.d(Constants.TAG_CAMERA, "proxyToBitmap {width:${yuvImage.width} height:${yuvImage.height}}")

        val imageBytes = out.toByteArray()
        val opt = BitmapFactory.Options()
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888

        var bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size, opt)
        Log.d(Constants.TAG_CAMERA, "proxyToBitmap bitmap {width:${bitmap.width} height:${bitmap.height}}")

//        // Rotate bitmap to 90 degree.
//        val matrix = Matrix()
//        matrix.setRotate(
//            90f,
//            (bitmap.width / 2).toFloat(),
//            (bitmap.height / 2).toFloat()
//        )
//        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//        Log.d(Constants.TAG_CAMERA, "proxyToBitmap rotated {width:${bitmap.width} height:${bitmap.height}}")

        Log.d(Constants.TAG_CAMERA, "proxyToBitmap end")
        return bitmap
    }
}