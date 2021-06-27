package com.ellison.jetpackdemo.cameraX.analysis

import android.content.Context
import androidx.camera.core.ImageProxy

class OCRAnalysis: RealTimeAnalysis {
    override fun analyzeContent(imageProxy: ImageProxy, context: Context): AnalysisResult {
        // Todo: use google's ml kit
        imageProxy.close()
        return defaultResult
    }
}