package com.ellison.jetpackdemo.cameraX.analysis

import android.graphics.Rect
import com.ellison.jetpackdemo.cameraX.utils.Constants

data class AnalysisResult(val content: String, val zoomScale: Double, val rect: Rect)

val defaultResult = AnalysisResult("", Constants.DEFAULT_ZOOM_SCALE, Rect())