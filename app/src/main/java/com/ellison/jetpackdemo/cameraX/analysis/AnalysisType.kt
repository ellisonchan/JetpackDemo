package com.ellison.jetpackdemo.cameraX.analysis

import com.ellison.jetpackdemo.R

data class AnalysisType internal constructor(
    val logo: Int,
    val name: String?,
    val clazz: Class<out RealTimeAnalysis?>?
)

val typeList = listOf(
    AnalysisType(R.drawable.huawei_logo,"Huawei ScanKit", HuaweiScanAnalysis::class.java),
    AnalysisType(R.drawable.zxing_logo_transparent,"Google Zxing", ZXingAnalysis::class.java),
    AnalysisType(R.drawable.ml_logo,"Google ML Kit", OCRAnalysis::class.java)
)