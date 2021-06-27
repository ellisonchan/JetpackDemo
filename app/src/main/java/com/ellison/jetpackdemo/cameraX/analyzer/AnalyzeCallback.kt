package com.ellison.jetpackdemo.cameraX.analyzer

import com.ellison.jetpackdemo.cameraX.analysis.AnalysisResult

interface AnalyzeCallback {
    fun onZoomPreview(scale: Double)
    fun onAnalyzeResult(result: AnalysisResult)
}