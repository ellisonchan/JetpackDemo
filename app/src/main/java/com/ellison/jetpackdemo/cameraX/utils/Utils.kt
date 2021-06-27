package com.ellison.jetpackdemo.cameraX.utils

import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.camera.core.ImageProxy

class Utils {
    companion object {
        // fun convertRectToPoint(proxy: ImageProxy, rect: Rect): Point {
        fun convertRectToPoint(rect: Rect, preview: View): Point {
            Log.d(Constants.TAG_CAMERA_UI, "convertRectToCenter"
                    +" rect:$rect centerX:${rect.centerX()} centerY:${rect.centerY()}"
                    // +" proxy:{width:${proxy.width} height:${proxy.height}}"
                    +" previewView:{width:${preview.width} height:${preview.height}}")
            if (rect.isEmpty) {
                return Point()
            }

            return Point(
                preview.width - rect.centerY(),
                rect.centerX()
            )
        }

        fun convertPointsToCenter(proxy: ImageProxy, points: Array<Point>?, preview: View): Point {
            Log.d(Constants.TAG_CAMERA_UI, "convertPointsToCenter"
                    +" points:${points.contentToString()}"
                    +" proxy:{width:${proxy.width} height:${proxy.height}}"
                    +" previewView:{width:${preview.width} height:${preview.height}}")

            if (points == null || points.isEmpty())
                return Point()

            // Points[0]: right bottom
            // Points[1]: left bottom
            // Points[2]: left up
            // Points[3]: right up

            val xOffset = (points[0].x - points[2].x) / 2
            val yOffset = (points[0].y - points[2].y) / 2

//        val xScaleFactor = binding.previewView.width.toFloat() / proxy.width.toFloat()
//        val yScaleFactor = binding.previewView.height.toFloat() / proxy.height.toFloat()
//        Log.d(Constants.TAG_CAMERA_UI, "xScaleFactor:$xScaleFactor yScaleFactor:$yScaleFactor")

            return Point(preview.width - yOffset - points[2].y, xOffset + points[2].x)
            // return Point(xOffset + points[2].x, yOffset + points[2].y)
            // return Point(yOffset + points[2].y, xOffset + points[2].x)
//        return Point(
//            ((xOffset + points[2].x) * xScaleFactor).toInt(),
//            ((yOffset + points[2].y) * yScaleFactor).toInt()
//        )
        }
    }
}