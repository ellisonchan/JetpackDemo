package com.ellison.jetpackdemo.cameraX.analysis

import android.content.Context
import androidx.camera.core.ImageProxy

interface RealTimeAnalysis {
    fun analyzeContent(imageProxy: ImageProxy, context: Context): AnalysisResult { return defaultResult }
}